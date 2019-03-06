package nuntium.fhooe.at.nuntium.messagepolling

import android.app.*
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import nuntium.fhooe.at.nuntium.R
import nuntium.fhooe.at.nuntium.R.id.message
import nuntium.fhooe.at.nuntium.conversationoverview.mvvm.ConversationOverviewView
import nuntium.fhooe.at.nuntium.networking.ConversationsServiceFactory
import nuntium.fhooe.at.nuntium.networking.MessagesServiceFactory
import nuntium.fhooe.at.nuntium.networking.ParticipantServiceFactory
import nuntium.fhooe.at.nuntium.room.DatabaseCreator
import nuntium.fhooe.at.nuntium.room.conversation.Conversation
import nuntium.fhooe.at.nuntium.room.message.Message
import nuntium.fhooe.at.nuntium.utils.Constants.LOG_TAG
import nuntium.fhooe.at.nuntium.utils.NuntiumPreferences
import nuntium.fhooe.at.nuntium.utils.parseDate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//To be done for preferences:
//After each message fetching, the preferences for the fetching time get reset to this time
class MessagePollingService : JobService() {
    val messageFactory = MessagesServiceFactory.build()
    val conversationFactory = ConversationsServiceFactory.build()

    override fun onStopJob(p0: JobParameters?): Boolean {
        //Handle errors here, and return true for rescheduling
        Log.i(LOG_TAG, "Error while job was scheduled...rescheduling!")
        return true
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        startMessageFetching(params)
        Log.i(LOG_TAG, "Executing job for message fetching now!")
        return false
    }

    private fun fetchConversationsOnPage(params: JobParameters?, messages: List<Message>, nextPage: Int) {
        conversationFactory.getConversationsOnPage(nextPage, 20).enqueue(object : Callback<List<Conversation>> {
            override fun onFailure(call: Call<List<Conversation>>, t: Throwable) {
                Log.i(LOG_TAG, "Job scheduler failed during message fetching, probably no internet connection...")
                t.printStackTrace()
                rescheduleService()
                jobFinished(params, true)
            }

            override fun onResponse(call: Call<List<Conversation>>, response: Response<List<Conversation>>) {
                if (!response.isSuccessful) {
                    Log.i(LOG_TAG, "Message fetching failed in Job Scheduler with code: ${response.code()} and message: ${response.message()}")
                    rescheduleService()
                    return
                }

                response.body()?.let {
                    val filteredConversation = it.filterConversations(messages)

                    //Show notification
                    showNotification(messages.sortedBy { message -> message.senderId }, filteredConversation)

                    if (it.count() == 20) {
                        fetchConversationsOnPage(params, messages, nextPage + 1)
                    } else {
                        //Reschedule service
                        rescheduleService()

                        jobFinished(params, false)
                    }
                }
            }
        })
    }

    private fun fetchMessageOnPage(params: JobParameters?, nextPage: Int) {
        messageFactory.getMessagesOnPage(nextPage, 20).enqueue(object : Callback<List<Message>> {
            override fun onFailure(call: Call<List<Message>>, t: Throwable) {
                Log.i(LOG_TAG, "Job scheduler failed during message fetching on page $nextPage, probably no internet connection...")
                t.printStackTrace()
                rescheduleService()
                jobFinished(params, true)
            }

            override fun onResponse(call: Call<List<Message>>, response: Response<List<Message>>) {
                if (!response.isSuccessful) {
                    Log.i(LOG_TAG, "Message fetching failed in Job Scheduler with code: ${response.code()} and message: ${response.message()}")
                    rescheduleService()
                    return
                }

                response.body()?.let {
                    val filteredMessages = filterMessages(it)
                    insertMessagesIntoDb(filteredMessages)

                    //showNotification(filteredMessages, )

                    if (it.count() == 20) {
                        fetchMessageOnPage(params, nextPage + 1)
                    } else {
                        //Reschedule service
                        rescheduleService()

                        jobFinished(params, false)
                    }
                }

            }

        })
    }
    
    private fun startMessageFetching(params: JobParameters?) {
        Observable.just(
            messageFactory.getMessageAfterDate(NuntiumPreferences.getParticipantId(this)).enqueue(object :
                Callback<List<Message>> {
                override fun onFailure(call: Call<List<Message>>, t: Throwable) {
                    Log.i(LOG_TAG, "Job scheduler failed during message fetching, probably no internet connection...")
                    t.printStackTrace()
                    rescheduleService()
                    jobFinished(params, true)
                }

                override fun onResponse(call: Call<List<Message>>, response: Response<List<Message>>) {
                    if (!response.isSuccessful) {
                        Log.i(LOG_TAG, "Message fetching failed in Job Scheduler with code: ${response.code()} and message: ${response.message()}")
                        rescheduleService()
                        return
                    }

                    response.body()?.let {
                        val filteredMessages = filterMessages(it)
                        insertMessagesIntoDb(filteredMessages)
                        //Show notification
                        //to be done

                        //showNotification(filteredMessages.sortedBy { it.senderId })

                        fetchConversationsOnPage(params, filteredMessages, 0)

                        if (it.count() == 20) {
                            fetchMessageOnPage(params, 1)
                        } else {
                            //Reschedule service
                            rescheduleService()

                            jobFinished(params, false)
                        }
                    }
                }

            })
        )
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    private fun insertMessagesIntoDb(messages: List<Message>) {
        Completable
            .fromAction { DatabaseCreator.database.messageDaoAccess().insertMessages(messages) }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    private fun filterMessages(messages: List<Message>): List<Message> {
        val lastFetchDate = NuntiumPreferences.getLastFetchDate(this@MessagePollingService).parseDate()
        val filter: (Message) -> (Boolean) = {
            val otherDate = it.createdDate.parseDate()
            if (otherDate != null && lastFetchDate != null) otherDate > lastFetchDate
            else true
        }

        Log.i(LOG_TAG, "Fetched ${messages.count()} messages in job!")

        val messagesAfterDate =
            messages.filter { message -> filter(message) }

        return messagesAfterDate
    }

    private fun rescheduleService() {
        val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(
            JobInfo.Builder(
                MessagePollingService.MESSAGE_POLLING_JOB_ID,
                ComponentName(this, MessagePollingService::class.java)
            )
                .setMinimumLatency(30000)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .build()
        )
    }

    private fun showNotification(messages: List<Message>, conversations: List<Conversation>) {
        Log.i(LOG_TAG, "Fetched ${messages.count()} messages for notification (after last fetch date) in job!")
        if (messages.count() == 0) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE_DEFAULT)
                .apply { description = CHANNEL_DESCRIPTION }

            val pendingIntent: PendingIntent =
                Intent(this, ConversationOverviewView::class.java)
                    .let { notificationIntent ->
                        notificationIntent.putExtra(UPDATE_FETCH_PREFERENCE, true)
                        PendingIntent.getActivity(this, 0, notificationIntent, 0)
                    }

            val inboxStyle = Notification.InboxStyle()
            inboxStyle.setSummaryText("${messages.count()} new messages in Nuntium")
            messages.forEach {
                inboxStyle.addLine("Message from ${it.senderId}: ${it.content}")
            }

            val notification: Notification = Notification.Builder(this, channel.id)
                .setContentTitle("${messages.count()} new messages in Nuntium")
                .setContentText("${messages[0].content}")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setChannelId(CHANNEL_ID)
                .setStyle(inboxStyle)
                .build()

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(IntentService.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            notificationManager.notify(NOTIFICATION_ID, notification)
        }

    }

    companion object {
        const val MESSAGE_POLLING_JOB_ID = 777
        const val NOTIFICATION_ID = 111
        const val CHANNEL_ID = "NuntiumChannel"
        const val CHANNEL_DESCRIPTION = "Channel for fetching and displaying new messages from the Nuntium Server"
        const val CHANNEL_NAME = "Nuntium"
        const val UPDATE_FETCH_PREFERENCE = "UPDATE_FETCH_PREFERENCE"
    }
}

private fun List<Conversation>.filterConversations(messages: List<Message>): List<Conversation> = filter { conversation ->
    messages.map { it.conversationId }.contains(conversation.id)
}