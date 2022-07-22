package licenta.beatyourmeal.database.cookingStep;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CookingStepDao {

    @Insert
    long insert(CookingStep cookingStep);

    @Query("select * from cooking_step")
    List<CookingStep> getAll();

    @Query("select * from cooking_step where id_recipe=(:introducedRecipeId)")
    List<CookingStep> getCookingStepsByRecipeId(long introducedRecipeId);

    @Update
    int update(CookingStep cookingStep);

    @Delete
    int delete(CookingStep cookingStep);
}
