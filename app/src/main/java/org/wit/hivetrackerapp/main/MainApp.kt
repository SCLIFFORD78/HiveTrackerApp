package org.wit.hivetrackerapp.main

import android.app.Application
import org.wit.hivetrackerapp.models.HiveMemStore
import org.wit.hivetrackerapp.models.HiveModel
import org.wit.hivetrackerapp.models.HiveJSONStore
import org.wit.hivetrackerapp.models.HiveStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var hives: HiveStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        hives = HiveJSONStore(applicationContext)
        i("HiveTracker started")
        //hives.add(HiveModel("One", "About one..."))
        //hives.add(HiveModel("Two", "About two..."))
        //hives.add(HiveModel("Three", "About three..."))
    }
}