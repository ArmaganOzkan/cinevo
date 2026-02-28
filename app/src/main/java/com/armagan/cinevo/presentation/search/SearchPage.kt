package com.armagan.cinevo.presentation.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
import com.armagan.cinevo.ui.customcomposables.CustomCheckbox
import com.armagan.cinevo.ui.customcomposables.CustomDivider
import com.armagan.cinevo.ui.customcomposables.RatingBadge
import com.armagan.cinevo.ui.customcomposables.RatingBadgeSize
import com.armagan.cinevo.ui.theme.ThemeViewModel
import com.armagan.cinevo.util.GenreUtil
import com.armagan.cinevo.util.noRippleClick
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchPage(navController: NavController) {
    val focusManager = LocalFocusManager.current
    val viewModel: SearchPageViewModel = hiltViewModel()
    val listState = rememberLazyListState()
    val themeViewModel: ThemeViewModel = hiltViewModel()
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()


    val error by viewModel.errorMessage.collectAsState()
    var query by remember { mutableStateOf("") }


    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Search,
        BottomNavItem.Profile
    )
    var selectedindex by remember { mutableStateOf(1) }

    var filtermenuenabled by remember { mutableStateOf(false) }
    val selectedCategories = remember { mutableStateListOf<String>() }
    val categories = GenreUtil.getAllGenres()
    val MAX_FILTER_SELECTION = 2
    val uiState by viewModel.uiState.collectAsState()

    val isFilterApplied = selectedCategories.isNotEmpty() && viewModel.isFilterMode





    LaunchedEffect(Unit) {
        viewModel.fetchNextPage()
    }


    LaunchedEffect(listState, uiState) {
        if (uiState !is SearchUiState.Success || viewModel.isFilterMode) return@LaunchedEffect

        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collectLatest { lastVisibleIndex ->
                val totalItems = listState.layoutInfo.totalItemsCount
                if (totalItems > 0 && lastVisibleIndex!! >= totalItems - 2) {
                    viewModel.fetchNextPage()
                }
            }
    }




    Box(modifier = Modifier.fillMaxSize()) {
        if(isDarkTheme){
            BlurredDarkBackground()
        }


        Scaffold(
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
            }, content = { padding ->
                when (selectedindex) {
                    0 -> navController.navigate(Screen.HomeScreen.route)
                    2 -> navController.navigate(Screen.ProfileScreen.route)
                }


                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .noRippleClick { focusManager.clearFocus() }
                ) {

                    Column {

                        TextField(
                            value = query,
                            onValueChange = {
                                viewModel.searchQuery.value = it
                                query = it


                                if (it.isNotBlank()) {
                                    selectedCategories.clear()
                                    viewModel.setFilterMode(false)
                                }

                            },
                            label = {
                                Text(
                                    stringResource(id = R.string.searchmovies),
                                    color = MaterialTheme.colorScheme.onSecondary
                                )
                            },
                            singleLine = true,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Search,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSecondary
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .padding(start = 12.dp, end = 12.dp, top = 52.dp),
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
                            trailingIcon = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    if (query.isNotEmpty()) {
                                        IconButton(
                                            onClick = {
                                                query = ""
                                                viewModel.searchQuery.value = ""
                                                focusManager.clearFocus()
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "ClearSearch"
                                            )
                                        }
                                    }

                                    IconButton(
                                        onClick = { filtermenuenabled = !filtermenuenabled }
                                    ) {
                                        Box {
                                            Icon(
                                                imageVector = Icons.Filled.Menu,
                                                contentDescription = "FilterButton"
                                            )

                                            if (isFilterApplied && !filtermenuenabled) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(8.dp)
                                                        .background(Color.Red, shape = RoundedCornerShape(50))
                                                        .align(Alignment.TopEnd)
                                                )
                                            }
                                        }
                                    }
                                }
                            }


                        )

                        CustomDivider(
                            paddingval = PaddingValues(
                                horizontal = 10.dp,
                                vertical = 16.dp
                            )
                        )

                        AnimatedVisibility(
                            visible = filtermenuenabled,
                            enter = expandVertically() + fadeIn(),
                            exit = shrinkVertically() + fadeOut()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.tertiary)
                                    .padding(12.dp)
                            ) {
                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    categories.forEach { category ->
                                        CustomCheckbox(
                                            label = category,
                                            checked = selectedCategories.contains(category),
                                            onCheckedChange = { isChecked ->
                                                if (isChecked) {
                                                    if (selectedCategories.size < MAX_FILTER_SELECTION) {
                                                        selectedCategories.add(category)
                                                    }

                                                } else {
                                                    selectedCategories.remove(category)
                                                }
                                            }
                                        )
                                    }

                                }
                                Spacer(modifier = Modifier.height(24.dp))

                                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Button(
                                        enabled = selectedCategories.isNotEmpty(),
                                        onClick = {
                                            query = ""
                                            viewModel.searchQuery.value = ""
                                            viewModel.setLoading()
                                            viewModel.setFilterMode(true)
                                            val genreIds = GenreUtil.getGenreIds(selectedCategories)
                                            if (genreIds.isNotEmpty()) {
                                                viewModel.fetchMoviesByGenre(genreIds.first())
                                            }
                                            filtermenuenabled = false
                                        }
                                    ) {
                                        Text(stringResource(R.string.filter))

                                    }
                                    OutlinedButton(
                                        onClick = {
                                            query = ""
                                            viewModel.searchQuery.value = ""
                                            selectedCategories.clear()
                                            viewModel.setFilterMode(false)
                                            viewModel.resetPageAndList()

                                        },
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            contentColor = Color.White
                                        ),
                                        border = BorderStroke(1.dp, Color.White)
                                    ) {
                                        Text(stringResource(R.string.clear))
                                    }


                                }


                            }
                        }




                        LazyColumn(
                            state = listState,
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            when (uiState) {

                                is SearchUiState.Loading -> {
                                    items(10) {
                                        MovieCard(movie = null, isLoading = true, onMovieClick = {})
                                    }
                                }

                                is SearchUiState.Searching -> {
                                    items(6) {
                                        MovieCard(movie = null, isLoading = true, onMovieClick = {})
                                    }
                                }

                                is SearchUiState.Empty -> {
                                    item {
                                        EmptySearchState()
                                    }
                                }

                                is SearchUiState.Success -> {
                                    val movies = (uiState as SearchUiState.Success).movies
                                    items(
                                        count = movies.size,
                                        key = {index -> movies[index].id},
                                        contentType = {"movie_item"}
                                        ) { index ->
                                        val movie = movies[index]
                                        MovieCard(movie = movie,
                                            isLoading = false,
                                            onMovieClick = { movieId ->
                                                navController.navigate(Screen.DetailsScreen.passMovieId(movieId))
                                            }
                                        )
                                    }
                                }

                                is SearchUiState.Error -> {
                                    item {
                                        Text(
                                            text = (uiState as SearchUiState.Error).message,
                                            color = Color.Red
                                        )
                                    }
                                }
                            }
                        }


                    }

                }

            }

        )
    }


}

@Composable
fun MovieCard(movie: Movie?, isLoading: Boolean, onMovieClick: (Int) -> Unit) {
    val shimmerModifier = if (isLoading) Modifier.shimmer() else Modifier

    val backdropUrl = movie?.backdropPath?.let {
        "https://image.tmdb.org/t/p/w300$it"
    }
    val posterUrl = movie?.poster_path?.let {
        "https://image.tmdb.org/t/p/w342$it"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .then(shimmerModifier)
            .clickable(enabled = !isLoading && movie != null) {
                movie?.id?.let { onMovieClick(it) }
            },
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
                    model = coil.request.ImageRequest.Builder(androidx.compose.ui.platform.LocalContext.current)
                        .data(backdropUrl)
                        .crossfade(true)
                        .size(coil.size.Size.ORIGINAL)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .matchParentSize()
                        .alpha(0.5f)
                )
            }
            Row(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                AsyncImage(
                    model = coil.request.ImageRequest.Builder(androidx.compose.ui.platform.LocalContext.current)
                        .data(posterUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Film Posteri",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(width = 80.dp, height = 120.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .then(
                            if (isLoading)
                                Modifier.background(Color.Gray.copy(alpha = 0.3f))
                            else Modifier
                        )
                )

                Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                    Text(
                        text = movie?.title ?: "",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = if (isLoading) Color.Transparent else Color.White,
                        modifier = Modifier.padding(top = 3.dp, bottom = 3.dp)
                    )

                    Text(
                        text = stringResource(R.string.genre) +
                                movie?.genre_ids?.let { GenreUtil.getGenreNames(it) },
                        fontSize = 12.sp,
                        color = if (isLoading) Color.Transparent else Color.White
                    )


                    Row{
                        Text(text= stringResource(R.string.rating),
                            fontSize = 12.sp,
                            color = if (isLoading) Color.Transparent else Color.White
                            )
                        RatingBadge(movie?.vote_average ?: 0.0,size = RatingBadgeSize.SMALL)
                    }
                }
            }
        }
    }
}

@Composable
fun EmptySearchState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = R.drawable.emptybox,
            contentDescription = null,
            modifier = Modifier.size(200.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.empty_state_message),
            color = Color.Gray,
            fontSize = 16.sp
        )
    }
}
