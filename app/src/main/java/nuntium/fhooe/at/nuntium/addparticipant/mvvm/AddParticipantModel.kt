package nuntium.fhooe.at.nuntium.addparticipant.mvvm

import android.util.Log
import nuntium.fhooe.at.nuntium.utils.isValidEmail
import nuntium.fhooe.at.nuntium.room.participant.Participant
import nuntium.fhooe.at.nuntium.networking.ParticipantServiceFactory
import nuntium.fhooe.at.nuntium.networking.entity.NetworkParticipant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * author = thomasmaier
 */
class AddParticipantModel(val viewModel: AddParticipantMVVM.ViewModel) : AddParticipantMVVM.Model {
    private val participantService = ParticipantServiceFactory.build()

    override fun submitClicked(firstname: String?, lastname: String?, email: String?) {
        val invalidViews = mutableListOf<AddParticipantMVVM.Views>()
        val newParticipant = NetworkParticipant()

        if (firstname == null) invalidViews.add(AddParticipantMVVM.Views.FIRSTNAME) else newParticipant.firstName = firstname
        if (lastname == null) invalidViews.add(AddParticipantMVVM.Views.LASTNAME) else newParticipant.lastName = lastname
        if (email == null || !email.isValidEmail()) invalidViews.add(AddParticipantMVVM.Views.EMAIL) else newParticipant.email = email

        if (invalidViews.size > 0) {
            viewModel.displayErrorMessage()
            viewModel.shakeViews(invalidViews)
        } else {
            //Success case ==> send data to server and create participant
            viewModel.displayNetworkCallMessage()
            viewModel.startProgressWheel()

            //make network call and create participant
            postParticipant(newParticipant)
        }
    }

    /**
     * Posts a participant to the server, and handle response.
     * NOTE: Done in model because repo would be overkill for just one small posting method
     */
    private fun postParticipant(newParticipant: NetworkParticipant) =
        participantService.createParticipant(newParticipant).enqueue(object : Callback<Participant> {
            override fun onFailure(call: Call<Participant>, t: Throwable) {
                with(viewModel) {
                    displayNetworkErrorMessage()
                    stopProgressWheel()
                }
                Log.e("LOG_TAG", t.message)
                t.printStackTrace()
            }

            override fun onResponse(call: Call<Participant>, response: Response<Participant>) {
                if(!response.isSuccessful) {
                    viewModel.displayServerErrorMessage()
                    return
                }

                with(viewModel) {
                    displaySuccessMessage()
                    stopProgressWheel()
                    response.body()?.let { saveParticipant(it) }
                    startConversationActivity()
                }
            }
        })
}