package licenta.beatyourmeal.activities.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import licenta.beatyourmeal.R;
import licenta.beatyourmeal.auxiliary.listed.ListedIngredient;
import licenta.beatyourmeal.async.Callback;
import licenta.beatyourmeal.auxiliary.adapters.IngredientAdapter;
import licenta.beatyourmeal.database.aliment.Aliment;
import licenta.beatyourmeal.database.aliment.AlimentService;
import licenta.beatyourmeal.database.ingredient.Ingredient;
import licenta.beatyourmeal.database.ingredient.IngredientService;

public class AddIngredientsActivity extends AppCompatActivity {
    private String[] alimentsNames;
    private AutoCompleteTextView actvName;
    private TextInputEditText tietQuantity;
    private Spinner spnUnitOfMeasurement;
    private Button btnAddIngredient;
    private List<Aliment> aliments = new ArrayList<>();
    private List<Aliment> allAliments=new ArrayList<>();
    private List<ListedIngredient> listedIngredients=new ArrayList<>();
    private ListView lvIngredients;
    private Button btnSubmit;

    private Intent intent;
    private long recipeId;
    private long alimentId;
    private Ingredient ingredient;

    private AlimentService alimentService;
    private IngredientService ingredientService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredients);
        initComponents();
        intent=getIntent();
        recipeId=intent.getLongExtra("recipe_id",recipeId);
        alimentService=new AlimentService(getApplicationContext());
        ingredientService=new IngredientService(getApplicationContext());
        alimentService.getAll(getAllAlimentsCallback());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_asset_logo_wide);
        getSupportActionBar().setTitle("");
    }

    private void initComponents() {
        actvName=findViewById(R.id.add_ingredients_actv_aliment_name);
        tietQuantity=findViewById(R.id.add_ingredients_tiet_quantity);
        spnUnitOfMeasurement=findViewById(R.id.add_ingredients_spn_unit_of_measurement);
        addUnitsOfMeasurementAdapter();
        btnAddIngredient=findViewById(R.id.add_ingredients_btn_add_ingredient);
        lvIngredients =findViewById(R.id.add_ingredients_lv_ingredients);
        addAdapter();
        btnSubmit =findViewById(R.id.add_ingredients_btn_submit);
        btnSubmit.setOnClickListener(saveIngredientsEventListener());
        btnAddIngredient.setOnClickListener(addIngredientEventListener());

    }

    private void addUnitsOfMeasurementAdapter() {
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.add_ingredients_unit_of_measurement_values,
                R.layout.support_simple_spinner_dropdown_item);
        spnUnitOfMeasurement.setAdapter(adapter);
    }

    private Callback<List<Aliment>> getAllAlimentsCallback() {
        return new Callback<List<Aliment>>() {
            @Override
            public void runResultOnUiThread(List<Aliment> results) {
                if (results != null) {
                    allAliments.clear();
                    allAliments.addAll(results);
                    List<String> listAlimentNames=new ArrayList<String>();
                    for(Aliment aliment:allAliments){
                        listAlimentNames.add(aliment.getName());
                    }
                    alimentsNames =new String[listAlimentNames.size()];
                    alimentsNames =listAlimentNames.toArray(alimentsNames);
                    ArrayAdapter<String> adapter=new ArrayAdapter<String>(
                            getApplicationContext(),android.R.layout.simple_list_item_1,alimentsNames);
                    actvName.setAdapter(adapter);

                }
            }
        };
    }
    private View.OnClickListener addIngredientEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isValid())
                {
                    alimentService.getByName(actvName.getText().toString(),getByNameCallback());
                }

            }
        };
    }

    private boolean isValid() {
        if (actvName.getText() == null || actvName.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "Invalid aliment!",
                    Toast.LENGTH_SHORT)
                    .show();
            return false;
        }

        if(tietQuantity.getText()==null || tietQuantity.getText().toString().isEmpty()
        || Double.parseDouble(tietQuantity.getText().toString()) < 0)
        {
            Toast.makeText(getApplicationContext(),
                    "Invalid quantity!",
                    Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        for(Aliment aliment:allAliments){
            if(actvName.getText().toString().equals(aliment.getName()))
                return true;
        }

        Toast.makeText(getApplicationContext(),"Aliment not found!",Toast.LENGTH_SHORT).show();
        return false;
    }

    private Callback<Ingredient> getInsertIngredientCallback() {
        return new Callback<Ingredient>() {
            @Override
            public void runResultOnUiThread(Ingredient result) {
                if (result != null) {
                    listedIngredients.add(new ListedIngredient(actvName.getText().toString(),Double.parseDouble(tietQuantity.getText().toString()),spnUnitOfMeasurement.getSelectedItem().toString()));
                    notifyAdapter();
                }
            }
        };
    }

    private void addAdapter() {
        IngredientAdapter adapter = new IngredientAdapter(getApplicationContext(), R.layout.lv_ingredient_row,
                listedIngredients, getLayoutInflater());
        lvIngredients.setAdapter(adapter);
    }

    public void notifyAdapter() {
        ArrayAdapter<ListedIngredient> adapter = (ArrayAdapter<ListedIngredient>) lvIngredients.getAdapter();
        adapter.notifyDataSetChanged();
    }

    private void createFromViews() {
        Double quantity = Double.parseDouble(tietQuantity.getText().toString());
        String unitOfMeasurement=spnUnitOfMeasurement.getSelectedItem().toString();
        ingredient =new Ingredient(quantity,unitOfMeasurement,recipeId,alimentId);
    }

    private Callback<Aliment> getByNameCallback() {
        return new Callback<Aliment>() {
            @Override
            public void runResultOnUiThread(Aliment result) {
                if (result != null) {
                    alimentId=result.getId();
                    aliments.add(result);
                    createFromViews();
                    ingredientService.insert(ingredient,getInsertIngredientCallback());
                }
            }
        };
    }

    private View.OnClickListener saveIngredientsEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    setResult(RESULT_OK, intent);
                    finish();
                    Intent intent=new Intent(getApplicationContext(), AddCookingStepsActivity.class);
                    intent.putExtra("recipe_id",recipeId);
                    startActivity(intent);
                }
            };
        }
    }
