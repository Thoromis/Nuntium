package nuntium.fhooe.at.nuntium.newconversation.mvvm

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.IntentSender
import android.util.Log
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import nuntium.fhooe.at.nuntium.networking.ParticipantServiceFactory
import nuntium.fhooe.at.nuntium.room.DatabaseCreator
import nuntium.fhooe.at.nuntium.room.participant.Participant
import nuntium.fhooe.at.nuntium.utils.Constants.LOG_TAG
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewConversationRepository : NewConversationMVVM.Repository {
    private val participantService = ParticipantServiceFactory.build()
    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun fetchAllParticipantsFromNetwork(fetchingFinished: (List<Participant>, Int) -> Unit) {
        participantService.getAllParticipants().enqueue(object : Callback<List<Participant>> {
            override fun onFailure(call: Call<List<Participant>>, t: Throwable) {
                Log.i(LOG_TAG, "Error while fetching participants from the server...")
                t.printStackTrace()
                fetchingFinished(listOf(), -1)
            }

            override fun onResponse(call: Call<List<Participant>>, response: Response<List<Participant>>) {
                Log.i(LOG_TAG, "Fetching participants from the server done successfully")
                response.body()?.let {
                    val nextPage = if (it.count() < 20) -1 else 1
                    fetchingFinished(it, nextPage)
                }
            }
        })
    }

    override fun fetchParticipantsFromPage(nextPage: Int, fetchingFinished: (List<Participant>, Int) -> Unit) {
        Log.i(LOG_TAG, "Fetching participants from page $nextPage")

        participantService.getParticipantsOnPage(1, 20).enqueue(object : Callback<List<Participant>> {
            override fun onFailure(call: Call<List<Participant>>, t: Throwable) {
                Log.i(LOG_TAG, "Error while fetching participants from page $nextPage...")
                t.printStackTrace()
                fetchingFinished(listOf(), -1)
            }

            override fun onResponse(call: Call<List<Participant>>, response: Response<List<Participant>>) {
                Log.i(LOG_TAG, "Fetching participants from page $nextPage successfully")
                response.body()?.let {
                    val calcNextPage = if (it.count() < 20) -1 else nextPage + 1
                    fetchingFinished(it, calcNextPage)
                }
            }
        })
    }

    override fun updateParticipantsInDatabase(participants: List<Participant>) {
        disposables.add(Completable.fromAction {
            participants.forEach { singleParticipant ->
                DatabaseCreator.database.participantDaoAccess().insertParticipant(singleParticipant)
            }
        }
            .subscribeOn(Schedulers.io())
            .subscribe { Log.i(LOG_TAG, "Participants from network updated in database...") })
    }

    override fun fetchAllParticipantsFromDatabase(fetchingFinished: (LiveData<List<Participant>>) -> (Unit)) {
        disposables.add(
            Observable.just(
                DatabaseCreator.database.participantDaoAccess().getAllParticipants()
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.i(LOG_TAG, "Participants fetched from database...")
                    fetchingFinished(it)
                }, {
                    Log.i(LOG_TAG, "Error while fetching participants from the database...")
                    it.printStackTrace()
                })
        )
    }

    override fun deleteParticipantsFromDatabase(participants: List<Participant>) {
        Completable.fromAction {
            participants.forEach { DatabaseCreator.database.participantDaoAccess().deleteParticipant(it) }
        }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }
}