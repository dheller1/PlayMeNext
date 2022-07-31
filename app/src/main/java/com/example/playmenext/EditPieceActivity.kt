package com.example.playmenext

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.playmenext.application.MainApplication
import com.example.playmenext.domain.PieceToPractice

const val EXTRA_PIECE_INST = "com.example.playmenext.PIECE_INST"

class EditPieceActivity : AppCompatActivity() {
    private lateinit var editTitleView: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_piece)

        editTitleView = findViewById(R.id.edit_title)

        val repo = (application as MainApplication).repository


        val existingPiece = intent.getParcelableExtra<PieceToPractice>(EXTRA_PIECE_INST)
        if(existingPiece != null) {
            Toast.makeText(this,
                existingPiece.title ?: "Invalid entry!", Toast.LENGTH_SHORT).show()
        }

        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener{
            val replyIntent = Intent()
            if(TextUtils.isEmpty(editTitleView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            }
            else {
                val word = editTitleView.text.toString()
                replyIntent.putExtra(EXTRA_REPLY, word)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY = "com.example.playmenext.REPLY"
    }
}