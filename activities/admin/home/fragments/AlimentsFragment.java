package licenta.beatyourmeal.activities.admin.home.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import licenta.beatyourmeal.R;
import licenta.beatyourmeal.activities.admin.AddAlimentActivity;
import licenta.beatyourmeal.auxiliary.adapters.AlimentAdapter;
import licenta.beatyourmeal.async.Callback;
import licenta.beatyourmeal.database.aliment.Aliment;
import licenta.beatyourmeal.database.aliment.AlimentService;

public class AlimentsFragment extends Fragment {
    public static final String ALIMENT_KEY = "alimentKey";

    private Button btnAddAliment;
    private ListView lvAliments;
    private ArrayList<Aliment> aliments =new ArrayList<>();
    private ActivityResultLauncher<Intent> addAlimentLauncher;
    private AlimentService alimentService;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_admin_aliments,container,false);
        initComponents(view);
        return view;
    }

    private void initComponents(View view) {
        btnAddAliment=view.findViewById(R.id.fragment_admin_aliments_btn_add_aliment);
        lvAliments=view.findViewById(R.id.fragment_admin_aliments_lv_aliments);
        addAdapter();
        btnAddAliment.setOnClickListener(addAlimentEventListener());
    }

    private View.OnClickListener addAlimentEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext().getApplicationContext(), AddAlimentActivity.class);
                addAlimentLauncher.launch(intent);
            }
        };
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addAlimentLauncher=getAddAlimentLauncher();
        alimentService=new AlimentService(getContext().getApplicationContext());
        alimentService.getAll(getAllAlimentsCallback());
    }

    private ActivityResultLauncher<Intent> getAddAlimentLauncher() {
        ActivityResultCallback<ActivityResult> callback = getAddAlimentActivityResultCallback();
        return registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), callback);
    }

    private ActivityResultCallback<ActivityResult> getAddAlimentActivityResultCallback() {
        return new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result != null && result.getResultCode() == -1 && result.getData() != null) {
                    Aliment aliment = (Aliment) result.getData().getSerializableExtra(AddAlimentActivity.ALIMENT_KEY);
                    //inserare in baza de date
                    alimentService.insert(aliment, getInsertAlimentCallback());
                }
            }
        };
    }

    private Callback<Aliment> getInsertAlimentCallback() {
        return new Callback<Aliment>() {
            @Override
            public void runResultOnUiThread(Aliment aliment) {
                if (aliment != null) {
                    aliments.add(aliment);
                    notifyAdapter();
                    Toast.makeText(getContext().getApplicationContext(),"Aliment successfully added!",Toast.LENGTH_SHORT).show();

                }
            }
        };
    }

    private void addAdapter() {
        AlimentAdapter adapter = new AlimentAdapter(getContext().getApplicationContext(), R.layout.lv_aliment_row,
                aliments, getLayoutInflater());
        lvAliments.setAdapter(adapter);
    }

    public void notifyAdapter() {
        ArrayAdapter<Aliment> adapter = (ArrayAdapter<Aliment>) lvAliments.getAdapter();
        adapter.notifyDataSetChanged();
    }

    private Callback<List<Aliment>> getAllAlimentsCallback() {
        return new Callback<List<Aliment>>() {
            @Override
            public void runResultOnUiThread(List<Aliment> results) {
                if (results != null) {
                    aliments.clear();
                    aliments.addAll(results);
                    notifyAdapter();
                }
            }
        };
    }

    public AlimentsFragment() {
    }

}
