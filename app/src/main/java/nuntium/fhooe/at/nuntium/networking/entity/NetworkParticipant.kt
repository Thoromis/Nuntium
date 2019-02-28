package nuntium.fhooe.at.nuntium.networking.entity

import java.io.Serializable

data class NetworkParticipant(
    var firstName: String,
    var lastName: String,
    var email: String,
    var avatar: String
) : Serializable {
    constructor() : this("", "", "", "")
}