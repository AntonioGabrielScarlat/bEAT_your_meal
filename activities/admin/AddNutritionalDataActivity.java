package licenta.beatyourmeal.activities.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import licenta.beatyourmeal.R;
import licenta.beatyourmeal.async.Callback;
import licenta.beatyourmeal.database.nutritionalData.NutritionalData;
import licenta.beatyourmeal.database.nutritionalData.NutritionalDataService;

public class AddNutritionalDataActivity extends AppCompatActivity {
    private TextInputEditText tietCalories;
    private TextInputEditText tietCarbs;
    private TextInputEditText tietProteins;
    private TextInputEditText tietFat;
    private Button btnSubmit;

    private NutritionalData nutritionalData;
    private Intent intent;
    private long recipeId;

    private NutritionalDataService nutritionalDataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_nutritional_data);
        initComponents();
        intent = getIntent();
        recipeId = intent.getLongExtra("recipe_id", recipeId);
        nutritionalDataService = new NutritionalDataService(getApplicationContext());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_asset_logo_wide);
        getSupportActionBar().setTitle("");
    }

    private void initComponents() {
        tietCalories = findViewById(R.id.add_nutritional_data_tiet_calories);
        tietCarbs = findViewById(R.id.add_nutritional_data_tiet_carbs);
        tietProteins = findViewById(R.id.add_nutritional_data_tiet_proteins);
        tietFat = findViewById(R.id.add_nutritional_data_tiet_fat);
        btnSubmit = findViewById(R.id.add_nutritional_data_btn_submit);
        btnSubmit.setOnClickListener(saveNutritionalDataEventListener());
    }

    private View.OnClickListener saveNutritionalDataEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid()){
                    createFromViews();
                    nutritionalDataService.insert(nutritionalData,getAddNutritionalDataCallback());
                }

            }
        };
    }

    private Callback<NutritionalData> getAddNutritionalDataCallback() {
        return new Callback<NutritionalData>() {
            @Override
            public void runResultOnUiThread(NutritionalData nutritionalData) {
                if (nutritionalData != null) {
                    Toast.makeText(getApplicationContext(),"Nutritional Data successfully added!",Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        };
    }

    private void createFromViews() {
        Double calories = Double.parseDouble(tietCalories.getText().toString());
        Double carbs = Double.parseDouble(tietCarbs.getText().toString());
        Double proteins = Double.parseDouble(tietProteins.getText().toString());
        Double fat = Double.parseDouble(tietFat.getText().toString());
        nutritionalData = new NutritionalData(calories, carbs, proteins, fat, recipeId);
    }
    private boolean isValid() {

        if (tietCalories.getText() == null || tietCalories.getText().toString().isEmpty()
                || Double.parseDouble(tietCalories.getText().toString()) < 0) {
            Toast.makeText(getApplicationContext(),
                    "Invalid calories value!",
                    Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        if (tietCarbs.getText() == null || tietCarbs.getText().toString().isEmpty()
                || Double.parseDouble(tietCarbs.getText().toString()) < 0) {
            Toast.makeText(getApplicationContext(),
                    "Invalid carbs value!",
                    Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        if (tietProteins.getText() == null || tietProteins.getText().toString().isEmpty()
                || Double.parseDouble(tietProteins.getText().toString()) < 0) {
            Toast.makeText(getApplicationContext(),
                    "Invalid proteins value!",
                    Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        if (tietFat.getText() == null || tietFat.getText().toString().isEmpty()
                || Double.parseDouble(tietFat.getText().toString()) < 0) {
            Toast.makeText(getApplicationContext(),
                    "Invalid fat value!",
                    Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        return true;
    }
}
