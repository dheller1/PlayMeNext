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
    private lateinit var _editTitleView: EditText
    private lateinit var _editSubtitleView: EditText
    private lateinit var _editComposerView: EditText
    private lateinit var _editArrangerView: EditText
    private lateinit var _editPriorityView: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_piece)

        _editTitleView = findViewById(R.id.edit_title)
        _editSubtitleView = findViewById(R.id.edit_subtitle)
        _editComposerView = findViewById(R.id.edit_composer)
        _editArrangerView = findViewById(R.id.edit_arranger)
        _editPriorityView = findViewById(R.id.edit_priority)

        val existingPiece = intent.getParcelableExtra<PieceToPractice>(EXTRA_PIECE_INST)
        if(existingPiece != null) {
            applyData(existingPiece)
        }

        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener{
            val replyIntent = Intent()
            if(TextUtils.isEmpty(_editTitleView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            }
            else {
                val word = _editTitleView.text.toString()
                replyIntent.putExtra(EXTRA_REPLY, word)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    private fun applyData(pieceData: PieceToPractice) {
        _editTitleView.setText(pieceData.title)
        _editSubtitleView.setText(pieceData.subTitle)
        _editComposerView.setText(pieceData.composer)
        _editArrangerView.setText(pieceData.arranger)
        _editPriorityView.setText(pieceData.practicePriority.toString())
    }

    companion object {
        const val EXTRA_REPLY = "com.example.playmenext.REPLY"
    }
}