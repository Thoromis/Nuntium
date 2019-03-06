package nuntium.fhooe.at.nuntium.viewconversation.mvvm

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import nuntium.fhooe.at.nuntium.room.conversation.Conversation
import nuntium.fhooe.at.nuntium.room.message.Message
import nuntium.fhooe.at.nuntium.room.participant.Participant

class ViewConversationViewModel(private val view: ViewConversationMVVM.View,
                                p: Participant,
                                c: Conversation, id: Int
) :
    ViewModel(), ViewConversationMVVM.ViewModel {


    private val model:ViewConversationMVVM.Model
    override var liveData : LiveData<List<Message>>? = null
    override lateinit var conversation : Conversation
    override lateinit var participant: Participant
    override val myID: Int
    init {
        model = ViewConversationModel(this)
        conversation = c
        participant = p
        myID = id
    }


    override fun initializeRecyclerView(messages: LiveData<List<Message>>) {
        liveData = messages

        when{
            messages.value != null -> messages.value?.let {
                view.initRecyclerView(it)
            }
            messages.value == null -> view.initRecyclerView()
        }
        view.startMessagesObservation()
    }

    override fun submitClicked(msgText: String) = model.submitClicked(msgText)

    override fun startUpFinished() = model.startUpFinished()

    override fun displayNoNetwork() = view.displayMessage("You seem to be offline, no sending possible.")
    override fun displayNoMessageText() = view.displayMessage("No text entered.")
    override fun displayNoNetworkConnection() = view.displayMessage("You seem to be offline, using locally saved messages.")

    override fun recyclerViewDataChanged(messages: List<Message>) = model.recyclerViewDataChanged(messages)

    override fun updateRecyclerView(messages: List<Message>) = view.updateRecyclerView(messages)

    override fun setEditTextEmpty() = view.setEditTextEmpty()

}