package nuntium.fhooe.at.nuntium.conversationoverview.mvvm

import android.arch.lifecycle.LiveData
import nuntium.fhooe.at.nuntium.conversationoverview.ConversationItem
import nuntium.fhooe.at.nuntium.room.conversation.Conversation

interface ConversationOverviewMVVM {
    interface View {
        fun setConversationsInRecyclerView(conversations: ArrayList<ConversationItem>)
        fun addConversationItem(item: ConversationItem)
        fun startConversationObservation()
        fun updateFetchPreference()
    }

    interface Model {
        fun loadAllConversationsForUser()
        fun onConversationsReceived(conversations: List<Conversation>)
    }

    interface ViewModel {
        val userParticipantId: Int
        var livedataConversations:  LiveData<List<Conversation>>?
        fun loadAllConversations()
        fun addConversationToView(item: ConversationItem)
        fun setConversationsOnView(conversations: ArrayList<ConversationItem>)
        fun onConversationsReceived(conversations: List<Conversation>)
        fun initializeConversationsRecyclerView(conversations: LiveData<List<Conversation>>)
        fun updateFetchPreference()
    }
}