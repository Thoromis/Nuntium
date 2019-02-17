package nuntium.fhooe.at.nuntium.addparticipant.mvvm

interface AddParticipantMVVM {
    interface View {
        fun displayMessage(message: String)
        fun shakeFirstname()
        fun shakeLastname()
        fun shakeEmail()
        fun startProgressWheel()
        fun stopProgressWheel()
        fun startConversationActivity()
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
    }

    enum class Views {
        FIRSTNAME,
        LASTNAME,
        EMAIL
    }
}