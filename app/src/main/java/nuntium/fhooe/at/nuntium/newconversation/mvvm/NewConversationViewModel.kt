package nuntium.fhooe.at.nuntium.newconversation.mvvm

import android.arch.lifecycle.LiveData
import nuntium.fhooe.at.nuntium.room.participant.Participant

class NewConversationViewModel(private val view: NewConversationMVVM.View) : NewConversationMVVM.ViewModel {
    override var livedata: LiveData<List<Participant>>? = null
    private val model: NewConversationMVVM.Model

    init {
        model = NewConversationModel(this)
    }

    override fun initializeRecyclerView(participants: LiveData<List<Participant>>) {
        livedata = participants


        when {
            participants.value != null -> participants.value?.let {
                view.initializeRecyclerView(it)
            }
            participants.value == null -> view.initializeRecyclerView()
        }

        view.startParticipantObservation()
    }

    override fun startConversationWithParticipant(other: Participant) = view.startConversationWithParticipant(other)

    override fun startUpFinished() = model.startUpFinished()

    override fun submitClicked(selected: Participant?) = model.submitClicked(selected)

    override fun activateButton() = view.activateButton()

    override fun deactivateButton() = view.deactivateButton()

    override fun participantSelected() = model.participantSelected()

    override fun recyclerViewDataChanged(participants: List<Participant>) = model.recyclerViewDataChanged(participants)

    override fun updateRecyclerView(participants: List<Participant>) = view.updateRecyclerView(participants)

    override fun displayNoNetwork() =
        view.displayMessage("You seem to be offline, so Nuntium is using your locally saved participants.")

    override fun displayNoParticipantSelected() =
        view.displayMessage("You need to select a participant before you can start a conversation.")
}