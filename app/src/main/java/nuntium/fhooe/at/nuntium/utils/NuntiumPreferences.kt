package nuntium.fhooe.at.nuntium.utils

import android.content.Context
import android.util.Log
import nuntium.fhooe.at.nuntium.room.participant.Participant
import nuntium.fhooe.at.nuntium.utils.Constants.LOG_TAG

/**
 * Gives access to shared preferences and provides a set of methods to save/fetch shared preference values.
 */
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

        fun updateLastFetchDate(context: Context, date: String) {
            val sharedPreferences = context.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)
            Log.i(LOG_TAG, "Updated last fetch date to $date")
            sharedPreferences
                .edit()
                .putString(CREATION_DATE, date)
                .apply()
        }

        fun getLastFetchDate(context: Context) : String {
            return context.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE).getString(CREATION_DATE, "")
        }

        fun getParticipantName(context: Context): String {
            val sharedPreferences = context.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)
            return sharedPreferences.getString(PARTICIPANT_NAME, "")
        }

        private const val SHARED_PREF_KEY = "NuntiumPreferences"
        private const val PARTICIPANT_ID = "id"
        private const val PARTICIPANT_NAME = "fullname"
        private const val CREATION_DATE = "creationDate"
    }
}