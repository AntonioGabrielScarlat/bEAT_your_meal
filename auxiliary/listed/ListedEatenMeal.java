package licenta.beatyourmeal.auxiliary.listed;

import java.io.Serializable;

public class ListedEatenMeal implements Serializable {

    private String name;
    private Double calories;
    private String mealOfTheDay;

    public ListedEatenMeal() {

    }

    public ListedEatenMeal(String name, Double calories, String mealOfTheDay) {
        this.name = name;
        this.calories = calories;
        this.mealOfTheDay = mealOfTheDay;
    }

    public ListedEatenMeal(String mealOfTheDay) {
        this.mealOfTheDay = mealOfTheDay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCalories() {
        return calories;
    }

    public void setCalories(Double calories) {
        this.calories = calories;
    }

    public String getMealOfTheDay() {
        return mealOfTheDay;
    }

    public void setMealOfTheDay(String mealOfTheDay) {
        this.mealOfTheDay = mealOfTheDay;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EatenMeal{");
        sb.append("name='").append(name).append('\'');
        sb.append(", calories=").append(calories);
        sb.append(", mealOfTheDay='").append(mealOfTheDay).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
