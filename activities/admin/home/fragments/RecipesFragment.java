package licenta.beatyourmeal.activities.admin.home.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import licenta.beatyourmeal.auxiliary.adapters.RecipeAdapter;
import licenta.beatyourmeal.activities.admin.AddIngredientsActivity;
import licenta.beatyourmeal.activities.admin.AddRecipeActivity;
import licenta.beatyourmeal.async.Callback;
import licenta.beatyourmeal.database.recipe.Recipe;
import licenta.beatyourmeal.database.recipe.RecipeService;
import licenta.beatyourmeal.activities.user.SeeRecipeActivity;

public class RecipesFragment extends Fragment {
    public static final String RECIPE_KEY = "recipeKey";

    private Button btnAddRecipe;
    private AlertDialog.Builder builder;
    private ListView lvRecipes;
    private List<Recipe> recipes=new ArrayList<>();
    private ActivityResultLauncher<Intent> addRecipeLauncher;
    private RecipeService recipeService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_admin_recipes,container,false);
        initComponents(view);
        return view;
    }

    private void initComponents(View view) {
        btnAddRecipe=view.findViewById(R.id.fragment_admin_recipes_btn_add_recipe);
        lvRecipes=view.findViewById(R.id.fragment_admin_recipes_lv_recipes);
        addAdapter();
        btnAddRecipe.setOnClickListener(addRecipeEventListener());
        lvRecipes.setOnItemClickListener(selectRecipeEventListener());
        lvRecipes.setOnItemLongClickListener(deleteRecipeEventListener());
    }

    private AdapterView.OnItemLongClickListener deleteRecipeEventListener() {
        return new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                builder.setMessage("Are you sure you want to delete the recipe for "+recipes.get(position).getName()+"?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                recipeService.delete(recipes.get(position),deleteRecipeCallback(position));
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.setTitle("Delete recipe");
                alert.show();
                return true;
            }

        };
    }

    private Callback<Boolean> deleteRecipeCallback(int position) {
        return new Callback<Boolean>() {
            @Override
            public void runResultOnUiThread(Boolean result) {
                if (result) {
                    recipes.remove(position);
                    notifyAdapter();
                    Toast.makeText(getContext().getApplicationContext(),"Recipe successfully deleted!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private AdapterView.OnItemClickListener selectRecipeEventListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext().getApplicationContext(), SeeRecipeActivity.class);
                intent.putExtra("recipe_name", recipes.get(position).getName());
                startActivity(intent);
            }
        };
    }

    private View.OnClickListener addRecipeEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext().getApplicationContext(), AddRecipeActivity.class);
                addRecipeLauncher.launch(intent);
            }
        };
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addRecipeLauncher= getAddRecipeLauncher();
        builder = new AlertDialog.Builder(this.getContext());
        recipeService=new RecipeService(getContext().getApplicationContext());
        recipeService.getAll(getAllRecipesCallback());
    }



    private ActivityResultLauncher<Intent> getAddRecipeLauncher() {
        ActivityResultCallback<ActivityResult> callback = getAddRecipeActivityResultCallback();
        return registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), callback);
    }

    private ActivityResultCallback<ActivityResult> getAddRecipeActivityResultCallback() {
        return new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result != null && result.getResultCode() == -1 && result.getData() != null) {
                    Recipe recipe = (Recipe) result.getData().getSerializableExtra(AddRecipeActivity.RECIPE_KEY);
                    //inserare in baza de date
                    recipeService.insert(recipe, getInsertRecipeCallback());
                }
            }
        };
    }

    private Callback<Recipe> getInsertRecipeCallback() {
        return new Callback<Recipe>() {
            @Override
            public void runResultOnUiThread(Recipe recipe) {
                if (recipe != null) {
                    recipes.add(recipe);
                    notifyAdapter();
                    Toast.makeText(getContext().getApplicationContext(),"Recipe successfully added!",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getContext().getApplicationContext(), AddIngredientsActivity.class);
                    intent.putExtra("recipe_id",recipe.getId());
                    startActivity(intent);
                }
            }
        };
    }
    private void addAdapter() {
        RecipeAdapter adapter = new RecipeAdapter(getContext().getApplicationContext(), R.layout.lv_recipe_row,
                recipes, getLayoutInflater());
        lvRecipes.setAdapter(adapter);
    }

    public void notifyAdapter() {
        ArrayAdapter<Recipe> adapter = (ArrayAdapter<Recipe>) lvRecipes.getAdapter();
        adapter.notifyDataSetChanged();
    }

    private Callback<List<Recipe>> getAllRecipesCallback() {
        return new Callback<List<Recipe>>() {
            @Override
            public void runResultOnUiThread(List<Recipe> results) {
                if (results != null) {
                    recipes.clear();
                    recipes.addAll(results);
                    notifyAdapter();
                }
            }
        };
    }

    public RecipesFragment() {
    }
}
