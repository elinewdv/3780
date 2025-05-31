import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
//test pour repush
@Entity(tableName = "meals")
data class Meal(
    @PrimaryKey(autoGenerate = true) val mealId: Long = 0,
    val name: String,
    val date: LocalDateTime = LocalDateTime.now(),
    val userId: String
)