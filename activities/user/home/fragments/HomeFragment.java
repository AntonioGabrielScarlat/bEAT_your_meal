package licenta.beatyourmeal.activities.user.home.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import licenta.beatyourmeal.R;
import licenta.beatyourmeal.async.Callback;
import licenta.beatyourmeal.database.biometricData.BiometricData;
import licenta.beatyourmeal.database.biometricData.BiometricDataService;
import licenta.beatyourmeal.database.eatenMeal.EatenMeal;
import licenta.beatyourmeal.database.eatenMeal.EatenMealService;
import licenta.beatyourmeal.database.nutritionalData.NutritionalData;
import licenta.beatyourmeal.database.nutritionalData.NutritionalDataService;
import licenta.beatyourmeal.database.objective.Objective;
import licenta.beatyourmeal.database.objective.ObjectiveService;
import licenta.beatyourmeal.database.recipe.RecipeService;
import licenta.beatyourmeal.auxiliary.converters.DateConverter;
import licenta.beatyourmeal.activities.user.AddEatenMealActivity;
import licenta.beatyourmeal.activities.user.AddObjectiveActivity;
import licenta.beatyourmeal.activities.user.SeeEatenMealsActivity;

public class HomeFragment extends Fragment {
    public static final String MEAL_KEY = "mealKey";

    private long userId;
    private TextView tvCaloriesBugdet;
    private ProgressBar pbConsumedCalories;
    private TextView tvConsumedCaloriesProgress;
    private ProgressBar pbConsumedCarbs;
    private ProgressBar pbConsumedProteins;
    private ProgressBar pbConsumedFat;
    private TextView tvLeftCalories;
    private TextView tvConsumedCarbs;
    private TextView tvLeftCarbs;
    private TextView tvConsumedProteins;
    private TextView tvLeftProteins;
    private TextView tvConsumedFat;
    private TextView tvLeftFat;
    private Button btnBreakfast;
    private Button btnLunch;
    private Button btnDinner;
    private TextView tvConsumedCaloriesBreakfast;
    private TextView tvConsumedCaloriesLunch;
    private TextView tvConsumedCaloriesDinner;
    private Button btnObjectives;
    private TextView tvObjectiveDescription;

    private Double currentWeight;
    private String sex;

    private Double caloriesBudget;
    private Double consumedCalories=0.0;
    private Double leftCalories=0.0;
    private Double allowedCarbs;
    private Double allowedProteins;
    private Double allowedFat;
    private Double consumedCarbs=0.0;
    private Double consumedProteins=0.0;
    private Double consumedFat=0.0;

    private List<NutritionalData> nutritionalDataToday=new ArrayList<>();
    private Double consumedCaloriesBreakfast=0.0;
    private Double consumedCaloriesLunch=0.0;
    private Double consumedCaloriesDinner=0.0;

    private String currentMeal;

    private List<EatenMeal> eatenMealsTodayBreakfast=new ArrayList<>();
    private List<EatenMeal> eatenMealsTodayLunches =new ArrayList<>();
    private List<EatenMeal> eatenMealsTodayDinner=new ArrayList<>();

    private ActivityResultLauncher<Intent> addEatenMealLauncher;
    private ActivityResultLauncher<Intent> addObjectiveLauncher;
    private ActivityResultLauncher<Intent> updateObjectiveLauncher;

    private ObjectiveService objectiveService;
    private BiometricDataService biometricDataService;
    private EatenMealService eatenMealService;
    private RecipeService recipeService;
    private NutritionalDataService nutritionalDataService;

    public HomeFragment(long userId) {
        this.userId = userId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addEatenMealLauncher= getAddEatenMealLauncher();
        addObjectiveLauncher = getAddObjectiveLauncher();
        updateObjectiveLauncher=getUpdateObjectiveLauncher();
        objectiveService=new ObjectiveService(getContext().getApplicationContext());
        biometricDataService=new BiometricDataService(getContext().getApplicationContext());
        eatenMealService =new EatenMealService(getContext().getApplicationContext());
        recipeService=new RecipeService(getContext().getApplicationContext());
        nutritionalDataService=new NutritionalDataService(getContext().getApplicationContext());
        biometricDataService.getBiometricDataByUserId(userId, getBiometricDataByUserIdCallback());
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_user_home,container,false);
        initComponents(view);
        return view;
    }

    private void initComponents(View view) {
        tvCaloriesBugdet=view.findViewById(R.id.fragment_user_home_tv_calories_buget);
        pbConsumedCalories=view.findViewById(R.id.fragment_user_home_pb_consumed_calories);
        tvConsumedCaloriesProgress=view.findViewById(R.id.fragment_user_home_tv_consumed_calories_progress);
        pbConsumedCarbs=view.findViewById(R.id.fragment_user_home_pb_carbs);
        pbConsumedProteins=view.findViewById(R.id.fragment_user_home_pb_proteins);
        pbConsumedFat=view.findViewById(R.id.fragment_user_home_pb_fat);
        tvLeftCalories =view.findViewById(R.id.fragment_user_home_tv_left_calories);
        tvConsumedCarbs=view.findViewById(R.id.fragment_user_home_tv_consumed_carbs);
        tvLeftCarbs=view.findViewById(R.id.fragment_user_home_tv_left_carbs);
        tvConsumedProteins=view.findViewById(R.id.fragment_user_home_tv_consumed_proteins);
        tvLeftProteins=view.findViewById(R.id.fragment_user_home_tv_left_proteins);
        tvConsumedFat=view.findViewById(R.id.fragment_user_home_tv_consumed_fat);
        tvLeftFat=view.findViewById(R.id.fragment_user_home_tv_left_fat);
        btnBreakfast=view.findViewById(R.id.fragment_user_home_btn_breakfast);
        btnLunch=view.findViewById(R.id.fragment_user_home_btn_lunch);
        btnDinner=view.findViewById(R.id.fragment_user_home_btn_dinner);
        tvConsumedCaloriesBreakfast=view.findViewById(R.id.fragment_user_home_tv_meals_kcal_breakfast);
        tvConsumedCaloriesLunch=view.findViewById(R.id.fragment_user_home_tv_meals_kcal_lunch);
        tvConsumedCaloriesDinner=view.findViewById(R.id.fragment_user_home_tv_meals_kcal_dinner);
        btnObjectives=view.findViewById(R.id.fragment_user_home_btn_objective);
        tvObjectiveDescription=view.findViewById(R.id.fragment_user_home_tv_objective_description);

        btnBreakfast.setOnClickListener(addEatenMealBreakfastEventListener());
        btnLunch.setOnClickListener(addEatenMealLunchEventListener());
        btnDinner.setOnClickListener(addEatenMealDinnerEventListener());
        btnObjectives.setOnClickListener(addObjectivesEventListener());
        pbConsumedCalories.setOnClickListener(seeEatenMealsEventListener());
    }

    private View.OnClickListener seeEatenMealsEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext().getApplicationContext(), SeeEatenMealsActivity.class);
                intent.putExtra("user_id",userId);
                startActivity(intent);
                getActivity().finish();
            }
        };
    }

    private ActivityResultLauncher<Intent> getAddObjectiveLauncher() {
        ActivityResultCallback<ActivityResult> callback = getAddObjectiveActivityResultCallback();
        return registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), callback);
    }

    private ActivityResultCallback<ActivityResult> getAddObjectiveActivityResultCallback() {
        return new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result != null && result.getResultCode() == -1 && result.getData() != null) {
                    Objective objective = (Objective) result.getData().getSerializableExtra(AddObjectiveActivity.OBJECTIVE_KEY);
                    //inserare in baza de date
                    objectiveService.insert(objective, getInsertObjectiveCallback());
                }
            }
        };
    }

    private Callback<Objective> getInsertObjectiveCallback() {
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
                    pbConsumedCalories.setMax(caloriesBudget.intValue());
                    pbConsumedCalories.setProgress(0);
                    tvConsumedCaloriesProgress.setText("0");
                    pbConsumedCalories.setVisibility(View.VISIBLE);
                    String message="The current objective is to lose "+
                            (currentWeight-objective.getNewWeight())+" kg until "+ DateConverter.fromDate(objective.getTargetDate());
                    tvObjectiveDescription.setText(message);

                    tvCaloriesBugdet.setText(caloriesBudget.toString()+" kcal");
                    tvLeftCalories.setText(caloriesBudget.toString()+" kcal Left");
                    allowedCarbs =(caloriesBudget*0.55)/4;
                    allowedProteins =(caloriesBudget*0.25)/4;
                    allowedFat =(caloriesBudget*0.2)/9;
                    pbConsumedCarbs.setMax(allowedCarbs.intValue());
                    pbConsumedCarbs.setVisibility(View.VISIBLE);
                    pbConsumedProteins.setMax(allowedProteins.intValue());
                    pbConsumedProteins.setVisibility(View.VISIBLE);
                    pbConsumedFat.setMax(allowedFat.intValue());
                    pbConsumedFat.setVisibility(View.VISIBLE);

                    eatenMealService.getEatenMealsByConsumptionDateAndUserId(userId,new Date(), getMealByConsumptionDateAndUserIdCallback());
                }
            }
        };
    }

    private ActivityResultLauncher<Intent> getUpdateObjectiveLauncher() {
        ActivityResultCallback<ActivityResult> callback = getUpdateObjectiveActivityResultCallback();
        return registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), callback);
    }

    private ActivityResultCallback<ActivityResult> getUpdateObjectiveActivityResultCallback() {
        return new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result != null && result.getResultCode() == -1 && result.getData() != null) {
                    Objective objective = (Objective) result.getData().getSerializableExtra(AddObjectiveActivity.OBJECTIVE_KEY);
                    //inserare in baza de date
                    objectiveService.update(objective, getUpdateObjectiveCallback());
                }
            }
        };
    }

    private Callback<Objective> getUpdateObjectiveCallback() {
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
                    pbConsumedCalories.setMax(caloriesBudget.intValue());
                    pbConsumedCalories.setProgress(0);
                    tvConsumedCaloriesProgress.setText("0");
                    pbConsumedCalories.setVisibility(View.VISIBLE);
                    String message="The current objective is to lose "+
                            (currentWeight-objective.getNewWeight())+" kg until "+ DateConverter.fromDate(objective.getTargetDate());
                    tvObjectiveDescription.setText(message);

                    tvCaloriesBugdet.setText(caloriesBudget.toString()+" kcal");
                    tvLeftCalories.setText(caloriesBudget.toString()+" kcal Left");
                    allowedCarbs =(caloriesBudget*0.55)/4;
                    allowedProteins =(caloriesBudget*0.25)/4;
                    allowedFat =(caloriesBudget*0.2)/9;
                    pbConsumedCarbs.setMax(allowedCarbs.intValue());
                    pbConsumedCarbs.setVisibility(View.VISIBLE);
                    pbConsumedProteins.setMax(allowedProteins.intValue());
                    pbConsumedProteins.setVisibility(View.VISIBLE);
                    pbConsumedFat.setMax(allowedFat.intValue());
                    pbConsumedFat.setVisibility(View.VISIBLE);


                    eatenMealService.getEatenMealsByConsumptionDateAndUserId(userId,new Date(), getMealByConsumptionDateAndUserIdCallback());
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
                    String message="The current objective is to lose "+
                            (currentWeight-objective.getNewWeight())+" kg until "+ DateConverter.fromDate(objective.getTargetDate());
                    tvObjectiveDescription.setText(message);
                }
                else{
                    if(sex.equals("Male"))
                        caloriesBudget=2800.0;
                    else
                        caloriesBudget=2400.0;
                }
                tvCaloriesBugdet.setText(caloriesBudget.toString()+" kcal");
                tvLeftCalories.setText(caloriesBudget.toString()+" kcal Left");
                allowedCarbs =(caloriesBudget*0.55)/4;
                allowedProteins =(caloriesBudget*0.25)/4;
                allowedFat =(caloriesBudget*0.2)/9;
                pbConsumedCarbs.setMax(allowedCarbs.intValue());
                pbConsumedCarbs.setVisibility(View.VISIBLE);
                pbConsumedProteins.setMax(allowedProteins.intValue());
                pbConsumedProteins.setVisibility(View.VISIBLE);
                pbConsumedFat.setMax(allowedFat.intValue());
                pbConsumedFat.setVisibility(View.VISIBLE);
                pbConsumedCalories.setMax(caloriesBudget.intValue());
                pbConsumedCalories.setProgress(0);
                tvConsumedCaloriesProgress.setText("0");
                pbConsumedCalories.setVisibility(View.VISIBLE);

                eatenMealService.getEatenMealsByConsumptionDateAndUserId(userId,new Date(), getMealByConsumptionDateAndUserIdCallback());

            }
        };
    }

    private Callback<Objective> getObjectiveByUserIdCallbackForUpdate() {
        return new Callback<Objective>() {
            @Override
            public void runResultOnUiThread(Objective objective) {
                Intent intent=new Intent(getContext().getApplicationContext(), AddObjectiveActivity.class);
                intent.putExtra("user_id",userId);
                if (objective != null) {
                    updateObjectiveLauncher.launch(intent);
                }
                else
                {
                    addObjectiveLauncher.launch(intent);
                }
            }
        };
    }

    private View.OnClickListener addObjectivesEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objectiveService.getObjectiveByUserId(userId,getObjectiveByUserIdCallbackForUpdate());
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

    private ActivityResultLauncher<Intent> getAddEatenMealLauncher() {
        ActivityResultCallback<ActivityResult> callback = getAddEatenMealActivityResultCallback();
        return registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), callback);
    }

    private ActivityResultCallback<ActivityResult> getAddEatenMealActivityResultCallback() {
        return new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result != null && result.getResultCode() == -1 && result.getData() != null) {
                    EatenMeal eatenMeal = (EatenMeal) result.getData().getSerializableExtra(AddEatenMealActivity.MEAL_KEY);
                    eatenMealService.insert(eatenMeal,getInsertMealCallback());
                }
            }
        };
    }

    private Callback<List<EatenMeal>> getMealByConsumptionDateAndUserIdCallback() {
        return new Callback<List<EatenMeal>>() {
            @Override
            public void runResultOnUiThread(List<EatenMeal> results) {
                if (results != null && results.size()>0) {
                    consumedCalories=0.0;
                    consumedCarbs=0.0;
                    consumedProteins=0.0;
                    consumedFat=0.0;
                    consumedCaloriesBreakfast=0.0;
                    consumedCaloriesLunch=0.0;
                    consumedCaloriesDinner=0.0;
                    eatenMealsTodayBreakfast.clear();
                    eatenMealsTodayLunches.clear();
                    eatenMealsTodayDinner.clear();
                    for(EatenMeal eatenMeal :results){
                        if(eatenMeal.getMealOfTheDay().equals("Breakfast")){
                            eatenMealsTodayBreakfast.add(eatenMeal);
                        }
                        else
                        {
                            if(eatenMeal.getMealOfTheDay().equals("Lunch")){
                                eatenMealsTodayLunches.add(eatenMeal);
                            }
                            else
                            {
                                eatenMealsTodayDinner.add(eatenMeal);
                            }
                        }
                    }

                   for(EatenMeal eatenMeal :eatenMealsTodayBreakfast){
                        nutritionalDataService.getNutritionalDataByRecipeId(eatenMeal.getIdRecipe(), getNutritionalDataBreakfastByRecipeIdCallback());
                    }
                    for(EatenMeal eatenMeal : eatenMealsTodayLunches){
                        nutritionalDataService.getNutritionalDataByRecipeId(eatenMeal.getIdRecipe(), getNutritionalDataLunchByRecipeIdCallback());
                    }
                    for(EatenMeal eatenMeal :eatenMealsTodayDinner){
                        nutritionalDataService.getNutritionalDataByRecipeId(eatenMeal.getIdRecipe(), getNutritionalDataDinnerByRecipeIdCallback());
                   }
                }
                else
                {
                    pbConsumedCalories.setProgress(0);
                    tvConsumedCaloriesProgress.setText("0 kcal");
                    pbConsumedCarbs.setProgress(0);
                    pbConsumedProteins.setProgress(0);
                    pbConsumedFat.setProgress(0);
                    tvConsumedCaloriesBreakfast.setText("0 kcal");
                    tvConsumedCaloriesLunch.setText("0 kcal");
                    tvConsumedCaloriesDinner.setText("0 kcal");
                    DecimalFormat decimalFormat=new DecimalFormat("###.#");
                    tvLeftCalories.setText(decimalFormat.format(caloriesBudget)+"kcal Left");
                    tvConsumedCarbs.setText("0g");
                    tvLeftCarbs.setText(decimalFormat.format(allowedCarbs)+"g Left");
                    tvConsumedProteins.setText("0g");
                    tvLeftProteins.setText(decimalFormat.format(allowedProteins)+"g Left");
                    tvConsumedFat.setText("0g");
                    tvLeftFat.setText(decimalFormat.format(allowedFat)+"g Left");
                }
            }
        };
    }

    private Callback<EatenMeal> getInsertMealCallback() {
        return new Callback<EatenMeal>() {
            @Override
            public void runResultOnUiThread(EatenMeal result) {
                if (result != null) {
                    eatenMealService.getEatenMealsByConsumptionDateAndUserId(userId,new Date(), getMealByConsumptionDateAndUserIdCallback());
                }
            }
        };
    }

    private Callback<NutritionalData> getNutritionalDataBreakfastByRecipeIdCallback() {
        return new Callback<NutritionalData>() {
            @Override
            public void runResultOnUiThread(NutritionalData result) {
                if (result != null) {
                    consumedCalories=consumedCalories+result.getCalories();
                    pbConsumedCalories.setProgress(consumedCalories.intValue());
                    tvConsumedCaloriesProgress.setText(consumedCalories.toString());
                    consumedCaloriesBreakfast=consumedCaloriesBreakfast+result.getCalories();
                    consumedCarbs=consumedCarbs+ result.getCarbs();
                    pbConsumedCarbs.setProgress(consumedCarbs.intValue());
                    consumedProteins=consumedProteins+result.getProteins();
                    pbConsumedProteins.setProgress(consumedProteins.intValue());
                    consumedFat=consumedFat+ result.getFat();
                    pbConsumedFat.setProgress(consumedFat.intValue());
                    DecimalFormat decimalFormat=new DecimalFormat("###.#");
                    tvConsumedCaloriesBreakfast.setText(decimalFormat.format(consumedCaloriesBreakfast)+"kcal");
                    leftCalories=caloriesBudget-consumedCalories;
                    tvLeftCalories.setText(decimalFormat.format(leftCalories)+"kcal Left");
                    tvConsumedCarbs.setText(decimalFormat.format(consumedCarbs)+"g");
                    tvLeftCarbs.setText(decimalFormat.format(allowedCarbs-consumedCarbs)+"g Left");
                    tvConsumedProteins.setText(decimalFormat.format(consumedProteins)+"g");
                    tvLeftProteins.setText(decimalFormat.format(allowedProteins-consumedProteins)+"g Left");
                    tvConsumedFat.setText(decimalFormat.format(consumedFat)+"g");
                    tvLeftFat.setText(decimalFormat.format(allowedFat-consumedFat)+"g Left");
                }
            }
        };
    }

    private Callback<NutritionalData> getNutritionalDataLunchByRecipeIdCallback() {
        return new Callback<NutritionalData>() {
            @Override
            public void runResultOnUiThread(NutritionalData result) {
                if (result != null) {
                    consumedCalories=consumedCalories+result.getCalories();
                    pbConsumedCalories.setProgress(consumedCalories.intValue());
                    tvConsumedCaloriesProgress.setText(consumedCalories.toString());
                    consumedCaloriesLunch=consumedCaloriesLunch+result.getCalories();
                    consumedCarbs=consumedCarbs+ result.getCarbs();
                    pbConsumedCarbs.setProgress(consumedCarbs.intValue());
                    consumedProteins=consumedProteins+result.getProteins();
                    pbConsumedProteins.setProgress(consumedProteins.intValue());
                    consumedFat=consumedFat+ result.getFat();
                    pbConsumedFat.setProgress(consumedFat.intValue());
                    DecimalFormat decimalFormat=new DecimalFormat("###.#");
                    tvConsumedCaloriesLunch.setText(decimalFormat.format(consumedCaloriesLunch)+"kcal");
                    leftCalories=caloriesBudget-consumedCalories;
                    tvLeftCalories.setText(decimalFormat.format(leftCalories)+"kcal Left");
                    tvConsumedCarbs.setText(decimalFormat.format(consumedCarbs)+"g");
                    tvLeftCarbs.setText(decimalFormat.format(allowedCarbs-consumedCarbs)+"g Left");
                    tvConsumedProteins.setText(decimalFormat.format(consumedProteins)+"g");
                    tvLeftProteins.setText(decimalFormat.format(allowedProteins-consumedProteins)+"g Left");
                    tvConsumedFat.setText(decimalFormat.format(consumedFat)+"g");
                    tvLeftFat.setText(decimalFormat.format(allowedFat-consumedFat)+"g Left");
                }
            }
        };
    }

    private Callback<NutritionalData> getNutritionalDataDinnerByRecipeIdCallback() {
        return new Callback<NutritionalData>() {
            @Override
            public void runResultOnUiThread(NutritionalData result) {
                if (result != null) {
                    consumedCalories=consumedCalories+result.getCalories();
                    pbConsumedCalories.setProgress(consumedCalories.intValue());
                    tvConsumedCaloriesProgress.setText(consumedCalories.toString());
                    consumedCaloriesDinner=consumedCaloriesDinner+result.getCalories();
                    consumedCarbs=consumedCarbs+ result.getCarbs();
                    pbConsumedCarbs.setProgress(consumedCarbs.intValue());
                    consumedProteins=consumedProteins+result.getProteins();
                    pbConsumedProteins.setProgress(consumedProteins.intValue());
                    consumedFat=consumedFat+ result.getFat();
                    pbConsumedFat.setProgress(consumedFat.intValue());
                    DecimalFormat decimalFormat=new DecimalFormat("###.#");
                    tvConsumedCaloriesDinner.setText(decimalFormat.format(consumedCaloriesDinner)+"kcal");
                    leftCalories=caloriesBudget-consumedCalories;
                    tvLeftCalories.setText(decimalFormat.format(leftCalories)+"kcal Left");
                    tvConsumedCarbs.setText(decimalFormat.format(consumedCarbs)+"g");
                    tvLeftCarbs.setText(decimalFormat.format(allowedCarbs-consumedCarbs)+"g Left");
                    tvConsumedProteins.setText(decimalFormat.format(consumedProteins)+"g");
                    tvLeftProteins.setText(decimalFormat.format(allowedProteins-consumedProteins)+"g Left");
                    tvConsumedFat.setText(decimalFormat.format(consumedFat)+"g");
                    tvLeftFat.setText(decimalFormat.format(allowedFat-consumedFat)+"g Left");
                }
            }
        };
    }

    private View.OnClickListener addEatenMealBreakfastEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext().getApplicationContext(), AddEatenMealActivity.class);
                intent.putExtra("user_id",userId);
                intent.putExtra("meal_name","Breakfast");
                addEatenMealLauncher.launch(intent);
            }
        };
    }

    private View.OnClickListener addEatenMealLunchEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext().getApplicationContext(), AddEatenMealActivity.class);
                intent.putExtra("user_id",userId);
                intent.putExtra("meal_name","Lunch");
                addEatenMealLauncher.launch(intent);
            }
        };
    }

    private View.OnClickListener addEatenMealDinnerEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext().getApplicationContext(), AddEatenMealActivity.class);
                intent.putExtra("user_id",userId);
                intent.putExtra("meal_name","Dinner");
                addEatenMealLauncher.launch(intent);
            }
        };
    }




}
