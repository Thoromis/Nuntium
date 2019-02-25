package nuntium.fhooe.at.nuntium.room.participant

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import retrofit2.http.DELETE

@Dao
interface ParticipantDaoAccess {
    @Insert
    fun insertParticipant(participant: Participant)

    @Query("SELECT * FROM Participant WHERE id= :participantId")
    fun getParticipantById(participantId: Int): LiveData<Participant>

    @Update
    fun updateParticipant(participant: Participant)

    @Delete
    fun deleteParticipant(participant: Participant)
}