package nuntium.fhooe.at.nuntium.addparticipant.mvvm

import nuntium.fhooe.at.nuntium.addparticipant.Entity.Participant

class AddParticipantModel(val viewModel: AddParticipantMVVM.ViewModel) : AddParticipantMVVM.Model {

    override fun submitClicked(firstname: String?, lastname: String?, email: String?) {
        val invalidViews = mutableListOf<AddParticipantMVVM.Views>()
        val newParticipant = Participant()

        if (firstname == null) invalidViews.add(AddParticipantMVVM.Views.FIRSTNAME) else newParticipant.firstName = firstname
        if (lastname == null) invalidViews.add(AddParticipantMVVM.Views.LASTNAME) else newParticipant.lastName = lastname
        if (email == null) invalidViews.add(AddParticipantMVVM.Views.EMAIL) else newParticipant.email = email

        if (invalidViews.size > 0) {
            viewModel.displayErrorMessage()
            viewModel.shakeViews(invalidViews)
        } else {
            //Success case ==> send data to server and create participant
            viewModel.displayNetworkCallMessage()
            viewModel.startProgressWheel()
            //make network call and create participant
        }
    }

}