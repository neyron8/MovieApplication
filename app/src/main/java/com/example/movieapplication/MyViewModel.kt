package com.example.movieapplication

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.movieapplication.modelsNew.FilmData
import com.example.movieapplication.modelsNew.Item
import com.example.movieapplication.modelsNew.ScreenShots
import com.example.movieapplication.modelsNew.Video
import com.example.movieapplication.repository.MainRepository
import com.example.movieapplication.utils.convertFilmToItem
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.auth
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    @ApplicationContext  val context : Context
) : ViewModel() {
    var listOfStates: MutableState<MainState> = mutableStateOf(MainState())
    var auth = Firebase.auth
    var screenShots: MutableList<ScreenShots.ItemX> = mutableListOf()
    var videos: MutableList<Video.Item> = mutableListOf()

    fun getStartMovies() = viewModelScope.launch {
        if (listOfStates.value.data.isEmpty()) {
            val k = mainRepository.getStartMovies()
            if (k.isSuccessful) {
                k.body()?.let {
                    listOfStates.value =
                        listOfStates.value.copy(data = it.items) //listofStates.value.
                    Log.d("Start", "USED")
                }
            } else {
                val errorBodyString = k.errorBody()?.string()
                if (errorBodyString != null) {
                    // Assuming error body is a JSON object with a "message" field
                    val errorObject = Gson().fromJson(errorBodyString, ErrorResponse::class.java)
                    errorObject.message ?: "Unknown error"
                    Toast.makeText(context, errorObject.message, Toast.LENGTH_SHORT).show()
                } else {
                    "Error: No error body"
                }
            }
        }
    }

    fun getDataById(id: Int) = viewModelScope.launch {
        screenShots = mutableListOf()
        val k = mainRepository.getDataById(id)

        if (k.isSuccessful) {
            Log.d("GetDataByID", "USED")
            listOfStates.value = listOfStates.value.copy(
                filmData = k.body()!!
            )
        }
    }

    fun getVideosById(id: Int) = viewModelScope.launch {
        val k = mainRepository.getVideosById(id)
        videos.clear()

        if (k.isSuccessful) {
            Log.d("VIDEOS", k.body().toString())
            k.body()?.items?.forEach {
                if (it.site == "YOUTUBE"){
                    videos.add(it)
                }
            }
            listOfStates.value = listOfStates.value.copy(
                videos = videos //k.body()?.items
            )
        }
    }

    fun getCinemaByName(name: String) = viewModelScope.launch {
        val k = mainRepository.getCinemaByName(name)
        if (k.isSuccessful) {
            k.body()?.let {
                val con = convertFilmToItem(it.films)
                Log.d("Before", listOfStates.value.data.toString())
                clearList()
                listOfStates.value = MainState(data = con)
                Log.d("After", listOfStates.value.data.toString())
            }
        }
    }

    fun getScreenShotsById(id: Int) = viewModelScope.launch {
        var k = mainRepository.getScreenShotsById(id,"SCREENSHOT")
        if(k.isSuccessful){
            k.body()?.let {
                screenShots.addAll(it.items)
                Log.d("SCREENS 1", screenShots.toString())
                //val j = listOfStates.value.screenShots?.plus(it.items)
                /*listOfStates.value = listOfStates.value.copy(
                     screenShots = it.items
                )*/
            }
        }
        k = mainRepository.getScreenShotsById(id,"POSTER")
        if(k.isSuccessful){
            k.body()?.let {
                screenShots.addAll(it.items)
                Log.d("SCREENS 2", screenShots.toString())
                //val j = listOfStates.value.screenShots?.plus(it.items)
                /*listOfStates.value = listOfStates.value.copy(
                     screenShots = it.items
                )*/
            }
        }
        Log.d("SCREENS", screenShots.toString())
        listOfStates.value = listOfStates.value.copy(screenShots = screenShots)

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

    fun returnPassword(email: String, navController: NavController, context: Context){
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    navController.navigate("Login Screen")
                    Toast.makeText(context, "Письмо для смены пароля отправлено на почту", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(context, task.exception?.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

}

data class MainState(
    val isLoading: Boolean = false,
    var data: List<Item> = emptyList(),
    val error: String = "",
    val filmData: FilmData = FilmData(),
    val screenShots: List<ScreenShots.ItemX>? = emptyList(),
    val videos: List<Video.Item>? = emptyList()
)

data class ErrorResponse(val message: String?)

