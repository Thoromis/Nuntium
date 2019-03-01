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
    private var networkParticipants = mutableListOf<Participant>()

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
        repository.fetchAllParticipantsFromNetwork { participants, nextPage ->
            participantNetworkFetchingFinished(participants, nextPage)
        }
    }

    override fun recyclerViewDataChanged(participants: List<Participant>) {
        when {
            !networkParticipants.isEmpty() -> {
                val toDelete = participants.filter { !networkParticipants.contains(it) }
                repository.deleteParticipantsFromDatabase(toDelete)
                viewModel.updateRecyclerView(participants.filter { networkParticipants.contains(it) })
            }
            networkParticipants.isEmpty() -> viewModel.updateRecyclerView(participants)
        }

    }

    private fun participantNetworkFetchingFinished(participants: List<Participant>, nextPage: Int) {
        //filter logged in user
        when {
            !participants.isEmpty() -> {
                repository.updateParticipantsInDatabase(participants.filter { it.id != viewModel.getCurrentParticipant() })
                networkParticipants.addAll(participants)
            }
            else -> {
                //Tell user data is local via Viewmodel
                Log.i(LOG_TAG, "Working locally, internet connection seems to not work out...")
                viewModel.displayNoNetwork()
            }
        }

        //Fetch participants from next page if needed
        if (nextPage != -1) repository.fetchParticipantsFromPage(nextPage) { parts, page ->
            participantNetworkFetchingFinished(parts, page)
        }

        repository.fetchAllParticipantsFromDatabase {
            participantDatabaseFetchingFinished(it)
        }
    }

    private fun participantDatabaseFetchingFinished(participants: LiveData<List<Participant>>) {
        viewModel.initializeRecyclerView(participants)
    }
}

