package com.armagan.cinevo.ui.customcomposables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.armagan.cinevo.presentation.home.detailpage.ratingColor

@Composable
fun RatingBadge(
    rating: Double,
    modifier: Modifier = Modifier,
    size: RatingBadgeSize = RatingBadgeSize.MEDIUM
) {
    val bgColor = ratingColor(rating)

    val (horizontalPadding, verticalPadding, textSize) = when (size) {
        RatingBadgeSize.SMALL -> Triple(6.dp, 2.dp, 10.sp)
        RatingBadgeSize.MEDIUM -> Triple(12.dp, 6.dp, 16.sp)
        RatingBadgeSize.LARGE -> Triple(16.dp, 8.dp, 20.sp)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor.copy(alpha = 0.15f))
            .border(
                width = 1.dp,
                color = bgColor.copy(alpha = 0.6f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(
                horizontal = horizontalPadding,
                vertical = verticalPadding
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = String.format("%.1f", rating),
            color = bgColor,
            fontWeight = FontWeight.Bold,
            fontSize = textSize
        )
    }
}
enum class RatingBadgeSize {
    SMALL,
    MEDIUM,
    LARGE
}