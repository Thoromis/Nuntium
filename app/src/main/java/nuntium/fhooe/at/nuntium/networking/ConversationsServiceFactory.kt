package nuntium.fhooe.at.nuntium.networking

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ConversationsServiceFactory {
    fun build(): ConversationsService {
        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(2000, TimeUnit.MILLISECONDS)
            .connectTimeout(2000, TimeUnit.MILLISECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl("http://37.252.185.148:8080/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ConversationsService::class.java)
    }
}