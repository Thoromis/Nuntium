package nuntium.fhooe.at.nuntium.viewconversation.mvvm

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_view_conversation_view.*
import nuntium.fhooe.at.nuntium.R
import nuntium.fhooe.at.nuntium.room.message.Message
import nuntium.fhooe.at.nuntium.viewconversation.MessagesAdapter

class ViewConversationView : AppCompatActivity(),ViewConversationMVVM.View {


    private lateinit var viewModel: ViewConversationMVVM.ViewModel
    private lateinit var rcViewAdapter: MessagesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_conversation_view)
        view_conversation_toolbar.title = "Title (Placeholder)"

        initMVVM()

        val rcView = view_conversation_rcview
        val layoutManager = LinearLayoutManager(this)

        layoutManager.stackFromEnd = true
        rcView.layoutManager = layoutManager

        rcViewAdapter = MessagesAdapter(this,1)
        rcView.adapter = rcViewAdapter

        rcViewAdapter.addMessage(Message(1,"Hey wie gehts?",42,2,1,1,"Heit"))
        rcViewAdapter.addMessage(Message(1,"Guad und dir?",42,1,2,2,"Heit"))
        rcViewAdapter.addMessage(Message(1,"Jo eh auch. Optimal, danke für die Info!",42,2,1,3,"Heit"))
        for(i in 1..100) {
            if(i % 2 != 0)
                rcViewAdapter.addMessage(Message(1,"Das ist ein Test von mir. Das ist ein Test von mir. $i",42,2,1,i+3,"me"))
            else
                rcViewAdapter.addMessage(Message(1,"Das ist ein Test von other. Das ist ein Test von other. $i",42,1,2,i+3,"other$i"))
        }
    }

    override fun initMVVM() {
        viewModel = ViewConversationViewModel(this,this.applicationContext)
    }
}