package licenta.beatyourmeal.auxiliary.listed;

import java.io.Serializable;

import licenta.beatyourmeal.database.aliment.Aliment;

public class ListedPreference implements Serializable {

    private Aliment aliment;
    private String isPreferred;

    public ListedPreference(Aliment aliment, String isPreferred) {
        this.aliment = aliment;
        this.isPreferred = isPreferred;
    }

    public ListedPreference() {
    }

    public ListedPreference(Aliment aliment) {
        this.aliment = aliment;
    }

    public Aliment getAliment() {
        return aliment;
    }

    public void setAliment(Aliment aliment) {
        this.aliment = aliment;
    }

    public String getIsPreferred() {
        return isPreferred;
    }

    public void setIsPreferred(String isPreferred) {
        this.isPreferred = isPreferred;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PreferredAliment{");
        sb.append("aliment=").append(aliment);
        sb.append(", isPreffered='").append(isPreferred).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
