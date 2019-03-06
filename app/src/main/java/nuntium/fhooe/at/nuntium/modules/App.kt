package nuntium.fhooe.at.nuntium.modules

import android.app.Application
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.provider.ContactsContract
import nuntium.fhooe.at.nuntium.messagepolling.MessagePollingService
import nuntium.fhooe.at.nuntium.room.DatabaseCreator

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        DatabaseCreator.createDatabase(applicationContext)
        val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        BootReceiver.startMessagePollingJob(jobScheduler, this)
    }

    override fun onTerminate() {
        super.onTerminate()
        DatabaseCreator.clear()
    }
}