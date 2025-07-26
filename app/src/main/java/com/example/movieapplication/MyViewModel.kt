package com.example.movieapplication

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapplication.modelsNew.CinemaKeywordResponse
import com.example.movieapplication.modelsNew.CinemaResponse
import com.example.movieapplication.modelsNew.FilmData
import com.example.movieapplication.modelsNew.Item
import com.example.movieapplication.repository.MainRepository
import com.example.movieapplication.utils.convertFilmToItem
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
        val k = mainRepository.getCinemaByName(name)
        if(k.isSuccessful){
            k.body()?.let {
                val con = convertFilmToItem(it.films)
                Log.d("Before", listOfStates.value.data.toString())
                //listOfStates.value = MainState(keywordData = it.films)
               // listOfStates.value = MainState(data = emptyList())
                clearList()
                listOfStates.value = MainState(data = con)
                Log.d("After", listOfStates.value.data.toString())
            }
        }
        //Log.d("JACKPOT", k.body()?.films.toString())
    }

    fun clearList(){
        listOfStates.value = MainState(data = emptyList())
        Log.d("Clear", listOfStates.value.data.toString())
    }

}

data class MainState(
    val isLoading: Boolean = false,
    var data: List<Item> = emptyList(),
    val error: String = "",
    val filmData: FilmData = FilmData(),
    val keywordData: List<CinemaKeywordResponse.Film> = emptyList()
)


