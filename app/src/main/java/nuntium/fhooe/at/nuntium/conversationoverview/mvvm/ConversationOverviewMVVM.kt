package nuntium.fhooe.at.nuntium.conversationoverview.mvvm

import nuntium.fhooe.at.nuntium.conversationoverview.ConversationItem
import nuntium.fhooe.at.nuntium.room.conversation.Conversation

interface ConversationOverviewMVVM {
    interface View {
        fun setConversationsInRecyclerView(conversations: ArrayList<ConversationItem>)
        fun addConversationItem(item: ConversationItem)
    }

    interface Model {
        fun loadAllConversationsForUser()
        fun onConversationsReceived(conversations: List<Conversation>)
    }

    interface ViewModel {
        fun loadAllConversations()
        fun addConversationToView(item: ConversationItem)
        fun setConversationsOnView(conversations: ArrayList<ConversationItem>)
        fun onConversationsReceived(conversations: List<Conversation>)
    }
}