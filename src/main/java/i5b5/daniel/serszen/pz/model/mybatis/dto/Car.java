package i5b5.daniel.serszen.pz.model.mybatis.dto;

import java.util.Calendar;

public class Car {
    private String brand;
    private String model;
    private Calendar productionStart;
    private Calendar productionEnd;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Calendar getProductionStart() {
        return productionStart;
    }

    public void setProductionStart(Calendar productionStart) {
        this.productionStart = productionStart;
    }

    public Calendar getProductionEnd() {
        return productionEnd;
    }

    public void setProductionEnd(Calendar productionEnd) {
        this.productionEnd = productionEnd;
    }
}
