package nuntium.fhooe.at.nuntium.addparticipant.mvvm

import android.animation.ObjectAnimator
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import kotlinx.android.synthetic.main.activity_main.*
import nuntium.fhooe.at.nuntium.R
import nuntium.fhooe.at.nuntium.utils.shakeErrorView

class AddParticipantView : AppCompatActivity(), AddParticipantMVVM.View {
    private lateinit var viewModel: AddParticipantMVVM.ViewModel
    private var btSubmit: Button? = null
    private var etEmail: EditText? = null
    private var etFirstname: EditText? = null
    private var etLastname: EditText? = null
    private var progressWheel: ProgressBar? = null
    private var spinAnimation: ObjectAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_participant_view)
        toolbar.title = "Create participant"
        initializeViews()
        initializeMVVM()
    }

    private fun initializeViews() {
        btSubmit = findViewById(R.id.create_participant_bt_submit)
        etEmail = findViewById(R.id.create_participant_et_email)
        etFirstname = findViewById(R.id.create_participant_et_firstname)
        etLastname = findViewById(R.id.create_participant_et_lastname)
        progressWheel = findViewById(R.id.create_participant_progress)

        btSubmit?.setOnClickListener {
            viewModel.submitClicked(etFirstname?.text.toString(), etLastname?.text.toString(), etEmail?.text.toString())
        }

        //Set up animation for progress circle
        progressWheel?.let {
            spinAnimation = ObjectAnimator.ofInt(it, "progress", 0, 100).apply {
                    duration = 400
                    repeatCount = ObjectAnimator.INFINITE
                    repeatMode = ObjectAnimator.RESTART
                }
        }
    }

    private fun initializeMVVM() {
        viewModel = AddParticipantViewModel(this, this.applicationContext)
    }

    override fun startConversationActivity() = finish()

    override fun startProgressWheel() {
        progressWheel?.visibility = View.VISIBLE
        spinAnimation?.start()
    }

    override fun stopProgressWheel() {
        progressWheel?.visibility = View.GONE
        spinAnimation?.cancel()
    }

    override fun displayMessage(message: String) {
        Snackbar.make(btSubmit as View, message, 2000).show()
    }

    override fun shakeFirstname() {
        etFirstname?.shakeErrorView()
    }

    override fun shakeLastname() {
        etLastname?.shakeErrorView()
    }

    override fun shakeEmail() {
        etEmail?.shakeErrorView()
    }
}
