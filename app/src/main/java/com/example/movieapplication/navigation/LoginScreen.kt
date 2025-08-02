package com.example.movieapplication.navigation

import android.content.Context
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.movieapplication.MainViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

@Composable
fun LoginScreen(mainViewModel: MainViewModel = hiltViewModel(), navController: NavController) {

    val context = LocalContext.current
    var passwordHidden by rememberSaveable { mutableStateOf(true) }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val isFormValid = email.isNotBlank() && password.isNotBlank()
    val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    checkAutoLogin(navController)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(32.dp))


        Spacer(Modifier.height(16.dp))

        Text(
            text = "Добро пожаловать!",
            style = MaterialTheme.typography.titleMedium,
            color = Color.DarkGray
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Вход в учётную запись",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
            )
        )

        Spacer(Modifier.height(24.dp))

        // Email Field
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = MaterialTheme.shapes.medium,
            leadingIcon = {
                Icon(
                    Icons.Filled.Email,
                    contentDescription = null,
                    tint = Color(0xFF6200EE)
                )
            },
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedContainerColor = Color.LightGray,
                unfocusedContainerColor = Color.LightGray,
                cursorColor = Color(0xFF6200EE),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black,
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        // Password Field
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль") },
            visualTransformation = if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            shape = MaterialTheme.shapes.medium,
            leadingIcon = {
                Icon(Icons.Filled.Lock, contentDescription = null, tint = Color(0xFF6200EE))
            },
            trailingIcon = {
                IconButton(onClick = { passwordHidden = !passwordHidden }) {
                    Icon(
                        imageVector = if (passwordHidden) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = if (passwordHidden) "Show" else "Hide",
                        tint = Color(0xFF6200EE)
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedContainerColor = Color.LightGray,
                unfocusedContainerColor = Color.LightGray,
                cursorColor = Color(0xFF6200EE),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black,
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                mainViewModel.logIn(
                    email = email,
                    password = password,
                    navController = navController,
                    context = context
                )
            },
            enabled = isFormValid && isEmailValid,
            colors = buttonColors(
                containerColor = Color(0xFF6200EE),
                contentColor = Color(0xFFFFFFFF),

                ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Войти")
        }
        TextButton(onClick = {
            navController.navigate("Password Screen")
        }) {
            Text(
                text = "Забыли пароль?",
                color = Color(0xFF6200EE)
            )
        }
        TextButton(onClick = {
            navController.navigate("SignUp Screen")
            email = ""
            password = ""
        }) {
            Text(
                text = "Нет аккаунта? Регистрация",
                color = Color(0xFF6200EE)
            )
        }
    }
}

private fun checkAutoLogin(navController: NavController) {
    val currentUser: FirebaseUser? = Firebase.auth.currentUser

    if (currentUser != null) {
        navController.navigate("Start Screen")
        Log.d("LOGIN", "Success ${currentUser.email}")
    } else {
        Log.d("LOGIN", "Failure")
    }
}