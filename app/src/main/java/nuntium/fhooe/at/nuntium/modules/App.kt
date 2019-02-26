package nuntium.fhooe.at.nuntium.modules

import android.app.Application
import android.provider.ContactsContract
import nuntium.fhooe.at.nuntium.room.DatabaseCreator

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        DatabaseCreator.createDatabase(applicationContext)
    }

    override fun onTerminate() {
        super.onTerminate()
        DatabaseCreator.clear()
    }
}