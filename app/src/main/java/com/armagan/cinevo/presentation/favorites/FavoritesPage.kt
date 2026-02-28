package com.armagan.cinevo.presentation.favorites

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.armagan.cinevo.R
import com.armagan.cinevo.model.MovieDetailResponse
import com.armagan.cinevo.navigation.Screen
import com.armagan.cinevo.ui.customcomposables.CustomDivider
import com.armagan.cinevo.ui.customcomposables.RatingBadge
import com.armagan.cinevo.ui.customcomposables.RatingBadgeSize
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun FavoritePage(navController: NavController){
    val viewModel: FavoriteViewModel = hiltViewModel()
    val movies by viewModel.favoriteMovies.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var isTimeoutReached by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(2000)
        isTimeoutReached = true
    }


    Scaffold (
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize()

    ){ paddingValues ->
        Box(modifier = Modifier.fillMaxSize()){

            if(movies.isNullOrEmpty()){
                if(!isTimeoutReached){
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }
                else{
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {

                        IconButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(top = 52.dp, start = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Geri",
                                tint = Color.White
                            )
                        }


                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 100.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.emptybox),
                                contentDescription = stringResource(R.string.empty_state_message),
                                modifier = Modifier.size(190.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = stringResource(R.string.empty_state_message),
                                fontSize = 18.sp,
                                color = Color.White
                            )
                        }
                    }
                }

            }
            else{
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
                ) {
                    Row {
                        IconButton(onClick = {
                            navController.popBackStack()
                        },
                            modifier = Modifier.padding(top = 22.dp)
                        ) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri")
                        }
                        Text(
                            stringResource(R.string.favorites),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 30.dp, bottom =16.dp )
                        )
                    }
                    CustomDivider(
                        paddingval = PaddingValues(
                            horizontal = 10.dp,
                            vertical = 16.dp
                        )
                    )
                    LazyColumn {
                        itemsIndexed(movies,key = { _, movie -> movie.id }){
                                index,movie ->
                            SwipeToDeleteCard(
                                movie = movie,
                                onDelete = {
                                    //db silme
                                    viewModel.removeFavorite2(movie.id.toString())
                                    scope.launch {
                                        val result = snackbarHostState.showSnackbar(
                                            message = "${movie.title} ${R.string.removedfromfav}",
                                            actionLabel = "Geri Al",
                                            duration = SnackbarDuration.Short
                                        )
                                        if (result == SnackbarResult.ActionPerformed) {
                                            viewModel.addFavorite(movie)
                                        }
                                    }

                                },
                                onClick = {
                                    navController.navigate(Screen.DetailsScreen.passMovieId(movie.id))

                                }

                            )
                        }
                    }
                }

            }

        }
    }


}
@Composable
fun SwipeToDeleteCard(
    movie: MovieDetailResponse,
    onDelete: () -> Unit,
    onClick: () -> Unit
) {
    val offsetX = remember { Animatable(0f) }
    val threshold = 380f
    val scope = rememberCoroutineScope()
    Box(modifier = Modifier.fillMaxWidth()) {


        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(8.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.CenterStart
        ) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "",
                tint = Color.White,
                modifier = Modifier.padding(start = 35.dp)
            )
        }



        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { _, dragAmount ->
                            scope.launch {
                                offsetX.snapTo((offsetX.value + dragAmount).coerceAtLeast(0f))
                            }
                        },
                        onDragEnd = {
                            scope.launch {
                                if (offsetX.value > threshold) {
                                    offsetX.animateTo(1150f, animationSpec = tween(durationMillis = 500))
                                    onDelete()
                                } else {
                                    offsetX.animateTo(0f, animationSpec = tween(durationMillis = 300))
                                }
                            }
                        }
                    )
                }
        ) {
            MovieCard2(
                movie = movie,
                onClick = onClick,
                modifier = Modifier
            )

        }
    }
}


@Composable
fun MovieCard2(
    movie: MovieDetailResponse,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val backdropUrl = movie.backdropPath?.let {
        "https://image.tmdb.org/t/p/w300$it"
    }

    val posterUrl = movie.posterPath?.let {
        "https://image.tmdb.org/t/p/w342$it"
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.tertiary)
        ) {


            if (!backdropUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = backdropUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .matchParentSize()
                        .alpha(0.5f)
                )
            }


            Row(
                modifier = Modifier.padding(8.dp)
            ) {

                AsyncImage(
                    model = posterUrl,
                    contentDescription = "Film Posteri",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(width = 80.dp, height = 120.dp)
                        .clip(RoundedCornerShape(16.dp))
                )

                Column(modifier = Modifier.padding(horizontal = 8.dp)) {

                    Text(
                        text = movie.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.White,
                        modifier = Modifier.padding(top = 3.dp, bottom = 3.dp)
                    )

                    Text(
                        text = stringResource(R.string.genre) +
                                movie.genres.joinToString(", ") { it.name },
                        fontSize = 12.sp,
                        color = Color.White
                    )

                    Row {
                        Text(
                            text = stringResource(R.string.rating),
                            fontSize = 12.sp,
                            color = Color.White
                        )

                        RatingBadge(
                            movie.vote_average,
                            size = RatingBadgeSize.SMALL
                        )
                    }
                }
            }
        }
    }
}
