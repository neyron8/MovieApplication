package com.example.movieapplication.navigation

import android.content.Context
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF2A2A2A),
                        Color(0xFFBB86FC)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .shadow(8.dp, RoundedCornerShape(16.dp))
                    .background(Color(0xFF2A2A2A))
                    .padding(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A2A))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "Вход",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 28.sp
                        ),
                        color = Color(0xFFBB86FC),
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(24.dp))

                    // Email Field
                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        shape = RoundedCornerShape(12.dp),
                        leadingIcon = {
                            Icon(
                                Icons.Filled.Email,
                                contentDescription = null,
                                tint = Color(0xFFBB86FC)
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = Color(0xFF4A4A4A),
                            unfocusedContainerColor = Color(0xFF4A4A4A),
                            cursorColor = Color(0xFFBB86FC),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedLabelColor = Color(0xFFBB86FC),
                            unfocusedLabelColor = Color(0xFFBB86FC)
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
                        shape = RoundedCornerShape(12.dp),
                        leadingIcon = {
                            Icon(
                                Icons.Filled.Lock,
                                contentDescription = null,
                                tint = Color(0xFFBB86FC)
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordHidden = !passwordHidden }) {
                                Icon(
                                    imageVector = if (passwordHidden) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                    contentDescription = if (passwordHidden) "Show" else "Hide",
                                    tint = Color(0xFFBB86FC)
                                )
                            }
                        },
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = Color(0xFF4A4A4A),
                            unfocusedContainerColor = Color(0xFF4A4A4A),
                            cursorColor = Color(0xFFBB86FC),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedLabelColor = Color(0xFFBB86FC),
                            unfocusedLabelColor = Color(0xFFBB86FC)
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
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFBB86FC),
                            contentColor = Color.Black,
                            disabledContainerColor = Color(0xFFB0BEC5),
                            disabledContentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text(
                            text = "Войти",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextButton(onClick = {
                            navController.navigate("Password Screen")
                        }) {
                            Text(
                                text = "Забыли пароль?",
                                color = Color(0xFFBB86FC),
                                fontSize = 14.sp
                            )
                        }
                        TextButton(onClick = {
                            navController.navigate("SignUp Screen")
                            email = ""
                            password = ""
                        }) {
                            Text(
                                text = "Нет аккаунта? Регистрация",
                                color = Color(0xFFBB86FC),
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
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