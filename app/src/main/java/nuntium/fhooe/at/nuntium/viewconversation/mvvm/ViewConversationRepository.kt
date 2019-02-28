package nuntium.fhooe.at.nuntium.viewconversation.mvvm

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import nuntium.fhooe.at.nuntium.room.DatabaseCreator
import nuntium.fhooe.at.nuntium.room.conversation.Conversation
import nuntium.fhooe.at.nuntium.room.message.Message

class ViewConversationRepository : ViewConversationMVVM.Repository {
    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun insertConversation(conversation: Conversation, messages: List<Message>) {
        if (!DatabaseCreator.isDatabaseCreated()) return

        disposables.add(
            Completable.fromAction {
                DatabaseCreator.database.messageDaoAccess().insertMessages(messages)
                DatabaseCreator.database.conversationDaoAccess().insertConversation(conversation)
            }
                .subscribeOn(Schedulers.computation())
                .subscribe({
                    Log.i("LOG_TAG", "Message insertion completed successfully!")
                }, {
                    Log.i("LOG_TAG", "Error during message insertion!")
                    it.printStackTrace()
                })
        )
    }

    override fun insertMessage(message: Message) {
        if (!DatabaseCreator.isDatabaseCreated()) return

        disposables.add(
            Completable
                .fromAction { DatabaseCreator.database.messageDaoAccess().insertMessage(message) }
                .subscribeOn(Schedulers.io())
                .subscribe({
                    Log.i("LOG_TAG", "Message insertion completed successfully!")
                }, {
                    Log.i("LOG_TAG", "Error during message insertion!")
                    it.printStackTrace()
                })
        )
    }

    override fun loadMessagesForConversation(conversationId: Int): LiveData<List<Message>> {
        if (!DatabaseCreator.isDatabaseCreated()) return MutableLiveData<List<Message>>()

        var result: LiveData<List<Message>> = MutableLiveData<List<Message>>()

        disposables.add(
            Completable.fromAction {
                result =
                    DatabaseCreator.database.messageDaoAccess().getMessagesByConversationId(conversationId)
            }
                .subscribeOn(Schedulers.io())
                .subscribe({
                    Log.i("LOG_TAG", "Message reading completed successfully!")
                }, {
                    Log.i("LOG_TAG", "Error during message fetching!")
                    it.printStackTrace()
                })
        )
        return result
    }

    override fun clearRepository() {
        disposables.dispose()
    }
}