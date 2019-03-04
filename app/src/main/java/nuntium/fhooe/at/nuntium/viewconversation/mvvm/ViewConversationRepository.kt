package nuntium.fhooe.at.nuntium.viewconversation.mvvm

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import nuntium.fhooe.at.nuntium.networking.MessagesServiceFactory
import nuntium.fhooe.at.nuntium.networking.entity.NetworkMessage
import nuntium.fhooe.at.nuntium.room.DatabaseCreator
import nuntium.fhooe.at.nuntium.room.conversation.Conversation
import nuntium.fhooe.at.nuntium.room.message.Message
import nuntium.fhooe.at.nuntium.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewConversationRepository : ViewConversationMVVM.Repository {

    private val messageService = MessagesServiceFactory.build()
    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun updateMessagesInDatabase(messages: List<Message>) {
        disposables.add(Completable.fromAction {
            messages.forEach { singleMessage ->
                DatabaseCreator.database.messageDaoAccess().insertMessage(singleMessage)
            }
        }
            .subscribeOn(Schedulers.io())
            .subscribe { Log.i(Constants.LOG_TAG, "Messages from network updated in database...") })
    }

    override fun addMessageToDatabase(message: Message) {
        disposables.add(Completable.fromAction {
                DatabaseCreator.database.messageDaoAccess().insertMessage(message)
        }
            .subscribeOn(Schedulers.io())
            .subscribe { Log.i(Constants.LOG_TAG, "Message sent updated in database...") })
    }

    override fun fetchMessagesFromPage(nextPage: Int, fetchingFinished: (List<Message>, Int) -> Unit) {
        Log.i(Constants.LOG_TAG, "Fetching messages from page $nextPage")

        messageService.getMessagesOnPage(nextPage, 20).enqueue(object : Callback<List<Message>> {
            override fun onFailure(call: Call<List<Message>>, t: Throwable) {
                Log.i(Constants.LOG_TAG, "Error while fetching messages from page $nextPage...")
                t.printStackTrace()
                fetchingFinished(listOf(), -1)
            }

            override fun onResponse(call: Call<List<Message>>, response: Response<List<Message>>) {
                Log.i(Constants.LOG_TAG, "Fetching messages from page $nextPage successfully")
                response.body()?.let {
                    val calcNextPage = if (it.count() < 20) -1 else nextPage + 1
                    fetchingFinished(it, calcNextPage)
                }
            }
        })
    }

    override fun fetchAllMessagesFromDatabase(convID: Int,fetchingFinished: (LiveData<List<Message>>) -> Unit) {
        disposables.add(
            Observable.just(
                DatabaseCreator.database.messageDaoAccess().getMessagesByConversationId(convID)
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.i(Constants.LOG_TAG, "Messages fetched from database...")
                    fetchingFinished(it)
                }, {
                    Log.i(Constants.LOG_TAG, "Error while fetching messages from the database...")
                    it.printStackTrace()
                })
        )
    }

    override fun fetchAllMessagesFromNetwork(fetchingFinished: (List<Message>, Int) -> Unit) {
        messageService.getAllMessages().enqueue(object : Callback<List<Message>> {
            override fun onFailure(call: Call<List<Message>>, t: Throwable) {
                Log.i(Constants.LOG_TAG, "Error while fetching messages from the server...")
                t.printStackTrace()
                fetchingFinished(listOf(), -1)
            }

            override fun onResponse(call: Call<List<Message>>, response: Response<List<Message>>) {
                Log.i(Constants.LOG_TAG, "Fetching messages from the server done successfully")
                response.body()?.let {
                    val nextPage = if (it.count() < 20) -1 else 1
                    fetchingFinished(it, nextPage)
                }
            }
        })
    }

    override fun sendMessageViaNetwork(message: NetworkMessage, sendingFinished: (Message?) -> Unit) =
        messageService.createMessage(message).enqueue(object : Callback<Message> {
            override fun onFailure(call: Call<Message>, t: Throwable) {
                Log.e("LOG_TAG", t.message)
                t.printStackTrace()
                sendingFinished(null)
            }

            override fun onResponse(call: Call<Message>, response: Response<Message>) {
                Log.i("LOG_TAG","Sending of message was a success!")
                sendingFinished(response.body())
            }
        })


    override fun deleteMessagesFromDatabase(messages: List<Message>) {
        Completable.fromAction {
            messages.forEach{ DatabaseCreator.database.messageDaoAccess().deleteMessage(it)}
        }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }


}