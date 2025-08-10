package com.example.movieapplication.navigation

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.movieapplication.MainViewModel
import com.example.movieapplication.modelsNew.Item
import com.example.movieapplication.utils.BackPressSample
import com.example.movieapplication.utils.nameGiver

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreen(mainViewModel: MainViewModel = hiltViewModel(), navController: NavHostController) {

    mainViewModel.getStartMovies()

    val state = mainViewModel.listOfStates
    val query: MutableState<String> = remember { mutableStateOf("") }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    BackPressSample()

    Scaffold(
        modifier = Modifier
            .background(Color(0xFF1A1A1A))
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopBar(
                navController = navController,
                scrollBehavior = scrollBehavior
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                OutlinedTextField(
                    value = query.value,
                    onValueChange = { query.value = it },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = { mainViewModel.getCinemaByName(query.value) }
                    ),
                    enabled = true,
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = Color(0xFFBB86FC)
                        )
                    },
                    label = { Text(text = "Поиск", color = Color(0xFFBB86FC)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFBB86FC),
                        unfocusedBorderColor = Color(0xFF4A4A4A),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color(0xFFBB86FC)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                if (state.value.error.isNotBlank()) {
                    Log.d("TAG", "MainContent: ${state.value.error}")
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center)
                    ) {
                        Text(
                            text = state.value.error,
                            color = Color(0xFFEF5350),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 8.dp),
                        content = {
                            items(state.value.data.size) {
                                ItemUi(
                                    itemIndex = it,
                                    movieList = state.value.data,
                                    navController = navController
                                )
                            }
                        }
                    )
                }
            }
        },
        containerColor = Color(0xFF1A1A1A)
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemUi(itemIndex: Int, movieList: List<Item>, navController: NavHostController) {
    val name = nameGiver(movieList[itemIndex])
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(2f / 3f)
            .padding(8.dp)
            .shadow(8.dp, RoundedCornerShape(16.dp))
            .clickable {
                movieList.getOrNull(itemIndex)?.kinopoiskId?.let { id ->
                    navController.navigate("Details screen/$id")
                }
            },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2A2A2A)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            AsyncImage(
                model = movieList[itemIndex].posterUrl,
                contentDescription = movieList[itemIndex].nameOriginal,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFF121212).copy(alpha = 0.7f)
                    )
                    .padding(8.dp)
            ) {
                Text(
                    text = name.toString(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .basicMarquee(),
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = movieList[itemIndex].year?.toString() ?: "",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = Color(0xFFBB86FC),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    mainViewModel: MainViewModel = hiltViewModel(),
    navController: NavHostController,
    scrollBehavior: TopAppBarScrollBehavior
) {
    val openDialog = remember { mutableStateOf(false) }

    TopAppBar(
        title = {
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF121212),
            scrolledContainerColor = Color(0xFF121212).copy(alpha = 0.9f)
        ),
        actions = {
            IconButton(onClick = { openDialog.value = true }) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Log Out",
                    tint = Color(0xFFBB86FC)
                )
            }
            if (openDialog.value) {
                AlertLogOut(openDialog, mainViewModel, navController)
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@Composable
private fun AlertLogOut(
    openDialog: MutableState<Boolean>,
    mainViewModel: MainViewModel,
    navController: NavHostController
) {
    AlertDialog(
        onDismissRequest = { openDialog.value = false },
        title = {
            Text(
                text = "Подтверждение",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Text(
                text = "Вы точно хотите выйти из аккаунта?",
                color = Color.White,
                fontSize = 16.sp
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    openDialog.value = false
                    mainViewModel.signOut()
                    navController.navigate("Login Screen") {
                        popUpTo("Start Screen") { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFBB86FC),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Да", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        },
        dismissButton = {
            Button(
                onClick = { openDialog.value = false },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4A4A4A),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Нет", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        },
        containerColor = Color(0xFF2A2A2A),
        shape = RoundedCornerShape(16.dp)
    )
}