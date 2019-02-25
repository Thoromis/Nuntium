package nuntium.fhooe.at.nuntium.room.message

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity
data class Message(
    @PrimaryKey(autoGenerate = true) var localId: Int,
    var content: String,
    var conversationId: Int,
    var receiverId: Int,
    var senderId: Int,
    var id: Int,
    var creationDate: String
)