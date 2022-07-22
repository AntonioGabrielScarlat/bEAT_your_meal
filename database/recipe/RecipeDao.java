package licenta.beatyourmeal.database.recipe;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RecipeDao {

    @Insert
    long insert(Recipe recipe);

    @Query("select * from recipe")
    List<Recipe> getAll();

    @Query("select * from recipe where name=(:introducedName)")
    Recipe getRecipeByName(String introducedName);

    @Query("select * from recipe where id=(:introducedId)")
    Recipe getRecipeById(long introducedId);

    @Query("select * from recipe where category=(:introducedCategory)")
    List<Recipe> getRecipesByType(String introducedCategory);

    @Query("select recipe.* from recipe, nutritional_data where recipe.id=nutritional_data.id_recipe and recipe.category=(:introducedCategory) and nutritional_data.calories<(:introducedCalories)")
    List<Recipe> getRecipesByTypeAndCalories(String introducedCategory, Double introducedCalories);

    @Query("select recipe.* from recipe,ingredient where recipe.category=(:introducedCategory) and ingredient.id_aliment in (select id_aliment from preference where preference.id_user=(:introducedUserId)) and recipe.id=ingredient.id_recipe")
    List<Recipe> getRecipesByTypeAndFoodPreferences(String introducedCategory, long introducedUserId);

    @Query("select recipe.* from recipe, nutritional_data,ingredient where recipe.id=nutritional_data.id_recipe and recipe.id=ingredient.id_recipe and recipe.category=(:introducedCategory) " +
            "and nutritional_data.calories<(:introducedCalories) and ingredient.id_aliment in (select id_aliment from preference where preference.id_user=(:introducedUserId))")
    List<Recipe> getRecipesByTypeAndCaloriesAndFoodPreferences(String introducedCategory, Double introducedCalories, long introducedUserId);

    @Update
    int update(Recipe recipes);

    @Delete
    int delete(Recipe recipes);
}
