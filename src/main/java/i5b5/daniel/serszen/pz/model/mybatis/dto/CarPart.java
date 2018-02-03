package i5b5.daniel.serszen.pz.model.mybatis.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CarPart {
    private long id;
    @XmlElement(name = "Name", required = true)
    private String name;
    @XmlElement(name = "Producer", required = true)
    private String producer;
    @XmlElement(name = "ProducerModelCode")
    private String producerModelCode;
    @XmlElement(name = "Category", required = true)
    private String category;
    @XmlElement(name = "ImgUri")
    private String imgUri;

    public CarPart() {
    }

    public CarPart(String name, String producer, String producerModelCode, String category, String imgUri) {
        this.name = name;
        this.producer = producer;
        this.producerModelCode = producerModelCode;
        this.category = category;
        this.imgUri = imgUri;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getProducerModelCode() {
        return producerModelCode;
    }

    public void setProducerModelCode(String producerModelCode) {
        this.producerModelCode = producerModelCode;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
        if(!(obj instanceof CarPart)){
            return false;
        }
        CarPart other = (CarPart) obj;
        if(!name.equals(other.getName())){
            return false;
        }
        if(!producer.equals(other.getProducer())){
            return false;
        }
        if(!category.equals(other.getCategory())){
            return false;
        }
        return true;
    }
}
