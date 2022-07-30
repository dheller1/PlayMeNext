package com.example.playmenext.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

const val TABLE_NAME : String = "pieces_table"

@Entity(tableName = TABLE_NAME)
data class PieceToPractice(
    var title: String, // the main title of the piece
    var subTitle: String = "", // the subtitle or further details, e.g. which movement
    var composer: String = "", // the piece's composer
    var arranger: String = "", // who arranged the piece
    var practicePriority: Double = 1.0, // how frequently the user wants to practice this piece
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null  // unique identifier of the piece
}
