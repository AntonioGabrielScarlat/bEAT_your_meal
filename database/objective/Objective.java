package licenta.beatyourmeal.database.objective;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

import licenta.beatyourmeal.database.user.User;


@Entity(tableName = "objective",foreignKeys = {@ForeignKey(entity = User.class,
parentColumns = "id",
childColumns = "id_user",
onDelete = ForeignKey.CASCADE)})
public class Objective implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "new_weight")
    private Double newWeight;
    @ColumnInfo(name = "lost_kg_per_week")
    private Double lostKgPerWeek;
    @ColumnInfo(name="target_date")
    private Date targetDate;
    @ColumnInfo(name="id_user")
    private long idUser;

    public Objective(long id, Double newWeight, Double lostKgPerWeek, Date targetDate, long idUser) {
        this.id = id;
        this.newWeight = newWeight;
        this.lostKgPerWeek = lostKgPerWeek;
        this.targetDate = targetDate;
        this.idUser = idUser;
    }

    @Ignore
    public Objective(Double newWeight, Double lostKgPerWeek, Date targetDate, long idUser) {
        this.newWeight = newWeight;
        this.lostKgPerWeek = lostKgPerWeek;
        this.targetDate = targetDate;
        this.idUser = idUser;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Double getNewWeight() {
        return newWeight;
    }

    public void setNewWeight(Double newWeight) {
        this.newWeight = newWeight;
    }

    public Double getLostKgPerWeek() {
        return lostKgPerWeek;
    }

    public void setLostKgPerWeek(Double lostKgPerWeek) {
        this.lostKgPerWeek = lostKgPerWeek;
    }

    public Date getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(Date targetDate) {
        this.targetDate = targetDate;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Objective{");
        sb.append("id=").append(id);
        sb.append(", newWeight=").append(newWeight);
        sb.append(", lostKgPerWeek=").append(lostKgPerWeek);
        sb.append(", targetDate=").append(targetDate);
        sb.append(", idUser=").append(idUser);
        sb.append('}');
        return sb.toString();
    }
}
