package nuntium.fhooe.at.nuntium.room.participant

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import nuntium.fhooe.at.nuntium.networking.entity.NetworkParticipant
import java.io.Serializable

@Entity
data class Participant(
    @PrimaryKey(autoGenerate = false) var id: Int,
    var firstName: String,
    var lastName: String,
    var email: String,
    var avatar: String
) : Serializable {
    constructor() : this(0, "", "", "", "")
}

fun Participant.toNetworkParticipant() : NetworkParticipant {
    return NetworkParticipant(firstName, lastName, email, avatar)
}