package nuntium.fhooe.at.nuntium.viewconversation.mvvm

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.Telephony
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.RoundedBitmapDrawable
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.Request
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import kotlinx.android.synthetic.main.activity_view_conversation_view.*
import nuntium.fhooe.at.nuntium.R
import nuntium.fhooe.at.nuntium.room.conversation.Conversation
import nuntium.fhooe.at.nuntium.room.message.Message
import nuntium.fhooe.at.nuntium.room.participant.Participant
import nuntium.fhooe.at.nuntium.utils.GlideApp
import nuntium.fhooe.at.nuntium.utils.NuntiumPreferences
import nuntium.fhooe.at.nuntium.viewconversation.MessagesAdapter
import java.util.*


class ViewConversationView : AppCompatActivity(),ViewConversationMVVM.View {


    private var viewModel: ViewConversationMVVM.ViewModel? = null
    private lateinit var rvAdapter: MessagesAdapter
    private lateinit var rvMessages: RecyclerView
    private lateinit var btnSend: Button
    private lateinit var toolbar: Toolbar
    private lateinit var text: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_conversation_view)
        initMVVM()

        viewModel?.startUpFinished()
    }

    private fun initMVVM() {
        //viewModel = ViewModelProviders.of(this).get(ViewConversationViewModel::class.java)
        var participant = intent.getSerializableExtra("receiver")
        var conversation = intent.getSerializableExtra("conversation")

        viewModel = ViewConversationViewModel(this,participant as Participant,
            Conversation(1,"MoinMoin", Date(System.currentTimeMillis())),NuntiumPreferences.getParticipantId(this))


        if(conversation == null) {
            conversation = Conversation(1,"MoinMoin",Date(System.currentTimeMillis()))
        }

        initViews((conversation as Conversation).topic,"${(participant as Participant).firstName} ${participant.lastName}")
        setImageWithGlide(participant)
    }

    private fun initViews(topic: String,name: String) {

        rvMessages = findViewById(R.id.view_conversation_rcview)
        btnSend = findViewById(R.id.view_conversation_btn_send)
        toolbar = findViewById(R.id.view_conversation_toolbar)
        text = findViewById(R.id.view_conversation_textfield)
        toolbar.title = topic
        toolbar.subtitle = name

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        btnSend.setOnClickListener {
            viewModel?.submitClicked(text.text.toString())
            text.onEditorAction(EditorInfo.IME_ACTION_DONE)
        }

    }


    override fun initRecyclerView(messages: List<Message>) {

        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true

        rvMessages.layoutManager = layoutManager
        rvAdapter = MessagesAdapter(applicationContext,messages.toMutableList(),NuntiumPreferences.getParticipantId(this))
        rvMessages.adapter = rvAdapter
    }

    override fun initRecyclerView() {
        initRecyclerView(listOf())
    }

    override fun startMessagesObservation() {
        viewModel?.liveData?.let { liveMessageData ->
            liveMessageData.observe(this,
                android.arch.lifecycle.Observer<List<Message>> {
                    messages -> messages?.let {
                    viewModel?.recyclerViewDataChanged(it)
                }
                })
        }
    }

    override fun updateRecyclerView(messages: List<Message>) {
        rvAdapter.messages = messages.toMutableList()
        rvAdapter.notifyDataSetChanged()
        rvMessages.scrollToPosition(rvAdapter.itemCount - 1);
    }

    override fun setEditTextEmpty() {
        text.setText("")
    }

    override fun displayMessage(message: String) = Snackbar.make(btnSend,message,Snackbar.LENGTH_SHORT).show()

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home -> {
                finish()
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun setImageWithGlide(participant: Participant) {

        Glide.with(applicationContext)
            .asDrawable()
            .load(participant.avatar)
            .apply(RequestOptions.circleCropTransform())
            .placeholder(ContextCompat.getDrawable(applicationContext, R.color.white))
            .into(view_conversation_toolbar_imageview)
    }
}