package org.wit.hivetrackerapp.main

import android.app.Application
import org.wit.hivetrackerapp.models.HiveMemStore
import org.wit.hivetrackerapp.models.HiveModel
import org.wit.hivetrackerapp.models.HiveJSONStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    val hives = HiveMemStore()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("HiveTracker started")
        //hives.add(HiveModel("One", "About one..."))
        //hives.add(HiveModel("Two", "About two..."))
        //hives.add(HiveModel("Three", "About three..."))
    }
}