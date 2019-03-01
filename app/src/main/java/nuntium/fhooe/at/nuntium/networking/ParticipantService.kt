package nuntium.fhooe.at.nuntium.networking

import nuntium.fhooe.at.nuntium.networking.entity.NetworkParticipant
import nuntium.fhooe.at.nuntium.room.participant.Participant
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ParticipantService {

    @Headers("content-type: application/json")
    @POST("api/participants")
    fun createParticipant(@Body participant: NetworkParticipant): Call<Participant>

    @Headers("content-type: application/json")
    @GET("api/participants")
    fun getAllParticipants(): Call<List<Participant>>

    @Headers("content-type: application/json")
    @GET("api/participants")
    fun getParticipantsOnPage(@Query("page") page: Int, @Query("size") size: Int): Call<List<Participant>>

    @Headers("content-type: application/json")
    @GET("api/participants/{id}")
    fun getParticipantById(@Path("id") id: Int): Call<Participant>

    @GET
    fun getProfilePicture(@Url url: String): Call<String>

    @Headers("content-type: application/json")
    @GET("api/participants/count")
    fun countParticipant(): Call<Int>

    @Headers("content-type: application/json")
    @DELETE("api/participants/{id}/")
    fun deleteParticipantById(@Path("id") id: Int): Call<ResponseBody>

    @PUT("api/participants/{id}")
    fun updateParticipantById(@Path("id") id: Int, @Body participant: Participant)
}