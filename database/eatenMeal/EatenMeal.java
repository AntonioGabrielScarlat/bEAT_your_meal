package licenta.beatyourmeal.database.eatenMeal;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

import licenta.beatyourmeal.database.recipe.Recipe;
import licenta.beatyourmeal.database.user.User;


@Entity(tableName = "eaten_meal",foreignKeys = {
        @ForeignKey(entity = Recipe.class, parentColumns = "id", childColumns = "id_recipe", onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = User.class,parentColumns = "id",childColumns ="id_user",onDelete = ForeignKey.CASCADE)})
public class EatenMeal implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name="date_of_consumption")
    private Date dateOfConsumption;
    @ColumnInfo(name="meal_of_the_day")
    private String mealOfTheDay;
    @ColumnInfo(name="id_recipe")
    private long idRecipe;
    @ColumnInfo(name="id_user")
    private long idUser;

    public EatenMeal(long id, Date dateOfConsumption, String mealOfTheDay, long idRecipe, long idUser) {
        this.id = id;
        this.dateOfConsumption = dateOfConsumption;
        this.mealOfTheDay = mealOfTheDay;
        this.idRecipe = idRecipe;
        this.idUser = idUser;
    }

    @Ignore
    public EatenMeal(Date dateOfConsumption, String mealOfTheDay, long idRecipe, long idUser) {
        this.dateOfConsumption = dateOfConsumption;
        this.mealOfTheDay = mealOfTheDay;
        this.idRecipe = idRecipe;
        this.idUser = idUser;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDateOfConsumption() {
        return dateOfConsumption;
    }

    public void setDateOfConsumption(Date dateOfConsumption) {
        this.dateOfConsumption = dateOfConsumption;
    }

    public String getMealOfTheDay() {
        return mealOfTheDay;
    }

    public void setMealOfTheDay(String mealOfTheDay) {
        this.mealOfTheDay = mealOfTheDay;
    }

    public long getIdRecipe() {
        return idRecipe;
    }

    public void setIdRecipe(long idRecipe) {
        this.idRecipe = idRecipe;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Meal{");
        sb.append("id=").append(id);
        sb.append(", dateOfConsumption=").append(dateOfConsumption);
        sb.append(", mealOfTheDay='").append(mealOfTheDay).append('\'');
        sb.append(", idRecipe=").append(idRecipe);
        sb.append(", idUser=").append(idUser);
        sb.append('}');
        return sb.toString();
    }
}
