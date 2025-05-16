package com.example.diabeteapp

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TargetPageScreen() {
    val color = Color(0xFF2264FF)
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Your Health Goals",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Blood Sugar Progress Ring
        BloodSugarRing(current = 128, targetMin = 80, targetMax = 130)

        Text(
            text = "Target: 80–130 mg/dL",
            fontSize = 14.sp,
            color = color.copy(alpha = 0.8f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        GoalCard(
            title = "Activity",
            current = "4,500 steps",
            target = "Goal: 10,000 steps",
            color = Color(0xFF3FA796)
        )

        GoalCard(
            title = "Weight",
            current = "70 kg",
            target = "Goal: 68 kg",
            color = Color(0xFFF4A261)
        )

        GoalCard(
            title = "Nutrition",
            current = "Tracked 2/3 meals",
            target = "Goal: 3/3 meals",
            color = Color(0xFF2A9D8F)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { /* Navigate to goal creation */ },
            colors = ButtonDefaults.buttonColors(containerColor = color),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Goal", tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add New Goal", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun GoalCard(title: String, current: String, target: String, color: Color) {
    Card(
        shape = RoundedCornerShape(16.dp),

        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .height(110.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = color)
            Text(current, fontSize = 16.sp, color = Color.DarkGray)
            Text(target, fontSize = 14.sp, color = Color.Gray)
        }
    }
}

@Composable
fun BloodSugarRing(
    current: Int,
    targetMin: Int,
    targetMax: Int,
    modifier: Modifier = Modifier
) {
    val progress = when {
        current < targetMin -> 0f
        current > targetMax -> 1f
        else -> (current - targetMin).toFloat() / (targetMax - targetMin).toFloat()
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(180.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Background ring
            drawArc(
                color = Color(0xFFE0E0E0),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = 24f, cap = StrokeCap.Round)
            )

            // Progress ring
            drawArc(
                color = Color(0xFF2264FF),
                startAngle = -90f,
                sweepAngle = 360f * progress,
                useCenter = false,
                style = Stroke(width = 24f, cap = StrokeCap.Round)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "$current", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text(text = "mg/dL", fontSize = 16.sp, color = Color.Black)
        }
    }
}
