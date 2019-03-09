package nuntium.fhooe.at.nuntium.modules

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import nuntium.fhooe.at.nuntium.messagepolling.MessagePollingService
import nuntium.fhooe.at.nuntium.utils.Constants.LOG_TAG

/**
 * Class that is notified after reboot of the phone;
 * Schedules the MessagePollingService so user sees messages even after rebooting the device.
 */
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i(LOG_TAG, "Bootreceiver called.")

        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.i(LOG_TAG, "Bootreceiver is starting job now...")
            context?.let {
                val jobScheduler = it.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
                startMessagePollingJob(jobScheduler, it)
            }
        }
    }

    companion object {
        fun startMessagePollingJob(jobScheduler: JobScheduler, context: Context) {
            jobScheduler.schedule(
                JobInfo.Builder(
                    MessagePollingService.MESSAGE_POLLING_JOB_ID, ComponentName(context, MessagePollingService::class.java)
                )
                    .setMinimumLatency(30000)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setPersisted(true)
                    .build()
            )
        }
    }
}