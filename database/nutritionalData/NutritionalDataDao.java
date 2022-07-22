package licenta.beatyourmeal.database.nutritionalData;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NutritionalDataDao {

    @Insert
    long insert(NutritionalData nutritionalData);

    @Query("select * from nutritional_data")
    List<NutritionalData> getAll();

    @Query("select * from nutritional_data where id_recipe=(:introducedRecipeId)")
    NutritionalData getNutritionalDataByRecipeId(long introducedRecipeId);


    @Update
    int update(NutritionalData nutritionalData);

    @Delete
    int delete(NutritionalData nutritionalData);
}
