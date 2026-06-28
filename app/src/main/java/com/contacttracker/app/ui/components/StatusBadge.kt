package com.contacttracker.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.contacttracker.app.data.ContactStatus
import com.contacttracker.app.ui.theme.StatusContactedColor
import com.contacttracker.app.ui.theme.StatusDoneColor
import com.contacttracker.app.ui.theme.StatusInProgressColor
import com.contacttracker.app.ui.theme.StatusNewColor

@Composable
fun StatusBadge(status: String, modifier: Modifier = Modifier) {
    val color = when (status) {
        ContactStatus.NEW -> StatusNewColor
        ContactStatus.CONTACTED -> StatusContactedColor
        ContactStatus.IN_PROGRESS -> StatusInProgressColor
        ContactStatus.DONE -> StatusDoneColor
        else -> Color.Gray
    }
    Text(
        text = status,
        color = Color.White,
        style = MaterialTheme.typography.labelMedium,
        modifier = modifier
            .background(color, RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}
