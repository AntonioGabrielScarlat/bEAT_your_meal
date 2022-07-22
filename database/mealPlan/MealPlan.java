package licenta.beatyourmeal.database.mealPlan;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

import licenta.beatyourmeal.database.user.User;


@Entity(tableName = "meal_plan",foreignKeys = {@ForeignKey(entity = User.class,
parentColumns = "id",
childColumns = "id_user",
onDelete = ForeignKey.CASCADE)})
public class MealPlan implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name="start_date")
    private Date startDate;
    @ColumnInfo(name="id_user")
    private long idUser;

    public MealPlan(long id, Date startDate, long idUser) {
        this.id = id;
        this.startDate = startDate;
        this.idUser = idUser;
    }

    @Ignore
    public MealPlan(Date startDate, long idUser) {
        this.startDate = startDate;
        this.idUser = idUser;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MealPlan{");
        sb.append("id=").append(id);
        sb.append(", startDate=").append(startDate);
        sb.append(", idUser=").append(idUser);
        sb.append('}');
        return sb.toString();
    }
}
