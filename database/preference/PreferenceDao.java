package licenta.beatyourmeal.database.preference;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PreferenceDao {

    @Insert
    long insert(Preference preference);

    @Query("select * from preference")
    List<Preference> getAll();

    @Query("select * from preference where id_user=(:introducedUserId) and id_aliment=(:introducedAlimentId)")
    Preference getPreferenceByUserIdAndAlimentId(long introducedUserId,long introducedAlimentId);


    @Query("select * from preference where id_user=(:introducedUserId)")
    List<Preference> getPreferenceByUserId(long introducedUserId);

    @Update
    int update(Preference preference);

    @Delete
    int delete(Preference preference);
}
