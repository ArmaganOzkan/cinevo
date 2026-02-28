package com.armagan.cinevo.presentation.allreviews

import ReviewCardShimmer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.armagan.cinevo.R
import com.armagan.cinevo.ui.customcomposables.ReviewCard

@Composable
fun AllReviewsPage(navController: NavController, movieId: String, title: String) {
    val viewModel: AllReviewsViewModel = hiltViewModel()
    val reviews by viewModel.reviewsByMovie.collectAsState()
    val isLoading by viewModel.isLoading2.collectAsState()
    val isLoadingMore by viewModel.isLoadingMore.collectAsState()
    val listState = rememberLazyListState()


    LaunchedEffect(Unit) {
        viewModel.loadInitialReviews(movieId)
    }


    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (!isLoadingMore && lastVisibleIndex != null && lastVisibleIndex >= reviews.size - 2) {
                    viewModel.fetchMoreReviews(movieId)
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 36.dp, bottom = 16.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Geri"
                )
            }
            Text(
                text = title + stringResource(id = R.string.reviews),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(start = 2.dp)
            )
        }

        if (isLoading) {

            LazyColumn(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                items(8) {
                    ReviewCardShimmer()
                }
            }
        } else {
            LazyColumn(
                state = listState,
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                items(reviews) { review ->
                    ReviewCard(
                        review.username,
                        review.rating,
                        review.review,
                        review.createdAt!!,
                        review.isSpoiler,
                    )
                }


                if (isLoadingMore) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            androidx.compose.material3.CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}

