package nuntium.fhooe.at.nuntium.networking

import nuntium.fhooe.at.nuntium.room.conversation.Conversation
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface ConversationsService {
    @Headers("content-type: application/json")
    @GET("api/conversations")
    fun getAllConversations(): Call<List<Conversation>>
}