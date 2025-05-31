import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "food_items")
data class FoodItem(
    @PrimaryKey val foodId: String,
    val name: String,
    val latinName: String,
    val foodGroupId: String,

    // Portions
    val defaultPortionG: Float = 100f, // 100g par défaut
    val selectedPortionG: Float = 100f, // Portion choisie par l'user

    // Nutriments principaux (pour 100g)
    val energyKcal: Float,
    val energyKj: Float,
    val proteins: Float,
    val fats: Float,
    val carbohydrates: Float,
    val fiber: Float?,

    // Autres champs utiles
    val ediblePartPercent: Int = 100,
    val uri: String? = null
) {
    // Méthodes de calcul dynamique
    fun getEnergyKcalForPortion(): Float = (energyKcal * selectedPortionG) / 100
    fun getProteinsForPortion(): Float = (proteins * selectedPortionG) / 100
    // ... Idem pour les autres nutriments
}