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
import licenta.beatyourmeal.auxiliary.listed.ListedEatenMeal;


public class EatenMealAdapter extends ArrayAdapter<ListedEatenMeal> {

    private Context context;
    private int resource;
    private List<ListedEatenMeal> listedEatenMeals;
    private LayoutInflater inflater;

    public EatenMealAdapter(@NonNull Context context, int resource, @NonNull List<ListedEatenMeal> objects, LayoutInflater inflater) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.listedEatenMeals = objects;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = inflater.inflate(resource, parent, false);
        ListedEatenMeal listedEatenMeal = listedEatenMeals.get(position);
        if (listedEatenMeal != null) {
            addMealOfTheDay(view, listedEatenMeal.getMealOfTheDay());
            addName(view, listedEatenMeal.getName());
            addCalories(view, listedEatenMeal.getCalories());
        }
        return view;
    }

    private void addName(View view, String name) {
        TextView textView = view.findViewById(R.id.lv_eaten_meal_row_tv_name);
        addTextViewContent(textView,name);
    }

    private void addMealOfTheDay(View view, String mealOfTheDay) {
        TextView textView = view.findViewById(R.id.lv_eaten_meal_row_tv_meal_of_the_day);
        addTextViewContent(textView,mealOfTheDay);
    }

    private void addCalories(View view, Double calories) {
        TextView textView = view.findViewById(R.id.lv_eaten_meal_row_tv_calories);
        addTextViewContent(textView,Double.toString(calories)+" kcal");
    }

    private void addTextViewContent(TextView textView, String value) {
        if (value != null && !value.isEmpty()) {
            textView.setText(value);
        } else {
            textView.setText(R.string.lv_expense_row_default_value);
        }
    }
}
