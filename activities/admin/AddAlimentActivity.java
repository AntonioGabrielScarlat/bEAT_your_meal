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
import licenta.beatyourmeal.database.aliment.Aliment;

public class AddAlimentActivity extends AppCompatActivity {
    public static final String ALIMENT_KEY = "alimentKey";
    private TextInputEditText tietName;
    private Spinner spnCategory;
    private TextInputEditText tietNutritionalValue;
    private Button btnSubmit;

    private Aliment aliment;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_aliment);
        initComponents();
        intent=getIntent();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_asset_logo_wide);
        getSupportActionBar().setTitle("");
    }

    private void initComponents() {
        tietName=findViewById(R.id.add_aliments_tiet_name);
        spnCategory=findViewById(R.id.add_aliments_spn_category);
        tietNutritionalValue=findViewById(R.id.add_aliments_tiet_nutritionalValue);
        btnSubmit =findViewById(R.id.add_aliments_btn_submit);
        addCategoryAdapter();
        btnSubmit.setOnClickListener(saveAlimentEventListener());
    }

    private void addCategoryAdapter() {
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.add_aliments_category_values,
                R.layout.support_simple_spinner_dropdown_item);
        spnCategory.setAdapter(adapter);
    }

    private View.OnClickListener saveAlimentEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid()){
                    createFromViews();
                    intent.putExtra(ALIMENT_KEY, aliment);
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
        if (tietNutritionalValue.getText() == null || tietNutritionalValue.getText().toString().isEmpty()
                || Double.parseDouble(tietNutritionalValue.getText().toString()) < 0) {
            Toast.makeText(getApplicationContext(),
                    "Invalid nutritional value!",
                    Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        return true;
    }

    private void createFromViews() {
        String name = tietName.getText().toString();
        String category = spnCategory.getSelectedItem().toString();
        Double nutritionalValue = Double.parseDouble(tietNutritionalValue.getText().toString());
            aliment = new Aliment(name, category, nutritionalValue);
    }
}