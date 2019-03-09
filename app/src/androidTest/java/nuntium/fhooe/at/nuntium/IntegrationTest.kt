package nuntium.fhooe.at.nuntium

import android.arch.persistence.room.Room
import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.filters.MediumTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import nuntium.fhooe.at.nuntium.room.NuntiumDatabase
import nuntium.fhooe.at.nuntium.room.conversation.Conversation
import nuntium.fhooe.at.nuntium.room.participant.Participant
import nuntium.fhooe.at.nuntium.viewconversation.mvvm.ViewConversationView
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class IntegrationTest {


    var participant : Participant? = null
    var conversation: Conversation? = null
    @Before
    fun setUp() {
        /* Implemented for the integration test. Participant and conversation with right ID have to be created in web-service
        * and database first.*/
        participant = Participant(13,"Te","St","test@test.at","https://robohash.org/test@test.at")
        conversation = Conversation(10,"Test",Date(System.currentTimeMillis()))

    }

    @Rule
    @JvmField
    val rule  = ActivityTestRule(ViewConversationView::class.java)


    @Test
    @MediumTest
    fun addMessageToConversation() {

        val i = Intent()
        i.putExtra("receiver",participant)
        i.putExtra("conversation",conversation)
        rule.launchActivity(i)

        onView(withId(R.id.view_conversation_textfield)).perform(typeText("Test"))
        onView(withId(R.id.view_conversation_btn_send)).perform(click())

        val database = getDb()
        val lastMsg = database.messageDaoAccess().getAllMessages().value
        assert(lastMsg?.get(lastMsg.size-1)?.content == "Test")
    }

    private fun getDb() : NuntiumDatabase {
        return Room.databaseBuilder(InstrumentationRegistry.getTargetContext(), NuntiumDatabase::class.java, NuntiumDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
}