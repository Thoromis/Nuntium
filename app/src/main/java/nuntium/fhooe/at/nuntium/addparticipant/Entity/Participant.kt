package nuntium.fhooe.at.nuntium.addparticipant.Entity

data class Participant(var firstName: String, var lastName: String, var email: String) {
    constructor() : this("", "", "")
}