package nuntium.fhooe.at.nuntium.room

import android.arch.persistence.room.Room
import android.content.Context
import android.util.Log
import io.reactivex.Completable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.atomic.AtomicBoolean

/**
 * object that provides a single database instance for the whole application to use.
 */

object DatabaseCreator {
    private var disposable = Disposables.disposed()
    lateinit var database: NuntiumDatabase
        @Synchronized get

    fun createDatabase(context: Context) {
        disposable = Completable.fromAction {
            database = Room.databaseBuilder(context, NuntiumDatabase::class.java, NuntiumDatabase.DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }
            .subscribeOn(Schedulers.computation())
            .subscribe {
                Log.i("LOG_TAG", "Database is created!")
            }
    }

    fun clear() {
        disposable.dispose()
    }
}