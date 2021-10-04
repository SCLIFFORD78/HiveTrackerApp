package org.wit.hivetrackerapp.models

interface HiveStore {
    fun findAll(): List<HiveModel>
    fun create(placemark: HiveModel)
    fun update(hive: HiveModel)
}