package nuntium.fhooe.at.nuntium.newconversation.mvvm

import android.arch.lifecycle.LiveData
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import nuntium.fhooe.at.nuntium.conversationoverview.mvvm.ConversationOverviewView
import nuntium.fhooe.at.nuntium.networking.entity.NetworkConversation
import nuntium.fhooe.at.nuntium.room.conversation.Conversation
import nuntium.fhooe.at.nuntium.room.participant.Participant
import retrofit2.Call

interface NewConversationMVVM {
    interface View {
        fun initializeRecyclerView(participants: List<Participant>)
        fun startConversationWithParticipant(other: Participant, conversation: Conversation)
        fun initializeRecyclerView()
        fun startParticipantObservation()
        fun displayMessage(message: String)
        fun activateButton()
        fun deactivateButton()
        fun updateRecyclerView(participants: List<Participant>)
        fun getCurrentParticipant(): Int
        fun showContentDialog(dialogMessage: String)
    }

    interface ViewModel {
        var livedata: LiveData<List<Participant>>?

        fun initializeRecyclerView(participants: LiveData<List<Participant>>)
        fun submitClicked(selected: Participant?)
        fun startUpFinished()
        fun startConversationWithParticipant(other: Participant, conversation: Conversation)
        fun displayNoNetwork()
        fun displayNoParticipantSelected()
        fun participantSelected()
        fun activateButton()
        fun deactivateButton()
        fun recyclerViewDataChanged(participants: List<Participant>)
        fun updateRecyclerView(participants: List<Participant>)
        fun getCurrentParticipant(): Int
        fun showContentDialog()
        fun topicChoosen(topic: String)
    }

    interface Model {
        fun submitClicked(selected: Participant?)
        fun startUpFinished()
        fun participantSelected()
        fun recyclerViewDataChanged(participants: List<Participant>)
        fun topicChoosen(topic: String)
    }

    interface Repository {
        fun updateParticipantsInDatabase(participants: List<Participant>)
        fun fetchAllParticipantsFromDatabase(fetchingFinished: (LiveData<List<Participant>>) -> Unit)
        fun deleteParticipantsFromDatabase(participants: List<Participant>)
        fun fetchAllParticipantsFromNetwork(fetchingFinished: (List<Participant>, Int) -> Unit)
        fun fetchParticipantsFromPage(nextPage: Int, fetchingFinished: (List<Participant>, Int) -> Unit)
        fun postConversationToServer(conversation: NetworkConversation, taskFinished: (Conversation) -> Unit)
        fun saveConversationToDatabase(conversation: Conversation)
    }
}