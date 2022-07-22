package licenta.beatyourmeal.activities.user;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import licenta.beatyourmeal.R;
import licenta.beatyourmeal.activities.user.home.UserHomeActivity;
import licenta.beatyourmeal.auxiliary.adapters.EatenMealAdapter;
import licenta.beatyourmeal.async.Callback;
import licenta.beatyourmeal.auxiliary.listed.ListedEatenMeal;
import licenta.beatyourmeal.database.eatenMeal.EatenMeal;
import licenta.beatyourmeal.database.eatenMeal.EatenMealService;
import licenta.beatyourmeal.database.nutritionalData.NutritionalData;
import licenta.beatyourmeal.database.nutritionalData.NutritionalDataService;
import licenta.beatyourmeal.database.recipe.Recipe;
import licenta.beatyourmeal.database.recipe.RecipeService;

public class SeeEatenMealsActivity extends AppCompatActivity {

    private ListView lvEatenMeals;
    private Button btnClose;
    private AlertDialog.Builder builder;

    private List<ListedEatenMeal> listedEatenMeals =new ArrayList<>();
    private List<EatenMeal> eatenMeals =new ArrayList<>();
    private List<Recipe> recipes=new ArrayList<>();
    private List<NutritionalData> nutritionalData=new ArrayList<>();

    private Intent intent;
    private long userId;
    private EatenMealService eatenMealService;
    private RecipeService recipeService;
    private NutritionalDataService nutritionalDataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_eaten_meals);
        initComponents();
        builder = new AlertDialog.Builder(this);
        intent=getIntent();
        userId=intent.getLongExtra("user_id",userId);
        eatenMealService =new EatenMealService(getApplicationContext());
        recipeService=new RecipeService(getApplicationContext());
        nutritionalDataService=new NutritionalDataService(getApplicationContext());
        eatenMealService.getEatenMealsByConsumptionDateAndUserId(userId,new Date(),getMealByConsumptionDateAndUserIdCallback());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_asset_logo_wide);
        getSupportActionBar().setTitle("");
    }

    private Callback<List<licenta.beatyourmeal.database.eatenMeal.EatenMeal>> getMealByConsumptionDateAndUserIdCallback() {
        return new Callback<List<EatenMeal>>() {
            @Override
            public void runResultOnUiThread(List<licenta.beatyourmeal.database.eatenMeal.EatenMeal> results) {
                if (results != null && results.size()>0) {
                    for(EatenMeal eatenMeal:results){
                        if(eatenMeal.getMealOfTheDay().equals("Breakfast")){
                            eatenMeals.add(eatenMeal);
                        }
                    }
                    for(EatenMeal eatenMeal:results){
                        if(eatenMeal.getMealOfTheDay().equals("Lunch")){
                            eatenMeals.add(eatenMeal);
                        }
                    }
                    for(EatenMeal eatenMeal:results){
                        if(eatenMeal.getMealOfTheDay().equals("Dinner")){
                            eatenMeals.add(eatenMeal);
                        }
                    }

                    for(EatenMeal eatenMeal : eatenMeals)
                    {
                        recipeService.getRecipeById(eatenMeal.getIdRecipe(),getRecipeByIdCallback());
                    }

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"No eaten meals today!",Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private Callback<Recipe> getRecipeByIdCallback() {
        return new Callback<Recipe>() {
            @Override
            public void runResultOnUiThread(Recipe result) {
                if (result != null) {
                    recipes.add(result);
                    nutritionalDataService.getNutritionalDataByRecipeId(result.getId(), getNutritionalDataByRecipeIdCallback());
                }
            }
        };
    }

    private Callback<NutritionalData> getNutritionalDataByRecipeIdCallback() {
        return new Callback<NutritionalData>() {
            @Override
            public void runResultOnUiThread(NutritionalData result) {
                if (result != null) {
                    nutritionalData.add(result);
                    if(nutritionalData.size()== eatenMeals.size()){
                        for(EatenMeal eatenMeal : eatenMeals){
                            listedEatenMeals.add(new ListedEatenMeal(recipes.get(eatenMeals.indexOf(eatenMeal)).getName(),
                                    nutritionalData.get(eatenMeals.indexOf(eatenMeal)).getCalories(), eatenMeal.getMealOfTheDay()));

                        }
                        notifyAdapter();
                    }
                }
            }
        };
    }

    private void initComponents() {
        lvEatenMeals=findViewById(R.id.see_eaten_meals_lv_eaten_meals);
        addAdapter();
        btnClose=findViewById(R.id.see_eaten_meals_btn_close);
        btnClose.setOnClickListener(closeEatenMealsPageEventListener());
        lvEatenMeals.setOnItemClickListener(selectEatenMealEventListener());
        lvEatenMeals.setOnItemLongClickListener(deleteEatenMealEventListener());
    }

    private AdapterView.OnItemLongClickListener deleteEatenMealEventListener() {
        return new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                builder.setMessage("Are you sure you want to delete this eaten meal?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            eatenMealService.getEatenMealsByRecipeNameAndMealOfTheDay(listedEatenMeals.get(position).getName(),listedEatenMeals.get(position).getMealOfTheDay(),getEatenMealsByRecipeNameAndMealOfTheDayCallback(position));

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.setTitle("Delete recipe");
                alert.show();
                return true;
            }

        };
    }

    private Callback<List<EatenMeal>> getEatenMealsByRecipeNameAndMealOfTheDayCallback(int position) {
        return new Callback<List<EatenMeal>>() {
            @Override
            public void runResultOnUiThread(List<EatenMeal> results) {
                if (results != null && results.size()>0) {
                    Date date=new Date();
                    for(EatenMeal eatenMeal:results){
                        if(eatenMeal.getIdUser()==userId && eatenMeal.getDateOfConsumption().getDay()==date.getDay()){
                            listedEatenMeals.remove(position);
                            notifyAdapter();
                            for(Recipe recipe:recipes){
                                if(recipe.getId()==eatenMeal.getIdRecipe()){
                                    recipes.remove(recipe);
                                }
                            }
                            eatenMeals.remove(eatenMeal);
                            eatenMealService.delete(eatenMeal,deleteEatenMealCallback());

                        }
                    }

                }
            }
        };
    }

    private Callback<Boolean> deleteEatenMealCallback() {
        return new Callback<Boolean>() {
            @Override
            public void runResultOnUiThread(Boolean result) {
                if (result) {
                    Toast.makeText(getApplicationContext(),"Eaten Meal successfully deleted!",
                            Toast.LENGTH_SHORT).show();

                }
            }
        };
    }

    private AdapterView.OnItemClickListener selectEatenMealEventListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             Intent intent = new Intent(getApplicationContext(), SeeRecipeActivity.class);
             intent.putExtra("recipe_name", listedEatenMeals.get(position).getName());
              startActivity(intent);
            }
        };
    }

    private View.OnClickListener closeEatenMealsPageEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserHomeActivity.class);
                intent.putExtra("user_id",userId);
                startActivity(intent);
                finish();
            }
        };
    }

    private void addAdapter() {
        EatenMealAdapter adapter = new EatenMealAdapter(getApplicationContext(), R.layout.lv_eaten_meal_row,
                listedEatenMeals, getLayoutInflater());
        lvEatenMeals.setAdapter(adapter);
    }

    public void notifyAdapter() {
        ArrayAdapter<ListedEatenMeal> adapter = (ArrayAdapter<ListedEatenMeal>) lvEatenMeals.getAdapter();
        adapter.notifyDataSetChanged();
    }
}