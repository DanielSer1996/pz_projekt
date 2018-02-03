package i5b5.daniel.serszen.pz.model.mybatis.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Car")
@XmlAccessorType(XmlAccessType.FIELD)
public class Car {
    private long id;
    @XmlElement(name = "Brand", required = true)
    private String brand;
    @XmlElement(name = "Model", required = true)
    private String model;
    @XmlElement(name = "ProductionStart", required = true)
    private String productionStart;
    @XmlElement(name = "ProductionEnd", required = true)
    private String productionEnd;
    @XmlElement(name = "ImgUri")
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

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if(!(obj instanceof Car)){
            return false;
        }
        Car other = (Car)obj;
        if(!this.brand.equals(other.getBrand())){
            return false;
        }
        if(!this.model.equals(other.getModel())){
            return false;
        }
        if(!this.productionStart.equals(other.getProductionStart())){
            return false;
        }
        if(!this.productionEnd.equals(other.getProductionEnd())){
            return false;
        }

        return true;
    }
}
