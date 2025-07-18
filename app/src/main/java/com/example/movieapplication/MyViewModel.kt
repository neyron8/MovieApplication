package com.example.movieapplication

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapplication.modelsNew.CinemaResponse
import com.example.movieapplication.modelsNew.FilmData
import com.example.movieapplication.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewVodel @Inject constructor(
    private val mainRepository: MainRepository
): ViewModel() {
    var listOfStates: MutableState<MainState> = mutableStateOf(MainState())

    init {
        getStartMovies()
        //getCinemaByName("Matrix")
    }

    fun getStartMovies() = viewModelScope.launch{
        val k = mainRepository.getStartMovies()
        if (k.isSuccessful){
            k.body()?.let {
                listOfStates.value = MainState(data = it.items)
            }
        }
    }

    fun getDataById(id: Int) = viewModelScope.launch{
        val k = mainRepository.getDataById(id)

        if(k.isSuccessful){
            listOfStates.value = listOfStates.value.copy(
                filmData = k.body()!!
            )
        }
    }

    fun getCinemaByName(name: String)= viewModelScope.launch{
        val k = mainRepository.getCinemaByName("Matrix")
        Log.d("JACKPOT", k.body()?.films.toString())
    }
}

data class MainState(
    val isLoading: Boolean = false,
    val data: List<CinemaResponse.Item> = emptyList(),
    val error: String = "",
    val filmData: FilmData = FilmData()
)


