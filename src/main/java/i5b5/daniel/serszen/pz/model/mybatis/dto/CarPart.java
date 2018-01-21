package i5b5.daniel.serszen.pz.model.mybatis.dto;

public class CarPart {
    private long id;
    private String name;
    private String producer;
    private String producerModelCode;
    private String category;
    private String imgUri;

    public CarPart() {
    }

    public CarPart(String name, String producer, String producerModelCode, String category) {
        this.name = name;
        this.producer = producer;
        this.producerModelCode = producerModelCode;
        this.category = category;
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
