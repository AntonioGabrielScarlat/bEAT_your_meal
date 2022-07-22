package licenta.beatyourmeal.activities.user;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import licenta.beatyourmeal.R;
import licenta.beatyourmeal.auxiliary.adapters.PreferenceAdapter;
import licenta.beatyourmeal.async.Callback;
import licenta.beatyourmeal.auxiliary.listed.ListedPreference;
import licenta.beatyourmeal.database.aliment.Aliment;
import licenta.beatyourmeal.database.aliment.AlimentService;
import licenta.beatyourmeal.database.preference.Preference;
import licenta.beatyourmeal.database.preference.PreferenceService;

public class AddFoodPreferencesActivity extends AppCompatActivity {
    public static final String PREFERENCE_KEY = "mealKey";

    private ListView lvPreferredAliments;
    private Button btnSubmit;
    private List<Aliment> aliments =new ArrayList<>();
    private List<ListedPreference> listedPreferences =new ArrayList<>();

    private long userId;

    private AlertDialog.Builder builder;
    private Intent intent;
    private AlimentService alimentService;
    private PreferenceService preferenceService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_preferences);
        initComponents();
        intent=getIntent();
        userId=intent.getLongExtra("user_id",userId);
        builder = new AlertDialog.Builder(this);
        alimentService=new AlimentService(getApplicationContext());
        preferenceService=new PreferenceService(getApplicationContext());
        alimentService.getAll(getAllAlimentsCallback());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_asset_logo_wide);
        getSupportActionBar().setTitle("");
    }

    private Callback<List<Aliment>> getAllAlimentsCallback() {
        return new Callback<List<Aliment>>() {
            @Override
            public void runResultOnUiThread(List<Aliment> results) {
                if (results != null) {
                    aliments.clear();
                    aliments.addAll(results);
                    for(Aliment aliment:aliments){
                        listedPreferences.add(new ListedPreference(aliment,"NO"));
                    }
                    notifyAdapter();
                    for(Aliment aliment:aliments){
                        preferenceService.getPreferenceByUserIdAndAlimentId(userId,aliment.getId(),getPreferenceByUserIdAndAlimentIdInitCallback());
                    }
                }
            }
        };
    }

    private Callback<Preference> getPreferenceByUserIdAndAlimentIdInitCallback() {
        return new Callback<Preference>() {
            @Override
            public void runResultOnUiThread(Preference result) {
                if (result != null) {
                    alimentService.getById(result.getIdAliment(),getAlimentByIdCallback());
                }
            }
        };
    }

    private Callback<Aliment> getAlimentByIdCallback() {
        return new Callback<Aliment>() {
            @Override
            public void runResultOnUiThread(Aliment result) {
                if (result != null) {
                    for(ListedPreference listedPreference : listedPreferences){
                        if(listedPreference.getAliment().getId()==result.getId()){
                            listedPreference.setIsPreferred("YES");
                            notifyAdapter();
                        }
                    }
                }
            }
        };
    }

    private void initComponents() {
        lvPreferredAliments =findViewById(R.id.add_food_preferences_lv_aliments);
        btnSubmit =findViewById(R.id.add_food_preferences_btn_submit);
        addAdapter();
        lvPreferredAliments.setOnItemClickListener(selectItemEventListener());
        btnSubmit.setOnClickListener(savePreferencesEventListener());
    }

    private View.OnClickListener savePreferencesEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    finish();
            }
        };
    }

    private AdapterView.OnItemClickListener selectItemEventListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(listedPreferences.get(position).getIsPreferred().equals("YES")){
                        builder.setMessage("This aliment is already in your preferences. Do you want to remove it?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        listedPreferences.get(position).setIsPreferred("NO");
                                        notifyAdapter();
                                        preferenceService.getPreferenceByUserIdAndAlimentId(userId, listedPreferences.get(position).getAliment().getId(),getPreferenceByUserIdAndAlimentIdCallback());
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        //Creating dialog box
                        AlertDialog alert = builder.create();
                        //Setting the title manually
                        alert.setTitle("Remove preference");
                        alert.show();
                    }
                    else {
                        listedPreferences.get(position).setIsPreferred("YES");
                        notifyAdapter();
                    Preference preference=new Preference(userId, listedPreferences.get(position).getAliment().getId());
                    preferenceService.insert(preference,getInsertPreferenceCallback());
                }
            }
        };
    }

    private Callback<Preference> getPreferenceByUserIdAndAlimentIdCallback() {
        return new Callback<Preference>() {
            @Override
            public void runResultOnUiThread(Preference result) {
                if (result != null) {
                    preferenceService.delete(result,getDeletePreferenceCallback());
                }
            }
        };
    }

    private Callback<Boolean> getDeletePreferenceCallback() {
        return new Callback<Boolean>() {
            @Override
            public void runResultOnUiThread(Boolean result) {
                if (result) {

                }
            }
        };
    }

    private Callback<Preference> getInsertPreferenceCallback() {
        return new Callback<Preference>() {
            @Override
            public void runResultOnUiThread(Preference result) {
                if (result != null) {

                }
            }
        };
    }


    private void addAdapter() {
        PreferenceAdapter adapter = new PreferenceAdapter(getApplicationContext(), R.layout.lv_preference_row,
                listedPreferences, getLayoutInflater());
        lvPreferredAliments.setAdapter(adapter);
    }

    private void notifyAdapter() {
        PreferenceAdapter adapter = (PreferenceAdapter) lvPreferredAliments.getAdapter();
        adapter.notifyDataSetChanged();
    }


}