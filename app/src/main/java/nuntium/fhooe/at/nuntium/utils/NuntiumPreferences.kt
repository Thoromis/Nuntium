package nuntium.fhooe.at.nuntium.utils

import android.content.Context
import nuntium.fhooe.at.nuntium.room.participant.Participant

class NuntiumPreferences {
    companion object {
        fun insertParticipant(context: Context, participant: Participant) {
            val sharedpreferences = context.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)

            sharedpreferences
                .edit()
                .putInt(PARTICIPANT_ID, participant.id)
                .putString(PARTICIPANT_NAME, "${participant.firstName} ${participant.lastName}")
                .apply()
        }

        fun getParticipantId(context: Context): Int {
            val sharedpreferences = context.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)
            return sharedpreferences.getInt(PARTICIPANT_ID, -1)
        }

        fun getParticipantName(context: Context): String {
            val sharedpreferences = context.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)
            return sharedpreferences.getString(PARTICIPANT_NAME, "")
        }

        private const val SHARED_PREF_KEY = "NuntiumPreferences"
        private const val PARTICIPANT_ID = "id"
        private const val PARTICIPANT_NAME = "fullname"
    }
}