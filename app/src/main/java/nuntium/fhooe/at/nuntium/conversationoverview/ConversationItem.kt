package nuntium.fhooe.at.nuntium.conversationoverview

import nuntium.fhooe.at.nuntium.room.conversation.Conversation
import nuntium.fhooe.at.nuntium.room.message.Message
import nuntium.fhooe.at.nuntium.room.participant.Participant

/**
 * Represents the content of an item displayed in the "conversation overview"-recyclerview
 */
data class ConversationItem (
    val conversation:Conversation,
    val lastMessage: Message,
    val conversationPartner: Participant
)
