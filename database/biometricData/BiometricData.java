package licenta.beatyourmeal.database.biometricData;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

import licenta.beatyourmeal.database.user.User;


@Entity(tableName = "biometric_data",foreignKeys = {@ForeignKey(entity = User.class,
parentColumns = "id",
childColumns = "id_user",
onDelete = ForeignKey.CASCADE)})
public class BiometricData implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "sex")
    private String sex;
    @ColumnInfo(name = "height")
    private Double height;
    @ColumnInfo(name = "weight")
    private Double weight;
    @ColumnInfo(name = "birth_date")
    private Date birthDate;
    @ColumnInfo(name="id_user")
    private long idUser;

    public BiometricData(long id, String sex, Double height, Double weight, Date birthDate, long idUser) {
        this.id = id;
        this.sex = sex;
        this.height = height;
        this.weight = weight;
        this.birthDate = birthDate;
        this.idUser = idUser;
    }

    @Ignore
    public BiometricData(String sex, Double height, Double weight, Date birthDate, long idUser) {
        this.sex = sex;
        this.height = height;
        this.weight = weight;
        this.birthDate = birthDate;
        this.idUser = idUser;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BiometricData{");
        sb.append("id=").append(id);
        sb.append(", sex='").append(sex).append('\'');
        sb.append(", height=").append(height);
        sb.append(", weight=").append(weight);
        sb.append(", birthDate=").append(birthDate);
        sb.append(", idUser=").append(idUser);
        sb.append('}');
        return sb.toString();
    }
}
