package licenta.beatyourmeal.activities.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Date;

import licenta.beatyourmeal.R;
import licenta.beatyourmeal.async.Callback;
import licenta.beatyourmeal.database.biometricData.BiometricData;
import licenta.beatyourmeal.database.biometricData.BiometricDataService;
import licenta.beatyourmeal.database.objective.Objective;
import licenta.beatyourmeal.database.objective.ObjectiveService;

public class AddObjectiveActivity extends AppCompatActivity {
    public static final String OBJECTIVE_KEY = "obiectivKey";
    private TextView tvCurrentWeight;
    private TextInputEditText tietNewWeight;
    private Spinner spnLostKgPerWeek;
    private Button btnSubmit;

    private Objective objective;
    private Intent intent;

    private Double currentWeight;
    private long userId;
    private BiometricDataService biometricDataService;
    private ObjectiveService objectiveService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_objective);
        initComponents();
        biometricDataService=new BiometricDataService(getApplicationContext());
        objectiveService=new ObjectiveService(getApplicationContext());
        intent=getIntent();
        userId=intent.getLongExtra("user_id",userId);
        biometricDataService.getBiometricDataByUserId(userId,getBiometricDataByUserIdCallback());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_asset_logo_wide);
        getSupportActionBar().setTitle("");
    }

    private Callback<BiometricData> getBiometricDataByUserIdCallback() {
        return new Callback<BiometricData>() {
            @Override
            public void runResultOnUiThread(BiometricData biometricData) {
                if (biometricData != null) {
                    currentWeight=biometricData.getWeight();
                    tvCurrentWeight.setText(currentWeight.toString());
                    objectiveService.getObjectiveByUserId(userId,getObjectiveByUserIdCallback());
                }
            }
        };
    }

    private Callback<Objective> getObjectiveByUserIdCallback() {
        return new Callback<Objective>() {
            @Override
            public void runResultOnUiThread(Objective searchedObjective) {
                if (searchedObjective != null) {
                    objective=searchedObjective;
                }
            }
        };
    }

    private void initComponents() {
        tvCurrentWeight=findViewById(R.id.add_objective_tv_current_weight);
        tietNewWeight =findViewById(R.id.add_objective_tiet_new_weight);
        spnLostKgPerWeek=findViewById(R.id.add_objective_spn_weekly_lost_kg);
        btnSubmit =findViewById(R.id.add_objective_btn_submit);
        addLostKgPerWeekAdapter();
        btnSubmit.setOnClickListener(saveObjectiveEventListener());
    }

    private View.OnClickListener saveObjectiveEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid()){
                    createFromViews();
                    intent.putExtra(OBJECTIVE_KEY, objective);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        };
    }

    private boolean isValid() {
        if (tietNewWeight.getText() == null || tietNewWeight.getText().toString().isEmpty()
                || Double.parseDouble(tietNewWeight.getText().toString()) < 0) {
            Toast.makeText(getApplicationContext(),
                    "Invalid desired weight!",
                    Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        if (Double.parseDouble(tietNewWeight.getText().toString()) > currentWeight) {
            Toast.makeText(getApplicationContext(),
                    "Your target weight is bigger than the actual one!",
                    Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        return true;
    }

    private void createFromViews() {
        Double newWeight = Double.parseDouble(tietNewWeight.getText().toString());
        Double lostKgPerWeek = Double.parseDouble(spnLostKgPerWeek.getSelectedItem().toString());
        int numberOfDays=(int)((currentWeight-newWeight)/lostKgPerWeek)*7;
        Calendar c=Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE,numberOfDays);
        Date targetDate=c.getTime();
        if(objective==null){
            objective=new Objective(newWeight,lostKgPerWeek,targetDate,userId);
        }
        else
        {
            objective.setNewWeight(newWeight);
            objective.setLostKgPerWeek(lostKgPerWeek);
            objective.setTargetDate(targetDate);
            objective.setIdUser(userId);
        }

    }

    private void addLostKgPerWeekAdapter() {
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.add_objective_lost_kg_per_week_values,
                R.layout.support_simple_spinner_dropdown_item);
        spnLostKgPerWeek.setAdapter(adapter);
    }
}