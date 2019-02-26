package nuntium.fhooe.at.nuntium.viewconversation.mvvm

import android.arch.lifecycle.LiveData
import nuntium.fhooe.at.nuntium.room.conversation.Conversation
import nuntium.fhooe.at.nuntium.room.message.Message

interface ViewConversationMVVM {
    interface View {
        fun initMVVM()
    }

    interface Model {
    }

    interface ViewModel {
    }

    interface Repository {
        fun insertConversation(conversation: Conversation, messages: List<Message>)
        fun insertMessage(message: Message)
        fun loadMessagesForConversation(conversationId: Int): LiveData<List<Message>>?
        fun clearRepository()
    }
}