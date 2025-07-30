package com.example.movieapplication

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.movieapplication.modelsNew.CinemaKeywordResponse
import com.example.movieapplication.modelsNew.FilmData
import com.example.movieapplication.modelsNew.Item
import com.example.movieapplication.repository.MainRepository
import com.example.movieapplication.utils.convertFilmToItem
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.auth
import com.google.firebase.options
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {
    var listOfStates: MutableState<MainState> = mutableStateOf(MainState())
    var auth = Firebase.auth

    init {
        getStartMovies()
    }

    fun getStartMovies() = viewModelScope.launch {
        val k = mainRepository.getStartMovies()
        if (k.isSuccessful) {
            k.body()?.let {
                listOfStates.value = MainState(data = it.items)
            }
        }
    }

    fun getDataById(id: Int) = viewModelScope.launch {
        val k = mainRepository.getDataById(id)

        if (k.isSuccessful) {
            listOfStates.value = listOfStates.value.copy(
                filmData = k.body()!!
            )
        }
    }

    fun getCinemaByName(name: String) = viewModelScope.launch {
        val k = mainRepository.getCinemaByName(name)
        if (k.isSuccessful) {
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
    }

    fun clearList() {
        listOfStates.value = MainState(data = emptyList())
        Log.d("Clear", listOfStates.value.data.toString())
    }

    fun signOut() {
        auth.signOut()
    }

    fun signUp(email: String, password: String, navController: NavController, context: Context) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    navController.navigate("Start Screen")
                } else {
                    Toast.makeText(context, task.exception?.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    fun logIn(email: String, password: String, navController: NavController, context: Context) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    navController.navigate("Start Screen")
                } else {
                    val errorMessage = when (task.exception) {
                        is FirebaseAuthInvalidCredentialsException -> "Неверный email или пароль"
                        is FirebaseAuthInvalidUserException -> "Пользователь не существует"
                        else -> task.exception?.message ?: "Ошибка аутентификации"
                    }
                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                }
            }
    }

}

data class MainState(
    val isLoading: Boolean = false,
    var data: List<Item> = emptyList(),
    val error: String = "",
    val filmData: FilmData = FilmData(),
    val keywordData: List<CinemaKeywordResponse.Film> = emptyList()
)


