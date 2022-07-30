package com.example.playmenext.viewmodel

import androidx.lifecycle.*
import com.example.playmenext.domain.PieceToPractice
import com.example.playmenext.domain.PiecesRepository
import kotlinx.coroutines.launch

class PieceToPracticeViewModel(private val _repo : PiecesRepository) : ViewModel() {
    // use LiveData and caching to get observable data (instead of active polling)
    val allPieces: LiveData<List<PieceToPractice>> = _repo.allPieces.asLiveData()

    // launch new coroutine to insert data non-blocking
    fun insert(piece : PieceToPractice) = viewModelScope.launch {
        _repo.insert(piece)
    }
}

class PieceToPracticeViewModelFactory(private val _repo : PiecesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(PieceToPracticeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PieceToPracticeViewModel(_repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}