package nuntium.fhooe.at.nuntium.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import nuntium.fhooe.at.nuntium.room.conversation.Conversation
import nuntium.fhooe.at.nuntium.room.conversation.ConversationDaoAccess
import nuntium.fhooe.at.nuntium.room.message.Message
import nuntium.fhooe.at.nuntium.room.message.MessageDaoAccess
import nuntium.fhooe.at.nuntium.room.participant.Participant
import nuntium.fhooe.at.nuntium.room.participant.ParticipantDaoAccess

@Database(entities = [Message::class, Participant::class, Conversation::class], version = 1, exportSchema = false)
abstract class NuntiumDatabase : RoomDatabase() {
    abstract fun participantDaoAccess() : ParticipantDaoAccess
    abstract fun messageDaoAccess() : MessageDaoAccess
    abstract fun conversationDaoAccess() : ConversationDaoAccess

    companion object {
        fun getInstance(context: Context): NuntiumDatabase? {
            if(INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context, NuntiumDatabase::class.java, NuntiumDatabase.DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE
        }

        private var INSTANCE: NuntiumDatabase? = null
        const val DATABASE_NAME = "nuntium_db"
    }
}