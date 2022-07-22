package licenta.beatyourmeal.database.preference;

import android.content.Context;

import java.util.List;
import java.util.concurrent.Callable;

import licenta.beatyourmeal.async.AsyncTaskRunner;
import licenta.beatyourmeal.async.Callback;
import licenta.beatyourmeal.database.DatabaseManager;


public class PreferenceService {

    private final PreferenceDao preferenceDao;
    private final AsyncTaskRunner asyncTaskRunner;

    public PreferenceService(Context context) {
        this.preferenceDao = DatabaseManager.getInstance(context).getPreferenceDao();
        this.asyncTaskRunner = new AsyncTaskRunner();
    }

    public void insert(Preference preference, Callback<Preference> activityThread) {
        //operation executata pe un alt thread
        Callable<Preference> insertOperation = new Callable<Preference>() {
            @Override
            public Preference call() {
                if (preference == null || preference.getId() > 0) {
                    return null;
                }
                long id = preferenceDao.insert(preference);
                if (id < 0) { //ceva nu a functionat in scriptul de insert
                    return null;
                }
                preference.setId(id);
                return preference;
            }
        };

        //pornire thread secundar
        asyncTaskRunner.executeAsync(insertOperation, activityThread);
    }

    public void getAll(Callback<List<Preference>> activityThread) {
        Callable<List<Preference>> selectOperation = new Callable<List<Preference>>() {
            @Override
            public List<Preference> call() {
                return preferenceDao.getAll();
            }
        };

        asyncTaskRunner.executeAsync(selectOperation, activityThread);
    }

    public void getPreferenceByUserIdAndAlimentId(long introducedUserId,long introducedAlimentId, Callback<Preference> activityThread) {
        Callable<Preference> selectOperation = new Callable<Preference>() {
            @Override
            public Preference call() {
                return preferenceDao.getPreferenceByUserIdAndAlimentId(introducedUserId,introducedAlimentId);
            }
        };

        asyncTaskRunner.executeAsync(selectOperation, activityThread);
    }

    public void update(Preference preference, Callback<Preference> activityThread) {
        Callable<Preference> updateOperation = new Callable<Preference>() {
            @Override
            public Preference call() {
                if (preference == null || preference.getId() <= 0) {
                    return null;
                }
                int count = preferenceDao.update(preference);
                if (count <= 0) {
                    return null;
                }
                return preference;
            }
        };

        asyncTaskRunner.executeAsync(updateOperation, activityThread);
    }

    public void delete(Preference preference, Callback<Boolean> activityThread) {
        Callable<Boolean> deleteOperation = new Callable<Boolean>() {
            @Override
            public Boolean call() {
                if (preference == null || preference.getId() <= 0) {
                    return false;
                }
                int count = preferenceDao.delete(preference);
                return count > 0;
            }
        };

        asyncTaskRunner.executeAsync(deleteOperation, activityThread);
    }

}
