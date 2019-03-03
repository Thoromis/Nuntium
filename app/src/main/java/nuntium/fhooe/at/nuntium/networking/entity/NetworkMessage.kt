package nuntium.fhooe.at.nuntium.networking.entity

import java.io.Serializable
import java.util.*

data class NetworkMessage(
    var content: String,
    var conversationId: Int,
    var receiverId: Int,
    var senderId: Int

) : Serializable {
    constructor(): this("",0,0,0)
}