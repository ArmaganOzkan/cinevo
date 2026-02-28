package com.armagan.cinevo.presentation.myreviews

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.armagan.cinevo.R
import com.armagan.cinevo.data.model.Review
import com.armagan.cinevo.ui.customcomposables.CustomDivider
import com.armagan.cinevo.ui.customcomposables.MyReviewsCard
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun MyReviewsPage(navController: NavController,
                  viewModel: MyReviewsViewModel = hiltViewModel()
){

    val reviews by viewModel.reviews.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val userId = FirebaseAuth.getInstance().currentUser?.uid!!

    val reviewRemovedText = stringResource(R.string.reviewremoved)
    val undoText = stringResource(R.string.undo)


    LaunchedEffect(userId) {
        viewModel.loadReviewsByUserId(userId)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->


        if(isLoading){
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }
        else{
            if(reviews.isEmpty()){
                Box(
                    modifier = Modifier
                        .fillMaxSize()
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
                            stringResource(id = R.string.myreviews),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(top = 30.dp, bottom = 16.dp)
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
                            stringResource(id = R.string.myreviews),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(top = 30.dp, bottom = 16.dp)
                        )
                    }

                    CustomDivider(paddingval = PaddingValues(6.dp))

                    LazyColumn {
                        items(
                            items = reviews,
                            key = { it.id }
                        ) { review ->
                            SwipeCard(
                                review = review,
                                onSwiped = {
                                    viewModel.removeFromList(review)

                                    scope.launch {
                                        val result = snackbarHostState.showSnackbar(
                                            message = reviewRemovedText,
                                            actionLabel = undoText,
                                            duration = SnackbarDuration.Short
                                        )

                                        if (result == SnackbarResult.ActionPerformed) {
                                            viewModel.restoreReview()
                                        } else {
                                            viewModel.confirmDelete()
                                        }
                                    }
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
fun SwipeCard(
    review: Review,
    onSwiped: () -> Unit
) {
    val offsetX = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    val threshold = 0.35f

    Box(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(8.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.CenterStart
        ) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = null,
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
                                val screenWidth = size.width.toFloat()
                                if (offsetX.value > screenWidth * threshold) {
                                    offsetX.animateTo(
                                        screenWidth,
                                        tween(500)
                                    )
                                    onSwiped()
                                } else {
                                    offsetX.animateTo(0f, tween(300))
                                }
                            }
                        }
                    )
                }
        ) {
            MyReviewsCard(
                movieName = review.moviename,
                rating = review.rating,
                reviewText = review.review,
                date = review.createdAt ?: "",
                backdroppath = review.backdroppath?:""
            )
        }
    }
}
