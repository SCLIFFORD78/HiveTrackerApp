package org.wit.hivetrackerapp.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class HiveMemStore : HiveStore {

    val hives = ArrayList<HiveModel>()

    override fun findAll(): List<HiveModel> {
        return hives
    }

    override fun create(hive: HiveModel) {
        hive.id = getId()
        hives.add(hive)
        logAll()
    }

    override fun update(hive: HiveModel) {
        var foundHive: HiveModel? = hives.find { p -> p.id == hive.id }
        if (foundHive != null) {
            foundHive.title = hive.title
            foundHive.description = hive.description
            foundHive.image = hive.image
            foundHive.lat = hive.lat
            foundHive.lng = hive.lng
            foundHive.zoom = hive.zoom
            logAll()
        }
    }

    private fun logAll() {
        hives.forEach { i("$it") }
    }
}