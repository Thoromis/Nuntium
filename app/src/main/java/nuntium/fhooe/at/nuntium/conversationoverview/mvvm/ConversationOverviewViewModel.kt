package nuntium.fhooe.at.nuntium.conversationoverview.mvvm

import android.arch.lifecycle.LiveData
import android.content.Context
import nuntium.fhooe.at.nuntium.conversationoverview.ConversationItem
import nuntium.fhooe.at.nuntium.room.conversation.Conversation
import nuntium.fhooe.at.nuntium.utils.NuntiumPreferences

class ConversationOverviewViewModel(val view: ConversationOverviewMVVM.View, val context: Context) : ConversationOverviewMVVM.ViewModel {
    override val userParticipantId = NuntiumPreferences.getParticipantId(context)
    private val model: ConversationOverviewMVVM.Model = ConversationOverviewModel(this)
    override var liveDataConversations: LiveData<List<Conversation>>? = null

    override fun updateFetchPreference() = view.updateFetchPreference()

    override fun loadAllConversations(){
        model.loadAllConversationsForUser()
    }

    override fun addConversationToView(item: ConversationItem){
        view.addConversationItem(item)
    }

    override fun setConversationsOnView(conversations: ArrayList<ConversationItem>){
        view.setConversationsInRecyclerView(conversations)
    }

    override fun onConversationsReceived(conversations: List<Conversation>) {
        model.onConversationsReceived(conversations)
    }

    override fun initializeConversationsRecyclerView(conversations: LiveData<List<Conversation>>){
        liveDataConversations = conversations
        view.startConversationObservation()
    }


}