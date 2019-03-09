package nuntium.fhooe.at.nuntium.addparticipant.mvvm

import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import nuntium.fhooe.at.nuntium.R
import nuntium.fhooe.at.nuntium.conversationoverview.mvvm.ConversationOverviewView
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AddParticipantViewTest {
    @Rule
    @JvmField
    val rule  = ActivityTestRule(AddParticipantView::class.java)

    @Rule
    @JvmField
    val rule2  = ActivityTestRule(ConversationOverviewView::class.java)

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun userFlow(){
        loginUser()
        createConversationWithUser()
    }

    private fun loginUser(){
        Espresso.onView(withId(R.id.create_participant_et_email))
            .perform(typeText("generated@user.com"))
        Espresso.onView(ViewMatchers.withId(R.id.create_participant_et_firstname))
            .perform(typeText("Generated"))
        Espresso.onView(withId(R.id.create_participant_et_lastname)).perform(typeText("User"), closeSoftKeyboard())
        Espresso.onView(withId(R.id.create_participant_bt_submit)).perform(click())
    }

    private fun createConversationWithUser(){
        Thread.sleep(10000)
        Espresso.onView(withId(R.id.fab)).perform(click())

    }
}