package nuntium.fhooe.at.nuntium.room.conversation

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface ConversationDaoAccess {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertConversation(conversation: Conversation)

    @Query("SELECT * FROM Conversation WHERE id= :conversationId")
    fun getConversationById(conversationId: Int): LiveData<Conversation>

    @Query("SELECT * FROM Conversation")
    fun getAllConversations(): LiveData<List<Conversation>>

    @Update
    fun updateConversation(conversation: Conversation)

    @Delete
    fun deleteConversation(conversation: Conversation)
}