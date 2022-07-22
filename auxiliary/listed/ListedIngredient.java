package licenta.beatyourmeal.auxiliary.listed;

import java.io.Serializable;

public class ListedIngredient implements Serializable {

    private String name;
    private Double quantity;
    private String unitOfMeasurement;

    public ListedIngredient(String name, Double quantity, String unitOfMeasurement) {
        this.name = name;
        this.quantity = quantity;
        this.unitOfMeasurement = unitOfMeasurement;
    }

    public ListedIngredient() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ListedIngredient{");
        sb.append("name='").append(name).append('\'');
        sb.append(", quantity=").append(quantity);
        sb.append(", unitOfMeasurement='").append(unitOfMeasurement).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
