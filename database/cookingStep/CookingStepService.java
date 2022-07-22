package licenta.beatyourmeal.database.cookingStep;

import android.content.Context;

import java.util.List;
import java.util.concurrent.Callable;

import licenta.beatyourmeal.async.AsyncTaskRunner;
import licenta.beatyourmeal.async.Callback;
import licenta.beatyourmeal.database.DatabaseManager;


public class CookingStepService {

    private final CookingStepDao cookingStepDao;
    private final AsyncTaskRunner asyncTaskRunner;

    public CookingStepService(Context context) {
        this.cookingStepDao = DatabaseManager.getInstance(context).getCookingStepDao();
        this.asyncTaskRunner = new AsyncTaskRunner();
    }

    public void insert(CookingStep cookingStep, Callback<CookingStep> activityThread) {
        //operation executata pe un alt thread
        Callable<CookingStep> insertOperation = new Callable<CookingStep>() {
            @Override
            public CookingStep call() {
                if (cookingStep == null || cookingStep.getId() > 0) {
                    return null;
                }
                long id = cookingStepDao.insert(cookingStep);
                if (id < 0) { //ceva nu a functionat in scriptul de insert
                    return null;
                }
                cookingStep.setId(id);
                return cookingStep;
            }
        };

        //pornire thread secundar
        asyncTaskRunner.executeAsync(insertOperation, activityThread);
    }

    public void getAll(Callback<List<CookingStep>> activityThread) {
        Callable<List<CookingStep>> selectOperation = new Callable<List<CookingStep>>() {
            @Override
            public List<CookingStep> call() {
                return cookingStepDao.getAll();
            }
        };

        asyncTaskRunner.executeAsync(selectOperation, activityThread);
    }

    public void getCookingStepsByRecipeId(long introducedRecipeId, Callback<List<CookingStep>> activityThread) {
        Callable<List<CookingStep>> selectOperation = new Callable<List<CookingStep>>() {
            @Override
            public List<CookingStep> call() {
                return cookingStepDao.getCookingStepsByRecipeId(introducedRecipeId);
            }
        };

        asyncTaskRunner.executeAsync(selectOperation, activityThread);
    }

    public void update(CookingStep cookingStep, Callback<CookingStep> activityThread) {
        Callable<CookingStep> updateOperation = new Callable<CookingStep>() {
            @Override
            public CookingStep call() {
                if (cookingStep == null || cookingStep.getId() <= 0) {
                    return null;
                }
                int count = cookingStepDao.update(cookingStep);
                if (count <= 0) {
                    return null;
                }
                return cookingStep;
            }
        };

        asyncTaskRunner.executeAsync(updateOperation, activityThread);
    }

    public void delete(CookingStep cookingStep, Callback<Boolean> activityThread) {
        Callable<Boolean> deleteOperation = new Callable<Boolean>() {
            @Override
            public Boolean call() {
                if (cookingStep == null || cookingStep.getId() <= 0) {
                    return false;
                }
                int count = cookingStepDao.delete(cookingStep);
                return count > 0;
            }
        };

        asyncTaskRunner.executeAsync(deleteOperation, activityThread);
    }

}
