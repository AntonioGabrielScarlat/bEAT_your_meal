package licenta.beatyourmeal.database.eatenMeal;

import android.content.Context;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import licenta.beatyourmeal.async.AsyncTaskRunner;
import licenta.beatyourmeal.async.Callback;
import licenta.beatyourmeal.database.DatabaseManager;


public class EatenMealService {

    private final EatenMealDao eatenMealDao;
    private final AsyncTaskRunner asyncTaskRunner;

    public EatenMealService(Context context) {
        this.eatenMealDao = DatabaseManager.getInstance(context).getEatenMealDao();
        this.asyncTaskRunner = new AsyncTaskRunner();
    }

    public void insert(EatenMeal eatenMeal, Callback<EatenMeal> activityThread) {
        //operation executata pe un alt thread
        Callable<EatenMeal> insertOperation = new Callable<EatenMeal>() {
            @Override
            public EatenMeal call() {
                if (eatenMeal == null || eatenMeal.getId() > 0) {
                    return null;
                }
                long id = eatenMealDao.insert(eatenMeal);
                if (id < 0) { //ceva nu a functionat in scriptul de insert
                    return null;
                }
                eatenMeal.setId(id);
                return eatenMeal;
            }
        };

        //pornire thread secundar
        asyncTaskRunner.executeAsync(insertOperation, activityThread);
    }

    public void getAll(Callback<List<EatenMeal>> activityThread) {
        Callable<List<EatenMeal>> selectOperation = new Callable<List<EatenMeal>>() {
            @Override
            public List<EatenMeal> call() {
                return eatenMealDao.getAll();
            }
        };

        asyncTaskRunner.executeAsync(selectOperation, activityThread);
    }

    public void getEatenMealsByConsumptionDateAndUserId(long introducedUserId, Date introducedDate, Callback<List<EatenMeal>> activityThread) {
        Callable<List<EatenMeal>> selectOperation = new Callable<List<EatenMeal>>() {
            @Override
            public List<EatenMeal> call() {
                return eatenMealDao.getEatenMealByConsumptionDateAndUserId(introducedUserId,introducedDate);
            }
        };

        asyncTaskRunner.executeAsync(selectOperation, activityThread);
    }

    public void getEatenMealsByRecipeNameAndMealOfTheDay(String introducedName, String introducedMealOfTheDay, Callback<List<EatenMeal>> activityThread) {
        Callable<List<EatenMeal>> selectOperation = new Callable<List<EatenMeal>>() {
            @Override
            public List<EatenMeal> call() {
                return eatenMealDao.getEatenMealsByRecipeNameAndMealOfTheDay(introducedName,introducedMealOfTheDay);
            }
        };

        asyncTaskRunner.executeAsync(selectOperation, activityThread);
    }

    public void update(EatenMeal eatenMeal, Callback<EatenMeal> activityThread) {
        Callable<EatenMeal> updateOperation = new Callable<EatenMeal>() {
            @Override
            public EatenMeal call() {
                if (eatenMeal == null || eatenMeal.getId() <= 0) {
                    return null;
                }
                int count = eatenMealDao.update(eatenMeal);
                if (count <= 0) {
                    return null;
                }
                return eatenMeal;
            }
        };

        asyncTaskRunner.executeAsync(updateOperation, activityThread);
    }

    public void delete(EatenMeal eatenMeal, Callback<Boolean> activityThread) {
        Callable<Boolean> deleteOperation = new Callable<Boolean>() {
            @Override
            public Boolean call() {
                if (eatenMeal == null || eatenMeal.getId() <= 0) {
                    return false;
                }
                int count = eatenMealDao.delete(eatenMeal);
                return count > 0;
            }
        };

        asyncTaskRunner.executeAsync(deleteOperation, activityThread);
    }

}
