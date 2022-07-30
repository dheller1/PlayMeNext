package com.example.playmenext

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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
        val adapter = PiecesListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        _piecesViewModel.allPieces.observe(this, Observer { pieces ->
            pieces?.let { adapter.submitList(it) }
        })

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, EditPieceActivity::class.java)
            startActivityForResult(intent, newPieceActivityRequestCode)
        }
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == newPieceActivityRequestCode && resultCode == Activity.RESULT_OK) {
            data?.getStringExtra(EditPieceActivity.EXTRA_REPLY)?.let {
                val piece = PieceToPractice(title = it)
                _piecesViewModel.insert(piece)
            }
        }
        else {
            Toast.makeText(applicationContext, R.string.not_saved, Toast.LENGTH_LONG).show()
        }
    }
}