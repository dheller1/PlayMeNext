package com.example.playmenext.domain

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

const val TABLE_NAME : String = "pieces_table"

@Parcelize
@Entity(tableName = TABLE_NAME)
data class PieceToPractice(
    var title: String, // the main title of the piece
    var subTitle: String = "", // the subtitle or further details, e.g. which movement
    var composer: String = "", // the piece's composer
    var arranger: String = "", // who arranged the piece
    var practicePriority: Double = 1.0, // how frequently the user wants to practice this piece
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,  // unique identifier of the piece

    // the following parameters should always keep their default values and not be set explicitly
    val dateAdded: LocalDate = LocalDate.now(),
    var dateLastPlayed: LocalDate? = null,
    val daysPlayedCount: Int = 0,
) : Parcelable
