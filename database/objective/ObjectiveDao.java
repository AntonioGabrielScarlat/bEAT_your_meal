package licenta.beatyourmeal.database.objective;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ObjectiveDao {

    @Insert
    long insert(Objective objective);

    @Query("select * from objective")
    List<Objective> getAll();

    @Query("select * from objective where id_user=(:introducedUserId)")
    Objective getObjectiveByUserId(long introducedUserId);

    @Update
    int update(Objective objective);

    @Delete
    int delete(Objective objective);
}
