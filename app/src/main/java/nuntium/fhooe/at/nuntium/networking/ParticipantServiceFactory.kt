package nuntium.fhooe.at.nuntium.networking

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

object ParticipantServiceFactory {
    fun build(): ParticipantService {
        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(2000, TimeUnit.MILLISECONDS)
            .connectTimeout(2000, TimeUnit.MILLISECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl("http://37.252.185.148:8080/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ParticipantService::class.java)
    }
}