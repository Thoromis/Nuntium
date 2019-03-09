package nuntium.fhooe.at.nuntium.conversationoverview.mvvm

import android.util.Log
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import nuntium.fhooe.at.nuntium.conversationoverview.ConversationItem
import nuntium.fhooe.at.nuntium.conversationoverview.NetworkDataLoader
import nuntium.fhooe.at.nuntium.room.DatabaseCreator
import nuntium.fhooe.at.nuntium.room.conversation.Conversation
import nuntium.fhooe.at.nuntium.utils.Constants.LOG_TAG
import java.util.concurrent.TimeUnit

/**
 * Model of the conversation overview (fetches data from the network into the database and updates the data of the view)
 */
class ConversationOverviewModel(private val viewModel: ConversationOverviewMVVM.ViewModel) :
    ConversationOverviewMVVM.Model {

    private val disposables: CompositeDisposable = CompositeDisposable()
    private val networkDataLoader: NetworkDataLoader = NetworkDataLoader(disposables, viewModel.userParticipantId) { viewModel.updateFetchPreference() }

    /**
     * Called when the conversations got read out of the database. For each conversation the last message and the
     * conversation-partner is read an added to the conversation list, which is the data of the recyclerview.
     */
    override fun onConversationsReceived(conversations: List<Conversation>) {
        val conversationItemList = mutableListOf<ConversationItem>()
        disposables.add(Completable.fromAction {
            conversations.forEach {
                DatabaseCreator.database.messageDaoAccess().getLastMessageFromConversationById(it.id)?.let { message ->
                    val senderId = message.senderId
                    val receiverId = message.receiverId
                    val userId = viewModel.userParticipantId
                    // check if the partner is the sender or receiver id, leave if it is none
                    var partnerId = senderId
                    //  the message fits to the conversationId, but the current user is no sender or receiver
                    if (receiverId != userId && senderId != userId)
                        return@let
                    if (senderId == userId) {
                        partnerId = receiverId
                    }
                    val participant = DatabaseCreator.database.participantDaoAccess().getParticipantDirectlyById(partnerId)
                    conversationItemList.add(ConversationItem(it, message, participant))
                }
            }
        }
            .subscribeOn(Schedulers.io())
            .subscribe {
                conversationItemList.forEach { item ->
                    Log.i(LOG_TAG, "${item.conversation.topic} ; ${item.conversationPartner.email} ; ${item.lastMessage.content} ")
                    viewModel.addConversationToView(item)
                }
            })
    }

    /**
     * Starts periodically updating the database with serverdata and loads data from the database into the recyclerview.
     */
    override fun loadAllConversationsForUser() {
        disposables.add(Observable.create<Unit> {
            Schedulers.newThread().schedulePeriodicallyDirect({
                networkDataLoader.fetchAllData()
            }, 0, 2000, TimeUnit.MILLISECONDS)
        }.subscribe())

        loadAllConversationsFromDatabase()
    }

    /**
     * Load the conversations from the database and subscribe the recyclerview for it.
     */
    private fun loadAllConversationsFromDatabase() {
        disposables.add(
            Observable.just(
                DatabaseCreator.database.conversationDaoAccess().getAllConversations()
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.i(LOG_TAG, "Conversations fetched from database...")
                    viewModel.initializeConversationsRecyclerView(it)
                }, {
                    Log.i(LOG_TAG, "Error while fetching conversations from the database...")
                    it.printStackTrace()
                })
        )
    }
}