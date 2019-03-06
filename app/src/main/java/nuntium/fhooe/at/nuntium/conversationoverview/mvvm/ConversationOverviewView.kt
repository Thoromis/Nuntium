package nuntium.fhooe.at.nuntium.conversationoverview.mvvm

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import nuntium.fhooe.at.nuntium.R
import nuntium.fhooe.at.nuntium.addparticipant.mvvm.AddParticipantView
import nuntium.fhooe.at.nuntium.conversationoverview.ConversationItem
import nuntium.fhooe.at.nuntium.conversationoverview.ConversationsAdapter
import nuntium.fhooe.at.nuntium.newconversation.mvvm.NewConversationView
import nuntium.fhooe.at.nuntium.room.conversation.Conversation
import nuntium.fhooe.at.nuntium.room.message.Message
import nuntium.fhooe.at.nuntium.room.participant.Participant
import nuntium.fhooe.at.nuntium.utils.Constants
import nuntium.fhooe.at.nuntium.utils.NuntiumPreferences
import nuntium.fhooe.at.nuntium.viewconversation.mvvm.ViewConversationRepository
import nuntium.fhooe.at.nuntium.viewconversation.mvvm.ViewConversationView
import java.util.*


class ConversationOverviewView : AppCompatActivity(),
    ConversationOverviewMVVM.View {
    private lateinit var viewModel: ConversationOverviewMVVM.ViewModel
    lateinit var  conversationsAdapter: ConversationsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        when {
            intent.getBooleanExtra("EXIT", false) -> finish()
            NuntiumPreferences.getParticipantId(applicationContext) == -1 -> startAddParticipant()
            else -> Toast.makeText(this, "You are logged in as ${NuntiumPreferences.getParticipantName(applicationContext)}...", Toast.LENGTH_LONG).show()
        }

        viewModel = ConversationOverviewViewModel(this, this.applicationContext)

        // Recyclerview stuff
        val conversationsView = findViewById<RecyclerView>(R.id.conversation_recycler_view)
        conversationsView.layoutManager = LinearLayoutManager(this)
        conversationsView.setHasFixedSize(true)

        conversationsAdapter = ConversationsAdapter{
            val intent = Intent(applicationContext, ViewConversationView::class.java)
            intent.putExtra("receiver",it.conversationPartner)
            intent.putExtra("conversation",it.conversation)
            startActivity(intent)
        }

        conversationsView.adapter = conversationsAdapter
        viewModel.loadAllConversations()


        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            val intent = Intent(this, NewConversationView::class.java)
            startActivity(intent)
        }

        //repoTest()
    }

    override fun setConversationsInRecyclerView(conversations: ArrayList<ConversationItem>){
        conversationsAdapter.conversationList = conversations
    }
    override fun addConversationItem(item: ConversationItem){
        conversationsAdapter.addNewConversationItem(item)
    }

    override fun startConversationObservation() {
        Log.i(Constants.LOG_TAG, "Conversations livedata is now observed in the view!")

        viewModel?.livedataConversations?.let { liveConversationsData ->
            liveConversationsData.observe(this,
                Observer<List<Conversation>> { conversations ->
                    conversations?.let {
                        viewModel.onConversationsReceived(conversations)
                        Log.i(Constants.LOG_TAG, "Received updates from database!")
                    }
                })
        }
    }


    private fun startAddParticipant() {
        val intent = Intent(this, AddParticipantView::class.java)
        startActivity(intent)
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
