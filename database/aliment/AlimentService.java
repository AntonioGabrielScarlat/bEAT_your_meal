package licenta.beatyourmeal.database.aliment;

import android.content.Context;

import java.util.List;
import java.util.concurrent.Callable;

import licenta.beatyourmeal.async.AsyncTaskRunner;
import licenta.beatyourmeal.async.Callback;
import licenta.beatyourmeal.database.DatabaseManager;


public class AlimentService {

    private final AlimentDao alimentDao;
    private final AsyncTaskRunner asyncTaskRunner;

    public AlimentService(Context context) {
        this.alimentDao = DatabaseManager.getInstance(context).getAlimentDao();
        this.asyncTaskRunner = new AsyncTaskRunner();
    }

    public void insert(Aliment aliment, Callback<Aliment> activityThread) {
        Callable<Aliment> insertOperation = new Callable<Aliment>() {
            @Override
            public Aliment call() {
                if (aliment == null || aliment.getId() > 0) {
                    return null;
                }
                long id = alimentDao.insert(aliment);
                if (id < 0) {
                    return null;
                }
                aliment.setId(id);
                return aliment;
            }
        };

        asyncTaskRunner.executeAsync(insertOperation, activityThread);
    }

    public void getAll(Callback<List<Aliment>> activityThread) {
        Callable<List<Aliment>> selectOperation = new Callable<List<Aliment>>() {
            @Override
            public List<Aliment> call() {
                return alimentDao.getAll();
            }
        };

        asyncTaskRunner.executeAsync(selectOperation, activityThread);
    }

    public void getByName(String introducedName, Callback<Aliment> activityThread) {
        Callable<Aliment> selectOperation = new Callable<Aliment>() {
            @Override
            public Aliment call() {
                return alimentDao.getByName(introducedName);
            }
        };

        asyncTaskRunner.executeAsync(selectOperation, activityThread);
    }

    public void getById(long introducedId, Callback<Aliment> activityThread) {
        Callable<Aliment> selectOperation = new Callable<Aliment>() {
            @Override
            public Aliment call() {
                return alimentDao.getById(introducedId);
            }
        };

        asyncTaskRunner.executeAsync(selectOperation, activityThread);
    }

    public void update(Aliment aliment, Callback<Aliment> activityThread) {
        Callable<Aliment> updateOperation = new Callable<Aliment>() {
            @Override
            public Aliment call() {
                if (aliment == null || aliment.getId() <= 0) {
                    return null;
                }
                int count = alimentDao.update(aliment);
                if (count <= 0) {
                    return null;
                }
                return aliment;
            }
        };

        asyncTaskRunner.executeAsync(updateOperation, activityThread);
    }

    public void delete(Aliment aliment, Callback<Boolean> activityThread) {
        Callable<Boolean> deleteOperation = new Callable<Boolean>() {
            @Override
            public Boolean call() {
                if (aliment == null || aliment.getId() <= 0) {
                    return false;
                }
                int count = alimentDao.delete(aliment);
                return count > 0;
            }
        };

        asyncTaskRunner.executeAsync(deleteOperation, activityThread);
    }

}
