package nuntium.fhooe.at.nuntium.networking

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object ParticipantServiceFactory {
    fun build(): ParticipantService {
        return Retrofit.Builder()
            .baseUrl("http://10.0.0.22:8080/")
            //.baseUrl("http://localhost:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ParticipantService::class.java)
    }
}