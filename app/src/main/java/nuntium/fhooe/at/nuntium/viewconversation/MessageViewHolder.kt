package nuntium.fhooe.at.nuntium.viewconversation

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import nuntium.fhooe.at.nuntium.R
import nuntium.fhooe.at.nuntium.room.message.Message
import org.w3c.dom.Text

class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val messageView: TextView = itemView.findViewById(R.id.view_conversation_message)
    private val timeView: TextView = itemView.findViewById(R.id.view_conversation_time)
    fun bind(message: Message?) {
        messageView.text = message?.content
        timeView.text = message?.createdDate?.slice(11..15)
    }
}