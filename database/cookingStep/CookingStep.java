package licenta.beatyourmeal.database.cookingStep;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import licenta.beatyourmeal.database.recipe.Recipe;


@Entity(tableName = "cooking_step",foreignKeys = {@ForeignKey(entity = Recipe.class,
parentColumns = "id",
childColumns = "id_recipe",
onDelete = ForeignKey.CASCADE)})
public class CookingStep implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name="step_number")
    private int stepNumber;
    @ColumnInfo(name = "cooking_indication")
    private String cookingIndication;
    @ColumnInfo(name="id_recipe")
    private long idRecipe;

    public CookingStep(long id, int stepNumber, String cookingIndication, long idRecipe) {
        this.id = id;
        this.stepNumber = stepNumber;
        this.cookingIndication = cookingIndication;
        this.idRecipe = idRecipe;
    }

    @Ignore
    public CookingStep(int stepNumber, String cookingIndication, long idRecipe) {
        this.stepNumber = stepNumber;
        this.cookingIndication = cookingIndication;
        this.idRecipe = idRecipe;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    public String getCookingIndication() {
        return cookingIndication;
    }

    public void setCookingIndication(String cookingIndication) {
        this.cookingIndication = cookingIndication;
    }

    public long getIdRecipe() {
        return idRecipe;
    }

    public void setIdRecipe(long idRecipe) {
        this.idRecipe = idRecipe;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CookingStep{");
        sb.append("id=").append(id);
        sb.append(", step=").append(stepNumber);
        sb.append(", name='").append(cookingIndication).append('\'');
        sb.append(", idRecipe=").append(idRecipe);
        sb.append('}');
        return sb.toString();
    }
}
