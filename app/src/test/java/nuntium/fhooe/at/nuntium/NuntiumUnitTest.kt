package nuntium.fhooe.at.nuntium

import android.arch.persistence.room.Database
import junit.framework.TestCase
import nuntium.fhooe.at.nuntium.room.DatabaseCreator
import nuntium.fhooe.at.nuntium.room.participant.Participant
import nuntium.fhooe.at.nuntium.utils.isValidEmail
import org.junit.Test

class NuntiumUnitTest : TestCase() {

    @Test
    fun testEmailAssertionMethod() {
        val incorrectEmails = mutableListOf<String>().apply {
            add("someFalseEmail")
            add("some@FalseEmail.")
            add("someFalseEmail@1a@2b")
            add("a@.someFalseEmail")
            add("someFalseEmail@abc")
        }
        val correctEmails = mutableListOf<String>().apply {
            add("somelongassemailaddress@hotmail.com")
            add("s@a.d")
            add("random@x.xx")
        }

        incorrectEmails.forEach {
            assertFalse(it, it.isValidEmail())
        }

        correctEmails.forEach {
            assertTrue(it, it.isValidEmail())
        }
    }

}