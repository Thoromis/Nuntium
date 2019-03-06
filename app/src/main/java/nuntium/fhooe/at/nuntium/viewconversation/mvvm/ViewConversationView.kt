package nuntium.fhooe.at.nuntium.viewconversation.mvvm

import android.graphics.*
import android.os.Bundle
import android.support.design.widget.Snackbar
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
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import kotlinx.android.synthetic.main.activity_view_conversation_view.*
import nuntium.fhooe.at.nuntium.R
import nuntium.fhooe.at.nuntium.room.conversation.Conversation
import nuntium.fhooe.at.nuntium.room.message.Message
import nuntium.fhooe.at.nuntium.room.participant.Participant
import nuntium.fhooe.at.nuntium.utils.NuntiumPreferences
import nuntium.fhooe.at.nuntium.viewconversation.MessagesAdapter
import java.util.*


class ViewConversationView : AppCompatActivity(), ViewConversationMVVM.View {


    private var viewModel: ViewConversationMVVM.ViewModel? = null
    private lateinit var rvAdapter: MessagesAdapter
    private lateinit var rvMessages: RecyclerView
    private lateinit var btnSend: Button
    private lateinit var toolbar: Toolbar
    private lateinit var text: EditText
    private var lastMsg: Message? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_conversation_view)
        initMVVM()

        viewModel?.startUpFinished()
    }

    private fun initMVVM() {
        //viewModel = ViewModelProviders.of(this).get(ViewConversationViewModel::class.java)
        val participant = intent.getSerializableExtra("receiver")
        var conversation = intent.getSerializableExtra("conversation")

        if (conversation == null) {
            conversation = Conversation(1, "MoinMoin", Date(System.currentTimeMillis()))
        }

        viewModel = ViewConversationViewModel(
            this, participant as Participant,
            conversation as Conversation, NuntiumPreferences.getParticipantId(this)
        )

        initViews(conversation.topic, "${participant.firstName} ${participant.lastName}")

        view_conversation_toolbar_imageview.circleImage(participant.avatar + "?set=set4", 2f)
    }

    private fun initViews(topic: String, name: String) {

        rvMessages = findViewById(R.id.view_conversation_rcview)
        btnSend = findViewById(R.id.view_conversation_btn_send)
        toolbar = findViewById(R.id.view_conversation_toolbar)
        text = findViewById(R.id.view_conversation_textfield)
        view_conversation_toolbar_title.text = topic
        view_conversation_toolbar_participant.text = name

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        btnSend.setOnClickListener {
            viewModel?.submitClicked(text.text.toString())
            text.onEditorAction(EditorInfo.IME_ACTION_DONE)
        }

    }


    override fun initRecyclerView(messages: List<Message>) {
        Log.i("TEST","DEAF NED SEI")
        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true

        rvMessages.layoutManager = layoutManager
        rvAdapter =
            MessagesAdapter(applicationContext, messages.toMutableList(), NuntiumPreferences.getParticipantId(this))
        rvMessages.adapter = rvAdapter
    }

    override fun initRecyclerView() {
        initRecyclerView(listOf())
    }

    override fun startMessagesObservation() {
        viewModel?.liveData?.let { liveMessageData ->
            liveMessageData.observe(this,
                android.arch.lifecycle.Observer<List<Message>> { messages ->
                    messages?.let {
                        viewModel?.recyclerViewDataChanged(it)
                    }
                })
        }
    }

    override fun updateRecyclerView(messages: List<Message>) {
        //Log.i("TEST","inupdateRecyclerView ${messages.size} ${lastMsg.toString()}")
        var updateRequired = false
        lastMsg?.let {
            if(messages.isNotEmpty()) {
                val newLastMsg = messages[messages.size - 1]
                if (it != newLastMsg) {
                    lastMsg = newLastMsg
                    updateRequired = true
                }
            }
        }
        if (lastMsg == null && messages.isNotEmpty()) {
            lastMsg = messages[messages.size - 1]
            updateRequired = true
        }
        //Log.i("TEST","$updateRequired")
        if (updateRequired) {
            messages.forEach { Log.i("TEST",it.content) }
            rvAdapter.messages = messages.toMutableList()
            rvAdapter.notifyDataSetChanged()
            if (messages.isNotEmpty())
                rvMessages.scrollToPosition(rvAdapter.itemCount - 1)
        }
    }

    override fun setEditTextEmpty() {
        text.setText("")
    }

    override fun displayMessage(message: String) = Snackbar.make(btnSend, message, Snackbar.LENGTH_SHORT).show()

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun <T> ImageView.circleImage(uri: T, borderSize: Float) {
        Glide.with(context)
            .asBitmap()
            .load(uri)
            .apply(RequestOptions.circleCropTransform())
            .into(object : BitmapImageViewTarget(this) {
                override fun setResource(resource: Bitmap?) {
                    val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(
                        context.resources,
                        if (borderSize > 0) {
                            resource?.addBorder(borderSize)
                        } else {
                            resource
                        }
                    )
                    circularBitmapDrawable.isCircular = true
                    setImageDrawable(circularBitmapDrawable)
                }
            })
    }

    private fun Bitmap.addBorder(borderSize: Float): Bitmap {
        val borderOffset = (borderSize * 2).toInt()
        val radius = Math.min(height / 2, width / 2).toFloat()
        val output = Bitmap.createBitmap(width + borderOffset, height + borderOffset, Bitmap.Config.ARGB_8888)
        val paint = Paint()
        val borderX = width / 2 + borderSize
        val borderY = height / 2 + borderSize
        val canvas = Canvas(output)
        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.style = Paint.Style.FILL
        canvas.drawCircle(borderX, borderY, radius, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(this, borderSize, borderSize, paint)
        paint.xfermode = null
        paint.style = Paint.Style.STROKE
        paint.color = Color.WHITE
        paint.strokeWidth = borderSize
        canvas.drawCircle(borderX, borderY, radius, paint)
        return output
    }
}