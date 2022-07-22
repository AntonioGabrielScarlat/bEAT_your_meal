package licenta.beatyourmeal.database.ingredient;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface IngredientDao {

    @Insert
    long insert(Ingredient ingredient);

    @Query("select * from ingredient")
    List<Ingredient> getAll();

    @Query("select * from ingredient where id_recipe=(:introducedRecipeId)")
    List<Ingredient> getIngredientsByRecipeId(long introducedRecipeId);

    @Update
    int update(Ingredient ingredient);

    @Delete
    int delete(Ingredient ingredient);
}
