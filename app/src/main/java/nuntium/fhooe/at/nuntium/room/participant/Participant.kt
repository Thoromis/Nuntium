package nuntium.fhooe.at.nuntium.room.participant

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
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