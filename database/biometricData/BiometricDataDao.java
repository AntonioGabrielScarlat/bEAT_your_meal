package licenta.beatyourmeal.database.biometricData;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BiometricDataDao {

    @Insert
    long insert(BiometricData biometricData);

    @Query("select * from biometric_data")
    List<BiometricData> getAll();

    @Query("select * from biometric_data where id_user=(:introducedUserId)")
    BiometricData getBiometricDataByUserId(long introducedUserId);

    @Query("select * from biometric_data where id=(:introducedBiometricDataId)")
    BiometricData getBiometricDataById(long introducedBiometricDataId);

    @Update
    int update(BiometricData biometricData);

    @Delete
    int delete(BiometricData biometricData);
}
