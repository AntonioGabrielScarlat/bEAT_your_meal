package licenta.beatyourmeal.database.nutritionalData;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import licenta.beatyourmeal.database.recipe.Recipe;


@Entity(tableName = "nutritional_data",foreignKeys = {@ForeignKey(entity = Recipe.class,
parentColumns = "id",
childColumns = "id_recipe",
onDelete = ForeignKey.CASCADE)})
public class NutritionalData implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "calories")
    private Double calories;
    @ColumnInfo(name = "carbs")
    private Double carbs;
    @ColumnInfo(name = "proteins")
    private Double proteins;
    @ColumnInfo(name = "fat")
    private Double fat;
    @ColumnInfo(name="id_recipe")
    private long idRecipe;

    public NutritionalData(long id, Double calories, Double carbs, Double proteins, Double fat, long idRecipe) {
        this.id = id;
        this.calories = calories;
        this.carbs = carbs;
        this.proteins = proteins;
        this.fat = fat;
        this.idRecipe = idRecipe;
    }

    @Ignore
    public NutritionalData(Double calories, Double carbs, Double proteins, Double fat, long idRecipe) {
        this.calories = calories;
        this.carbs = carbs;
        this.proteins = proteins;
        this.fat = fat;
        this.idRecipe = idRecipe;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Double getCalories() {
        return calories;
    }

    public void setCalories(Double calories) {
        this.calories = calories;
    }

    public Double getCarbs() {
        return carbs;
    }

    public void setCarbs(Double carbs) {
        this.carbs = carbs;
    }

    public Double getProteins() {
        return proteins;
    }

    public void setProteins(Double proteins) {
        this.proteins = proteins;
    }

    public Double getFat() {
        return fat;
    }

    public void setFat(Double fat) {
        this.fat = fat;
    }

    public long getIdRecipe() {
        return idRecipe;
    }

    public void setIdRecipe(long idRecipe) {
        this.idRecipe = idRecipe;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("NutritionalData{");
        sb.append("id=").append(id);
        sb.append(", calories=").append(calories);
        sb.append(", carbs=").append(carbs);
        sb.append(", proteins=").append(proteins);
        sb.append(", fat=").append(fat);
        sb.append(", idRecipe=").append(idRecipe);
        sb.append('}');
        return sb.toString();
    }
}
