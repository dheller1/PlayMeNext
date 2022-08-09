package com.example.playmenext.ui.dialogs

import android.app.AlertDialog
import android.content.Context
import com.example.playmenext.R

class ConfirmPlayedDialog(context: Context) : AlertDialog.Builder(context) {

    lateinit var onResponse: (r: ResponseType) -> Unit

    enum class ResponseType {
        YES, NO
    }

    fun show(pieceTitle: String, listener: (r : ResponseType) -> Unit) {
        onResponse = listener

        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.confirm_played_dialog_title)
        val msg = String.format(context.resources.getString(R.string.confirm_played_dialog_message),
            pieceTitle)
        builder.setMessage(msg)
        builder.setIcon(R.drawable.ic_baseline_music_note_24)

        builder.setPositiveButton(R.string.yes) { _, _ ->
            onResponse(ResponseType.YES)
        }
        builder.setNegativeButton(R.string.no) { _, _ ->
            onResponse(ResponseType.NO)
        }

        val dlg = builder.create()
        dlg.setCancelable(false)
        dlg.show()
    }
}