package licenta.beatyourmeal.database.nutritionalData;

import android.content.Context;

import java.util.List;
import java.util.concurrent.Callable;

import licenta.beatyourmeal.async.AsyncTaskRunner;
import licenta.beatyourmeal.async.Callback;
import licenta.beatyourmeal.database.DatabaseManager;


public class NutritionalDataService {

    private final NutritionalDataDao nutritionalDataDao;
    private final AsyncTaskRunner asyncTaskRunner;

    public NutritionalDataService(Context context) {
        this.nutritionalDataDao = DatabaseManager.getInstance(context).getNutritionalDataDao();
        this.asyncTaskRunner = new AsyncTaskRunner();
    }

    public void insert(NutritionalData nutritionalData, Callback<NutritionalData> activityThread) {
        //operation executata pe un alt thread
        Callable<NutritionalData> insertOperation = new Callable<NutritionalData>() {
            @Override
            public NutritionalData call() {
                if (nutritionalData == null || nutritionalData.getId() > 0) {
                    return null;
                }
                long id = nutritionalDataDao.insert(nutritionalData);
                if (id < 0) { //ceva nu a functionat in scriptul de insert
                    return null;
                }
                nutritionalData.setId(id);
                return nutritionalData;
            }
        };

        //pornire thread secundar
        asyncTaskRunner.executeAsync(insertOperation, activityThread);
    }

    public void getAll(Callback<List<NutritionalData>> activityThread) {
        Callable<List<NutritionalData>> selectOperation = new Callable<List<NutritionalData>>() {
            @Override
            public List<NutritionalData> call() {
                return nutritionalDataDao.getAll();
            }
        };

        asyncTaskRunner.executeAsync(selectOperation, activityThread);
    }


    public void getNutritionalDataByRecipeId(long introducedRecipeId, Callback<NutritionalData> activityThread) {
        Callable<NutritionalData> selectOperation = new Callable<NutritionalData>() {
            @Override
            public NutritionalData call() {
                return nutritionalDataDao.getNutritionalDataByRecipeId(introducedRecipeId);
            }
        };

        asyncTaskRunner.executeAsync(selectOperation, activityThread);
    }

    public void update(NutritionalData nutritionalData, Callback<NutritionalData> activityThread) {
        Callable<NutritionalData> updateOperation = new Callable<NutritionalData>() {
            @Override
            public NutritionalData call() {
                if (nutritionalData == null || nutritionalData.getId() <= 0) {
                    return null;
                }
                int count = nutritionalDataDao.update(nutritionalData);
                if (count <= 0) {
                    return null;
                }
                return nutritionalData;
            }
        };

        asyncTaskRunner.executeAsync(updateOperation, activityThread);
    }

    public void delete(NutritionalData nutritionalData, Callback<Boolean> activityThread) {
        Callable<Boolean> deleteOperation = new Callable<Boolean>() {
            @Override
            public Boolean call() {
                if (nutritionalData == null || nutritionalData.getId() <= 0) {
                    return false;
                }
                int count = nutritionalDataDao.delete(nutritionalData);
                return count > 0;
            }
        };

        asyncTaskRunner.executeAsync(deleteOperation, activityThread);
    }

}
