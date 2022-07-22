package licenta.beatyourmeal.auxiliary.listed;

import java.util.Date;

import licenta.beatyourmeal.database.recipe.Recipe;

public class ListedRecommendedMeal {
    private Recipe recipe;
    private Double calories;
    private Date recommendedDateToEat;

    public ListedRecommendedMeal(Recipe recipe, Double calories, Date recommendedDateToEat) {
        this.recipe = recipe;
        this.calories = calories;
        this.recommendedDateToEat = recommendedDateToEat;
    }

    public ListedRecommendedMeal() {
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Double getCalories() {
        return calories;
    }

    public void setCalories(Double calories) {
        this.calories = calories;
    }

    public Date getRecommendedDateToEat() {
        return recommendedDateToEat;
    }

    public void setRecommendedDateToEat(Date recommendedDateToEat) {
        this.recommendedDateToEat = recommendedDateToEat;
    }
}
