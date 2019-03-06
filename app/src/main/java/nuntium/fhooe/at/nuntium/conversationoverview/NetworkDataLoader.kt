package nuntium.fhooe.at.nuntium.conversationoverview

import android.util.Log
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import nuntium.fhooe.at.nuntium.networking.ConversationsServiceFactory
import nuntium.fhooe.at.nuntium.networking.MessagesServiceFactory
import nuntium.fhooe.at.nuntium.networking.ParticipantServiceFactory
import nuntium.fhooe.at.nuntium.room.DatabaseCreator
import nuntium.fhooe.at.nuntium.room.conversation.Conversation
import nuntium.fhooe.at.nuntium.room.message.Message
import nuntium.fhooe.at.nuntium.room.participant.Participant
import nuntium.fhooe.at.nuntium.utils.Constants
import nuntium.fhooe.at.nuntium.utils.NuntiumPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetworkDataLoader(private val disposables: CompositeDisposable, private val userId: Int) {
    private val conversationsService = ConversationsServiceFactory.build()
    private val participantService = ParticipantServiceFactory.build()
    private val messagesService = MessagesServiceFactory.build()

    private var networkParticipants = mutableListOf<Participant>()
    private var networkMessages = mutableListOf<Message>()
    private var networkConversations = mutableListOf<Conversation>()


    fun fetchAllData(){
        // fetch Conversations
        fetchAllConversationsFromNetwork()
        // fetch Participants
        fetchAllParticipantsFromNetwork()
        // fetch Messages
        fetchAllMessagesFromNetwork()
    }

    private fun fetchAllConversationsFromNetwork() {
        conversationsService.getAllConversations().enqueue(object : Callback<List<Conversation>> {
            override fun onFailure(call: Call<List<Conversation>>, t: Throwable) {
                Log.i(Constants.LOG_TAG_NETWORK_LOADER, "Error while fetching conversations from the server...")
                t.printStackTrace()
                Log.i(Constants.LOG_TAG_NETWORK_LOADER, "Working locally, internet connection seems to not work out...")
            }

            override fun onResponse(call: Call<List<Conversation>>, response: Response<List<Conversation>>) {
                Log.i(Constants.LOG_TAG_NETWORK_LOADER, "Fetching conversations from the server done successfully")
                response.body()?.let {
                    val nextPage = if (it.count() < 20) -1 else 1
                    conversationNetworkFetchingFinished(it, nextPage)
                }
            }
        })
    }

    private fun conversationNetworkFetchingFinished(conversations: List<Conversation>, nextPage: Int) {
        //filter logged in user
        when {
            !conversations.isEmpty() -> {
                networkConversations.addAll(conversations)
                updateConversationsInDatabase(conversations)
            }
            else -> {
                //Tell user data is local via Viewmodel
                Log.i(Constants.LOG_TAG_NETWORK_LOADER, "Working locally, internet connection seems to not work out...")
                //viewModel.displayNoNetwork()
            }
        }

        //Fetch participants from next page if needed
        if (nextPage != -1) {
            fetchConversationsFromPage(nextPage)
        } else {
            // finished fetching data and updated
            // check if something is to delete in the database

        }
    }


    private fun fetchConversationsFromPage(nextPage: Int) {
        Log.i(Constants.LOG_TAG_NETWORK_LOADER, "Fetching conversations from page $nextPage")

        conversationsService.getConversationsOnPage(nextPage, 20).enqueue(object : Callback<List<Conversation>> {
            override fun onFailure(call: Call<List<Conversation>>, t: Throwable) {
                Log.i(Constants.LOG_TAG_NETWORK_LOADER, "Error while fetching conversations from page $nextPage...")
                t.printStackTrace()
                Log.i(Constants.LOG_TAG_NETWORK_LOADER, "Working locally, internet connection seems to not work out...")
            }

            override fun onResponse(call: Call<List<Conversation>>, response: Response<List<Conversation>>) {
                Log.i(Constants.LOG_TAG_NETWORK_LOADER, "Fetching conversations from page $nextPage successfully")
                response.body()?.let {
                    val calcNextPage = if (it.count() < 20) -1 else nextPage + 1
                    conversationNetworkFetchingFinished(it, calcNextPage)
                }
            }
        })
    }

    private fun updateConversationsInDatabase(conversations: List<Conversation>) {
        disposables.add(Completable.fromAction {
            conversations.forEach { singleConversation ->
                DatabaseCreator.database.conversationDaoAccess().insertConversation(singleConversation)
            }
        }
            .subscribeOn(Schedulers.io())
            .subscribe { Log.i(Constants.LOG_TAG_NETWORK_LOADER, "Conversations from network updated in database...") })
    }

    private fun fetchAllParticipantsFromNetwork() {
        participantService.getAllParticipants().enqueue(object : Callback<List<Participant>> {
            override fun onFailure(call: Call<List<Participant>>, t: Throwable) {
                Log.i(Constants.LOG_TAG_NETWORK_LOADER, "Error while fetching participants from the server...")
                t.printStackTrace()
                Log.i(Constants.LOG_TAG_NETWORK_LOADER, "Working locally, internet connection seems to not work out...")
            }

            override fun onResponse(call: Call<List<Participant>>, response: Response<List<Participant>>) {
                Log.i(Constants.LOG_TAG_NETWORK_LOADER, "Fetching participants from the server done successfully")
                response.body()?.let {
                    val nextPage = if (it.count() < 20) -1 else 1
                    participantNetworkFetchingFinished(it, nextPage)
                }
            }
        })
    }

    private fun participantNetworkFetchingFinished(participants: List<Participant>, nextPage: Int) {
        //filter logged in user
        when {
            !participants.isEmpty() -> {
                updateParticipantsInDatabase(participants)
                updateParticipantsInDatabase(participants.filter { it.id != userId})
            }
            else -> {
                //Tell user data is local via Viewmodel
                Log.i(Constants.LOG_TAG_NETWORK_LOADER, "Working locally, internet connection seems to not work out...")
                //viewModel.displayNoNetwork()
            }
        }

        //Fetch participants from next page if needed
        if (nextPage != -1) fetchParticipantsFromPage(nextPage)
    }
    private fun fetchParticipantsFromPage(nextPage: Int) {
        Log.i(Constants.LOG_TAG_NETWORK_LOADER, "Fetching participants from page $nextPage")

        participantService.getParticipantsOnPage(nextPage, 20).enqueue(object : Callback<List<Participant>> {
            override fun onFailure(call: Call<List<Participant>>, t: Throwable) {
                Log.i(Constants.LOG_TAG_NETWORK_LOADER, "Error while fetching participants from page $nextPage...")
                t.printStackTrace()
                participantNetworkFetchingFinished(listOf(), -1)
            }

            override fun onResponse(call: Call<List<Participant>>, response: Response<List<Participant>>) {
                Log.i(Constants.LOG_TAG_NETWORK_LOADER, "Fetching participants from page $nextPage successfully")
                response.body()?.let {
                    val calcNextPage = if (it.count() < 20) -1 else nextPage + 1
                    participantNetworkFetchingFinished(it, calcNextPage)
                }
            }
        })
    }

    private fun updateParticipantsInDatabase(participants: List<Participant>) {
        disposables.add(Completable.fromAction {
            participants.forEach { singleParticipant ->
                DatabaseCreator.database.participantDaoAccess().insertParticipant(singleParticipant)
            }
        }
            .subscribeOn(Schedulers.io())
            .subscribe { Log.i(Constants.LOG_TAG_NETWORK_LOADER, "Participants from network updated in database...") })
    }

    private fun fetchAllMessagesFromNetwork() {
        messagesService.getAllMessages().enqueue(object : Callback<List<Message>> {
            override fun onFailure(call: Call<List<Message>>, t: Throwable) {
                Log.i(Constants.LOG_TAG, "Error while fetching messages from the server...")
                t.printStackTrace()
                messagesNetworkFetchingFinished(listOf(), -1)
            }

            override fun onResponse(call: Call<List<Message>>, response: Response<List<Message>>) {
                Log.i(Constants.LOG_TAG_NETWORK_LOADER, "Fetching messages from the server done successfully")
                response.body()?.let {
                    val nextPage = if (it.count() < 20) -1 else 1
                    messagesNetworkFetchingFinished(it, nextPage)
                }
            }
        })
    }

    private fun messagesNetworkFetchingFinished(messages: List<Message>, nextPage: Int) {
        //filter logged in user
        when {
            !messages.isEmpty() -> {
                updateMessagesInDatabase(messages)
            }
            else -> {
                //Tell user data is local via Viewmodel
                Log.i(Constants.LOG_TAG_NETWORK_LOADER, "Working locally, internet connection seems to not work out...")
            }
        }

        //Fetch participants from next page if needed
        if (nextPage != -1) fetchMessagesFromPage(nextPage)
    }

    private fun fetchMessagesFromPage(nextPage: Int) {
        Log.i(Constants.LOG_TAG_NETWORK_LOADER, "Fetching messages from page $nextPage")

        messagesService.getMessagesOnPage(nextPage, 20).enqueue(object : Callback<List<Message>> {
            override fun onFailure(call: Call<List<Message>>, t: Throwable) {
                Log.i(Constants.LOG_TAG_NETWORK_LOADER, "Error while fetching messages from page $nextPage...")
                t.printStackTrace()
                conversationNetworkFetchingFinished(listOf(), -1)
            }

            override fun onResponse(call: Call<List<Message>>, response: Response<List<Message>>) {
                Log.i(Constants.LOG_TAG_NETWORK_LOADER, "Fetching messages from page $nextPage successfully")
                response.body()?.let {
                    val calcNextPage = if (it.count() < 20) -1 else nextPage + 1
                    messagesNetworkFetchingFinished(it, calcNextPage)
                }
            }
        })
    }

    private fun updateMessagesInDatabase(messages: List<Message>) {
        disposables.add(Completable.fromAction {
            messages.forEach { singleMessage ->
                DatabaseCreator.database.messageDaoAccess().insertMessage(singleMessage)
            }
        }
            .subscribeOn(Schedulers.io())
            .subscribe { Log.i(Constants.LOG_TAG_NETWORK_LOADER, "Messages from network updated in database...") })
    }

}