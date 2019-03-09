package nuntium.fhooe.at.nuntium.room.message

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.PrimaryKey
import nuntium.fhooe.at.nuntium.networking.entity.NetworkMessage
import nuntium.fhooe.at.nuntium.room.conversation.Conversation
import nuntium.fhooe.at.nuntium.room.participant.Participant
import java.util.*

@Entity
data class Message(
    var content: String,
    var conversationId: Int,
    var receiverId: Int,
    var senderId: Int,
    @PrimaryKey(autoGenerate = false) var id: Int,
    var createdDate: String
)

fun Message.toNetworkMessage() : NetworkMessage {
    return NetworkMessage(content,conversationId,receiverId,senderId)
}