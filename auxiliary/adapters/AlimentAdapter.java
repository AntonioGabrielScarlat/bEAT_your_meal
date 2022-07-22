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
import licenta.beatyourmeal.database.aliment.Aliment;


public class AlimentAdapter extends ArrayAdapter<Aliment> {

    private Context context;
    private int resource;
    private List<Aliment> aliments;
    private LayoutInflater inflater;

    public AlimentAdapter(@NonNull Context context, int resource, @NonNull List<Aliment> objects, LayoutInflater inflater) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.aliments = objects;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = inflater.inflate(resource, parent, false);
        Aliment aliment = aliments.get(position);
        if (aliment != null) {
            addName(view, aliment.getName());
        }
        return view;
    }

    private void addName(View view, String name) {
        TextView textView = view.findViewById(R.id.lv_aliment_row_tv_name);
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
