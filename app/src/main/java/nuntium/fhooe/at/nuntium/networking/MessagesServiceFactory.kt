package nuntium.fhooe.at.nuntium.networking

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.xml.datatype.DatatypeConstants.SECONDS
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


object MessagesServiceFactory {

    fun build(): MessagesService {
        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(500, TimeUnit.MILLISECONDS)
            .connectTimeout(500, TimeUnit.MILLISECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl("http://192.168.1.109:8080/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MessagesService::class.java)
    }
}