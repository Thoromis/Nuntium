package nuntium.fhooe.at.nuntium.conversationoverview.mvvm

import android.util.Log
import io.reactivex.Completable
import io.reactivex.Observable
import nuntium.fhooe.at.nuntium.conversationoverview.ConversationItem
import nuntium.fhooe.at.nuntium.room.conversation.Conversation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import nuntium.fhooe.at.nuntium.conversationoverview.NetworkDataLoader
import nuntium.fhooe.at.nuntium.room.DatabaseCreator
import nuntium.fhooe.at.nuntium.utils.Constants.LOG_TAG

class ConversationOverviewModel(val viewModel: ConversationOverviewViewModel) :
    ConversationOverviewMVVM.Model {

    private val disposables: CompositeDisposable = CompositeDisposable()
    private val networkDataLoader: NetworkDataLoader = NetworkDataLoader(disposables, viewModel.userParticipantId)


    override fun onConversationsReceived(conversations: List<Conversation>){
        val conversationItemList = mutableListOf<ConversationItem>()
        val disposable = Completable.fromAction{
            conversations.forEach {
                DatabaseCreator.database.messageDaoAccess().getLastMessageFromCoversationById(it.id)?.let {message->
                    val senderId = message.senderId
                    val receiverId = message.receiverId
                    val userId = viewModel.userParticipantId
                    // check if the partner is the sender or receiver id, leave if it is none
                    var partnerId = senderId
                    if (receiverId != userId && senderId != userId)
                       return@let
                    if (senderId == userId){
                        partnerId = receiverId
                    }
                    val participant = DatabaseCreator.database.participantDaoAccess().getParticipantDirectlyById(partnerId)
                    conversationItemList.add(ConversationItem(it, message, participant))
                }
            }
        }
            .subscribeOn(Schedulers.io())
            .subscribe {
                conversationItemList.forEach {item ->
                    Log.i(LOG_TAG, "${item.conversation.topic} ; ${item.conversationPartner.email} ; ${item.lastMessage.content} ")
                    viewModel.addConversationToView(item)
                }
            }
    }

    fun loadAllConversationsFromDatabase(){
        disposables.add(
            Observable.just(
                DatabaseCreator.database.conversationDaoAccess().getAllConversations()
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.i(LOG_TAG, "Conversations fetched from database...")
                    viewModel.initializeConversationsRecyclerView(it)
                    //fetchingConversationsFromDbFinished(it)
                }, {
                    Log.i(LOG_TAG, "Error while fetching conversations from the database...")
                    it.printStackTrace()
                })
        )
    }

    override fun loadAllConversationsForUser() {
        networkDataLoader.fetchAllData()
        loadAllConversationsFromDatabase()
    }
}