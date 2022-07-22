package licenta.beatyourmeal.database.biometricData;

import android.content.Context;

import java.util.List;
import java.util.concurrent.Callable;

import licenta.beatyourmeal.async.AsyncTaskRunner;
import licenta.beatyourmeal.async.Callback;
import licenta.beatyourmeal.database.DatabaseManager;


public class BiometricDataService {

    private final BiometricDataDao biometricDataDao;
    private final AsyncTaskRunner asyncTaskRunner;

    public BiometricDataService(Context context) {
        this.biometricDataDao = DatabaseManager.getInstance(context).getBiometricDataDao();
        this.asyncTaskRunner = new AsyncTaskRunner();
    }

    public void insert(BiometricData biometricData, Callback<BiometricData> activityThread) {
        //operation executata pe un alt thread
        Callable<BiometricData> insertOperation = new Callable<BiometricData>() {
            @Override
            public BiometricData call() {
                if (biometricData == null || biometricData.getId() > 0) {
                    return null;
                }
                long id = biometricDataDao.insert(biometricData);
                if (id < 0) { //ceva nu a functionat in scriptul de insert
                    return null;
                }
                biometricData.setId(id);
                return biometricData;
            }
        };

        //pornire thread secundar
        asyncTaskRunner.executeAsync(insertOperation, activityThread);
    }

    public void getAll(Callback<List<BiometricData>> activityThread) {
        Callable<List<BiometricData>> selectOperation = new Callable<List<BiometricData>>() {
            @Override
            public List<BiometricData> call() {
                return biometricDataDao.getAll();
            }
        };

        asyncTaskRunner.executeAsync(selectOperation, activityThread);
    }

    public void getBiometricDataByUserId(long introducedUserId, Callback<BiometricData> activityThread) {
        Callable<BiometricData> selectOperation = new Callable<BiometricData>() {
            @Override
            public BiometricData call() {
                return biometricDataDao.getBiometricDataByUserId(introducedUserId);
            }
        };

        asyncTaskRunner.executeAsync(selectOperation, activityThread);
    }

    public void getBiometricDataById(long introducedBiometricDataId, Callback<BiometricData> activityThread) {
        Callable<BiometricData> selectOperation = new Callable<BiometricData>() {
            @Override
            public BiometricData call() {
                return biometricDataDao.getBiometricDataById(introducedBiometricDataId);
            }
        };

        asyncTaskRunner.executeAsync(selectOperation, activityThread);
    }

    public void update(BiometricData biometricData, Callback<BiometricData> activityThread) {
        Callable<BiometricData> updateOperation = new Callable<BiometricData>() {
            @Override
            public BiometricData call() {
                if (biometricData == null || biometricData.getId() <= 0) {
                    return null;
                }
                int count = biometricDataDao.update(biometricData);
                if (count <= 0) {
                    return null;
                }
                return biometricData;
            }
        };

        asyncTaskRunner.executeAsync(updateOperation, activityThread);
    }

    public void delete(BiometricData biometricData, Callback<Boolean> activityThread) {
        Callable<Boolean> deleteOperation = new Callable<Boolean>() {
            @Override
            public Boolean call() {
                if (biometricData == null || biometricData.getId() <= 0) {
                    return false;
                }
                int count = biometricDataDao.delete(biometricData);
                return count > 0;
            }
        };

        asyncTaskRunner.executeAsync(deleteOperation, activityThread);
    }

}
