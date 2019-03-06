package nuntium.fhooe.at.nuntium.newconversation

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.participant_list_item.view.*
import nuntium.fhooe.at.nuntium.R
import nuntium.fhooe.at.nuntium.room.participant.Participant

class ParticipantAdapter(
    private val context: Context,
    var participants: MutableList<Pair<Participant, Boolean>>,
    val activateButton: () -> Unit
) : RecyclerView.Adapter<ParticipantViewHolder>() {

    override fun onCreateViewHolder(viewgroup: ViewGroup, position: Int): ParticipantViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.participant_list_item, viewgroup, false)
        return ParticipantViewHolder(view)
    }

    override fun getItemCount(): Int = participants.size

    override fun onBindViewHolder(viewHolder: ParticipantViewHolder, position: Int) {
        val participant = participants[position].first

        with(viewHolder.view) {
            participant_item_tv_fullname.text = "${participant.firstName} ${participant.lastName}"
            participant_item_tv_email.text = participant.email
            Glide.with(context)
                .load(participant.avatar + "?set=set4")
                .placeholder(ContextCompat.getDrawable(context, R.color.white))
                .into(participant_item_iv_avatar)
        }

        when {
            participants[position].second -> viewHolder.view.background = ContextCompat.getDrawable(context, R.color.lightGray)
            !participants[position].second -> viewHolder.view.background = ContextCompat.getDrawable(context, R.color.white)
        }

        viewHolder.view.setOnClickListener {
            val oldElement = participants.firstOrNull { it.second }
            oldElement?.let {
                val oldIndex = participants.indexOf(it)
                participants[oldIndex] = participants[oldIndex].first to false
                this.notifyItemChanged(oldIndex)
            }
            participants[position] = participants[position].first to true

            activateButton()
            this.notifyItemChanged(position)
        }
    }

    fun getSelectedParticipant() = participants.firstOrNull { it.second }?.first
}

data class ParticipantViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
}