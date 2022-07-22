package licenta.beatyourmeal.database.ingredient;

import android.content.Context;

import java.util.List;
import java.util.concurrent.Callable;

import licenta.beatyourmeal.async.AsyncTaskRunner;
import licenta.beatyourmeal.async.Callback;
import licenta.beatyourmeal.database.DatabaseManager;


public class IngredientService {

    private final IngredientDao ingredientDao;
    private final AsyncTaskRunner asyncTaskRunner;

    public IngredientService(Context context) {
        this.ingredientDao = DatabaseManager.getInstance(context).getIngredientDao();
        this.asyncTaskRunner = new AsyncTaskRunner();
    }

    public void insert(Ingredient ingredient, Callback<Ingredient> activityThread) {
        //operation executata pe un alt thread
        Callable<Ingredient> insertOperation = new Callable<Ingredient>() {
            @Override
            public Ingredient call() {
                if (ingredient == null || ingredient.getId() > 0) {
                    return null;
                }
                long id = ingredientDao.insert(ingredient);
                if (id < 0) { //ceva nu a functionat in scriptul de insert
                    return null;
                }
                ingredient.setId(id);
                return ingredient;
            }
        };

        //pornire thread secundar
        asyncTaskRunner.executeAsync(insertOperation, activityThread);
    }

    public void getAll(Callback<List<Ingredient>> activityThread) {
        Callable<List<Ingredient>> selectOperation = new Callable<List<Ingredient>>() {
            @Override
            public List<Ingredient> call() {
                return ingredientDao.getAll();
            }
        };

        asyncTaskRunner.executeAsync(selectOperation, activityThread);
    }

    public void getIngredientsByRecipeId(long introducedRecipeId, Callback<List<Ingredient>> activityThread) {
        Callable<List<Ingredient>> selectOperation = new Callable<List<Ingredient>>() {
            @Override
            public List<Ingredient> call() {
                return ingredientDao.getIngredientsByRecipeId(introducedRecipeId);
            }
        };

        asyncTaskRunner.executeAsync(selectOperation, activityThread);
    }

    public void update(Ingredient ingredient, Callback<Ingredient> activityThread) {
        Callable<Ingredient> updateOperation = new Callable<Ingredient>() {
            @Override
            public Ingredient call() {
                if (ingredient == null || ingredient.getId() <= 0) {
                    return null;
                }
                int count = ingredientDao.update(ingredient);
                if (count <= 0) {
                    return null;
                }
                return ingredient;
            }
        };

        asyncTaskRunner.executeAsync(updateOperation, activityThread);
    }

    public void delete(Ingredient ingredient, Callback<Boolean> activityThread) {
        Callable<Boolean> deleteOperation = new Callable<Boolean>() {
            @Override
            public Boolean call() {
                if (ingredient == null || ingredient.getId() <= 0) {
                    return false;
                }
                int count = ingredientDao.delete(ingredient);
                return count > 0;
            }
        };

        asyncTaskRunner.executeAsync(deleteOperation, activityThread);
    }

}
