import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(foods: List<FoodItem>)

    @Query("SELECT * FROM food_items WHERE name LIKE '%' || :query || '%'")
    suspend fun searchLocally(query: String): List<FoodItem>

    @Query("SELECT * FROM food_items")
    fun getAll(): Flow<List<FoodItem>>
}
