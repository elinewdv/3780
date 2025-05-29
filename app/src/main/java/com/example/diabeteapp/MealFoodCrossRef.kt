import androidx.room.Entity

@Entity(primaryKeys = ["mealId", "foodId"])
data class MealFoodCrossRef(
    val mealId: Long,
    val foodId: String,
    val customPortionG: Float? = null // Optionnel: surcharge de portion
)