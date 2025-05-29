package com.example.diabeteapp

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

data class HealthTarget(
    val id: String,
    val title: String,
    val current: String,
    val currentValue: Float,
    val target: String,
    val targetValue: Float,
    val unit: String,
    val color: Color
)

@Composable
fun TargetPageScreen() {
    val color = Color(0xFF2264FF)
    val scrollState = rememberScrollState()

    var targets by remember { mutableStateOf(emptyList<HealthTarget>()) }
    var selectedRingTarget by remember { mutableStateOf<HealthTarget?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var targetToEdit by remember { mutableStateOf<HealthTarget?>(null) }

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

        // Show progress ring only if there's a selected target
        if (selectedRingTarget != null) {
            ProgressRing(target = selectedRingTarget!!)
            Text(
                text = "Target: ${selectedRingTarget!!.target} ${selectedRingTarget!!.unit}",
                fontSize = 14.sp,
                color = color.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(24.dp))
        } else {
            // Empty state with message
            EmptyStateContent(color = color)
            Spacer(modifier = Modifier.height(32.dp))
        }

        // Goals list
        if (targets.isNotEmpty()) {
            targets.forEach { target ->
                GoalCard(
                    target = target,
                    isSelectedForRing = selectedRingTarget?.id == target.id,
                    onEdit = {
                        targetToEdit = target
                        showEditDialog = true
                    }
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
        }

        Button(
            onClick = { showAddDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = color),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Goal", tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (targets.isEmpty()) "Add Your First Goal" else "Add New Goal",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }

    // Add new target dialog
    if (showAddDialog) {
        AddTargetDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { newTarget ->
                targets = targets + newTarget
                // If this is the first target, automatically select it for the ring
                if (selectedRingTarget == null) {
                    selectedRingTarget = newTarget
                }
                showAddDialog = false
            }
        )
    }

    // Edit target dialog
    if (showEditDialog && targetToEdit != null) {
        val currentTarget = targetToEdit!!
        EditDialog(
            target = currentTarget,
            isSelectedForRing = selectedRingTarget?.id == currentTarget.id,
            onDismiss = {
                showEditDialog = false
                targetToEdit = null
            },
            onSave = { updatedTarget ->
                targets = targets.map { if (it.id == currentTarget.id) updatedTarget else it }
                if (selectedRingTarget?.id == currentTarget.id) {
                    selectedRingTarget = updatedTarget
                }
                showEditDialog = false
                targetToEdit = null
            },
            onDelete = {
                targets = targets.filter { it.id != currentTarget.id }
                if (selectedRingTarget?.id == currentTarget.id) {
                    val remainingTargets = targets.filter { it.id != currentTarget.id }
                    selectedRingTarget = remainingTargets.firstOrNull()
                }
                showEditDialog = false
                targetToEdit = null
            },
            onSelectForRing = {
                selectedRingTarget = currentTarget
            }
        )
    }
}

@Composable
fun EmptyStateContent(color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 40.dp)
    ) {
        // Empty circle placeholder
        Canvas(modifier = Modifier.size(180.dp)) {
            drawCircle(
                color = color.copy(alpha = 0.1f),
                style = Stroke(width = 24f, cap = StrokeCap.Round)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Add Your Goals Here",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = color,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Start tracking your health progress\nby adding your first goal below",
            fontSize = 16.sp,
            color = color.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )
    }
}

@Composable
fun GoalCard(
    target: HealthTarget,
    isSelectedForRing: Boolean,
    onEdit: () -> Unit
) {
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        target.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = target.color
                    )
                }

                // Edit button
                IconButton(
                    onClick = onEdit,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit goal",
                        tint = target.color,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Text("${target.current} ${target.unit}", fontSize = 16.sp, color = Color.DarkGray)
            Text("Goal: ${target.target} ${target.unit}", fontSize = 14.sp, color = Color.Gray)
        }
    }
}

@Composable
fun ProgressRing(
    target: HealthTarget,
    modifier: Modifier = Modifier
) {
    val progress = when {
        target.targetValue == 0f -> 0f
        target.id == "blood_sugar" -> {
            // Special handling for blood sugar
            val current = target.currentValue
            when {
                current < 80f -> 0f
                current > 130f -> 1f
                else -> (current - 80f) / (130f - 80f)
            }
        }
        target.id == "weight" -> {
            // For weight loss, closer to target = more progress
            val current = target.currentValue
            val startWeight = 75f // Assume starting weight
            val targetWeight = target.targetValue
            1f - ((current - targetWeight) / (startWeight - targetWeight)).coerceIn(0f, 1f)
        }
        else -> (target.currentValue / target.targetValue).coerceIn(0f, 1f)
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
                color = target.color,
                startAngle = -90f,
                sweepAngle = 360f * progress,
                useCenter = false,
                style = Stroke(width = 24f, cap = StrokeCap.Round)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = target.current,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = target.unit,
                fontSize = 16.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
fun AddTargetDialog(
    onDismiss: () -> Unit,
    onAdd: (HealthTarget) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var current by remember { mutableStateOf("") }
    var target by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(Color(0xFF2264FF)) }

    val colors = listOf(
        Color(0xFF2264FF),
        Color(0xFF3FA796),
        Color(0xFFF4A261),
        Color(0xFF2A9D8F),
        Color(0xFFE76F51),
        Color(0xFF9C4DCC)
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Add New Goal",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Goal Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = current,
                    onValueChange = { current = it },
                    label = { Text("Current Value") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = target,
                    onValueChange = { target = it },
                    label = { Text("Target Value") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = unit,
                    onValueChange = { unit = it },
                    label = { Text("Unit (e.g., kg, steps, hours)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Color:")
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    colors.forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clickable { selectedColor = color },
                            contentAlignment = Alignment.Center
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                drawCircle(
                                    color = color,
                                    style = if (selectedColor == color) {
                                        Stroke(width = 4.dp.toPx())
                                    } else {
                                        androidx.compose.ui.graphics.drawscope.Fill
                                    }
                                )
                                if (selectedColor == color) {
                                    drawCircle(
                                        color = color,
                                        radius = size.width * 0.3f
                                    )
                                }
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (title.isNotBlank() && current.isNotBlank() && target.isNotBlank()) {
                                val newTarget = HealthTarget(
                                    id = title.lowercase().replace(" ", "_"),
                                    title = title,
                                    current = current,
                                    currentValue = current.toFloatOrNull() ?: 0f,
                                    target = target,
                                    targetValue = target.toFloatOrNull() ?: 1f,
                                    unit = unit,
                                    color = selectedColor
                                )
                                onAdd(newTarget)
                            }
                        },
                        enabled = title.isNotBlank() && current.isNotBlank() && target.isNotBlank()
                    ) {
                        Text("Add")
                    }
                }
            }
        }
    }
}

@Composable
fun EditDialog(
    target: HealthTarget,
    isSelectedForRing: Boolean,
    onDismiss: () -> Unit,
    onSave: (HealthTarget) -> Unit,
    onDelete: () -> Unit,
    onSelectForRing: () -> Unit
) {
    var title by remember { mutableStateOf(target.title) }
    var current by remember { mutableStateOf(target.current) }
    var targetValue by remember { mutableStateOf(target.target) }
    var unit by remember { mutableStateOf(target.unit) }
    var selectedColor by remember { mutableStateOf(target.color) }

    val colors = listOf(
        Color(0xFF2264FF),
        Color(0xFF3FA796),
        Color(0xFFF4A261),
        Color(0xFF2A9D8F),
        Color(0xFFE76F51),
        Color(0xFF9C4DCC)
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Edit Goal",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Goal Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = current,
                    onValueChange = { current = it },
                    label = { Text("Current Value") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = targetValue,
                    onValueChange = { targetValue = it },
                    label = { Text("Target Value") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = unit,
                    onValueChange = { unit = it },
                    label = { Text("Unit (e.g., kg, steps, hours)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Color:")
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    colors.forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clickable { selectedColor = color },
                            contentAlignment = Alignment.Center
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                drawCircle(
                                    color = color,
                                    style = if (selectedColor == color) {
                                        Stroke(width = 4.dp.toPx())
                                    } else {
                                        androidx.compose.ui.graphics.drawscope.Fill
                                    }
                                )
                                if (selectedColor == color) {
                                    drawCircle(
                                        color = color,
                                        radius = size.width * 0.3f
                                    )
                                }
                            }
                        }
                    }
                }

                // Progress ring selection
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Display as progress ring:")
                    Switch(
                        checked = isSelectedForRing,
                        onCheckedChange = { if (it) onSelectForRing() }
                    )
                }

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Delete button
                    OutlinedButton(
                        onClick = onDelete
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                    }

                    Row {
                        TextButton(onClick = onDismiss) {
                            Text("Cancel")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (title.isNotBlank() && current.isNotBlank() && targetValue.isNotBlank()) {
                                    val updatedTarget = HealthTarget(
                                        id = target.id,
                                        title = title,
                                        current = current,
                                        currentValue = current.toFloatOrNull() ?: 0f,
                                        target = targetValue,
                                        targetValue = targetValue.toFloatOrNull() ?: 1f,
                                        unit = unit,
                                        color = selectedColor
                                    )
                                    onSave(updatedTarget)
                                }
                            },
                            enabled = title.isNotBlank() && current.isNotBlank() && targetValue.isNotBlank()
                        ) {
                            Text("Save")
                        }
                    }
                }
            }
        }
    }
}