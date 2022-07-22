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
import licenta.beatyourmeal.auxiliary.listed.ListedIngredient;


public class IngredientAdapter extends ArrayAdapter<ListedIngredient> {

    private Context context;
    private int resource;
    private List<ListedIngredient> listedIngredients;
    private LayoutInflater inflater;

    public IngredientAdapter(@NonNull Context context, int resource, @NonNull List<ListedIngredient> objects, LayoutInflater inflater) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.listedIngredients = objects;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = inflater.inflate(resource, parent, false);
        ListedIngredient listedIngredient = listedIngredients.get(position);
        if (listedIngredient != null) {
            addName(view, listedIngredient.getName());
            addQuantityAndUnitOfMeasurement(view, listedIngredient.getQuantity(),listedIngredient.getUnitOfMeasurement());
        }
        return view;
    }

    private void addName(View view, String name) {
        TextView textView = view.findViewById(R.id.lv_ingredient_row_tv_name);
        addTextViewContent(textView,name);
    }

    private void addQuantityAndUnitOfMeasurement(View view, Double quantity, String unitOfMeasurement) {
        TextView textView = view.findViewById(R.id.lv_ingredient_row_tv_quantity);
        addTextViewContent(textView,Double.toString(quantity)+" "+unitOfMeasurement);
    }

    private void addTextViewContent(TextView textView, String value) {
        if (value != null && !value.isEmpty()) {
            textView.setText(value);
        } else {
            textView.setText(R.string.lv_expense_row_default_value);
        }
    }
}
