package nuntium.fhooe.at.nuntium.room.conversation

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity
data class Conversation(
    @PrimaryKey(autoGenerate = false) var id: Int,
    var topic: String,
    var creationDate: Date
) : Serializable {
    constructor() : this(-1, "", Date())
}