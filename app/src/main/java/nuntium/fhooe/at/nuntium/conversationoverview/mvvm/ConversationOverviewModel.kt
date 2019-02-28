package nuntium.fhooe.at.nuntium.conversationoverview.mvvm

import android.util.Log
import io.reactivex.Observable
import nuntium.fhooe.at.nuntium.conversationoverview.ConversationItem
import nuntium.fhooe.at.nuntium.networking.ConversationsServiceFactory
import nuntium.fhooe.at.nuntium.room.conversation.Conversation
import nuntium.fhooe.at.nuntium.room.message.Message
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import nuntium.fhooe.at.nuntium.room.participant.Participant
import nuntium.fhooe.at.nuntium.utils.NuntiumPreferences
import java.io.IOException


class ConversationOverviewModel(val viewModel: ConversationOverviewMVVM.ViewModel) :
    ConversationOverviewMVVM.Model {
    private val conversationsService = ConversationsServiceFactory.build()

    override fun loadAllConversationsForUser(userId: Int) {
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
                    val conversationItems = ArrayList<ConversationItem>()

                    // create the conversationObject
                    for (conversation in conversations){
                        //
                        val messageCall  = conversationsService.getLastMessageOfId(conversation.id, 1, "lastModifiedDate,desc")
                        messageCall.enqueue(object : Callback<List<Message>> {
                            override fun onFailure(call: Call<List<Message>>, t: Throwable) {
                                Log.e("LOG_TAG", t.message)
                                t.printStackTrace()
                            }

                            override fun onResponse(call: Call<List<Message>>, response: Response<List<Message>>) {
                                if(!response.isSuccessful){
                                    Log.e("LOG_TAG", response.message())
                                    return
                                }
                                val message = response.body()
                                message?.let {
                                    if (message.isEmpty())
                                        return
                                    val senderId = message[0].senderId
                                    val receiverId = message[0].receiverId

                                    // check if the partner is the sender or receiver id, leave if it is none
                                    var partnerId = senderId
                                    if (receiverId != userId && senderId != userId)
                                        return
                                    if (senderId == userId){
                                        partnerId = receiverId
                                    }

                                    // get the participant
                                    val participantCall = conversationsService.getParticipantOfId(partnerId)
                                    participantCall.enqueue(object : Callback<Participant> {
                                        override fun onFailure(call: Call<Participant>, t: Throwable) {
                                            Log.e("LOG_TAG", t.message)
                                            t.printStackTrace()
                                        }

                                        override fun onResponse(call: Call<Participant>, response: Response<Participant>) {
                                            if(!response.isSuccessful){
                                                Log.e("LOG_TAG", response.message())
                                                return
                                            }
                                            val participant = response.body()
                                            participant?.let {
                                                val conversationItem = ConversationItem(conversation, message[0], participant)
                                                conversationItems.add(conversationItem)
                                                viewModel.setConversationsOnView(conversationItems)
                                            }
                                        }
                                    })
                                }
                            }
                        })
                    }
                }
            }
        })
    }
}