package licenta.beatyourmeal.activities.user.home.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;

import licenta.beatyourmeal.R;
import licenta.beatyourmeal.auxiliary.adapters.RecipeAdapter;
import licenta.beatyourmeal.async.Callback;
import licenta.beatyourmeal.auxiliary.adapters.RecommendedMealAdapter;
import licenta.beatyourmeal.auxiliary.converters.DateConverter;
import licenta.beatyourmeal.auxiliary.listed.ListedRecommendedMeal;
import licenta.beatyourmeal.database.DatabaseManager;
import licenta.beatyourmeal.database.aliment.Aliment;
import licenta.beatyourmeal.database.biometricData.BiometricData;
import licenta.beatyourmeal.database.biometricData.BiometricDataService;
import licenta.beatyourmeal.database.ingredient.Ingredient;
import licenta.beatyourmeal.database.mealPlan.MealPlan;
import licenta.beatyourmeal.database.mealPlan.MealPlanService;
import licenta.beatyourmeal.database.objective.Objective;
import licenta.beatyourmeal.database.objective.ObjectiveService;
import licenta.beatyourmeal.database.preference.Preference;
import licenta.beatyourmeal.database.recipe.Recipe;
import licenta.beatyourmeal.database.recommendedMeal.RecommendedMeal;
import licenta.beatyourmeal.database.recommendedMeal.RecommendedMealDao;
import licenta.beatyourmeal.database.recommendedMeal.RecommendedMealService;
import licenta.beatyourmeal.activities.user.AddFoodPreferencesActivity;
import licenta.beatyourmeal.activities.user.SeeRecipeActivity;

public class MealPlanFragment extends Fragment {

    private Button btnAddPreferences;
    private Button btnGenerateMealPlan;
    private ListView lvRecommendations;

    private AlertDialog.Builder builder;
    private Double currentWeight;
    private Double caloriesBudget;
    private Double dailyRemainedCalories;
    private String sex;
    private long userId;
    private List<Aliment> prefferedAliments=new ArrayList<>();
    private List<Ingredient> currentRecipeIngredients=new ArrayList<>();
    private List<Preference> preferences=new ArrayList<>();

    private List<Recipe> breakfastRecipes=new ArrayList<>();
    private List<Recipe> lunchRecipes=new ArrayList<>();
    private List<Recipe> dinnerRecipes=new ArrayList<>();
    private List<Recipe> recommendedMealPlan=new ArrayList<>();
    private List<ListedRecommendedMeal> recommendedMealPlanToShow=new ArrayList<>();

    private List<RecommendedMeal> recommendedMealPlanFromDatabase=new ArrayList<>();
    private List<RecommendedMeal> recommendedMeals=new ArrayList<>();

    private MealPlan currentMealPlan;
    private int randomMealIndex;
    private int i;

    private BiometricDataService biometricDataService;
    private ObjectiveService objectiveService;
    private MealPlanService mealPlanService;
    private RecommendedMealService recommendedMealService;

    private DatabaseManager db;
    private RecommendedMealDao recommendedMealDao;



    public MealPlanFragment(long userId) {
        this.userId = userId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_user_meal_plan,container,false);
        initComponents(view);
        return view;
    }

    private void initComponents(View view) {
       btnAddPreferences=view.findViewById(R.id.fragment_user_meal_plan_btn_see_food_preferences);
       btnGenerateMealPlan=view.findViewById(R.id.fragment_user_meal_plan_btn_generate);
       lvRecommendations=view.findViewById(R.id.fragment_user_meal_plan_lv_meals);
       addAdapter();
       btnAddPreferences.setOnClickListener(addPreferencesEventListener());
       btnGenerateMealPlan.setOnClickListener(generateMealPlanListener());
       lvRecommendations.setOnItemClickListener(selectSuggestedMealEventListener());
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        builder = new AlertDialog.Builder(this.getContext());
        db=DatabaseManager.getInstance(getContext().getApplicationContext());
        recommendedMealDao=db.getRecommendedMealDao();
        biometricDataService=new BiometricDataService(getContext().getApplicationContext());
        objectiveService=new ObjectiveService(getContext().getApplicationContext());
        mealPlanService=new MealPlanService(getContext().getApplicationContext());
        recommendedMealService=new RecommendedMealService(getContext().getApplicationContext());
        biometricDataService.getBiometricDataByUserId(userId, getBiometricDataByUserIdCallback());
        mealPlanService.getMealPlanByUserId(userId,getMealPlanByUserIdInitCallback());
    }

    private AdapterView.OnItemClickListener selectSuggestedMealEventListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext().getApplicationContext(), SeeRecipeActivity.class);
                intent.putExtra("recipe_name", recommendedMealPlanToShow.get(position).getRecipe().getName());
                startActivity(intent);
            }
        };
    }

    private void addAdapter() {
        RecommendedMealAdapter adapter = new RecommendedMealAdapter(getContext().getApplicationContext(), R.layout.lv_recommended_meal_row,
               recommendedMealPlanToShow , getLayoutInflater());
        lvRecommendations.setAdapter(adapter);
    }

    public void notifyAdapter() {
        ArrayAdapter<RecommendedMeal> adapter = (ArrayAdapter<RecommendedMeal>) lvRecommendations.getAdapter();
        adapter.notifyDataSetChanged();
    }



    private Callback<MealPlan> getMealPlanByUserIdInitCallback() {
        return new Callback<MealPlan>() {
            @Override
            public void runResultOnUiThread(MealPlan result) {
                if (result != null) {
                    Calendar c=Calendar.getInstance();
                    c.setTime(result.getStartDate());
                    c.add(Calendar.DATE,6);
                    Date endDate=c.getTime();
                    if(endDate.after(new Date())) {
                        recommendedMealService.getRecommendedMealByMealPlanId(result.getId(), getRecommendedMealByMealPlanIdInitCallback());
                    }
                    else {
                        mealPlanService.delete(result,deleteMealPlanCallbak());
                    }
                }
            }
        };
    }

    private Callback<Boolean> deleteMealPlanCallbak() {
        return new Callback<Boolean>() {
            @Override
            public void runResultOnUiThread(Boolean result) {
                if (result) {
                    Toast.makeText(getContext().getApplicationContext(),"No current meal plan!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private Callback<List<RecommendedMeal>> getRecommendedMealByMealPlanIdInitCallback() {
        return new Callback<List<RecommendedMeal>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void runResultOnUiThread(List<RecommendedMeal> results) {
                if (results != null) {
                    for(RecommendedMeal recommendedMeal:results){
                        recommendedMealPlanToShow.add(new ListedRecommendedMeal(db.getRecipeDao().getRecipeById(recommendedMeal.getIdRecipe()),
                                db.getNutritionalDataDao().getNutritionalDataByRecipeId(recommendedMeal.getIdRecipe()).getCalories(),
                                recommendedMeal.getRecommendedDateToEat()));
                    }
                    notifyAdapter();
                }
            }
        };
    }

    private View.OnClickListener generateMealPlanListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                prefferedAliments.clear();
                preferences.clear();
                preferences.addAll(db.getPreferenceDao().getPreferenceByUserId(userId));
                for(Preference preference:preferences){
                    prefferedAliments.add(db.getAlimentDao().getById(preference.getIdAliment()));
                }

                if(prefferedAliments.size()>0){
                    mealPlanService.getMealPlanByUserId(userId,getMealPlanByUserIdCallback());
                }
                else
                {
                    Toast.makeText(getContext().getApplicationContext(),
                            "You have to set your food preferences first!",Toast.LENGTH_SHORT).show();
                }

            }
        };
    }

    private Callback<MealPlan> getMealPlanByUserIdCallback() {
        return new Callback<MealPlan>() {
            @Override
            public void runResultOnUiThread(MealPlan result) {
                Calendar c=Calendar.getInstance();
                c.setTime(new Date());
                c.add(Calendar.DATE,1);
                Date startDate=c.getTime();
                if (result != null) {
                    builder.setMessage("You already have an active nutritional plan. Do you want to replace it?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    recommendedMealPlan.clear();
                                    recommendedMealPlanToShow.clear();
                                    result.setStartDate(startDate);
                                    mealPlanService.update(result,getUpdateMealPlanCallback());
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.setTitle("Change Meal Plan");
                    alert.show();
                }
                else
                {
                    recommendedMealPlan.clear();
                    recommendedMealPlanToShow.clear();
                    MealPlan mealPlan=new MealPlan(startDate,userId);
                    mealPlanService.insert(mealPlan,getInsertMealPlanCallback());
                }
            }
        }; }

    private Callback<MealPlan> getInsertMealPlanCallback() {
        return new Callback<MealPlan>() {
            @Override
            public void runResultOnUiThread(MealPlan result) {
                if (result != null) {
                    currentMealPlan=result;
                    breakfastRecipes.clear();
                    breakfastRecipes.addAll(db.getRecipeDao().getRecipesByType("Breakfast"));
                    lunchRecipes.clear();
                    lunchRecipes.addAll(db.getRecipeDao().getRecipesByType("Lunch"));
                    for(int i=0;i<7;i++){
                        int t=1;
                        int u;
                        while(t==1){
                            t=0;
                            randomMealIndex= new Random().nextInt(breakfastRecipes.size());
                            for(Recipe recipe:recommendedMealPlan){
                                if(recipe.getId()==breakfastRecipes.get(randomMealIndex).getId())
                                    t=1;
                            }
                            if(t==0)
                            {
                                currentRecipeIngredients.clear();
                            currentRecipeIngredients=db.getIngredientDao().getIngredientsByRecipeId(breakfastRecipes.get(randomMealIndex).getId());
                            for(Ingredient ingredient:currentRecipeIngredients)
                            {   u=0;
                                for(Aliment aliment:prefferedAliments){
                                    if(ingredient.getIdAliment()==aliment.getId()){
                                        u=1;
                                    }
                                }
                                if(u==0){
                                    t=1;
                                }
                            }
                            }
                        }
                        recommendedMealPlan.add(breakfastRecipes.get(randomMealIndex));
                        dailyRemainedCalories=caloriesBudget-db.getNutritionalDataDao()
                                .getNutritionalDataByRecipeId(breakfastRecipes.get(randomMealIndex).getId()).getCalories();
                        t=1;
                        while(t==1){
                            t=0;
                            randomMealIndex= new Random().nextInt(lunchRecipes.size());
                            for(Recipe recipe:recommendedMealPlan){
                                if(recipe.getId()==lunchRecipes.get(randomMealIndex).getId())
                                    t=1;
                            }
                            if(t==0)
                            {
                                currentRecipeIngredients.clear();
                            currentRecipeIngredients=db.getIngredientDao().getIngredientsByRecipeId(lunchRecipes.get(randomMealIndex).getId());
                            for(Ingredient ingredient:currentRecipeIngredients)
                            {   u=0;
                                for(Aliment aliment:prefferedAliments){
                                    if(ingredient.getIdAliment()==aliment.getId()){
                                        u=1;
                                    }
                                }
                                if(u==0){
                                    t=1;
                                }
                            }
                            }
                        }
                        recommendedMealPlan.add(lunchRecipes.get(randomMealIndex));
                        dailyRemainedCalories=caloriesBudget-db.getNutritionalDataDao()
                                .getNutritionalDataByRecipeId(lunchRecipes.get(randomMealIndex).getId()).getCalories();
                        dinnerRecipes.clear();
                        dinnerRecipes.addAll(db.getRecipeDao().getRecipesByTypeAndCalories("Dinner",dailyRemainedCalories));
                        t=1;
                        while(t==1){
                            t=0;
                            randomMealIndex= new Random().nextInt(dinnerRecipes.size());
                            for(Recipe recipe:recommendedMealPlan){
                                if(recipe.getId()==dinnerRecipes.get(randomMealIndex).getId())
                                    t=1;
                            }
                            if(t==0)
                            {
                                currentRecipeIngredients.clear();
                                currentRecipeIngredients=db.getIngredientDao().getIngredientsByRecipeId(dinnerRecipes.get(randomMealIndex).getId());
                                for(Ingredient ingredient:currentRecipeIngredients)
                                {   u=0;
                                    for(Aliment aliment:prefferedAliments){
                                        if(ingredient.getIdAliment()==aliment.getId()){
                                            u=1;
                                        }
                                    }
                                    if(u==0){
                                        t=1;
                                    }
                                }
                            }
                        }
                        recommendedMealPlan.add(dinnerRecipes.get(randomMealIndex));
                        dailyRemainedCalories=caloriesBudget;
                    }

                    if(recommendedMealPlan.size()==21)
                    {
                        Calendar c=Calendar.getInstance();
                        c.setTime(currentMealPlan.getStartDate());
                        Date currentDate=c.getTime();
                        i=0;
                        while(i<recommendedMealPlan.size()){
                            recommendedMealDao.insert(new RecommendedMeal(recommendedMealPlan.get(i).getId(),currentMealPlan.getId(),currentDate));
                            recommendedMealPlanToShow.add(new ListedRecommendedMeal(recommendedMealPlan.get(i),
                                    db.getNutritionalDataDao().getNutritionalDataByRecipeId(recommendedMealPlan.get(i).getId()).getCalories(),currentDate));
                            recommendedMealDao.insert(new RecommendedMeal(recommendedMealPlan.get(i+1).getId(),currentMealPlan.getId(),currentDate));
                            recommendedMealPlanToShow.add(new ListedRecommendedMeal(recommendedMealPlan.get(i+1),
                                    db.getNutritionalDataDao().getNutritionalDataByRecipeId(recommendedMealPlan.get(i).getId()).getCalories(),currentDate));
                            recommendedMealDao.insert(new RecommendedMeal(recommendedMealPlan.get(i+2).getId(),currentMealPlan.getId(),currentDate));
                            recommendedMealPlanToShow.add(new ListedRecommendedMeal(recommendedMealPlan.get(i+2),
                                    db.getNutritionalDataDao().getNutritionalDataByRecipeId(recommendedMealPlan.get(i).getId()).getCalories(),currentDate));
                            i=i+3;
                            c.add(Calendar.DATE,1);
                            currentDate=c.getTime();
                        }
                        notifyAdapter(); } } } }; }

    private Callback<MealPlan> getUpdateMealPlanCallback() {
        return new Callback<MealPlan>() {
            @Override
            public void runResultOnUiThread(MealPlan result) {
                if (result != null) {
                    recommendedMealPlanFromDatabase=db.getRecommendedMealDao().getRecommendedMealByMealPlanId(result.getId());
                    for(RecommendedMeal recommendedMeal:recommendedMealPlanFromDatabase){
                        recommendedMealDao.delete(recommendedMeal);
                    }

                    currentMealPlan=result;
                    breakfastRecipes.clear();
                    breakfastRecipes.addAll(db.getRecipeDao().getRecipesByType("Breakfast"));
                    lunchRecipes.clear();
                    lunchRecipes.addAll(db.getRecipeDao().getRecipesByType("Lunch"));

                    for(int i=0;i<7;i++){
                        int t=1;
                        int u;
                        while(t==1){
                            t=0;
                            randomMealIndex= new Random().nextInt(breakfastRecipes.size());
                            for(Recipe recipe:recommendedMealPlan){
                                if(recipe.getId()==breakfastRecipes.get(randomMealIndex).getId())
                                    t=1;
                            }
                            if(t==0)
                            {
                                currentRecipeIngredients.clear();
                                currentRecipeIngredients=db.getIngredientDao().getIngredientsByRecipeId(breakfastRecipes.get(randomMealIndex).getId());
                                for(Ingredient ingredient:currentRecipeIngredients)
                                {   u=0;
                                    for(Aliment aliment:prefferedAliments){
                                        if(ingredient.getIdAliment()==aliment.getId()){
                                            u=1;
                                        }
                                    }
                                    if(u==0){
                                        t=1;
                                    }
                                }
                            }
                        }
                        recommendedMealPlan.add(breakfastRecipes.get(randomMealIndex));
                        dailyRemainedCalories=caloriesBudget-db.getNutritionalDataDao()
                                .getNutritionalDataByRecipeId(breakfastRecipes.get(randomMealIndex).getId()).getCalories();
                        t=1;
                        while(t==1){
                            t=0;
                            randomMealIndex= new Random().nextInt(lunchRecipes.size());
                            for(Recipe recipe:recommendedMealPlan){
                                if(recipe.getId()==lunchRecipes.get(randomMealIndex).getId())
                                    t=1;
                            }
                            if(t==0)
                            {
                                currentRecipeIngredients.clear();
                                currentRecipeIngredients=db.getIngredientDao().getIngredientsByRecipeId(lunchRecipes.get(randomMealIndex).getId());
                                for(Ingredient ingredient:currentRecipeIngredients)
                                {   u=0;
                                    for(Aliment aliment:prefferedAliments){
                                        if(ingredient.getIdAliment()==aliment.getId()){
                                            u=1;
                                        }
                                    }
                                    if(u==0){
                                        t=1;
                                    }
                                }
                            }
                        }
                        recommendedMealPlan.add(lunchRecipes.get(randomMealIndex));
                        dailyRemainedCalories=caloriesBudget-db.getNutritionalDataDao()
                                .getNutritionalDataByRecipeId(lunchRecipes.get(randomMealIndex).getId()).getCalories();
                        dinnerRecipes.clear();
                        dinnerRecipes.addAll(db.getRecipeDao().getRecipesByTypeAndCalories("Dinner",dailyRemainedCalories));
                        t=1;
                        while(t==1){
                            t=0;
                            randomMealIndex= new Random().nextInt(dinnerRecipes.size());
                            for(Recipe recipe:recommendedMealPlan){
                                if(recipe.getId()==dinnerRecipes.get(randomMealIndex).getId())
                                    t=1;
                            }
                            if(t==0)
                            {
                                currentRecipeIngredients.clear();
                                currentRecipeIngredients=db.getIngredientDao().getIngredientsByRecipeId(dinnerRecipes.get(randomMealIndex).getId());
                                for(Ingredient ingredient:currentRecipeIngredients)
                                {   u=0;
                                    for(Aliment aliment:prefferedAliments){
                                        if(ingredient.getIdAliment()==aliment.getId()){
                                            u=1;
                                        }
                                    }
                                    if(u==0){
                                        t=1;
                                    }
                                }
                            }
                        }
                        recommendedMealPlan.add(dinnerRecipes.get(randomMealIndex));
                        dailyRemainedCalories=caloriesBudget;
                    }

                    if(recommendedMealPlan.size()==21)
                    {
                        Calendar c=Calendar.getInstance();
                        c.setTime(currentMealPlan.getStartDate());
                        Date currentDate=c.getTime();

                        i=0;
                        while(i<recommendedMealPlan.size()){
                            recommendedMealDao.insert(new RecommendedMeal(recommendedMealPlan.get(i).getId(),currentMealPlan.getId(),currentDate));
                            recommendedMealPlanToShow.add(new ListedRecommendedMeal(recommendedMealPlan.get(i),
                                    db.getNutritionalDataDao().getNutritionalDataByRecipeId(recommendedMealPlan.get(i).getId()).getCalories(),currentDate));
                            recommendedMealDao.insert(new RecommendedMeal(recommendedMealPlan.get(i+1).getId(),currentMealPlan.getId(),currentDate));
                            recommendedMealPlanToShow.add(new ListedRecommendedMeal(recommendedMealPlan.get(i+1),
                                    db.getNutritionalDataDao().getNutritionalDataByRecipeId(recommendedMealPlan.get(i).getId()).getCalories(),currentDate));
                            recommendedMealDao.insert(new RecommendedMeal(recommendedMealPlan.get(i+2).getId(),currentMealPlan.getId(),currentDate));
                            recommendedMealPlanToShow.add(new ListedRecommendedMeal(recommendedMealPlan.get(i+2),
                                    db.getNutritionalDataDao().getNutritionalDataByRecipeId(recommendedMealPlan.get(i).getId()).getCalories(),currentDate));
                            i=i+3;
                            c.add(Calendar.DATE,1);
                            currentDate=c.getTime();
                        }

                        notifyAdapter();

                    }
                }
            }
        };
    }

    private Callback<BiometricData> getBiometricDataByUserIdCallback() {
        return new Callback<BiometricData>() {
            @Override
            public void runResultOnUiThread(BiometricData biometricData) {
                if (biometricData != null) {
                    currentWeight=biometricData.getWeight();
                    sex=biometricData.getSex();
                    objectiveService.getObjectiveByUserId(userId, getObjectiveByUserIdCallback());
                }
            }
        };
    }

    private Callback<Objective> getObjectiveByUserIdCallback() {
        return new Callback<Objective>() {
            @Override
            public void runResultOnUiThread(Objective objective) {
                if (objective != null) {
                    if(sex.equals("Male"))
                        caloriesBudget=2800.0;
                    else
                        caloriesBudget=2400.0;
                    Double kgToLose=currentWeight-objective.getNewWeight();
                    Double weeksToLoseWeight=kgToLose/objective.getLostKgPerWeek();
                    Double daysToLoseWeight=weeksToLoseWeight*7;
                    Double caloriesDeficit=(kgToLose*7000.0)/daysToLoseWeight;
                    caloriesBudget=caloriesBudget-caloriesDeficit;
                }
                else{
                    if(sex.equals("Male"))
                        caloriesBudget=2800.0;
                    else
                        caloriesBudget=2400.0;

                }
            }
        };
    }

    private View.OnClickListener addPreferencesEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext().getApplicationContext(), AddFoodPreferencesActivity.class);
                intent.putExtra("user_id",userId);
                startActivity(intent);
            }
        };
    }
}
