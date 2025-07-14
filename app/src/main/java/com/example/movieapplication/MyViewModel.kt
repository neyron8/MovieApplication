package com.example.movieapplication

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemasearch.network.Cinemas
import com.example.movieapplication.modelsNew.CinemaResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.Response
import javax.inject.Inject

@HiltViewModel
class MainViewVodel @Inject constructor(
    private val mainRepository: MainRepository
): ViewModel() {
    var listOfStates: MutableState<MainState> = mutableStateOf(MainState())
    init {
        getStartMovies()
    }
    fun getStartMovies() = viewModelScope.launch{
        val k = mainRepository.getStartMovies()
        //k.body().items
        if (k.isSuccessful){
            k.body()?.let {
                listOfStates.value = MainState(data = it.items)
            }
        }
    }
}

data class MainState(
    val isLoading: Boolean = false,
    val data: List<CinemaResponse.Item> = emptyList(),
    val error: String = ""
)


