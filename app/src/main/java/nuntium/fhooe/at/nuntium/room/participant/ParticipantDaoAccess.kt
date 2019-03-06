package nuntium.fhooe.at.nuntium.room.participant

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import retrofit2.http.DELETE

@Dao
interface ParticipantDaoAccess {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertParticipant(participant: Participant)

    @Query("SELECT * FROM Participant")
    fun getAllParticipants(): LiveData<List<Participant>>

    @Query("SELECT * FROM Participant WHERE id= :participantId")
    fun getParticipantById(participantId: Int): LiveData<Participant>

    @Query("SELECT * FROM Participant WHERE id= :participantId")
    fun getParticipantDirectlyById(participantId: Int): Participant

    @Update
    fun updateParticipant(participant: Participant)

    @Delete
    fun deleteParticipant(participant: Participant)
}