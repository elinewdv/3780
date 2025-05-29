import com.example.diabeteapp.data.api.FoodApiService
import com.example.diabeteapp.data.api.toFoodItem


class FoodRepository(
    private val foodItemDao: FoodItemDao,
    private val apiService: FoodApiService // Remplacez YourApiService par FoodApiService
) {
    suspend fun searchAndSaveFoods(query: String): List<FoodItem> {
        val apiResponse = apiService.getFoods() // Appel direct à l'API
        val foodItems = apiResponse.foods
            .filter { it.foodName.contains(query, ignoreCase = true) }
            .map { it.toFoodItem() }

        foodItems.forEach { foodItemDao.insert(it) }
        return foodItems
    }
}