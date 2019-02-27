package nuntium.fhooe.at.nuntium.conversationoverview.mvvm

import nuntium.fhooe.at.nuntium.room.conversation.Conversation

interface ConversationOverviewMVVM {
    interface View {
        fun setConversationsInRecyclerView(conversations: List<Conversation>)
    }

    interface Model {
        fun loadAllConversations()
    }

    interface ViewModel {
        fun loadAllConversations()
        fun setConversationsOnView(conversations: List<Conversation>)
    }
}