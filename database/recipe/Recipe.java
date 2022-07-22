package licenta.beatyourmeal.database.recipe;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "recipe")
public class Recipe implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "category")
    private String category;
    @ColumnInfo(name = "cooking_time")
    private Double cookingTime;

    public Recipe(long id, String name, String category, Double cookingTime) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.cookingTime = cookingTime;
    }

    @Ignore
    public Recipe(String name, String category, Double cookingTime) {
        this.name = name;
        this.category = category;
        this.cookingTime = cookingTime;
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

    public Double getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(Double cookingTime) {
        this.cookingTime = cookingTime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Recipe{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", category='").append(category).append('\'');
        sb.append(", cookingTime=").append(cookingTime);
        sb.append('}');
        return sb.toString();
    }
}
