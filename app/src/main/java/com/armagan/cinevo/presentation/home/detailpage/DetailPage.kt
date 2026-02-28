package com.armagan.cinevo.presentation.home.detailpage

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.armagan.cinevo.R
import com.armagan.cinevo.data.model.VideoResult
import com.armagan.cinevo.model.MovieDetailResponse
import com.armagan.cinevo.navigation.Screen
import com.armagan.cinevo.ui.customcomposables.CustomDivider
import com.armagan.cinevo.ui.customcomposables.ExpandablePoster
import com.armagan.cinevo.ui.customcomposables.RatingBadge
import com.armagan.cinevo.ui.customcomposables.ReviewCard
import com.armagan.cinevo.ui.theme.ThemeViewModel
import com.armagan.cinevo.util.noRippleClick
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale



@Composable
fun DetailPage(navController: NavController,movieId:Int){

    val viewModel: DetailPageViewModel = hiltViewModel()
    val movieDetail =viewModel.movie.collectAsState()
    val trailers: List<VideoResult> by viewModel.trailers.collectAsState()


    LaunchedEffect (movieId){
        viewModel.getMovieDetail(movieId)
        viewModel.checkIfFavorite(movieId.toString())
    }
    movieDetail.value?.let {
        val videoKey = trailers.firstOrNull { it.site == "YouTube" && it.type == "Trailer" }?.key
        LoadDetails(movieDetail,viewModel,videoKey,navController)

    }


}

@Composable
fun LoadDetails(
    movieDetail: State<MovieDetailResponse?>,
    viewModel: DetailPageViewModel,
    videokey: String?,
    navController: NavController
) {
    val formattedRating = String.format(Locale.US, "%.2f", movieDetail.value?.vote_average ?: 0.0)

    val isFavorite by viewModel.isFavorite.collectAsState()
    val context = LocalContext.current
    val themeViewModel: ThemeViewModel = hiltViewModel()
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()


    var reviewEnabled by remember { mutableStateOf(false) }
    var rating by remember { mutableStateOf(0f) }
    var isSpoiler by remember { mutableStateOf(false) }
    var reviewText by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    val reviews by viewModel.reviewsByMovie.collectAsState()
    val userResult by viewModel.userData.collectAsState()
    val username by viewModel.username.collectAsState()

    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    val today = dateFormat.format(Date())

    LaunchedEffect(movieDetail.value?.id!!) {
        viewModel.fetchReviewsByMovieId(movieDetail.value?.id!!.toString())
    }

    LaunchedEffect(Unit) {
        viewModel.fetchUsername(FirebaseAuth.getInstance().currentUser?.uid!!)
    }

    Box(modifier = Modifier.fillMaxSize()) {


        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500${movieDetail.value?.backdropPath ?: ""}",
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .matchParentSize()
                .alpha(0.4f)
                .blur(12.dp)
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.7f),
                            Color.Black.copy(alpha = 0.4f),
                            Color.Black.copy(alpha = 0.8f)
                        )
                    )
                )
        )


        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(if(isDarkTheme)Color.Transparent else MaterialTheme.colorScheme.background )
                .padding(16.dp)
                .noRippleClick { focusManager.clearFocus() }
        ) {


            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp)
                ) {
                    Box {
                        ExpandablePoster(
                            "https://image.tmdb.org/t/p/w500${movieDetail.value?.posterPath ?: ""}",
                            movieDetail.value?.title!!
                        )
                        IconButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Overlay Icon",
                                tint = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 15.dp, end = 8.dp),
                        verticalArrangement = Arrangement.Top
                    ) {
                        Text(
                            text = movieDetail.value?.title ?: "",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row {
                            Button(
                                onClick = {
                                    if (videokey != null) {
                                        val intent = Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse("https://www.youtube.com/watch?v=$videokey")
                                        )
                                        context.startActivity(intent)
                                    } else {
                                        println("Error: No trailer available")
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                modifier = Modifier.padding(end = 8.dp)
                            ) {
                                Text(
                                    stringResource(id = R.string.watchtrailer),
                                    color = Color.White
                                )
                            }

                            IconButton(
                                onClick = {
                                    movieDetail.value?.id?.toString()
                                        ?.let { viewModel.toggleFavorite(it) }
                                }
                            ) {
                                Icon(
                                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                    contentDescription = "favorite",
                                    tint = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                        //----

                        Spacer(modifier = Modifier.height(8.dp))
                        Row {
                            Text(
                                "${stringResource(R.string.rating)} ",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(top = 6.dp)
                            )
                            RatingBadge(rating = formattedRating.toDouble())

                        }

                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                CustomDivider(paddingval = PaddingValues(0.dp))
                Spacer(modifier = Modifier.height(8.dp))
            }



            item {
                Text(
                    text = stringResource(R.string.overview),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 12.dp, bottom = 6.dp)
                )
                Text(
                    modifier = Modifier.padding(top = 2.dp, bottom = 6.dp),
                    text = "${stringResource(R.string.runtime)} ${movieDetail.value?.runtime ?: 0} ${
                        stringResource(
                            R.string.minute
                        )
                    }",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Thin,
                    fontSize = 14.sp
                )
                Text(
                    text = movieDetail.value?.overview ?: "",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.padding(top = 6.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
            }


            item {
                Button(
                    onClick = { reviewEnabled = !reviewEnabled },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                ) {
                    Text(
                        if (!reviewEnabled) stringResource(id = R.string.showreview) else stringResource(
                            id = R.string.hidereview
                        ),
                        color = Color.White
                    )
                }
            }


            if (reviewEnabled) {
                item {
                    CustomDivider(paddingval = PaddingValues(6.dp))
                    TextField(
                        value = reviewText,
                        onValueChange = { reviewText = it },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Edit,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSecondary
                            )
                        },
                        placeholder = { Text(stringResource(id = R.string.leaveareview)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { /*writeReviewEnabled = true*/ },
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.onBackground,
                            unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                            focusedContainerColor = MaterialTheme.colorScheme.tertiary,
                            unfocusedContainerColor = MaterialTheme.colorScheme.tertiary,
                            cursorColor = MaterialTheme.colorScheme.onBackground,
                            focusedLabelColor = MaterialTheme.colorScheme.onSecondary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.secondary,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = isSpoiler,
                                onCheckedChange = { isSpoiler = it }
                            )
                            Text(
                                "Spoiler",
                                color = Color.White,
                                modifier = Modifier.padding(top = 9.dp)
                            )
                        }

                        StarRatingInput(currentRating = rating, onRatingChanged = { rating = it })
                    }
                }


                if (rating != 0f && reviewText.isNotBlank()) {
                    item {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = {
                                    val userId = FirebaseAuth.getInstance().currentUser?.uid!!
                                    viewModel.submitReview(
                                        userId,
                                        movieDetail.value?.id!!.toString(),
                                        reviewText,
                                        rating,
                                        isSpoiler,
                                        username.toString(),
                                        movieDetail.value?.title!!.toString(),
                                        movieDetail.value?.backdropPath!!.toString()
                                    )
                                    reviewText = ""
                                    rating = 0f
                                    isSpoiler = false
                                    viewModel.fetchReviewsByMovieId(movieDetail.value?.id!!.toString())
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                modifier = Modifier.fillMaxWidth(0.5f)
                            ) {
                                Text(stringResource(id = R.string.send), color = Color.White)
                            }
                        }
                    }
                }
            }


            if (reviewEnabled) {

                items(reviews) { review ->

                    ReviewCard(
                        userName = review.username,
                        rating = review.rating,
                        reviewText = review.review,
                        date = review.createdAt ?: today,
                        isSpoiler = review.isSpoiler
                    )
                }
                item {

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        OutlinedButton(
                            onClick = {
                                val title1 = movieDetail.value?.title!!
                                navController.navigate(
                                    Screen.AllReviewsScreen.passArguments(
                                        movieDetail.value?.id!!.toString(),
                                        title1
                                    )
                                )
                            },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.primary
                            ),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                        ) {
                            Text(
                                stringResource(id = R.string.seeallreviews),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                    }
                }
            }

        }
    }
}
fun ratingColor(rating: Double): Color {
    return when {
        rating >= 7.5 -> Color(0xFF2ECC71)
        rating >= 5.0 -> Color(0xFFF39C12)
        else -> Color(0xFFE74C3C)
    }
}







@Composable
fun StarRatingInput(
    modifier: Modifier = Modifier,
    maxStars: Int = 5,
    currentRating: Float,
    onRatingChanged: (Float) -> Unit
) {
    Row(modifier = modifier) {
        for (star in 1..maxStars) {
            Icon(
                imageVector = if (star <= currentRating) Icons.Default.Star else Icons.Outlined.Star,
                contentDescription = "Star $star",
                tint = if (star <= currentRating) Color.Yellow else Color.Gray,
                modifier = Modifier
                    .size(32.dp)
                    .clickable { onRatingChanged(star.toFloat()) }
            )
        }
    }
}

fun formatRuntime(totalMinutes: Int): String {
    if (totalMinutes <= 0) return ""

    val hours = totalMinutes / 60
    val minutes = totalMinutes % 60

    return when {
        hours > 0 && minutes > 0 -> "${hours}h ${minutes}m"
        hours > 0 -> "${hours}h"
        else -> "${minutes}m"
    }
}



