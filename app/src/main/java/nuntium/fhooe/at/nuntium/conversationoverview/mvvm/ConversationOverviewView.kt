package nuntium.fhooe.at.nuntium.conversationoverview.mvvm

import android.app.IntentService
import android.app.NotificationManager
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.arch.lifecycle.Observer
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import nuntium.fhooe.at.nuntium.R
import nuntium.fhooe.at.nuntium.addparticipant.mvvm.AddParticipantView
import nuntium.fhooe.at.nuntium.conversationoverview.ConversationItem
import nuntium.fhooe.at.nuntium.conversationoverview.ConversationsAdapter
import nuntium.fhooe.at.nuntium.messagepolling.MessagePollingService
import nuntium.fhooe.at.nuntium.newconversation.mvvm.NewConversationView
import nuntium.fhooe.at.nuntium.room.conversation.Conversation
import nuntium.fhooe.at.nuntium.room.message.Message
import nuntium.fhooe.at.nuntium.room.participant.Participant
import nuntium.fhooe.at.nuntium.utils.Constants
import nuntium.fhooe.at.nuntium.utils.NuntiumPreferences
import nuntium.fhooe.at.nuntium.utils.parseString
import nuntium.fhooe.at.nuntium.viewconversation.mvvm.ViewConversationRepository
import nuntium.fhooe.at.nuntium.viewconversation.mvvm.ViewConversationView
import java.util.*


class ConversationOverviewView : AppCompatActivity(),
    ConversationOverviewMVVM.View {
    private lateinit var viewModel: ConversationOverviewMVVM.ViewModel
    lateinit var  conversationsAdapter: ConversationsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        //startJobScheduler()
        checkExtras()

        viewModel = ConversationOverviewViewModel(this, this.applicationContext)

        // Recyclerview stuff
        val conversationsView = findViewById<RecyclerView>(R.id.conversation_recycler_view)
        conversationsView.layoutManager = LinearLayoutManager(this)
        conversationsView.setHasFixedSize(true)

        conversationsAdapter = ConversationsAdapter {
            val intent = Intent(applicationContext, ViewConversationView::class.java)
            intent.putExtra("receiver", it.conversationPartner)
            intent.putExtra("conversation", it.conversation)
            startActivity(intent)
        }

        conversationsView.adapter = conversationsAdapter
        viewModel.loadAllConversations()


        fab.setOnClickListener { view ->
            val intent = Intent(this, NewConversationView::class.java)
            startActivity(intent)
        }

        //repoTest()
    }

    override fun updateFetchPreference() = NuntiumPreferences.updateLastFetchDate(this, Date().parseString())

    override fun setConversationsInRecyclerView(conversations: ArrayList<ConversationItem>) {
        conversationsAdapter.conversationList = conversations
    }

    override fun addConversationItem(item: ConversationItem) {
        conversationsAdapter.addNewConversationItem(item)
    }

    override fun startConversationObservation() {
        Log.i(Constants.LOG_TAG, "Conversations livedata is now observed in the view!")

        viewModel?.livedataConversations?.let { liveConversationsData ->
            liveConversationsData.observe(this,
                Observer<List<Conversation>> { conversations ->
                    conversations?.let {
                        viewModel.onConversationsReceived(conversations)
                        Log.i(Constants.LOG_TAG, "Received updates from database!")
                    }
                })
        }
    }

    private fun checkExtras() {
        if (intent.getBooleanExtra("EXIT", false)) finish()
        else if (NuntiumPreferences.getParticipantId(applicationContext) == -1) startAddParticipant()
        else Toast.makeText(this, "You are logged in as ${NuntiumPreferences.getParticipantName(applicationContext)}...", Toast.LENGTH_LONG).show()

        if (intent.getBooleanExtra(MessagePollingService.UPDATE_FETCH_PREFERENCE, false)) {
            val notificationManager = getSystemService(IntentService.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(MessagePollingService.NOTIFICATION_ID)
            NuntiumPreferences.updateLastFetchDate(applicationContext, Date().parseString())
        }
    }

    private fun startJobScheduler() {
        val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(
            JobInfo.Builder(
                MessagePollingService.MESSAGE_POLLING_JOB_ID, ComponentName(this, MessagePollingService::class.java)
            )
                .setMinimumLatency(30000)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .build()
        )
    }

    private fun startAddParticipant() {
        val intent = Intent(this, AddParticipantView::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
}
