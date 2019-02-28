package nuntium.fhooe.at.nuntium.conversationoverview

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import nuntium.fhooe.at.nuntium.R
import nuntium.fhooe.at.nuntium.room.conversation.Conversation

class ConversationsAdapter : RecyclerView.Adapter<ConversationsAdapter.ConversationsHolder>() {
    var conversationList = ArrayList<Conversation>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationsHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.conversation_item, parent, false)
        return ConversationsHolder(itemView)
    }

    override fun getItemCount(): Int {
        return conversationList.size
    }

    override fun onBindViewHolder(holder: ConversationsHolder, position: Int) {
        val currentConversation = conversationList[position]
        holder.textViewConversationTitle.text = currentConversation.topic
        holder.textViewConversationDescription.text = "some desc"
    }



    class ConversationsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewConversationTitle: TextView  = itemView.findViewById(R.id.textview_conversation_title)
        val textViewConversationDescription: TextView  = itemView.findViewById(R.id.textview_conversation_description)
        val imageViewConversationAvatar: ImageView = itemView.findViewById(R.id.imageview_conversation_avatar)
    }
}