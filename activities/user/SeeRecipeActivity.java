package licenta.beatyourmeal.activities.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import licenta.beatyourmeal.R;
import licenta.beatyourmeal.auxiliary.adapters.CookingStepAdapter;
import licenta.beatyourmeal.auxiliary.adapters.IngredientAdapter;
import licenta.beatyourmeal.async.Callback;
import licenta.beatyourmeal.auxiliary.listed.ListedIngredient;
import licenta.beatyourmeal.database.aliment.Aliment;
import licenta.beatyourmeal.database.aliment.AlimentService;
import licenta.beatyourmeal.database.cookingStep.CookingStep;
import licenta.beatyourmeal.database.cookingStep.CookingStepService;
import licenta.beatyourmeal.database.ingredient.Ingredient;
import licenta.beatyourmeal.database.ingredient.IngredientService;
import licenta.beatyourmeal.database.nutritionalData.NutritionalData;
import licenta.beatyourmeal.database.nutritionalData.NutritionalDataService;
import licenta.beatyourmeal.database.recipe.Recipe;
import licenta.beatyourmeal.database.recipe.RecipeService;

public class SeeRecipeActivity extends AppCompatActivity {
    private TextView tvRecipeName;
    private TextView tvMealType;
    private TextView tvCookingTime;
    private ListView lvIngredients;
    private ListView lvCookingSteps;
    private TextView tvCalories;
    private TextView tvCarbs;
    private TextView tvProteins;
    private TextView tvFat;
    private Button btnClose;

    private List<CookingStep> cookingSteps=new ArrayList<>();
    private List<ListedIngredient> listedIngredients=new ArrayList<>();
    private List<Double> quantities=new ArrayList<>();
    private List<String> unitsOfMeasurement=new ArrayList<>();
    private List<Aliment> aliments=new ArrayList<>();
    private int numberOfIngredients;

    private Intent intent;
    private String recipeName;
    private RecipeService recipeService;
    private IngredientService ingredientService;
    private CookingStepService cookingStepService;
    private NutritionalDataService nutritionalDataService;
    private AlimentService alimentService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_recipe);
        initComponents();
        intent=getIntent();
        recipeName=intent.getStringExtra("recipe_name");
        tvRecipeName.setText(recipeName);
        recipeService=new RecipeService(getApplicationContext());
        ingredientService=new IngredientService(getApplicationContext());
        cookingStepService=new CookingStepService(getApplicationContext());
        nutritionalDataService=new NutritionalDataService(getApplicationContext());
        alimentService=new AlimentService(getApplicationContext());
        recipeService.getRecipeByName(recipeName,getRecipeByNameCallback());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_asset_logo_wide);
        getSupportActionBar().setTitle("");
    }

    private Callback<Recipe> getRecipeByNameCallback() {
        return new Callback<Recipe>() {
            @Override
            public void runResultOnUiThread(Recipe result) {
                if (result != null) {
                   tvMealType.setText(result.getCategory());
                   tvCookingTime.setText(result.getCookingTime().toString());
                   nutritionalDataService.getNutritionalDataByRecipeId(result.getId(),getNutritionalDataByRecipeIdCallbak());
                }
            }
        };
    }

    private Callback<NutritionalData> getNutritionalDataByRecipeIdCallbak() {
        return new Callback<NutritionalData>() {
            @Override
            public void runResultOnUiThread(NutritionalData result) {
                if (result != null) {
                    tvCalories.setText(result.getCalories().toString());
                    tvCarbs.setText(result.getCarbs().toString());
                    tvProteins.setText(result.getProteins().toString());
                    tvFat.setText(result.getFat().toString());
                    cookingStepService.getCookingStepsByRecipeId(result.getIdRecipe(),getCookingStepsByRecipeIdCallback());
                }
            }
        };
    }

    private Callback<List<CookingStep>> getCookingStepsByRecipeIdCallback() {
        return new Callback<List<CookingStep>>() {
            @Override
            public void runResultOnUiThread(List<CookingStep> results) {
                if (results != null) {
                   cookingSteps.addAll(results);
                   notifyAdapterCookingSteps();
                   ingredientService.getIngredientsByRecipeId(results.get(0).getIdRecipe(), getIngredientsByRecipeIdCallback());
                }
            }
        };
    }


    private Callback<List<Ingredient>> getIngredientsByRecipeIdCallback() {
        return new Callback<List<Ingredient>>() {
            @Override
            public void runResultOnUiThread(List<Ingredient> results) {
                if (results != null) {
                    numberOfIngredients=results.size();
                    for(Ingredient ingredient:results){
                        quantities.add(ingredient.getQuantity());
                        unitsOfMeasurement.add(ingredient.getUnitOfMeasurement());
                        alimentService.getById(ingredient.getIdAliment(),getAlimentByIdCallback());
                    }

                }
            }
        };

    }

    private Callback<Aliment> getAlimentByIdCallback() {
        return new Callback<Aliment>() {
            @Override
            public void runResultOnUiThread(Aliment result) {
                if (result != null) {
                    aliments.add(result);
                    if(aliments.size()==numberOfIngredients){
                        for(int i=0;i<numberOfIngredients;i++){
                            listedIngredients.add(new ListedIngredient(aliments.get(i).getName(),quantities.get(i),unitsOfMeasurement.get(i)));
                        }
                        notifyAdapterListedIngredients();
                    }
                }
            }
        };
    }

    private void initComponents() {
        tvRecipeName=findViewById(R.id.see_recipe_tv_title);
        tvMealType=findViewById(R.id.see_recipe_tv_meal_type);
        tvCookingTime=findViewById(R.id.see_recipe_tv_cooking_time);
        lvIngredients=findViewById(R.id.see_recipe_lv_ingredients);
        lvCookingSteps=findViewById(R.id.see_recipe_lv_cooking_steps);
        tvCalories=findViewById(R.id.see_recipe_tv_calories);
        tvCarbs=findViewById(R.id.see_recipe_tv_carbs);
        tvProteins=findViewById(R.id.see_recipe_tv_proteins);
        tvFat=findViewById(R.id.see_recipe_tv_fat);
        btnClose=findViewById(R.id.see_recipe_btn_close_recipe);
        addAdapterCookingSteps();
        addAdapterListedIngredients();
        btnClose.setOnClickListener(closeRecipeEventListener());
    }

    private View.OnClickListener closeRecipeEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
    }

    private void addAdapterCookingSteps() {
        CookingStepAdapter cookingStepAdapter = new CookingStepAdapter(getApplicationContext(), R.layout.lv_cooking_step_row,
                cookingSteps, getLayoutInflater());
        lvCookingSteps.setAdapter(cookingStepAdapter);
    }

    public void notifyAdapterCookingSteps() {
        ArrayAdapter<CookingStep> cookingStepAdapter = (ArrayAdapter<CookingStep>) lvCookingSteps.getAdapter();
        cookingStepAdapter.notifyDataSetChanged();
    }

    private void addAdapterListedIngredients() {
        IngredientAdapter listedIngredientAdapter = new IngredientAdapter(getApplicationContext(), R.layout.lv_ingredient_row,
                listedIngredients, getLayoutInflater());
        lvIngredients.setAdapter(listedIngredientAdapter);
    }

    public void notifyAdapterListedIngredients() {
        ArrayAdapter<ListedIngredient> listedIngredientAdapter = (ArrayAdapter<ListedIngredient>) lvIngredients.getAdapter();
        listedIngredientAdapter.notifyDataSetChanged();
    }
}