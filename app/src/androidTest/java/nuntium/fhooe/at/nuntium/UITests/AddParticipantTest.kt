package nuntium.fhooe.at.nuntium.UITests

import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import nuntium.fhooe.at.nuntium.R
import nuntium.fhooe.at.nuntium.addparticipant.mvvm.AddParticipantView
import nuntium.fhooe.at.nuntium.conversationoverview.mvvm.ConversationOverviewView

import org.junit.Rule
import org.junit.Test

class AddParticipantTest {
    @Rule
    @JvmField
    val rule  = ActivityTestRule(AddParticipantView::class.java)

    @Rule
    @JvmField
    val rule2  = ActivityTestRule(ConversationOverviewView::class.java)

    /**
     * Test the login of a new user
     */
    @Test
    fun userFlow(){
        loginUser()
        createConversationWithUser()
    }

    /**
     * Create an account to sign the user up to the system
     */
    private fun loginUser(){
        Espresso.onView(withId(R.id.create_participant_et_email))
            .perform(typeText("generated@user.com"))
        Espresso.onView(ViewMatchers.withId(R.id.create_participant_et_firstname))
            .perform(typeText("Generated"))
        Espresso.onView(withId(R.id.create_participant_et_lastname)).perform(typeText("User"), closeSoftKeyboard())
        Espresso.onView(withId(R.id.create_participant_bt_submit)).perform(click())
    }

    /**
     * Open the create Conversation view
     */
    private fun createConversationWithUser(){
        Thread.sleep(5000)
        Espresso.onView(withId(R.id.fab)).perform(click())

    }
}