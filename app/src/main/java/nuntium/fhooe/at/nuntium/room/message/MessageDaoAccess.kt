package nuntium.fhooe.at.nuntium.room.message

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface MessageDaoAccess {
    @Insert
    fun insertMessage(message: Message)

    @Insert
    fun insertMessages(messages: List<Message>)

    @Query("SELECT * FROM Message")
    fun getAllMessages(): LiveData<List<Message>>

    @Query("SELECT * FROM Message WHERE conversationId= :conversationId")
    fun getMessagesByConversationId(conversationId: Int): LiveData<List<Message>>

    @Update
    fun updateMessage(message: Message)

    @Delete
    fun deleteMessage(message: Message)
}