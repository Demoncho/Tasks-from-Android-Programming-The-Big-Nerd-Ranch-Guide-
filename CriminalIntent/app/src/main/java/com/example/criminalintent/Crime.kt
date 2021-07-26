package com.example.criminalintent

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.DateFormat
import java.util.*

@Entity
data class Crime(
@PrimaryKey val id: UUID = UUID.randomUUID(),
var title: String = "",
    var date: Date = Date(),
    var isSolved: Boolean = false,
    var suspect: String = "",
    var phone: String = ""
    // var requiresPolice: Boolean = false - не работает для базы данных из учебника
) {
    val photoFileName
        get() = "IMG_$id.jpg"

    fun getDateFormat() = DateFormat.getDateInstance(DateFormat.FULL).format(date)
    fun getTimeFormat() = DateFormat.getTimeInstance(DateFormat.SHORT).format(date)
}
