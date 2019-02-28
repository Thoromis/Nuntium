package nuntium.fhooe.at.nuntium.conversationoverview.mvvm

import android.app.Application
import android.util.Log
import nuntium.fhooe.at.nuntium.networking.ConversationsServiceFactory
import nuntium.fhooe.at.nuntium.room.conversation.Conversation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConversationOverviewModel(val viewModel: ConversationOverviewMVVM.ViewModel) :
    ConversationOverviewMVVM.Model {
    private val conversationsService = ConversationsServiceFactory.build()

    override fun loadAllConversations() {
        val conversationsCall  = conversationsService.getAllConversations()
        conversationsCall.enqueue(object : Callback<List<Conversation>> {
            override fun onFailure(call: Call<List<Conversation>>, t: Throwable) {
                Log.e("LOG_TAG", t.message)
                t.printStackTrace()
            }

            override fun onResponse(call: Call<List<Conversation>>, response: Response<List<Conversation>>) {
                if(!response.isSuccessful){
                    Log.e("LOG_TAG", response.message())
                    return
                }
                val conversations = response.body()
                conversations?.let {
                    viewModel.setConversationsOnView(conversations)
                }
            }
        })

    }
}