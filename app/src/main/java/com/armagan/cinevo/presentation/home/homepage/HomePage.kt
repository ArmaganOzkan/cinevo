package com.armagan.cinevo.presentation.home.homepage

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.armagan.cinevo.R
import com.armagan.cinevo.data.model.Movie
import com.armagan.cinevo.navigation.Screen
import com.armagan.cinevo.presentation.main.BottomNavItem
import com.armagan.cinevo.ui.customcomposables.BlurredDarkBackground
import com.armagan.cinevo.ui.theme.ThemeViewModel
import com.armagan.cinevo.util.GenreUtil
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomePage(navController: NavController) {
    val viewModel: HomeViewModel = hiltViewModel()
    val themeViewModel: ThemeViewModel = hiltViewModel()
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()

    val popularMovies by viewModel.movies.collectAsState()
    val error by viewModel.errorMessage.collectAsState()

    val genreMoviesMap by viewModel.genreMoviesMap.collectAsState()

    val randomGenres by viewModel.randomGenres.collectAsState()

    val firstGenreId = randomGenres.getOrNull(0)
    val secondGenreId = randomGenres.getOrNull(1)
    val thirdGenreId = randomGenres.getOrNull(2)


    val firstGenreMovies = firstGenreId?.let { genreMoviesMap[it] } ?: emptyList()
    val secondGenreMovies = secondGenreId?.let { genreMoviesMap[it] } ?: emptyList()
    val thirdGenreMovies = thirdGenreId?.let { genreMoviesMap[it] } ?: emptyList()

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Search,
        BottomNavItem.Profile
    )
    var selectedindex by remember { mutableStateOf(0) }

    if (error != null) {
        Text(
            text = error ?: "Hata",
            color = Color.White,
            modifier = Modifier.fillMaxSize(),
            textAlign = TextAlign.Center
        )
        return
    }
    Box(modifier = Modifier.fillMaxSize()) {

        if(isDarkTheme){
            BlurredDarkBackground()
        }

        Scaffold(
            containerColor = Color.Black.copy(alpha = 0.55f),
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.background.copy(alpha = if(isDarkTheme) 0.15f else 1.0f),
                    tonalElevation = 0.dp
                ) {
                    items.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = index == selectedindex,
                            onClick = { selectedindex = index },
                            icon = { Icon(imageVector = item.icon, contentDescription = "") },
                            label = { Text(stringResource(id = item.labelResId)) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                indicatorColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.onBackground,
                                unselectedTextColor = MaterialTheme.colorScheme.onBackground,
                                unselectedIconColor = MaterialTheme.colorScheme.onBackground

                            )

                        )
                    }
                }
            },
            content = { padding ->
                when (selectedindex) {
                    1 -> navController.navigate(Screen.SearchScreen.route)
                    2 -> navController.navigate(Screen.ProfileScreen.route)


                }
                LazyColumn(
                    modifier = Modifier
                        //.background(MaterialTheme.colorScheme.background)
                        .background(if(!isDarkTheme)MaterialTheme.colorScheme.background else Color.Transparent)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {

                    stickyHeader {

                        CategoryTitle(stringResource(R.string.popularmovies))

                    }

                    item {
                        MovieCategoryRow(popularMovies, navController)
                    }
                    stickyHeader {
                        firstGenreId?.let {
                            CategoryTitle(GenreUtil.getGenreNames(listOf(it)))
                        }
                    }
                    item {
                        MovieCategoryRow(firstGenreMovies, navController)
                    }
                    stickyHeader {
                        secondGenreId?.let {
                            CategoryTitle(GenreUtil.getGenreNames(listOf(it)))
                        }
                    }
                    item {
                        MovieCategoryRow(secondGenreMovies, navController)
                    }
                    stickyHeader {
                        thirdGenreId?.let {
                            CategoryTitle(GenreUtil.getGenreNames(listOf(it)))
                        }
                    }
                    item {
                        MovieCategoryRow(thirdGenreMovies, navController)
                    }
                    item {
                        Spacer(modifier = Modifier.height(40.dp))
                    }

                }
            }
        )
    }
}
@Composable
fun MovieCategoryRow(movies: List<Movie>, navController: NavController) {
    val showShimmer = movies.isEmpty()

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(640.dp)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (showShimmer) {
            items(5) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    ShimmerMovieItem()
                    ShimmerMovieItem()
                }
            }
        } else {
            items(movies.chunked(2)) { moviePair ->
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    moviePair.forEach { movie ->
                        MovieItem(movie, navController)
                    }
                }
            }
        }
    }


    HorizontalDivider(
        color = Color.Gray,
        thickness = 3.dp,
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Composable
fun MovieItem(movie: Movie, navController: NavController) {

    var imageLoading by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .padding(top = 12.dp)
            .height(270.dp)
            .clickable {
                navController.navigate(Screen.DetailsScreen.passMovieId(movie.id))
            }
    ) {

        Box {
            if (imageLoading) {
                ShimmerPosterPlaceholder(
                    modifier = Modifier
                        .size(width = 180.dp, height = 240.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
            }

            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500${movie.poster_path}",
                contentDescription = "Film Posteri",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = 180.dp, height = 240.dp)
                    .clip(RoundedCornerShape(16.dp)),
                onSuccess = { imageLoading = false },
                onError = { imageLoading = false }
            )
        }

        Text(
            text = movie.title,
            modifier = Modifier
                .padding(top = 6.dp)
                .width(170.dp),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.SemiBold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun CategoryTitle(genreName:String) {
    Column(modifier =
        Modifier
            .background(color = MaterialTheme.colorScheme.background.copy(alpha = 0.55f))
            .clip(RoundedCornerShape(12.dp))) {
        Text(
            text = genreName,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 44.dp, bottom = 8.dp)
        )
    }

}

@Composable
fun ShimmerMovieItem() {
    val shimmerInstance = rememberShimmer(shimmerBounds = ShimmerBounds.Window)

    Column(
        modifier = Modifier
            .padding(top = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(width = 180.dp, height = 240.dp)
                .clip(RoundedCornerShape(16.dp))
                .shimmer(shimmerInstance)
                .background(Color.Gray)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .width(170.dp)
                .height(20.dp)
                .shimmer(shimmerInstance)
                .background(Color.Gray)
        )
    }
}

@Composable
fun ShimmerPosterPlaceholder(
    modifier: Modifier = Modifier
) {
    val shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.Window)

    Box(
        modifier = modifier
            .shimmer(shimmer)
            .background(Color.Gray.copy(alpha = 0.4f))
    )
}
