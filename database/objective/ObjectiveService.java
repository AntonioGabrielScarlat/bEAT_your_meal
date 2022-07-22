package licenta.beatyourmeal.database.objective;

import android.content.Context;

import java.util.List;
import java.util.concurrent.Callable;

import licenta.beatyourmeal.async.AsyncTaskRunner;
import licenta.beatyourmeal.async.Callback;
import licenta.beatyourmeal.database.DatabaseManager;


public class ObjectiveService {

    private final ObjectiveDao objectiveDao;
    private final AsyncTaskRunner asyncTaskRunner;

    public ObjectiveService(Context context) {
        this.objectiveDao = DatabaseManager.getInstance(context).getObjectiveDao();
        this.asyncTaskRunner = new AsyncTaskRunner();
    }

    public void insert(Objective objective, Callback<Objective> activityThread) {
        //operation executata pe un alt thread
        Callable<Objective> insertOperation = new Callable<Objective>() {
            @Override
            public Objective call() {
                if (objective == null || objective.getId() > 0) {
                    return null;
                }
                long id = objectiveDao.insert(objective);
                if (id < 0) { //ceva nu a functionat in scriptul de insert
                    return null;
                }
                objective.setId(id);
                return objective;
            }
        };

        //pornire thread secundar
        asyncTaskRunner.executeAsync(insertOperation, activityThread);
    }

    public void getAll(Callback<List<Objective>> activityThread) {
        Callable<List<Objective>> selectOperation = new Callable<List<Objective>>() {
            @Override
            public List<Objective> call() {
                return objectiveDao.getAll();
            }
        };

        asyncTaskRunner.executeAsync(selectOperation, activityThread);
    }

    public void getObjectiveByUserId(long introducedUserId, Callback<Objective> activityThread) {
        Callable<Objective> selectOperation = new Callable<Objective>() {
            @Override
            public Objective call() {
                return objectiveDao.getObjectiveByUserId(introducedUserId);
            }
        };

        asyncTaskRunner.executeAsync(selectOperation, activityThread);
    }

    public void update(Objective objective, Callback<Objective> activityThread) {
        Callable<Objective> updateOperation = new Callable<Objective>() {
            @Override
            public Objective call() {
                if (objective == null || objective.getId() <= 0) {
                    return null;
                }
                int count = objectiveDao.update(objective);
                if (count <= 0) {
                    return null;
                }
                return objective;
            }
        };

        asyncTaskRunner.executeAsync(updateOperation, activityThread);
    }

    public void delete(Objective objective, Callback<Boolean> activityThread) {
        Callable<Boolean> deleteOperation = new Callable<Boolean>() {
            @Override
            public Boolean call() {
                if (objective == null || objective.getId() <= 0) {
                    return false;
                }
                int count = objectiveDao.delete(objective);
                return count > 0;
            }
        };

        asyncTaskRunner.executeAsync(deleteOperation, activityThread);
    }


}
