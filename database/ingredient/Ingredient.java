package licenta.beatyourmeal.database.ingredient;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import licenta.beatyourmeal.database.aliment.Aliment;
import licenta.beatyourmeal.database.recipe.Recipe;


@Entity(tableName = "ingredient",foreignKeys = {
        @ForeignKey(entity = Recipe.class, parentColumns = "id", childColumns = "id_recipe", onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Aliment.class,parentColumns = "id",childColumns ="id_aliment",onDelete = ForeignKey.CASCADE)})
public class Ingredient implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "quantity")
    private Double quantity;
    @ColumnInfo(name="unit_of_measurement")
    private String unitOfMeasurement;
    @ColumnInfo(name="id_recipe")
    private long idRecipe;
    @ColumnInfo(name="id_aliment")
    private long idAliment;

    public Ingredient(long id, Double quantity, String unitOfMeasurement, long idRecipe, long idAliment) {
        this.id = id;
        this.quantity = quantity;
        this.unitOfMeasurement = unitOfMeasurement;
        this.idRecipe = idRecipe;
        this.idAliment = idAliment;
    }

    @Ignore
    public Ingredient(Double quantity, String unitOfMeasurement, long idRecipe, long idAliment) {
        this.quantity = quantity;
        this.unitOfMeasurement = unitOfMeasurement;
        this.idRecipe = idRecipe;
        this.idAliment = idAliment;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getUnitOfMeasurement() {
        return unitOfMeasurement;
    }

    public void setUnitOfMeasurement(String unitOfMeasurement) {
        this.unitOfMeasurement = unitOfMeasurement;
    }

    public long getIdRecipe() {
        return idRecipe;
    }

    public void setIdRecipe(long idRecipe) {
        this.idRecipe = idRecipe;
    }

    public long getIdAliment() {
        return idAliment;
    }

    public void setIdAliment(long idAliment) {
        this.idAliment = idAliment;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Ingredient{");
        sb.append("id=").append(id);
        sb.append(", quantity=").append(quantity);
        sb.append(", unitOfMeasurement='").append(unitOfMeasurement).append('\'');
        sb.append(", idRecipe=").append(idRecipe);
        sb.append(", idAliment=").append(idAliment);
        sb.append('}');
        return sb.toString();
    }
}
