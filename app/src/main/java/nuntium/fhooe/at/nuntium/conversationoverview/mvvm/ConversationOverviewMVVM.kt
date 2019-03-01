package nuntium.fhooe.at.nuntium.conversationoverview.mvvm

import nuntium.fhooe.at.nuntium.conversationoverview.ConversationItem

interface ConversationOverviewMVVM {
    interface View {
        fun setConversationsInRecyclerView(conversations: ArrayList<ConversationItem>)
    }

    interface Model {
        fun loadAllConversationsForUser(userId: Int)
    }

    interface ViewModel {
        fun loadAllConversations()
        fun setConversationsOnView(conversations: ArrayList<ConversationItem>)
    }
}