package nuntium.fhooe.at.nuntium.addparticipant.mvvm

import android.content.Context
import nuntium.fhooe.at.nuntium.R

class AddParticipantViewModel(val view: AddParticipantMVVM.View, val context: Context) :
    AddParticipantMVVM.ViewModel {

    private val model: AddParticipantMVVM.Model

    init {
        model = AddParticipantModel(this)
    }

    override fun startProgressWheel() = view.startProgressWheel()

    override fun stopProgressWheel() = view.stopProgressWheel()

    override fun submitClicked(firstname: String, lastname: String, email: String) = model.submitClicked(
        if (firstname.isEmpty()) null else firstname,
        if (lastname.isEmpty()) null else lastname,
        if (email.isEmpty()) null else email
    )

    override fun displayNetworkCallMessage() =
        view.displayMessage(context.getString(R.string.create_participant_networkcall_message))

    override fun displayNetworkErrorMessage() =
        view.displayMessage(context.getString(R.string.create_participant_internet_error_message))

    override fun displayErrorMessage() =
        view.displayMessage(context.getString(R.string.create_participant_error_message))

    override fun displaySuccessMessage() =
        view.displayMessage(context.getString(R.string.create_participant_success_message))

    override fun shakeViews(views: List<AddParticipantMVVM.Views>) = views.forEach {
        when (it) {
            AddParticipantMVVM.Views.FIRSTNAME -> {
                view.shakeFirstname()
            }
            AddParticipantMVVM.Views.LASTNAME -> {
                view.shakeLastname()
            }
            AddParticipantMVVM.Views.EMAIL -> {
                view.shakeEmail()
            }
        }
    }
}