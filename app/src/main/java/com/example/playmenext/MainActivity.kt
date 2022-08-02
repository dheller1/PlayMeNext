package com.example.playmenext

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playmenext.adapters.PiecesListAdapter
import com.example.playmenext.application.MainApplication
import com.example.playmenext.domain.PieceToPractice
import com.example.playmenext.viewmodel.PieceToPracticeViewModel
import com.example.playmenext.viewmodel.PieceToPracticeViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private val _piecesViewModel: PieceToPracticeViewModel by viewModels {
        PieceToPracticeViewModelFactory((application as MainApplication).repository)
    }

    private val newPieceActivityRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = PiecesListAdapter(
            _onItemClickListener = this::onPieceClicked
        )


        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        _piecesViewModel.allPieces.observe(this, Observer { pieces ->
            pieces?.let { adapter.submitList(it) }
        })

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, EditPieceActivity::class.java)
            resultLauncher.launch(intent)
        }
    }

    private var resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
            result ->
            if(result.resultCode == Activity.RESULT_OK) {
                val intent : Intent? = result.data

                intent?.getParcelableExtra<PieceToPractice>(EditPieceActivity.EXTRA_REPLY)?.let {
                    it.id = intent.getIntExtra(EXTRA_PIECE_ID, -1)
                    if(intent.getBooleanExtra(EditPieceActivity.EXTRA_REQUEST_DELETE, false)) {
                        _piecesViewModel.delete(it)
                    }
                    else {
                        _piecesViewModel.insert(it)
                    }
                }
            }
        }

    private fun onPieceClicked(item : PieceToPractice) : Unit {
        // Toast.makeText(this, item.title, Toast.LENGTH_SHORT).show()
        if(item.id != null) {
            val intent = Intent(this, EditPieceActivity::class.java).apply {
                // Parcelize seems not to transfer the id field, so we do it manually
                // FIXME: This is a hack, until I understand how to do it better...
                item.id?.let {
                    putExtra(EXTRA_PIECE_ID, it)
                }
                putExtra(EXTRA_PIECE_INST, item)
            }
            resultLauncher.launch(intent)
        }
    }
}