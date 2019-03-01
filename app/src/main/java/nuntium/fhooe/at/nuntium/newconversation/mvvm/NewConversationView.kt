package nuntium.fhooe.at.nuntium.newconversation.mvvm

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.widget.Button
import android.widget.Toast
import nuntium.fhooe.at.nuntium.R
import nuntium.fhooe.at.nuntium.newconversation.ParticipantAdapter
import nuntium.fhooe.at.nuntium.room.participant.Participant
import nuntium.fhooe.at.nuntium.utils.Constants.LOG_TAG
import nuntium.fhooe.at.nuntium.utils.NuntiumPreferences
import nuntium.fhooe.at.nuntium.viewconversation.mvvm.ViewConversationView

class NewConversationView : NewConversationMVVM.View, AppCompatActivity() {
    private lateinit var rvParticipants: RecyclerView
    private lateinit var btSubmit: Button
    private lateinit var toolbar: Toolbar
    private lateinit var rvAdapter: ParticipantAdapter
    private var viewModel: NewConversationMVVM.ViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_conversation_view)

        initializeMvvm()
        initializeViews()
        viewModel?.startUpFinished()
    }

    override fun getCurrentParticipant() : Int {
        return NuntiumPreferences.getParticipantId(this)
    }

    private fun initializeMvvm() {
        viewModel = NewConversationViewModel(this)
    }

    private fun initializeViews() {
        rvParticipants = findViewById(R.id.new_conversation_rv_participants)
        btSubmit = findViewById(R.id.new_conversation_bt_submit)
        toolbar = findViewById(R.id.new_conversation_toolbar)

        toolbar.title = "Pick a conversation partner"
        btSubmit.setOnClickListener {
            viewModel?.submitClicked(rvAdapter.getSelectedParticipant())
        }
    }

    override fun activateButton() {
        btSubmit.isActivated = true
        btSubmit.background = ContextCompat.getDrawable(this, R.color.colorPrimary)
    }

    override fun deactivateButton() {
        btSubmit.isActivated = false
        btSubmit.background = ContextCompat.getDrawable(this, R.color.lightGray)
    }

    override fun startConversationWithParticipant(other: Participant) {
        val intent = Intent(this, ViewConversationView::class.java)
        intent.putExtra("receiver", other)
        startActivity(intent)
    }

    override fun initializeRecyclerView(participants: List<Participant>) {
        rvParticipants.layoutManager = LinearLayoutManager(this)
        rvAdapter = ParticipantAdapter(applicationContext, participants.map { it to false }.toMutableList()) {
            viewModel?.participantSelected()
        }

        rvParticipants.adapter = rvAdapter
    }

    override fun updateRecyclerView(participants: List<Participant>) {
        rvAdapter.participants = participants.map { it to false }.toMutableList()
        rvAdapter.notifyDataSetChanged()
        Log.i(LOG_TAG, "Updating recycler view.")
    }

    override fun initializeRecyclerView() {
        initializeRecyclerView(listOf())
    }

    override fun startParticipantObservation() {
        Log.i(LOG_TAG, "Participants livedata is now observed in the view!")

        viewModel?.livedata?.let { liveParticipantData ->
            liveParticipantData.observe(this,
                Observer<List<Participant>> { participants ->
                    participants?.let {
                        viewModel?.recyclerViewDataChanged(it)
                        Log.i(LOG_TAG, "Received updates from database!")
                    }
                })
        }
    }

    override fun displayMessage(message: String) = Snackbar.make(btSubmit, message, Snackbar.LENGTH_LONG).show()
}
