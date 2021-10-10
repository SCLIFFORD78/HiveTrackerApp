package org.wit.hivetrackerapp.models

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import timber.log.Timber.i

import org.wit.hivetrackerapp.helpers.*
import java.util.*


val JSON_FILE = "hives.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<java.util.ArrayList<HiveModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class HiveJSONStore : HiveStore {

    var hives = mutableListOf<HiveModel>()

    init {
        if (exists(JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<HiveModel> {
        return hives
    }

    fun findOne(id: Long) : HiveModel? {
        var foundHive: HiveModel? = hives.find { p -> p.id == id }
        return foundHive
    }

    override fun create(hive: HiveModel) {
        hive.id = generateRandomId()
        hives.add(hive)
        serialize()
        logAll()
    }

    override fun update(hive: HiveModel) {
        var foundHive = findOne(hive.id!!)
        if (foundHive != null) {
            foundHive.title = hive.title
            foundHive.description = hive.description
            foundHive.image = hive.image
            logAll()
        }
        serialize()
    }

    internal fun logAll() {
        hives.forEach { i("$it") }
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(hives, listType)
        write(JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(JSON_FILE)
        hives = Gson().fromJson(jsonString, listType)
    }
}