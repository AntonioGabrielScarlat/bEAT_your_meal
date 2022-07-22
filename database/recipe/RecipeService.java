package licenta.beatyourmeal.database.recipe;

import android.content.Context;

import java.util.List;
import java.util.concurrent.Callable;

import licenta.beatyourmeal.async.AsyncTaskRunner;
import licenta.beatyourmeal.async.Callback;
import licenta.beatyourmeal.database.DatabaseManager;

public class RecipeService {
    private final RecipeDao recipeDao;
    private final AsyncTaskRunner asyncTaskRunner;

    public RecipeService(Context context) {
        this.recipeDao = DatabaseManager.getInstance(context).getRecipeDao();
        this.asyncTaskRunner = new AsyncTaskRunner();
    }

    public void insert(Recipe recipe, Callback<Recipe> activityThread) {
        //operation executata pe un alt thread
        Callable<Recipe> insertOperation = new Callable<Recipe>() {
            @Override
            public Recipe call() {
                if (recipe == null || recipe.getId() > 0) {
                    return null;
                }
                long id = recipeDao.insert(recipe);
                if (id < 0) { //ceva nu a functionat in scriptul de insert
                    return null;
                }
                recipe.setId(id);
                return recipe;
            }
        };

        //pornire thread secundar
        asyncTaskRunner.executeAsync(insertOperation, activityThread);
    }

    public void getAll(Callback<List<Recipe>> activityThread) {
        Callable<List<Recipe>> selectOperation = new Callable<List<Recipe>>() {
            @Override
            public List<Recipe> call() {
                return recipeDao.getAll();
            }
        };

        asyncTaskRunner.executeAsync(selectOperation, activityThread);
    }

    public void getRecipeByName(String introducedName, Callback<Recipe> activityThread) {
        Callable<Recipe> selectOperation = new Callable<Recipe>() {
            @Override
            public Recipe call() {
                return recipeDao.getRecipeByName(introducedName);
            }
        };

        asyncTaskRunner.executeAsync(selectOperation, activityThread);
    }

    public void getRecipeById(long introducedId, Callback<Recipe> activityThread) {
        Callable<Recipe> selectOperation = new Callable<Recipe>() {
            @Override
            public Recipe call() {
                return recipeDao.getRecipeById(introducedId);
            }
        };

        asyncTaskRunner.executeAsync(selectOperation, activityThread);
    }

    public void getRecipesByType(String introducedCategory, Callback<List<Recipe>> activityThread) {
        Callable<List<Recipe>> selectOperation = new Callable<List<Recipe>>() {
            @Override
            public List<Recipe> call() {
                return recipeDao.getRecipesByType(introducedCategory);
            }
        };

        asyncTaskRunner.executeAsync(selectOperation, activityThread);
    }

    public void getRecipesByTypeAndCalories(String introducedCategory, Double introducedCalories, Callback<List<Recipe>> activityThread) {
        Callable<List<Recipe>> selectOperation = new Callable<List<Recipe>>() {
            @Override
            public List<Recipe> call() {
                return recipeDao.getRecipesByTypeAndCalories(introducedCategory,introducedCalories);
            }
        };

        asyncTaskRunner.executeAsync(selectOperation, activityThread);
    }

    public void getRecipesByTypeAndFoodPreferences(String introducedCategory,long introducedUserId, Callback<List<Recipe>> activityThread) {
        Callable<List<Recipe>> selectOperation = new Callable<List<Recipe>>() {
            @Override
            public List<Recipe> call() {
                return recipeDao.getRecipesByTypeAndFoodPreferences(introducedCategory,introducedUserId);
            }
        };

        asyncTaskRunner.executeAsync(selectOperation, activityThread);
    }

    public void getRecipesByTypeAndCaloriesAndFoodPreferences(String introducedCategory, Double introducedCalories,long introducedUserId, Callback<List<Recipe>> activityThread) {
        Callable<List<Recipe>> selectOperation = new Callable<List<Recipe>>() {
            @Override
            public List<Recipe> call() {
                return recipeDao.getRecipesByTypeAndCaloriesAndFoodPreferences(introducedCategory,introducedCalories,introducedUserId);
            }
        };

        asyncTaskRunner.executeAsync(selectOperation, activityThread);
    }

    public void update(Recipe recipe, Callback<Recipe> activityThread) {
        Callable<Recipe> updateOperation = new Callable<Recipe>() {
            @Override
            public Recipe call() {
                if (recipe == null || recipe.getId() <= 0) {
                    return null;
                }
                int count = recipeDao.update(recipe);
                if (count <= 0) {
                    return null;
                }
                return recipe;
            }
        };

        asyncTaskRunner.executeAsync(updateOperation, activityThread);
    }

    public void delete(Recipe recipe, Callback<Boolean> activityThread) {
        Callable<Boolean> deleteOperation = new Callable<Boolean>() {
            @Override
            public Boolean call() {
                if (recipe == null || recipe.getId() <= 0) {
                    return false;
                }
                int count = recipeDao.delete(recipe);
                return count > 0;
            }
        };

        asyncTaskRunner.executeAsync(deleteOperation, activityThread);
    }

}
