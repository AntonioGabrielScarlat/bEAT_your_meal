package licenta.beatyourmeal.database.recommendedMeal;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

import licenta.beatyourmeal.database.mealPlan.MealPlan;
import licenta.beatyourmeal.database.recipe.Recipe;


@Entity(tableName = "recommended_meal",foreignKeys = {
        @ForeignKey(entity = Recipe.class, parentColumns = "id", childColumns = "id_recipe", onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = MealPlan.class,parentColumns = "id",childColumns ="id_meal_plan",onDelete = ForeignKey.CASCADE)})
public class RecommendedMeal implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name="id_recipe")
    private long idRecipe;
    @ColumnInfo(name="id_meal_plan")
    private long idMealPlan;
    @ColumnInfo(name="recommended_date_to_eat")
    private Date recommendedDateToEat;

    public RecommendedMeal(long id, long idRecipe, long idMealPlan, Date recommendedDateToEat) {
        this.id = id;
        this.idRecipe = idRecipe;
        this.idMealPlan = idMealPlan;
        this.recommendedDateToEat = recommendedDateToEat;
    }

    @Ignore
    public RecommendedMeal(long idRecipe, long idMealPlan, Date recommendedDateToEat) {
        this.idRecipe = idRecipe;
        this.idMealPlan = idMealPlan;
        this.recommendedDateToEat = recommendedDateToEat;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdRecipe() {
        return idRecipe;
    }

    public void setIdRecipe(long idRecipe) {
        this.idRecipe = idRecipe;
    }

    public long getIdMealPlan() {
        return idMealPlan;
    }

    public void setIdMealPlan(long idMealPlan) {
        this.idMealPlan = idMealPlan;
    }

    public Date getRecommendedDateToEat() {
        return recommendedDateToEat;
    }

    public void setRecommendedDateToEat(Date recommendedDateToEat) {
        this.recommendedDateToEat = recommendedDateToEat;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RecommendedMeal{");
        sb.append("id=").append(id);
        sb.append(", idRecipe=").append(idRecipe);
        sb.append(", idMealPlan=").append(idMealPlan);
        sb.append(", recommendedDateToEat=").append(recommendedDateToEat);
        sb.append('}');
        return sb.toString();
    }
}
