package nuntium.fhooe.at.nuntium.newconversation.mvvm

import android.arch.lifecycle.LiveData
import android.util.Log
import com.bumptech.glide.Glide.init
import io.reactivex.disposables.Disposables
import nuntium.fhooe.at.nuntium.networking.entity.NetworkConversation
import nuntium.fhooe.at.nuntium.room.conversation.Conversation
import nuntium.fhooe.at.nuntium.room.participant.Participant
import nuntium.fhooe.at.nuntium.utils.Constants.LOG_TAG

class NewConversationModel(private val viewModel: NewConversationMVVM.ViewModel) : NewConversationMVVM.Model {
    private val repository: NewConversationMVVM.Repository
    private var disposable = Disposables.disposed()
    private var networkParticipants = mutableListOf<Participant>()
    private var selected: Participant? = null
    private var lastRecyclerViewCreation = mutableListOf<Participant>()

    init {
        repository = NewConversationRepository()
    }

    override fun submitClicked(selected: Participant?) = when (selected) {
        null -> viewModel.displayNoParticipantSelected()
        else -> {
            viewModel.showContentDialog()
            this.selected = selected
        }
    }

    override fun participantSelected() = viewModel.activateButton()

    override fun startUpFinished() {
        viewModel.deactivateButton()
        repository.fetchAllParticipantsFromNetwork { participants, nextPage ->
            participantNetworkFetchingFinished(participants, nextPage)
        }
    }

    override fun recyclerViewDataChanged(participants: List<Participant>) {
        var newData = false
        participants.forEach { participant ->
            if (!lastRecyclerViewCreation.map { it.id }.contains(participant.id)) {
                lastRecyclerViewCreation = participants.toMutableList()
                newData = true
            }
        }
        if (lastRecyclerViewCreation.count() == 0) newData = true
        if (!newData) return

        when {
            !networkParticipants.isEmpty() -> {
                val toDelete = participants.filter { !networkParticipants.contains(it) }.filter { it.id != viewModel.getCurrentParticipant() }
                repository.deleteParticipantsFromDatabase(toDelete)
                viewModel.updateRecyclerView(participants.filter { networkParticipants.contains(it) }.filter { it.id != viewModel.getCurrentParticipant() })
            }
            networkParticipants.isEmpty() -> viewModel.updateRecyclerView(participants)
        }
    }

    override fun topicChoosen(topic: String) {
        repository.postConversationToServer(NetworkConversation(topic)) { conversation ->
            if(conversation!= null) conversationPosted(conversation) else  {
                viewModel.displayNoNetworkForConversationCreation()
                viewModel.cancelDialog()
            }
        }
    }

    private fun conversationPosted(conversation: Conversation) {
        selected?.let {
            viewModel.startConversationWithParticipant(it, conversation)
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
        } else {
            repository.fetchAllParticipantsFromDatabase {
                participantDatabaseFetchingFinished(it)
            }
        }
    }

    private fun participantDatabaseFetchingFinished(participants: LiveData<List<Participant>>) {
        viewModel.initializeRecyclerView(participants)
    }
}

