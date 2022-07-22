package licenta.beatyourmeal.activities.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import licenta.beatyourmeal.R;
import licenta.beatyourmeal.auxiliary.adapters.CookingStepAdapter;
import licenta.beatyourmeal.async.Callback;
import licenta.beatyourmeal.database.cookingStep.CookingStep;
import licenta.beatyourmeal.database.cookingStep.CookingStepService;

public class AddCookingStepsActivity extends AppCompatActivity {

    private TextInputEditText tietStep;
    private TextInputEditText tietCookingIndication;
    private ListView lvCookingSteps;
    private Button btnAddCookingStep;
    private Button btnSubmit;

    private List<CookingStep> cookingSteps = new ArrayList<>();
    private Intent intent;
    private long recipeId;
    private CookingStep cookingStep;

    private CookingStepService cookingStepService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cooking_steps);
        initComponents();
        intent=getIntent();
        recipeId=intent.getLongExtra("recipe_id",recipeId);
        cookingStepService=new CookingStepService(getApplicationContext());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_asset_logo_wide);
        getSupportActionBar().setTitle("");
    }

    private void initComponents() {
        tietStep=findViewById(R.id.add_cooking_steps_tiet_step_number);
        tietCookingIndication =findViewById(R.id.add_cooking_steps_tiet_cooking_indication);
        lvCookingSteps=findViewById(R.id.add_cooking_steps_lv_cooking_steps);
        addAdapter();
        btnAddCookingStep=findViewById(R.id.add_cooking_steps_btn_add_cooking_step);
        btnSubmit =findViewById(R.id.add_cooking_steps_btn_submit);
        btnAddCookingStep.setOnClickListener(addCookingStepEventListener());
        btnSubmit.setOnClickListener(saveCookingStepsEventListener());
    }


    private View.OnClickListener addCookingStepEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isValid())
                {
                    createFromViews();
                    cookingStepService.insert(cookingStep,insertCookingStepCallback());
                }

            }
        };
    }

    private Callback<CookingStep> insertCookingStepCallback() {
        return new Callback<CookingStep>() {
            @Override
            public void runResultOnUiThread(CookingStep result) {
                if (result != null) {
                    cookingSteps.add(result);
                    notifyAdapter();
                }
            }
        };
    }

    private boolean isValid() {
        if (tietStep.getText() == null || tietStep.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "Invalid step!",
                    Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        if (tietCookingIndication.getText() == null || tietCookingIndication.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "Invalid cooking indication!",
                    Toast.LENGTH_SHORT)
                    .show();
            return false;
        }

        return true;
    }

    private void createFromViews() {
        Integer step = Integer.parseInt(tietStep.getText().toString());
        String cookingIndication= tietCookingIndication.getText().toString();
        cookingStep =new CookingStep(step,cookingIndication,recipeId);
    }

    private View.OnClickListener saveCookingStepsEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK, intent);
                finish();
                Intent intent=new Intent(getApplicationContext(), AddNutritionalDataActivity.class);
                intent.putExtra("recipe_id",recipeId);
                startActivity(intent);
            }
        };
    }

    private void addAdapter() {
        CookingStepAdapter adapter = new CookingStepAdapter(getApplicationContext(), R.layout.lv_cooking_step_row,
                cookingSteps, getLayoutInflater());
        lvCookingSteps.setAdapter(adapter);
    }

    public void notifyAdapter() {
        ArrayAdapter<CookingStep> adapter = (ArrayAdapter<CookingStep>) lvCookingSteps.getAdapter();
        adapter.notifyDataSetChanged();
    }
}