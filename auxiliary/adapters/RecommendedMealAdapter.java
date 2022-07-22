package licenta.beatyourmeal.auxiliary.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;
import java.util.List;

import licenta.beatyourmeal.R;
import licenta.beatyourmeal.auxiliary.converters.DateConverter;
import licenta.beatyourmeal.auxiliary.listed.ListedEatenMeal;
import licenta.beatyourmeal.auxiliary.listed.ListedRecommendedMeal;


public class RecommendedMealAdapter extends ArrayAdapter<ListedRecommendedMeal> {

    private Context context;
    private int resource;
    private List<ListedRecommendedMeal> listedRecommendedMeals;
    private LayoutInflater inflater;

    public RecommendedMealAdapter(@NonNull Context context, int resource, @NonNull List<ListedRecommendedMeal> objects, LayoutInflater inflater) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.listedRecommendedMeals = objects;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = inflater.inflate(resource, parent, false);
        ListedRecommendedMeal listedRecommendedMeal = listedRecommendedMeals.get(position);
        if (listedRecommendedMeal != null) {
            addName(view, listedRecommendedMeal.getRecipe().getName());
            addCalories(view,listedRecommendedMeal.getCalories());
            addMealOfTheDay(view, listedRecommendedMeal.getRecipe().getCategory());
            addCalories(view, listedRecommendedMeal.getCalories());
            addRecommendedDateToEat(view, listedRecommendedMeal.getRecommendedDateToEat());
        }
        return view;
    }

    private void addName(View view, String name) {
        TextView textView = view.findViewById(R.id.lv_recommended_meal_row_tv_name);
        addTextViewContent(textView,name);
    }

    private void addCalories(View view, Double calories) {
        TextView textView = view.findViewById(R.id.lv_recommended_meal_row_tv_calories);
        addTextViewContent(textView,Double.toString(calories)+" kcal");
    }

    private void addMealOfTheDay(View view, String mealOfTheDay) {
        TextView textView = view.findViewById(R.id.lv_recommended_meal_row_tv_meal_of_the_day);
        addTextViewContent(textView,mealOfTheDay);
    }

    private void addRecommendedDateToEat(View view, Date recommendedDateToEat) {
        TextView textView = view.findViewById(R.id.lv_recommended_meal_row_tv_recommended_date_to_eat);
        addTextViewContent(textView, DateConverter.fromDate(recommendedDateToEat));
    }

    private void addTextViewContent(TextView textView, String value) {
        if (value != null && !value.isEmpty()) {
            textView.setText(value);
        } else {
            textView.setText(R.string.lv_expense_row_default_value);
        }
    }
}
