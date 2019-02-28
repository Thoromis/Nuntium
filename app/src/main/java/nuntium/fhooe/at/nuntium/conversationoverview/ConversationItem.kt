package nuntium.fhooe.at.nuntium.conversationoverview

import nuntium.fhooe.at.nuntium.room.conversation.Conversation
import nuntium.fhooe.at.nuntium.room.message.Message
import nuntium.fhooe.at.nuntium.room.participant.Participant

data class ConversationItem (
    val conversation:Conversation,
    val lastMessage: Message,
    val conversationPartner: Participant
)
