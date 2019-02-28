package nuntium.fhooe.at.nuntium.newconversation.mvvm

import android.arch.lifecycle.LiveData
import android.util.Log
import com.bumptech.glide.Glide.init
import io.reactivex.disposables.Disposables
import nuntium.fhooe.at.nuntium.room.participant.Participant
import nuntium.fhooe.at.nuntium.utils.Constants.LOG_TAG

class NewConversationModel(private val viewModel: NewConversationMVVM.ViewModel) : NewConversationMVVM.Model {
    private val repository: NewConversationMVVM.Repository
    private var disposable = Disposables.disposed()
    private var networkParticipants = listOf<Participant>()

    init {
        repository = NewConversationRepository()
    }

    override fun submitClicked(selected: Participant?) = when (selected) {
        null -> viewModel.displayNoParticipantSelected()
        else -> viewModel.startConversationWithParticipant(selected)
    }

    override fun participantSelected() = viewModel.activateButton()

    override fun startUpFinished() {
        viewModel.deactivateButton()
        repository.fetchAllParticipantsFromNetwork {
            participantNetworkFetchingFinished(it)
        }
    }

    override fun recyclerViewDataChanged(participants: List<Participant>) {
        val toDelete = participants.filter { !networkParticipants.contains(it) }
        repository.deleteParticipantsFromDatabase(toDelete)
        viewModel.updateRecyclerView(participants.filter { networkParticipants.contains(it) })
    }

    private fun participantNetworkFetchingFinished(participants: List<Participant>) {
        //filter logged in user
        when {
            !participants.isEmpty() -> {
                repository.updateParticipantsInDatabase(participants)
                networkParticipants = participants
            }
            else -> {
                //Tell user data is local via Viewmodel
                Log.i(LOG_TAG, "Working locally, internet connection seems to not work out...")
            }
        }

        repository.fetchAllParticipantsFromDatabase {
            participantDatabaseFetchingFinished(it)
        }
    }

    private fun participantDatabaseFetchingFinished(participants: LiveData<List<Participant>>) {
        viewModel.initializeRecyclerView(participants)
    }
}

