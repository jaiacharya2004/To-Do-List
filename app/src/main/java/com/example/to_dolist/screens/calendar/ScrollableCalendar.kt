package com.example.to_dolist.screens.calendar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScrollableCalendar(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val currentDate = LocalDate.now()
    val dateList = remember { generateDates(currentDate, 15) } // Generate dates for one year
    val listState = rememberLazyListState()


    // Automatically scroll to the current date when the composable is loaded
    LaunchedEffect(Unit) {
        val currentIndex = dateList.indexOf(selectedDate)
        if (currentIndex >= 0) {
            listState.scrollToItem(currentIndex)
        }
    }

    Column {


        LazyRow(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(dateList.size) { index ->
                val date = dateList[index]
                DateItem(
                    date = date,
                    isSelected = date == selectedDate,
                    onClick = { onDateSelected(date) }
                )
            }
        }
    }
}

@Composable
fun DateItem(
    date: LocalDate,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val dayFormatter = DateTimeFormatter.ofPattern("E") // Short weekday format (e.g., Wed)

    val backgroundColor = if (isSelected) Color(0xFF6200EE) else Color.Transparent
    val textColor = if (isSelected) Color.White else Color.Black
    val borderColor = if (date == LocalDate.now()) Color(0xFF87CEEB) else Color.Transparent // Sky-blue color for current date

    Column(
        modifier = Modifier
            .width(40.dp)
            .height(70.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .border(
                width = 2.dp, // Border thickness
                color = borderColor, // Apply the border color conditionally
                shape = RoundedCornerShape(8.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Display the weekday
        Text(
            text = date.format(dayFormatter),
            fontSize = 12.sp,
            color = textColor,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp)) // Space between weekday and date
        // Display only the numeric day of the month
        Text(
            text = date.dayOfMonth.toString(), // Display "22"
            fontSize = 14.sp,
            color = textColor,
            textAlign = TextAlign.Center
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun generateDates(currentDate: LocalDate, range: Int): List<LocalDate> {
    val startDate = currentDate.minusDays(range.toLong()) // 15 days before
    val endDate = currentDate.plusDays(range.toLong())    // 15 days after
    return (0..(endDate.toEpochDay() - startDate.toEpochDay()).toInt())
        .map { startDate.plusDays(it.toLong()) }
    }