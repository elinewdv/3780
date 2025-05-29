import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FoodItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(foodItem: FoodItem)

    @Query("SELECT * FROM food_items WHERE foodId = :id")
    suspend fun getById(id: String): FoodItem?

    // Recherche par nom (pour l'UI)
    @Query("SELECT * FROM food_items WHERE name LIKE '%' || :query || '%'")
    suspend fun searchByName(query: String): List<FoodItem>
}