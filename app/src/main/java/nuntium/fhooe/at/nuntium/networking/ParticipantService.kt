package nuntium.fhooe.at.nuntium.networking

import nuntium.fhooe.at.nuntium.addparticipant.Entity.Participant
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ParticipantService {

    @Headers("content-type: application/json")
    @POST("api/participants")
    fun createParticipant(@Body participant: Participant): Call<Participant>
}