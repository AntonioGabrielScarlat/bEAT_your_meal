package licenta.beatyourmeal.auxiliary.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import licenta.beatyourmeal.R;
import licenta.beatyourmeal.database.recipe.Recipe;


public class RecipeAdapter extends ArrayAdapter<Recipe> {

    private Context context;
    private int resource;
    private List<Recipe> recipes;
    private LayoutInflater inflater;

    public RecipeAdapter(@NonNull Context context, int resource, @NonNull List<Recipe> objects, LayoutInflater inflater) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.recipes = objects;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = inflater.inflate(resource, parent, false);
        Recipe recipe = recipes.get(position);
        if (recipe != null) {
            addName(view, recipe.getName());
        }
        return view;
    }

    private void addName(View view, String name) {
        TextView textView = view.findViewById(R.id.lv_recipe_row_tv_name);
        addTextViewContent(textView,name);
    }

    private void addTextViewContent(TextView textView, String value) {
        if (value != null && !value.isEmpty()) {
            textView.setText(value);
        } else {
            textView.setText(R.string.lv_expense_row_default_value);
        }
    }
}
