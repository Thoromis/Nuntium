package nuntium.fhooe.at.nuntium.conversationoverview.mvvm

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.content.Context
import nuntium.fhooe.at.nuntium.room.conversation.Conversation

class ConversationOverviewViewModel(val view: ConversationOverviewMVVM.View, val context: Context) : ConversationOverviewMVVM.ViewModel {
    private val model: ConversationOverviewMVVM.Model = ConversationOverviewModel(this)

    override fun loadAllConversations(){
        model.loadAllConversations()
    }

    override fun setConversationsOnView(conversations: List<Conversation>){
        view.setConversationsInRecyclerView(conversations)
    }


}