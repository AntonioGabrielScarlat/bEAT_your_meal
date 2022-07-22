package licenta.beatyourmeal.database.recommendedMeal;

import android.content.Context;

import java.util.List;
import java.util.concurrent.Callable;

import licenta.beatyourmeal.async.AsyncTaskRunner;
import licenta.beatyourmeal.async.Callback;
import licenta.beatyourmeal.database.DatabaseManager;


public class RecommendedMealService {

    private final RecommendedMealDao recommendedMealDao;
    private final AsyncTaskRunner asyncTaskRunner;

    public RecommendedMealService(Context context) {
        this.recommendedMealDao = DatabaseManager.getInstance(context).getRecommendedMealDao();
        this.asyncTaskRunner = new AsyncTaskRunner();
    }

    public void insert(RecommendedMeal recommendedMeal, Callback<RecommendedMeal> activityThread) {
        //operation executata pe un alt thread
        Callable<RecommendedMeal> insertOperation = new Callable<RecommendedMeal>() {
            @Override
            public RecommendedMeal call() {
                if (recommendedMeal == null || recommendedMeal.getId() > 0) {
                    return null;
                }
                long id = recommendedMealDao.insert(recommendedMeal);
                if (id < 0) { //ceva nu a functionat in scriptul de insert
                    return null;
                }
                recommendedMeal.setId(id);
                return recommendedMeal;
            }
        };

        //pornire thread secundar
        asyncTaskRunner.executeAsync(insertOperation, activityThread);
    }

    public void getAll(Callback<List<RecommendedMeal>> activityThread) {
        Callable<List<RecommendedMeal>> selectOperation = new Callable<List<RecommendedMeal>>() {
            @Override
            public List<RecommendedMeal> call() {
                return recommendedMealDao.getAll();
            }
        };

        asyncTaskRunner.executeAsync(selectOperation, activityThread);
    }

    public void getRecommendedMealByMealPlanId(long introducedMealPlanId, Callback<List<RecommendedMeal>> activityThread) {
        Callable<List<RecommendedMeal>> selectOperation = new Callable<List<RecommendedMeal>>() {
            @Override
            public List<RecommendedMeal> call() {
                return recommendedMealDao.getRecommendedMealByMealPlanId(introducedMealPlanId);
            }
        };

        asyncTaskRunner.executeAsync(selectOperation, activityThread);
    }

    public void update(RecommendedMeal recommendedMeal, Callback<RecommendedMeal> activityThread) {
        Callable<RecommendedMeal> updateOperation = new Callable<RecommendedMeal>() {
            @Override
            public RecommendedMeal call() {
                if (recommendedMeal == null || recommendedMeal.getId() <= 0) {
                    return null;
                }
                int count = recommendedMealDao.update(recommendedMeal);
                if (count <= 0) {
                    return null;
                }
                return recommendedMeal;
            }
        };

        asyncTaskRunner.executeAsync(updateOperation, activityThread);
    }

    public void delete(RecommendedMeal recommendedMeal, Callback<Boolean> activityThread) {
        Callable<Boolean> deleteOperation = new Callable<Boolean>() {
            @Override
            public Boolean call() {
                if (recommendedMeal == null || recommendedMeal.getId() <= 0) {
                    return false;
                }
                int count = recommendedMealDao.delete(recommendedMeal);
                return count > 0;
            }
        };

        asyncTaskRunner.executeAsync(deleteOperation, activityThread);
    }

}
