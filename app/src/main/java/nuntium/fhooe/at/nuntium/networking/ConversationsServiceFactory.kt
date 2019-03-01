package nuntium.fhooe.at.nuntium.networking

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ConversationsServiceFactory {
    fun build(): ConversationsService {
        return Retrofit.Builder()
            .baseUrl("http://10.0.0.11:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ConversationsService::class.java)
    }
}