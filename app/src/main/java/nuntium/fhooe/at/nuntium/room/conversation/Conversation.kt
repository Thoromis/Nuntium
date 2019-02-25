package nuntium.fhooe.at.nuntium.room.conversation

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Conversation(
    @PrimaryKey(autoGenerate = false) var id: Int,
    var topic: String
)