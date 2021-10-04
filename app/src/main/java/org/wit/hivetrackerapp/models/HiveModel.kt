package org.wit.hivetrackerapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HiveModel(var id: Long = 0,var title: String = "", var description: String = ""):Parcelable
