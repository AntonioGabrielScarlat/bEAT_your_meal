package licenta.beatyourmeal.database.aliment;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;


@Entity(tableName = "aliment")
public class Aliment implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "category")
    private String category;
    @ColumnInfo(name = "nutritional_value")
    private Double calories;


    public Aliment(long id, String name, String category, Double calories) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.calories = calories;
    }

    @Ignore //specifica Room-ului ca NU trebuie sa foloseasca acest constructor
    public Aliment(String name, String category, Double calories) {
        this.name = name;
        this.category = category;
        this.calories = calories;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getCalories() {
        return calories;
    }

    public void setCalories(Double calories) {
        this.calories = calories;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Aliment{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", category='").append(category).append('\'');
        sb.append(", nutritionalValue=").append(calories);
        sb.append('}');
        return sb.toString();
    }
}
