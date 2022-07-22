package licenta.beatyourmeal.database.recommendedMeal;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RecommendedMealDao {

    @Insert
    long insert(RecommendedMeal recommendedMeal);

    @Query("select * from recommended_meal")
    List<RecommendedMeal> getAll();

    @Query("select * from recommended_meal where id_meal_plan=(:introducedMealPlanId)")
    List<RecommendedMeal> getRecommendedMealByMealPlanId(long introducedMealPlanId);

    @Update
    int update(RecommendedMeal recommendedMeal);

    @Delete
    int delete(RecommendedMeal recommendedMeal);


}
