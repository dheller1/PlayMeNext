package com.example.playmenext

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playmenext.adapters.PiecesListAdapter
import com.example.playmenext.application.MainApplication
import com.example.playmenext.domain.PieceToPractice
import com.example.playmenext.ui.dialogs.ConfirmPlayedDialog
import com.example.playmenext.viewmodel.PieceToPracticeViewModel
import com.example.playmenext.viewmodel.PieceToPracticeViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.time.LocalDateTime
import java.util.*

class MainActivity : AppCompatActivity() {
    private val _piecesViewModel: PieceToPracticeViewModel by viewModels {
        PieceToPracticeViewModelFactory((application as MainApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)

        val adapter =  PiecesListAdapter(
            _onItemClickListener = this::onPieceClicked,
            _onItemLongClickListener = this::onPieceLongClicked
        )
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val getPiecesJob = GlobalScope.async { _piecesViewModel.allPieces }
        getPiecesJob.invokeOnCompletion { cause ->
            if(cause != null) {
                Toast.makeText(this, R.string.retrieve_pieces_error, Toast.LENGTH_SHORT).show()
            }
            else {
                val pieces = getPiecesJob.getCompleted().sortedByDescending { it.urgency }
                adapter.submitList(pieces)
            }
        }

        //adapter.submitList(_piecesViewModel.allPieces)

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, EditPieceActivity::class.java)
            resultLauncher.launch(intent)
        }
    }

    private fun updatePiecesList() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        // use this.lifecycleScope ?
        val getPiecesJob = GlobalScope.async { _piecesViewModel.allPieces }
        getPiecesJob.invokeOnCompletion { cause ->
            if(cause != null) {
                Toast.makeText(this, R.string.retrieve_pieces_error, Toast.LENGTH_SHORT).show()
            }
            else {
                val pieces = getPiecesJob.getCompleted().sortedByDescending { it.urgency }
                val adapter = recyclerView.adapter as PiecesListAdapter
                adapter.submitList(pieces)
            }
        }
    }


    private var resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
            result ->
            if(result.resultCode == Activity.RESULT_OK) {
                val intent : Intent? = result.data

                intent?.getParcelableExtra<PieceToPractice>(EditPieceActivity.EXTRA_REPLY)?.let {
                    if(intent.getBooleanExtra(EditPieceActivity.EXTRA_REQUEST_DELETE, false)) {
                        _piecesViewModel.delete(it)
                    }
                    else {
                        _piecesViewModel.insert(it)
                    }
                }
            }
        }

    private fun onPieceClicked(item : PieceToPractice) {
        val dlg = ConfirmPlayedDialog(this)
        dlg.show(item.title) {
            if (it == ConfirmPlayedDialog.ResponseType.YES) {
                item.dateLastPlayed = LocalDateTime.now()
                ++item.daysPlayedCount
                _piecesViewModel.insert(item)
                updatePiecesList()
            }
        }
    }

    private fun onPieceLongClicked(item : PieceToPractice) : Boolean {
        if(item.id != null) {
            val intent = Intent(this, EditPieceActivity::class.java).apply {
                putExtra(EXTRA_PIECE_INST, item)
            }
            resultLauncher.launch(intent)
            return true
        }
        return false
    }
}