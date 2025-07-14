package com.example.movieapplication

import android.util.Log
import javax.inject.Inject

class WifiManager @Inject constructor(val settings: WifiSettings) {
    fun openConnection(){
        settings.open()
    }

    fun closeConnection(){
        settings.close()
    }
}

class WifiSettings @Inject constructor(){

    fun open(){
        Log.d("TAG", "Opened")
    }

    fun close(){
        Log.d("TAG","Closed")
    }
}