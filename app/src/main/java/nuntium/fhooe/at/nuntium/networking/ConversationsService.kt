package nuntium.fhooe.at.nuntium.networking

import nuntium.fhooe.at.nuntium.room.conversation.Conversation
import nuntium.fhooe.at.nuntium.room.message.Message
import nuntium.fhooe.at.nuntium.room.participant.Participant
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ConversationsService {
    @Headers("content-type: application/json")
    @GET("api/conversations")
    fun getAllConversations(): Call<List<Conversation>>

    @Headers("content-type: application/json")
    @GET("api/messages")
    fun getLastMessageOfId(@Query("conversationId.equals") conversationId: Int, @Query("size") size: Int, @Query("sort") sort: String): Call<List<Message>>

    @Headers("content-type: application/json")
    @GET("api/participants/{participantId}")
    fun getParticipantOfId(@Path("participantId") participantId: Int): Call<Participant>
}