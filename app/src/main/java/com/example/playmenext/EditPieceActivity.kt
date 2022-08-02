package com.example.playmenext

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.playmenext.domain.PieceToPractice

const val EXTRA_PIECE_ID = "com.example.playmenext.PIECE_ID"
const val EXTRA_PIECE_INST = "com.example.playmenext.PIECE_INST"

class EditPieceActivity : AppCompatActivity() {
    private lateinit var _editTitleView: EditText
    private lateinit var _editSubtitleView: EditText
    private lateinit var _editComposerView: EditText
    private lateinit var _editArrangerView: EditText
    private lateinit var _editPriorityView: EditText

    private var _existingPiece : PieceToPractice? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_piece)

        _editTitleView = findViewById(R.id.edit_title)
        _editSubtitleView = findViewById(R.id.edit_subtitle)
        _editComposerView = findViewById(R.id.edit_composer)
        _editArrangerView = findViewById(R.id.edit_arranger)
        _editPriorityView = findViewById(R.id.edit_priority)

        _existingPiece = intent.getParcelableExtra(EXTRA_PIECE_INST)
        val id = intent.getIntExtra(EXTRA_PIECE_ID, -1)
        if(_existingPiece != null && id >= 0) {
            _existingPiece?.id = id
        }

        if(_existingPiece != null) {
            applyDataToUi(_existingPiece)
        }
        else {
            _existingPiece = PieceToPractice("")
        }

        val saveBtn = findViewById<Button>(R.id.button_save)
        saveBtn.setOnClickListener{
            val replyIntent = Intent()
            if(TextUtils.isEmpty(_editTitleView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            }
            else {
                applyDataToPiece()
                replyIntent.putExtra(EXTRA_REPLY, _existingPiece)
                // Parcelize seems not to transfer the id field, so we do it manually
                // FIXME: This is a hack, until I understand how to do it better...
                _existingPiece?.id.let {
                    intent.putExtra(EXTRA_PIECE_ID, it)
                }
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }

        val deleteBtn = findViewById<Button>(R.id.button_delete)
        deleteBtn.setOnClickListener{
            applyDataToPiece()
            val intent = Intent()
            intent.putExtra(EXTRA_REPLY, _existingPiece)
            // Parcelize seems not to transfer the id field, so we do it manually
            // FIXME: This is a hack, until I understand how to do it better...
            _existingPiece?.id.let {
                intent.putExtra(EXTRA_PIECE_ID, it)
            }
            intent.putExtra(EXTRA_REQUEST_DELETE, true)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private fun applyDataToUi(pieceData: PieceToPractice?) {
        if(pieceData != null) {
            _editTitleView.setText(pieceData.title)
            _editSubtitleView.setText(pieceData.subTitle)
            _editComposerView.setText(pieceData.composer)
            _editArrangerView.setText(pieceData.arranger)
            _editPriorityView.setText(pieceData.practicePriority.toString())
        }
    }

    private fun applyDataToPiece() {
        _existingPiece?.title = _editTitleView.text.toString()
        _existingPiece?.subTitle = _editSubtitleView.text.toString()
        _existingPiece?.composer = _editComposerView.text.toString()
        _existingPiece?.arranger = _editArrangerView.text.toString()
        _existingPiece?.practicePriority = _editPriorityView.text.toString().toDouble()
    }

    companion object {
        const val EXTRA_REPLY = "com.example.playmenext.REPLY"
        const val EXTRA_REQUEST_DELETE = "com.example.playmenext.REQUEST_DELETE"
    }
}