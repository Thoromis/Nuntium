package nuntium.fhooe.at.nuntium.conversationoverview.mvvm

import android.content.Context
import nuntium.fhooe.at.nuntium.conversationoverview.ConversationItem
import nuntium.fhooe.at.nuntium.utils.NuntiumPreferences

class ConversationOverviewViewModel(val view: ConversationOverviewMVVM.View, val context: Context) : ConversationOverviewMVVM.ViewModel {
    private val model: ConversationOverviewMVVM.Model = ConversationOverviewModel(this)

    override fun loadAllConversations(){
        model.loadAllConversationsForUser(NuntiumPreferences.getParticipantId(context))
    }

    override fun setConversationsOnView(conversations: ArrayList<ConversationItem>){
        view.setConversationsInRecyclerView(conversations)
    }
}