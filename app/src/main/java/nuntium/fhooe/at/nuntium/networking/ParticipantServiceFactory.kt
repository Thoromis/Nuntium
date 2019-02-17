package nuntium.fhooe.at.nuntium.networking

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object ParticipantServiceFactory {
    fun build(): ParticipantService {
        return Retrofit.Builder()
            .baseUrl("http://192.168.10.9:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ParticipantService::class.java)
    }
}