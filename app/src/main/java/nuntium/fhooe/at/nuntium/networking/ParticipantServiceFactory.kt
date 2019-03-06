package nuntium.fhooe.at.nuntium.networking

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

object ParticipantServiceFactory {
    fun build(): ParticipantService {
        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(500, TimeUnit.MILLISECONDS)
            .connectTimeout(500, TimeUnit.MILLISECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl("http://192.168.1.109:8080/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ParticipantService::class.java)
    }
}