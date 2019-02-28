package nuntium.fhooe.at.nuntium

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import nuntium.fhooe.at.nuntium.newconversation.mvvm.NewConversationView
import nuntium.fhooe.at.nuntium.room.conversation.Conversation
import nuntium.fhooe.at.nuntium.room.message.Message
import nuntium.fhooe.at.nuntium.room.participant.Participant
import nuntium.fhooe.at.nuntium.viewconversation.mvvm.ViewConversationRepository
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            val intent = Intent(this, NewConversationView::class.java)
            startActivity(intent)
        }

        //repoTest()
    }

    private fun repoTest() {
        val repo = ViewConversationRepository()

        //two participants
        val part1 = Participant(1, "Franz", "Xaver", "mail@from.xaver", "")
        val part2 = Participant(2, "Josef", "Huber", "huber@mail.what", "")

        //a conversation
        val conversation = Conversation(1, "Topicic", Date())

        //some messages
        val messageList = listOf(
            Message(5, "Hallo Xaver", 1, 2, 1, 30, Date()),
            Message(2, "Hallo Franz!", 1, 1, 2, 31, Date()),
            Message(3, "Wie geht es dir?", 1, 2, 1, 32, Date()),
            Message(4, "Ganz gut. Lass mich in Ruhe!", 1, 1, 2, 33, Date())
        )
        val messageListRepo = listOf(
            Message(6, "Hallo Xaver", 1, 2, 1, 34, Date()),
            Message(7, "Hallo Franz!", 1, 1, 2, 35, Date()),
            Message(8, "Wie geht es dir?", 1, 2, 1, 36, Date()),
            Message(9, "Ganz gut. Lass mich in Ruhe!", 1, 1, 2, 37, Date())
        )

        //Insertion
        repo.loadMessagesForConversation(1).observe(this, object : Observer<List<Message>> {
            override fun onChanged(messages: List<Message>?) {
                messages?.forEach { message ->
                    Log.i("MESSAGE", message.content)
                }
                if(messages == null) Log.i("MESSAGE_LOG", "onChanged entered, but no messages received!")
            }
        })
        messageListRepo.forEach { message ->
            repo.insertMessage(message)
        }

        //Completable.fromAction {
        //    with(DatabaseCreator.database) {
        //        participantDaoAccess().insertParticipant(part1)
        //        participantDaoAccess().insertParticipant(part2)
//
        //        conversationDaoAccess().insertConversation(conversation)
//
        //        messageDaoAccess().insertMessages(messageList)
        //        messageListRepo.forEach { message ->
        //            repo.insertMessage(message)
        //        }
        //        Constants.i("LOG_TAG", "Insertion of objects is finished...")
        //    }
        //}
            //.subscribeOn(Schedulers.computation())
            //.subscribe()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
