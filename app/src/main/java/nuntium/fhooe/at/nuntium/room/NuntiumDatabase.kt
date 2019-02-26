package nuntium.fhooe.at.nuntium.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import nuntium.fhooe.at.nuntium.room.conversation.Conversation
import nuntium.fhooe.at.nuntium.room.conversation.ConversationDaoAccess
import nuntium.fhooe.at.nuntium.room.message.Message
import nuntium.fhooe.at.nuntium.room.message.MessageDaoAccess
import nuntium.fhooe.at.nuntium.room.participant.Participant
import nuntium.fhooe.at.nuntium.room.participant.ParticipantDaoAccess
import nuntium.fhooe.at.nuntium.utils.DateConverter

@Database(entities = [Message::class, Participant::class, Conversation::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class NuntiumDatabase : RoomDatabase() {
    abstract fun participantDaoAccess(): ParticipantDaoAccess
    abstract fun messageDaoAccess(): MessageDaoAccess

    abstract fun conversationDaoAccess(): ConversationDaoAccess

    companion object {
        const val DATABASE_NAME = "nuntium_db"
    }
}