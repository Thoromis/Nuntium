package nuntium.fhooe.at.nuntium.addparticipant.mvvm

import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import nuntium.fhooe.at.nuntium.R

class AddParticipantView : AppCompatActivity(), AddParticipantMVVM.View {
    lateinit var viewModel: AddParticipantMVVM.ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_participant_view)
        initializeMVVM()
    }

    private fun initializeMVVM() {
        viewModel = AddParticipantViewModel(this)
    }

}
