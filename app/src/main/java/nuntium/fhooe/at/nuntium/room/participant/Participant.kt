package nuntium.fhooe.at.nuntium.room.participant

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Participant(
    @PrimaryKey(autoGenerate = false) var id: Int,
    var firstName: String,
    var lastName: String,
    var email: String
) {
    constructor() : this(0, "", "", "")
}