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
import licenta.beatyourmeal.auxiliary.listed.ListedPreference;


public class PreferenceAdapter extends ArrayAdapter<ListedPreference> {

    private Context context;
    private int resource;
    private List<ListedPreference> listedPreferences;
    private LayoutInflater inflater;
    private int color;

    public PreferenceAdapter(@NonNull Context context, int resource, @NonNull List<ListedPreference> objects, LayoutInflater inflater) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.listedPreferences = objects;
        this.inflater = inflater;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = inflater.inflate(resource, parent, false);
        ListedPreference listedPreference = listedPreferences.get(position);
        if (listedPreference != null) {
            addName(view, listedPreference.getAliment().getName());
            addIsPreferred(view, listedPreference.getIsPreferred());
        }
        return view;
    }

    private void addName(View view, String name) {
        TextView textView = view.findViewById(R.id.lv_preference_row_tv_name);
        addTextViewContent(textView,name);
    }

    private void addIsPreferred(View view, String isPreferred) {
        TextView textView = view.findViewById(R.id.lv_preference_row_tv_is_preferred);
        addTextViewContent(textView,isPreferred);
    }

    private void addTextViewContent(TextView textView, String value) {
        if (value != null && !value.isEmpty()) {
            textView.setText(value);
        } else {
            textView.setText(R.string.lv_expense_row_default_value);
        }
    }
}
