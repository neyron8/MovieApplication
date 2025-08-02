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
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
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
    BackPressSample()
    val state = mainViewModel.listOfStates
    val query: MutableState<String> = remember { mutableStateOf("") }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier
            .background(Color.Black.copy(.8f))
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopBar(
                navController = navController,
                scrollBehavior = scrollBehavior
            )
        }, content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
               OutlinedTextField(
                    value = query.value, onValueChange = {
                        query.value = it
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = { mainViewModel.getCinemaByName(query.value) }
                    ),
                    enabled = true,
                    singleLine = true,
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null)
                    },
                    label = { Text(text = "Search here...") },
                    modifier = Modifier.fillMaxWidth()
                )

                if (state.value.error.isNotBlank()) {
                    Log.d("TAG", "MainContent: ${state.value.error}")
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = mainViewModel.listOfStates.value.error
                        )
                    }
                }
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    Modifier
                        .padding(8.dp)
                        .fillMaxSize()
                        .background(
                            Color.Transparent
                        ),
                    content = {
                        items(state.value.data.size) {
                            ItemUi(
                                itemIndex = it, movieList = state.value.data,
                                navController = navController
                            )
                        }
                    }
                )
            }
        },
        containerColor = Color.Transparent
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemUi(itemIndex: Int, movieList: List<Item>, navController: NavHostController) {
    val name = nameGiver(movieList[itemIndex])
    Card(
        Modifier
            .wrapContentSize()
            .padding(10.dp)
            .clickable {
                navController.navigate("Details screen/${movieList[itemIndex].kinopoiskId}")
            },
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            AsyncImage(
                model = movieList[itemIndex].posterUrl,
                contentDescription = movieList[itemIndex].nameOriginal,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(Color.LightGray.copy(.8f))
                    .padding(6.dp)
            ) {
                Text(
                    text = name.toString(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .basicMarquee(),
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
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
        title = { Text(text = "Новинки") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White.copy(.4f)
        ),
        actions = {
            IconButton(onClick = {
                openDialog.value = true
            }) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "LogOut")
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
        title = { Text(text = "Подтверждение действия") },
        text = { Text("Вы действительно хотите выйти?") },
        confirmButton = {
            Button(
                {
                    openDialog.value = false
                    mainViewModel.signOut()
                    navController.navigate("Login Screen") {
                        popUpTo("Start Screen") { inclusive = true }
                    }
                },
                colors = buttonColors(containerColor = Color.DarkGray),
                border = BorderStroke(1.dp, Color.LightGray)
            ) {
                Text("Выйти", fontSize = 22.sp)
            }
        },
        dismissButton = {
            Button(
                onClick = { openDialog.value = false },
                colors = buttonColors(containerColor = Color.DarkGray),
                border = BorderStroke(1.dp, Color.LightGray)
            ) {
                Text("Отмена", fontSize = 22.sp)
            }
        },
        containerColor = Color.DarkGray,
        titleContentColor = Color.LightGray,
        textContentColor = Color.LightGray,
        iconContentColor = Color.LightGray
    )
}