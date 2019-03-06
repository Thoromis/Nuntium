package nuntium.fhooe.at.nuntium

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import nuntium.fhooe.at.nuntium.room.NuntiumDatabase
import nuntium.fhooe.at.nuntium.room.participant.Participant
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NuntiumInstrumentedUnitTest {


    @Test
    fun testDbInsertReplacement() {
        val participant = Participant(777, "Julius", "Caesar", "julius.caesar@hotmail.com", "")
        val participantCopy = Participant(777, "Copy", "Cat", "copycat@mia.u", "")

        val database = getDb()

        //Insert both participants, second one should be saved in the end because of same id and replace strategy on insertion
        database.participantDaoAccess().insertParticipant(participant)
        val participantFromDb1 = database.participantDaoAccess().getParticipantDirectlyById(777)
        database.participantDaoAccess().insertParticipant(participantCopy)
        val participantFromDb2 = database.participantDaoAccess().getParticipantDirectlyById(777)

        assertEquals(participant, participantFromDb1)
        assertEquals(participantCopy, participantFromDb2)
        assertNotEquals(participant, participantFromDb2)
    }

    private fun getDb() : NuntiumDatabase {
        return Room.databaseBuilder(InstrumentationRegistry.getTargetContext(), NuntiumDatabase::class.java, NuntiumDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
}