package nuntium.fhooe.at.nuntium.viewconversation.mvvm

import android.arch.lifecycle.LiveData
import io.reactivex.disposables.Disposables
import nuntium.fhooe.at.nuntium.networking.entity.NetworkMessage
import nuntium.fhooe.at.nuntium.room.message.Message

class ViewConversationModel(private val viewModel: ViewConversationMVVM.ViewModel) : ViewConversationMVVM.Model {

    private val repository : ViewConversationMVVM.Repository
    private var disposable = Disposables.disposed()
    private var networkMessages = mutableListOf<Message>()

    init {
        repository = ViewConversationRepository()
    }

    override fun submitClicked(msgText: String) {
        if(msgText == "") {
            viewModel.displayNoMessageText()
            return
        } else {
            repository.sendMessageViaNetwork(NetworkMessage(msgText,viewModel.conversation.id,
                viewModel.participant.id,viewModel.myID)
            ) { message-> sendingMessagesOverNetWorkFinished(message)
            }
        }

    }

    override fun startUpFinished() {
        repository.fetchAllMessagesFromNetwork { messages, nextPage ->
            messageNetworkFetchingFinished(messages,nextPage)
        }
    }

    override fun recyclerViewDataChanged(messages: List<Message>) {
        when {
            !networkMessages.isEmpty() -> {
                val toDelete = messages.filter{!networkMessages.contains(it) }
                repository.deleteMessagesFromDatabase(toDelete)
                viewModel.updateRecyclerView(messages.filter{networkMessages.contains(it)})
            }
            networkMessages.isEmpty() -> viewModel.updateRecyclerView(messages)
        }
    }

    private fun messageNetworkFetchingFinished(messages: List<Message>, nextPage: Int) {
        when {
            !messages.isEmpty() -> {
                repository.updateMessagesInDatabase(messages)
                networkMessages.addAll(messages)
            }
            else -> {
                viewModel.displayNoMessagesYet()
            }
        }

        if (nextPage != -1) repository.fetchMessagesFromPage(nextPage) {
            parts, page -> messageNetworkFetchingFinished(parts,page)
        }

        repository.fetchAllMessagesFromDatabase {
           messageDatabaseFetchingFinished(it)
        }
    }

    private fun messageDatabaseFetchingFinished(messages: LiveData<List<Message>>) {
        viewModel.initializeRecyclerView(messages)
    }

    private fun sendingMessagesOverNetWorkFinished(message: Message?) {
        when {
            message != null -> {
                networkMessages.add(message)
                repository.addMessageToDatabase(message)
                viewModel.setEditTextEmpty()
            }
            else -> {
                viewModel.displayNoNetwork()
            }
        }
    }

}