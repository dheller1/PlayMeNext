package com.example.playmenext.domain

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.Duration
import java.time.LocalDateTime

const val TABLE_NAME : String = "pieces_table"
const val SecondsToDays : Double = 1.0 / 86400.0

@Parcelize
@Entity(tableName = TABLE_NAME)
data class PieceToPractice(
    var title: String, // the main title of the piece
    var subTitle: String = "", // the subtitle or further details, e.g. which movement
    var composer: String = "", // the piece's composer
    var arranger: String = "", // who arranged the piece
    var practicePriority: Double = 1.0, // how frequently the user wants to practice this piece
    // e.g. 1 = once per day
    // 1/7 = once per week
    // 1/30 = once per month
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,  // unique identifier of the piece

    // the following parameters should always keep their default values and not be set explicitly
    val dateAdded: LocalDateTime = LocalDateTime.now(),
    var dateLastPlayed: LocalDateTime? = null,
    var daysPlayedCount: Int = 0,
) : Parcelable
{
    private val urgencyReferenceDateTime : LocalDateTime get() = dateLastPlayed ?: dateAdded

    val urgency : Double get()
        = Duration.between(urgencyReferenceDateTime, LocalDateTime.now()).seconds * SecondsToDays * practicePriority
}
