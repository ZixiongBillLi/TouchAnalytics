package com.swen549.touchanalytics.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swen549.touchanalytics.Constants
import com.swen549.touchanalytics.ui.AppMode

@Composable
fun BottomBar(
    mode: AppMode,
    enrollmentCount: Int,
    matchCount: Int,
    nonmatchCount: Int,
) {
    HorizontalDivider(
        modifier = Modifier.fillMaxWidth(),
        thickness = 1.dp,
        color = Color.DarkGray
    )

    BottomAppBar {
        if (mode == AppMode.ENROLLMENT) {
            Text(
                text = "$mode MODE: $enrollmentCount / ${Constants.MIN_STROKE_COUNT}",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomBarColumn(
                    text = "MATCH",
                    count = matchCount,
                    modifier = Modifier.weight(1f)
                )

                VerticalDivider()

                BottomBarColumn(
                    text = "NONMATCH",
                    count = nonmatchCount,
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun BottomBarColumn(
    text: String,
    count: Int,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
        )

        Text(
            text = count.toString(),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}