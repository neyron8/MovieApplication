package com.example.movieapplication.navigation

import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.movieapplication.MainViewVodel
import com.example.movieapplication.modelsNew.CinemaKeywordResponse
import com.example.movieapplication.modelsNew.CinemaResponse
import com.example.movieapplication.modelsNew.Item

@Composable
fun StartScreen(mainViewModel: MainViewVodel = hiltViewModel(), navController: NavHostController) {
    val state = mainViewModel.listOfStates
    val query: MutableState<String> = remember { mutableStateOf("") }
    Scaffold(
        modifier = Modifier.background(Color.Black.copy(.8f)),
        topBar = {
            TopBar()
        },
        floatingActionButton = {
            FloatingButton(mainViewModel,navController)
        }, content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                OutlinedTextField(value = query.value, onValueChange = {
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

                            //items(state.value.keywordData.size) {
                            /*if (it >= state.movies.size - 1 && !state.endReached && !state.isLoading) {
                                movieViewModel.loadNextItems()
                            }*/
                            ItemUi(
                                itemIndex = it, movieList = state.value.data,
                                navController = navController
                            )
                            /*NewItemUi(
                                itemIndex = it, movieList = state.value.keywordData,
                                navController = navController
                            )*/

                        }
                        /*item(state.value.isLoading) {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator(color = ProgressIndicatorDefaults.circularColor)
                            }
                            if (!state.value.error.isEmpty()) {
                                Toast.makeText(
                                    LocalContext.current,
                                    state.value.error,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }*/
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
                        .basicMarquee(), //Эффект пролистывания текста, если он не вмещается в контейнер
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewItemUi(itemIndex: Int, movieList: List<CinemaKeywordResponse.Film>, navController: NavHostController) {
    val name = nameGiverNew(movieList[itemIndex])
    Card(
        Modifier
            .wrapContentSize()
            .padding(10.dp)
            .clickable {
                navController.navigate("Details screen/${movieList[itemIndex].filmId}")
            },
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            AsyncImage(
                model = movieList[itemIndex].posterUrl,
                contentDescription = movieList[itemIndex].nameEn,
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
                        .basicMarquee(), //Эффект пролистывания текста, если он не вмещается в контейнер
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
fun TopBar() {
    TopAppBar(
        title = { Text(text = "Новинки") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White.copy(.4f)
        )
    )
}

fun nameGiver(item: Item): String? {
    return listOf(
        item.nameRu.toString(),
        item.nameEn.toString(),
        item.nameOriginal.toString()
    ).first { it != "null" }
}

fun nameGiverNew(item: CinemaKeywordResponse.Film): String? {
    return listOf(
        item.nameRu.toString(),
        item.nameEn.toString(),
    ).first { it != "null" }
}

@Composable
fun FloatingButton(mainViewModel: MainViewVodel = hiltViewModel(),navController: NavController) {
    FloatingActionButton(modifier = Modifier.size(50.dp), onClick = {
        mainViewModel.clearList()
    }) {
    }
}