package com.example.diabeteapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

@Entity(tableName = "health_targets")
data class HealthTarget(
    @PrimaryKey val id: String,
    val title: String,
    val current: String,
    val currentValue: Float,
    val target: String,
    val targetValue: Float,
    val unit: String,
    val colorArgb: Int,
    val isSelectedForRing: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
) {
    //Helper property to convert to/from Compose Color
    val color: Color
        get() = Color(colorArgb)

    companion object {
        fun fromComposeColor(
            id: String,
            title: String,
            current: String,
            currentValue: Float,
            target: String,
            targetValue: Float,
            unit: String,
            color: Color,
            isSelectedForRing: Boolean = false
        ): HealthTarget {
            return HealthTarget(
                id = id,
                title = title,
                current = current,
                currentValue = currentValue,
                target = target,
                targetValue = targetValue,
                unit = unit,
                colorArgb = color.toArgb(),
                isSelectedForRing = isSelectedForRing
            )
        }
    }
}