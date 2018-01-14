package i5b5.daniel.serszen.pz.model.mybatis.dto;

public class Car {
    private long id;
    private String brand;
    private String model;
    private String productionStart;
    private String productionEnd;
    private String imgUri;

    public Car() {
    }

    public Car(String brand, String model, String productionStart, String productionEnd, String imgUri) {
        this.brand = brand;
        this.model = model;
        this.productionStart = productionStart;
        this.productionEnd = productionEnd;
        this.imgUri = imgUri;
    }

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

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }
}
