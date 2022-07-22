package licenta.beatyourmeal.activities.admin.home;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import licenta.beatyourmeal.R;
import licenta.beatyourmeal.activities.admin.home.fragments.AlimentsFragment;
import licenta.beatyourmeal.activities.admin.home.fragments.HomeFragment;
import licenta.beatyourmeal.activities.admin.home.fragments.RecipesFragment;
import licenta.beatyourmeal.database.biometricData.BiometricData;
import licenta.beatyourmeal.activities.user.AddBiometricDataActivity;
import licenta.beatyourmeal.activities.user.AddFoodPreferencesActivity;
import licenta.beatyourmeal.async.Callback;

public class AdminHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_asset_logo_wide);
        getSupportActionBar().setTitle("");
        BottomNavigationView bottomNavigationView=findViewById(R.id.admin_home_bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(navigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.admin_home_fragment_container,
                new HomeFragment()).commit();
    }

    private NavigationBarView.OnItemSelectedListener navigationItemSelectedListener= new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment=null;
            switch(item.getItemId()){
                case R.id.nav_admin_home:
                    selectedFragment=new HomeFragment();
                    break;
                case R.id.nav_admin_aliments:
                    selectedFragment=new AlimentsFragment();
                    break;
                case R.id.nav_admin_recipes:
                    selectedFragment=new RecipesFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.admin_home_fragment_container,
                    selectedFragment).commit();
            return true;
        }
    };

    private ActivityResultLauncher<Intent> getAddCorporalDataLauncher() {
        ActivityResultCallback<ActivityResult> callback = getAddCorporalDataActivityResultCallback();
        return registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), callback);
    }

    private ActivityResultCallback<ActivityResult> getAddCorporalDataActivityResultCallback() {
        return new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result != null && result.getResultCode() == RESULT_OK && result.getData() != null) {
                    BiometricData biometricData = (BiometricData) result.getData().getSerializableExtra(AddBiometricDataActivity.BIOMETRIC_DATA_KEY);
                    //inserare in baza de date
                   // corporalDataService.insert(corporalData, getInsertCorporalDataCallback());
                }
            }
        };
    }

    private Callback<BiometricData> getInsertCorporalDataCallback() {
        return new Callback<BiometricData>() {
            @Override
            public void runResultOnUiThread(BiometricData biometricData) {
                //aici suntem notificati din thread-ul secundar cand operatia de adaugare in baza de date s-a executat
                if (biometricData != null) {
                    //corporalDatas.add(corporalData);
                    Toast.makeText(getApplicationContext(),"Corporal Dates successfully added!",Toast.LENGTH_SHORT).show();
                }
            }
        };
    }





    private void initComponents() {
       // btnAddCorporalData.setOnClickListener(addCorporalDataEventListener());
       // btnPreferences.setOnClickListener(addFoodPreferencesEventListener());
    }

    private View.OnClickListener addFoodPreferencesEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddFoodPreferencesActivity.class);
                startActivity(intent);
            }
        };

    }

    private View.OnClickListener addCorporalDataEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddBiometricDataActivity.class);
               // addCorporalDataLauncher.launch(intent);
            }
        };
    }


}