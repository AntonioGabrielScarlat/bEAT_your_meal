package licenta.beatyourmeal.activities.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Date;

import licenta.beatyourmeal.R;
import licenta.beatyourmeal.database.biometricData.BiometricData;
import licenta.beatyourmeal.auxiliary.converters.DateConverter;

public class AddBiometricDataActivity extends AppCompatActivity {
    public static final String BIOMETRIC_DATA_KEY ="corporalDataKey";
    private Spinner spnSex;
    private TextInputEditText tietHeight;
    private TextInputEditText tietWeight;
    private TextInputEditText tietBirthDate;
    private Button btnSubmit;
    private long userId;

    private BiometricData biometricData;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_biometric_data);
        initComponents();
        intent=getIntent();
        userId=intent.getLongExtra("user_id",userId);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_asset_logo_wide);
        getSupportActionBar().setTitle("");
    }

    private void initComponents() {
        spnSex=findViewById(R.id.add_biometric_data_spn_sex);
        tietHeight =findViewById(R.id.add_biometric_data_tiet_height);
        tietWeight =findViewById(R.id.add_biometric_data_tiet_weight);
        tietBirthDate =findViewById(R.id.add_biometric_data_tiet_birth_date);
        btnSubmit =findViewById(R.id.add_biometric_data_btn_submit);
        addCategoryAdapter();
        btnSubmit.setOnClickListener(saveCorporalDataEventListener());
    }

    private void addCategoryAdapter() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.add_biometric_data_category_values,
                android.R.layout.simple_spinner_dropdown_item);
        spnSex.setAdapter(adapter);
    }

    private View.OnClickListener saveCorporalDataEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    createFromViews();
                    intent.putExtra(BIOMETRIC_DATA_KEY, biometricData);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        };
    }

    private boolean isValid() {

        if (tietBirthDate.getText() == null || tietBirthDate.getText().toString().trim().isEmpty()
                || DateConverter.fromString(tietBirthDate.getText().toString()) == null) {
            Toast.makeText(getApplicationContext(),
                    "Invalid birth date!",
                    Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        if (tietHeight.getText() == null || tietHeight.getText().toString().isEmpty()
                || Double.parseDouble(tietHeight.getText().toString()) < 0) {
            Toast.makeText(getApplicationContext(),
                    "Invalid height!",
                    Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        if (tietWeight.getText() == null || tietWeight.getText().toString().isEmpty()
                || Double.parseDouble(tietWeight.getText().toString()) < 0) {
            Toast.makeText(getApplicationContext(),
                    "Invalid weight!",
                    Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        return true;
    }

    private void createFromViews() {
        String sex = spnSex.getSelectedItem().toString();
        Double height = Double.parseDouble(tietHeight.getText().toString());
        Double weight = Double.parseDouble(tietWeight.getText().toString());
        Date date = DateConverter.fromString(tietBirthDate.getText().toString());
        biometricData =new BiometricData(sex,height,weight,date,userId);
    }


}