package licenta.beatyourmeal.activities.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import licenta.beatyourmeal.R;
import licenta.beatyourmeal.async.Callback;
import licenta.beatyourmeal.database.eatenMeal.EatenMeal;
import licenta.beatyourmeal.database.recipe.Recipe;
import licenta.beatyourmeal.database.recipe.RecipeService;

public class AddEatenMealActivity extends AppCompatActivity {
    public static final String MEAL_KEY = "mealKey";
    private String[] recipesNames;

    private AutoCompleteTextView actvName;
    private Button btnSubmit;

    private List<Recipe> recipes=new ArrayList<>();
    private Intent intent;

    private String mealName;
    private long recipeId;
    private long userId;
    private EatenMeal eatenMeal;
    private RecipeService recipeService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_eaten_meal);
        initComponents();
        intent=getIntent();
        userId=intent.getLongExtra("user_id",userId);
        mealName=intent.getStringExtra("meal_name");
        recipeService=new RecipeService(getApplicationContext());
        recipeService.getAll(getAllRecipesCallback());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_asset_logo_wide);
        getSupportActionBar().setTitle("");
    }



    private void initComponents() {
        actvName =findViewById(R.id.add_eaten_meal_actv_meal_name);
        btnSubmit =findViewById(R.id.add_eaten_meal_btn_submit);
        btnSubmit.setOnClickListener(saveEatenMealEventListener());
    }

    private View.OnClickListener saveEatenMealEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid()){
                    createFromViews();
                }
            }
        };
    }

    private Callback<List<Recipe>> getAllRecipesCallback() {
        return new Callback<List<Recipe>>() {
            @Override
            public void runResultOnUiThread(List<Recipe> results) {
                if (results != null) {
                    recipes.clear();
                    recipes.addAll(results);
                    List<String> listRecipeNames=new ArrayList<String>();
                    for(Recipe recipe:recipes){
                        listRecipeNames.add(recipe.getName());
                    }
                    recipesNames =new String[listRecipeNames.size()];
                    recipesNames =listRecipeNames.toArray(recipesNames);
                    ArrayAdapter<String> adapter=new ArrayAdapter<String>(
                            getApplicationContext(),android.R.layout.simple_list_item_1,recipesNames);
                    actvName.setAdapter(adapter);

                }
            }
        };
    }

    private boolean isValid() {
            if (actvName.getText() == null || actvName.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(),
                        "Invalid meal!",
                        Toast.LENGTH_SHORT)
                        .show();
                return false;
            }

            for(Recipe recipe:recipes){
                if(actvName.getText().toString().equals(recipe.getName()))
                    return true;
            }

            Toast.makeText(getApplicationContext(),"Recipe not found!",Toast.LENGTH_SHORT).show();
            return false;
        }


    private void createFromViews() {
        recipeService.getRecipeByName(actvName.getText().toString(),getRecipeByNameCallback());
    }

    private Callback<Recipe> getRecipeByNameCallback() {
        return new Callback<Recipe>() {
            @Override
            public void runResultOnUiThread(Recipe result) {
                if(result!=null)
                {
                    recipeId=result.getId();
                    eatenMeal =new EatenMeal(new Date(),mealName,recipeId,userId);
                    intent.putExtra(MEAL_KEY, eatenMeal);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        };
    }
}