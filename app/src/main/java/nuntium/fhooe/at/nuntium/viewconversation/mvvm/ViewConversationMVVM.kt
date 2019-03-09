package nuntium.fhooe.at.nuntium.viewconversation.mvvm

import android.arch.lifecycle.LiveData
import nuntium.fhooe.at.nuntium.networking.entity.NetworkMessage
import nuntium.fhooe.at.nuntium.room.conversation.Conversation
import nuntium.fhooe.at.nuntium.room.message.Message
import nuntium.fhooe.at.nuntium.room.participant.Participant

/**
 * author = tobiasbaumgartner
 */
interface ViewConversationMVVM {
    interface View {
        fun initRecyclerView(messages: List<Message>)
        fun initRecyclerView()
        fun startMessagesObservation()
        fun displayMessage(errorCode : Int)
        fun updateRecyclerView(messages: List<Message>)
        fun setEditTextEmpty()
    }

    interface ViewModel {
        var liveData: LiveData<List<Message>>?
        var conversation: Conversation
        var participant: Participant
        val myID: Int
        fun initializeRecyclerView(messages: LiveData<List<Message>>)
        fun submitClicked(msgText: String)
        fun startUpFinished()
        fun displayNoNetwork()
        fun displayNoMessageText()
        fun displayNoNetworkConnection()
        fun recyclerViewDataChanged(messages: List<Message>)
        fun updateRecyclerView(messages: List<Message>)
        fun setEditTextEmpty()
    }

    interface Model {
        fun submitClicked(msgText: String)
        fun startUpFinished()
        fun recyclerViewDataChanged(messages: List<Message>)

    }

    interface Repository {
        fun updateMessagesInDatabase(messages: List<Message>)
        fun addMessageToDatabase(message: Message)
        fun fetchMessagesFromPage(nextPage : Int, fetchingFinished: (List<Message>,Int) -> Unit)
        fun fetchAllMessagesFromDatabase(convID: Int,fetchingFinished: (LiveData<List<Message>>) -> Unit)
        fun fetchAllMessagesFromNetwork(fetchingFinished: (List<Message>, Int) -> Unit)
        fun sendMessageViaNetwork(message: NetworkMessage,sendingFinished: (Message?) -> Unit)
        fun deleteMessagesFromDatabase(messages: List<Message>)
    }
}