package nuntium.fhooe.at.nuntium.addparticipant.mvvm

import nuntium.fhooe.at.nuntium.room.participant.Participant

interface AddParticipantMVVM {
    interface View {
        fun displayMessage(message: String)
        fun shakeFirstname()
        fun shakeLastname()
        fun shakeEmail()
        fun startProgressWheel()
        fun stopProgressWheel()
        fun startConversationActivity()
        fun saveParticipant(newParticipant: Participant)
    }

    interface Model {
        fun submitClicked(firstname: String?, lastname: String?, email: String?)
    }

    interface ViewModel {
        fun submitClicked(firstname: String, lastname: String, email: String)
        fun displayErrorMessage()
        fun displaySuccessMessage()
        fun displayNetworkCallMessage()
        fun displayNetworkErrorMessage()
        fun startProgressWheel()
        fun stopProgressWheel()
        fun shakeViews(views: List<Views>)
        fun startConversationActivity()
        fun saveParticipant(newParticipant: Participant)
    }

    enum class Views {
        FIRSTNAME,
        LASTNAME,
        EMAIL
    }
}