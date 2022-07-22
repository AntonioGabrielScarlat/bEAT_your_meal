package licenta.beatyourmeal.database.aliment;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AlimentDao {

    @Insert
    long insert(Aliment aliment);

    @Query("select * from aliment")
    List<Aliment> getAll();

    @Query("select * from aliment where name=(:introducedName)")
    Aliment getByName(String introducedName);

    @Query("select * from aliment where id=(:introducedId)")
    Aliment getById(long introducedId);

    @Update
    int update(Aliment aliment);

    @Delete
    int delete(Aliment aliment);
}
