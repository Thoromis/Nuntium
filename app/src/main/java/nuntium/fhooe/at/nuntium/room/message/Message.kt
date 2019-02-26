package nuntium.fhooe.at.nuntium.room.message

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.PrimaryKey
import nuntium.fhooe.at.nuntium.room.conversation.Conversation
import nuntium.fhooe.at.nuntium.room.participant.Participant
import java.util.*

@Entity(
    foreignKeys = [ForeignKey(
        entity = Conversation::class,
        parentColumns = ["id"],
        childColumns = ["conversationId"],
        onDelete = CASCADE
    ), ForeignKey(
        entity = Participant::class,
        parentColumns = ["id"],
        childColumns = ["senderId"],
        onDelete = ForeignKey.NO_ACTION
    ), ForeignKey(
        entity = Participant::class,
        parentColumns = ["id"],
        childColumns = ["receiverId"],
        onDelete = ForeignKey.NO_ACTION
    )
    ]
)
data class Message(
    @PrimaryKey(autoGenerate = true) var localId: Int,
    var content: String,
    var conversationId: Int,
    var receiverId: Int,
    var senderId: Int,
    var id: Int,
    var creationDate: Date
)