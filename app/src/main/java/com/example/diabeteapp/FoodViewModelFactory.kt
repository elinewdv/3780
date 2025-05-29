// FoodViewModelFactory.kt
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.diabeteapp.data.FoodRepository
import com.example.diabeteapp.data.database.DatabaseProvider
import com.example.diabeteapp.data.api.RetrofitInstance
import com.example.diabeteapp.FoodViewModel

class FoodViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FoodViewModel::class.java)) {
            val database = DatabaseProvider.getDatabase(application)
            val apiService = RetrofitInstance.apiService

            // Créer le repository avec les bons paramètres
            val repository = FoodRepository(
                foodItemDao = database.foodItemDao(),
                apiService = apiService
            )

            // Créer le FoodViewModel avec les 3 paramètres requis
            @Suppress("UNCHECKED_CAST")
            return FoodViewModel(
                foodRepository = repository,
                mealDao = database.mealDao(),
                mealFoodCrossRefDao = database.mealFoodCrossRefDao()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}