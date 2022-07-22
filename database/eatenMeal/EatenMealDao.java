package licenta.beatyourmeal.database.eatenMeal;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

@Dao
public interface EatenMealDao {

    @Insert
    long insert(EatenMeal eatenMeal);

    @Query("select * from eaten_meal")
    List<EatenMeal> getAll();

    @Query("select * from eaten_meal where id_user=(:introducedUserId) and date_of_consumption=(:introducedDate)")
    List<EatenMeal> getEatenMealByConsumptionDateAndUserId(long introducedUserId, Date introducedDate);

    @Query("select eaten_meal.* from eaten_meal, recipe where eaten_meal.id_recipe=recipe.id and recipe.name=(:introducedName) and eaten_meal.meal_of_the_day=(:introducedMealOfTheDay)")
    List<EatenMeal> getEatenMealsByRecipeNameAndMealOfTheDay(String introducedName, String introducedMealOfTheDay);

    @Update
    int update(EatenMeal eatenMeal);

    @Delete
    int delete(EatenMeal eatenMeal);
}
