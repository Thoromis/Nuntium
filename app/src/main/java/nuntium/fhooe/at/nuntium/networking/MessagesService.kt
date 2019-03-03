package nuntium.fhooe.at.nuntium.networking

import nuntium.fhooe.at.nuntium.networking.entity.NetworkMessage
import nuntium.fhooe.at.nuntium.room.conversation.Conversation
import nuntium.fhooe.at.nuntium.room.message.Message
import nuntium.fhooe.at.nuntium.room.participant.Participant
import retrofit2.Call
import retrofit2.http.*

interface MessagesService {

    @Headers("content-type: application/json")
    @GET("api/messages")
    fun getAllMessages(): Call<List<Message>>

    @Headers("content-type: application/json")
    @POST("api/messages")
    fun createMessage(@Body message: NetworkMessage): Call<Message>

    @Headers("content-type: application/json")
    @PUT("api/messages")
    fun updateMessage(): Call<List<Message>>

    @Headers("content-type: application/json")
    @GET("api/messages")
    fun getMessagesOnPage(@Query("page") page: Int, @Query("size") size: Int): Call<List<Message>>

}