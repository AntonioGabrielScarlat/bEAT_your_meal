package licenta.beatyourmeal.database.mealPlan;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MealPlanDao {

    @Insert
    long insert(MealPlan mealPlan);

    @Query("select * from meal_plan")
    List<MealPlan> getAll();

    @Query("select * from meal_plan where id_user=(:introducedUserId)")
    MealPlan getMealPlanByUserId(long introducedUserId);

    @Update
    int update(MealPlan mealPlan);

    @Delete
    int delete(MealPlan mealPlan);
}
