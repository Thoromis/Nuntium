package nuntium.fhooe.at.nuntium.viewconversation

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import nuntium.fhooe.at.nuntium.R
import nuntium.fhooe.at.nuntium.room.message.Message
import java.util.ArrayList

class MessagesAdapter(private val context: Context, private val author: Int) :
    RecyclerView.Adapter<MessageViewHolder>() {

    private val messages = ArrayList<Message>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(context).inflate(getLayout(viewType), viewGroup, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(messageViewHolder: MessageViewHolder, position: Int) {
        messageViewHolder.bind(messages[position])
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].senderId == author) {
            TYPE_MESSAGE_ME
        } else {
            TYPE_MESSAGE_OTHERS
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    fun addMessage(message: Message) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

    private fun getLayout(type: Int): Int {
        return if (type == TYPE_MESSAGE_ME) {
            R.layout.view_conversaton_message_me
        } else {
            R.layout.view_conversation_message_other
        }
    }

    fun replace(oldMessage: Message, newMessage: Message): Int {
        val index = messages.indexOf(oldMessage)
        if (index != -1) {
            messages[index] = newMessage
            notifyItemChanged(index)
        }
        return index
    }

    companion object {

        private const val TYPE_MESSAGE_ME = 0
        private const val TYPE_MESSAGE_OTHERS = 1
    }
}