package licenta.beatyourmeal.activities.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import licenta.beatyourmeal.R;
import licenta.beatyourmeal.async.Callback;
import licenta.beatyourmeal.database.biometricData.BiometricData;
import licenta.beatyourmeal.database.biometricData.BiometricDataService;

public class UpdateWeightActivity extends AppCompatActivity {
    public static final String UPDATE_WEIGHT_KEY = "updateWeightKey";
    private TextView tvCurrentWeight;
    private TextInputEditText tietNewWeight;
    private Button btnSubmit;

    private long biometricDataId;
    private Intent intent;
    private BiometricDataService biometricDataService;
    private BiometricData biometricData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_weight);
        initComponents();
        intent=getIntent();
        biometricDataId=intent.getLongExtra("biometric_data_id",biometricDataId);
        biometricDataService=new BiometricDataService(getApplicationContext());
        biometricDataService.getBiometricDataById(biometricDataId,getBiometricDataByIdCallback());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_asset_logo_wide);
        getSupportActionBar().setTitle("");
    }

    private Callback<BiometricData> getBiometricDataByIdCallback() {
        return new Callback<BiometricData>() {
            @Override
            public void runResultOnUiThread(BiometricData result) {
                if (result != null) {
                    biometricData=new BiometricData(result.getId(),result.getSex(),result.getHeight(),result.getWeight(),result.getBirthDate(),result.getIdUser());
                    tvCurrentWeight.setText(result.getWeight().toString());
                }
            }
        };
    }

    private void initComponents() {
        tvCurrentWeight=findViewById(R.id.update_weight_tv_current_weight);
        tietNewWeight=findViewById(R.id.update_weight_tiet_new_weight);
        btnSubmit =findViewById(R.id.update_weight_btn_submit);
        btnSubmit.setOnClickListener(saveNewWeightEventListener());
    }

    private View.OnClickListener saveNewWeightEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid()){
                    createFromViews();
                    intent.putExtra(UPDATE_WEIGHT_KEY, biometricData);
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
                    "Invalid new weight!",
                    Toast.LENGTH_SHORT)
                    .show();
            return false;
        }

        return true;
    }

    private void createFromViews() {
        Double newWeight=Double.parseDouble(tietNewWeight.getText().toString());
        biometricData.setWeight(newWeight);
    }
}