package licenta.beatyourmeal.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import licenta.beatyourmeal.database.aliment.Aliment;
import licenta.beatyourmeal.database.aliment.AlimentDao;
import licenta.beatyourmeal.database.biometricData.BiometricData;
import licenta.beatyourmeal.database.biometricData.BiometricDataDao;
import licenta.beatyourmeal.database.cookingStep.CookingStep;
import licenta.beatyourmeal.database.cookingStep.CookingStepDao;
import licenta.beatyourmeal.database.eatenMeal.EatenMeal;
import licenta.beatyourmeal.database.ingredient.Ingredient;
import licenta.beatyourmeal.database.ingredient.IngredientDao;
import licenta.beatyourmeal.database.eatenMeal.EatenMealDao;
import licenta.beatyourmeal.database.mealPlan.MealPlan;
import licenta.beatyourmeal.database.mealPlan.MealPlanDao;
import licenta.beatyourmeal.database.nutritionalData.NutritionalData;
import licenta.beatyourmeal.database.nutritionalData.NutritionalDataDao;
import licenta.beatyourmeal.database.objective.Objective;
import licenta.beatyourmeal.database.objective.ObjectiveDao;
import licenta.beatyourmeal.database.preference.Preference;
import licenta.beatyourmeal.database.preference.PreferenceDao;
import licenta.beatyourmeal.database.rating.Rating;
import licenta.beatyourmeal.database.rating.RatingDao;
import licenta.beatyourmeal.database.recipe.Recipe;
import licenta.beatyourmeal.database.recipe.RecipeDao;
import licenta.beatyourmeal.database.recommendedMeal.RecommendedMeal;
import licenta.beatyourmeal.database.recommendedMeal.RecommendedMealDao;
import licenta.beatyourmeal.database.user.User;
import licenta.beatyourmeal.database.user.UserDao;
import licenta.beatyourmeal.auxiliary.converters.DateConverter;


@Database(entities = {User.class, Aliment.class, Recipe.class, BiometricData.class, Ingredient.class, NutritionalData.class,
        Objective.class, Preference.class, EatenMeal.class, MealPlan.class, RecommendedMeal.class, CookingStep.class, Rating.class}
        , exportSchema = false, version = 1)
@TypeConverters({DateConverter.class})
public abstract class DatabaseManager extends RoomDatabase {
    public static final String bEAT_your_meal_DB = "bEAT_your_meal_db";
    private static DatabaseManager databaseManager;
    public static DatabaseManager getInstance(Context context) {
        if (databaseManager == null) {
            synchronized (DatabaseManager.class) {
                if (databaseManager == null) {
                    databaseManager = Room.databaseBuilder(context, DatabaseManager.class, bEAT_your_meal_DB)
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return databaseManager;
    }
    public abstract UserDao getUserDao();
    public abstract AlimentDao getAlimentDao();
    public abstract RecipeDao getRecipeDao();
    public abstract BiometricDataDao getBiometricDataDao();
    public abstract IngredientDao getIngredientDao();
    public abstract NutritionalDataDao getNutritionalDataDao();
    public abstract ObjectiveDao getObjectiveDao();
    public abstract PreferenceDao getPreferenceDao();
    public abstract EatenMealDao getEatenMealDao();
    public abstract MealPlanDao getMealPlanDao();
    public abstract RecommendedMealDao getRecommendedMealDao();
    public abstract CookingStepDao getCookingStepDao();
    public abstract RatingDao getRatingDao();  }
