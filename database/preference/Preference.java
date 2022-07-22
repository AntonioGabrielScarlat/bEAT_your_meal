package licenta.beatyourmeal.database.preference;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import licenta.beatyourmeal.database.aliment.Aliment;
import licenta.beatyourmeal.database.user.User;


@Entity(tableName = "preference",foreignKeys = {
        @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "id_user", onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Aliment.class,parentColumns = "id",childColumns ="id_aliment",onDelete = ForeignKey.CASCADE)})
public class Preference implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name="id_user")
    private long idUser;
    @ColumnInfo(name="id_aliment")
    private long idAliment;

    public Preference(long id, long idUser, long idAliment) {
        this.id = id;
        this.idUser = idUser;
        this.idAliment = idAliment;
    }

    @Ignore
    public Preference(long idUser, long idAliment) {
        this.idUser = idUser;
        this.idAliment = idAliment;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public long getIdAliment() {
        return idAliment;
    }

    public void setIdAliment(long idAliment) {
        this.idAliment = idAliment;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Preference{");
        sb.append("id=").append(id);
        sb.append(", idUser=").append(idUser);
        sb.append(", idAliment=").append(idAliment);
        sb.append('}');
        return sb.toString();
    }
}
