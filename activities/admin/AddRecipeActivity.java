package licenta.beatyourmeal.activities.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import licenta.beatyourmeal.R;
import licenta.beatyourmeal.database.recipe.Recipe;

public class AddRecipeActivity extends AppCompatActivity {

    public static final String RECIPE_KEY = "recipeKey";
    private TextInputEditText tietName;
    private Spinner spnCategory;
    private TextInputEditText tietCookingTime;
    private Button btnSubmit;

    private Recipe recipe;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        initComponents();
        intent=getIntent();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_asset_logo_wide);
        getSupportActionBar().setTitle("");
    }

    private void initComponents() {
        tietName=findViewById(R.id.add_recipes_tiet_name);
        spnCategory=findViewById(R.id.add_recipes_spn_category);
        tietCookingTime =findViewById(R.id.add_recipes_tiet_cooking_time);
        btnSubmit =findViewById(R.id.add_recipes_btn_submit);
        addCategoryAdapter();
        btnSubmit.setOnClickListener(saveRecipeEventListener());
    }

    private void addCategoryAdapter() {
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.add_recipes_category_values,
                R.layout.support_simple_spinner_dropdown_item);
        spnCategory.setAdapter(adapter);
    }

    private View.OnClickListener saveRecipeEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid()){
                    createFromViews();
                    intent.putExtra(RECIPE_KEY, recipe);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        };
    }

    private boolean isValid() {
        if (tietName.getText() == null || tietName.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "Invalid name!",
                    Toast.LENGTH_SHORT)
                    .show();
            return false;
        }

        if (tietCookingTime.getText() == null || tietCookingTime.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "Invalid cooking time!",
                    Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        return true;
    }

    private void createFromViews() {
        String name = tietName.getText().toString();
        String category = spnCategory.getSelectedItem().toString();
        Double cookingTime = Double.parseDouble(tietCookingTime.getText().toString());
        recipe = new Recipe(name, category,cookingTime);
    }
}