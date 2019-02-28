package nuntium.fhooe.at.nuntium.newconversation.mvvm

import android.arch.lifecycle.LiveData
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import nuntium.fhooe.at.nuntium.room.participant.Participant

interface NewConversationMVVM {
    interface View {
        fun initializeRecyclerView(participants: List<Participant>)
        fun startConversationWithParticipant(other: Participant)
        fun initializeRecyclerView()
        fun startParticipantObservation()
        fun displayMessage(message: String)
        fun activateButton()
        fun deactivateButton()
        fun updateRecyclerView(participants: List<Participant>)
    }

    interface ViewModel {
        var livedata: LiveData<List<Participant>>?
        fun initializeRecyclerView(participants: LiveData<List<Participant>>)
        fun submitClicked(selected: Participant?)
        fun startUpFinished()
        fun startConversationWithParticipant(other: Participant)
        fun displayNoNetwork()
        fun displayNoParticipantSelected()
        fun participantSelected()
        fun activateButton()
        fun deactivateButton()
        fun recyclerViewDataChanged(participants: List<Participant>)
        fun updateRecyclerView(participants: List<Participant>)
    }

    interface Model {
        fun submitClicked(selected: Participant?)
        fun startUpFinished()
        fun participantSelected()
        fun recyclerViewDataChanged(participants: List<Participant>)
    }

    interface Repository {
        fun updateParticipantsInDatabase(participants: List<Participant>)
        fun fetchAllParticipantsFromNetwork(fetchingFinished: (List<Participant>) -> Unit)
        fun fetchAllParticipantsFromDatabase(fetchingFinished: (LiveData<List<Participant>>) -> Unit)
        fun deleteParticipantsFromDatabase(participants: List<Participant>)
    }
}