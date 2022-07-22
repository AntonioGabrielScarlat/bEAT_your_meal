package licenta.beatyourmeal.database.mealPlan;

import android.content.Context;

import java.util.List;
import java.util.concurrent.Callable;

import licenta.beatyourmeal.async.AsyncTaskRunner;
import licenta.beatyourmeal.async.Callback;
import licenta.beatyourmeal.database.DatabaseManager;


public class MealPlanService {

    private final MealPlanDao mealPlanDao;
    private final AsyncTaskRunner asyncTaskRunner;

    public MealPlanService(Context context) {
        this.mealPlanDao = DatabaseManager.getInstance(context).getMealPlanDao();
        this.asyncTaskRunner = new AsyncTaskRunner();
    }

    public void insert(MealPlan mealPlan, Callback<MealPlan> activityThread) {
        //operation executata pe un alt thread
        Callable<MealPlan> insertOperation = new Callable<MealPlan>() {
            @Override
            public MealPlan call() {
                if (mealPlan == null || mealPlan.getId() > 0) {
                    return null;
                }
                long id = mealPlanDao.insert(mealPlan);
                if (id < 0) { //ceva nu a functionat in scriptul de insert
                    return null;
                }
                mealPlan.setId(id);
                return mealPlan;
            }
        };

        //pornire thread secundar
        asyncTaskRunner.executeAsync(insertOperation, activityThread);
    }

    public void getAll(Callback<List<MealPlan>> activityThread) {
        Callable<List<MealPlan>> selectOperation = new Callable<List<MealPlan>>() {
            @Override
            public List<MealPlan> call() {
                return mealPlanDao.getAll();
            }
        };

        asyncTaskRunner.executeAsync(selectOperation, activityThread);
    }

    public void getMealPlanByUserId(long introducedUserId, Callback<MealPlan> activityThread) {
        Callable<MealPlan> selectOperation = new Callable<MealPlan>() {
            @Override
            public MealPlan call() {
                return mealPlanDao.getMealPlanByUserId(introducedUserId);
            }
        };

        asyncTaskRunner.executeAsync(selectOperation, activityThread);
    }

    public void update(MealPlan mealPlan, Callback<MealPlan> activityThread) {
        Callable<MealPlan> updateOperation = new Callable<MealPlan>() {
            @Override
            public MealPlan call() {
                if (mealPlan == null || mealPlan.getId() <= 0) {
                    return null;
                }
                int count = mealPlanDao.update(mealPlan);
                if (count <= 0) {
                    return null;
                }
                return mealPlan;
            }
        };

        asyncTaskRunner.executeAsync(updateOperation, activityThread);
    }

    public void delete(MealPlan mealPlan, Callback<Boolean> activityThread) {
        Callable<Boolean> deleteOperation = new Callable<Boolean>() {
            @Override
            public Boolean call() {
                if (mealPlan == null || mealPlan.getId() <= 0) {
                    return false;
                }
                int count = mealPlanDao.delete(mealPlan);
                return count > 0;
            }
        };

        asyncTaskRunner.executeAsync(deleteOperation, activityThread);
    }


}
