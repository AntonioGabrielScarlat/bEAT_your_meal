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
import licenta.beatyourmeal.database.cookingStep.CookingStep;


public class CookingStepAdapter extends ArrayAdapter<CookingStep> {

    private Context context;
    private int resource;
    private List<CookingStep> cookingSteps;
    private LayoutInflater inflater;

    public CookingStepAdapter(@NonNull Context context, int resource, @NonNull List<CookingStep> objects, LayoutInflater inflater) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.cookingSteps = objects;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = inflater.inflate(resource, parent, false);
        CookingStep cookingStep = cookingSteps.get(position);
        if (cookingStep != null) {
            addStep(view,cookingStep.getStepNumber());
            addCookingIndication(view, cookingStep.getCookingIndication());
        }
        return view;
    }

    private void addStep(View view, int step) {
        TextView textView = view.findViewById(R.id.lv_cooking_step_row_tv_step_number);
        addTextViewContent(textView,Integer.toString(step)+". ");
    }

    private void addCookingIndication(View view, String name) {
        TextView textView = view.findViewById(R.id.lv_cooking_step_row_tv_cooking_indication);
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
