package i5b5.daniel.serszen.pz.model.mybatis.dto;

public class Car {
    private long id;
    private String brand;
    private String model;
    private String productionStart;
    private String productionEnd;

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

    public String getProductionStart() {
        return productionStart;
    }

    public void setProductionStart(String productionStart) {
        this.productionStart = productionStart;
    }

    public String getProductionEnd() {
        return productionEnd;
    }

    public void setProductionEnd(String productionEnd) {
        this.productionEnd = productionEnd;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
