package nuntium.fhooe.at.nuntium.networking

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MessagesServiceFactory {

    fun build(): MessagesService {
        return Retrofit.Builder()
            .baseUrl("http://10.0.0.12:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MessagesService::class.java)
    }
}