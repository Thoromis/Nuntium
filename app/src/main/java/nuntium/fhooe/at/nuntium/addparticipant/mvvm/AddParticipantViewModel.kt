package nuntium.fhooe.at.nuntium.addparticipant.mvvm

class AddParticipantViewModel(val view: AddParticipantMVVM.View) :
    AddParticipantMVVM.ViewModel {
    private val model: AddParticipantMVVM.Model

    init {
        model = AddParticipantModel(this)
    }
}