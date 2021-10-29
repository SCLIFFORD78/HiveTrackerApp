package org.wit.hivetrackerapp.models

interface HiveStore {
    fun findAll(): List<HiveModel>
    fun findByType(type: String): List<HiveModel>
    fun create(hive: HiveModel)
    fun update(hive: HiveModel)
    fun delete(hive: HiveModel)
    fun find(hive: HiveModel) :HiveModel?
}