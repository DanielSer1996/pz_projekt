package i5b5.daniel.serszen.pz.controller.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimeApiResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("countryCode")
    @Expose
    private String countryCode;
    @SerializedName("countryName")
    @Expose
    private String countryName;
    @SerializedName("zoneName")
    @Expose
    private String zoneName;
    @SerializedName("abbreviation")
    @Expose
    private String abbreviation;
    @SerializedName("gmtOffset")
    @Expose
    private Integer gmtOffset;
    @SerializedName("dst")
    @Expose
    private String dst;
    @SerializedName("dstStart")
    @Expose
    private Integer dstStart;
    @SerializedName("dstEnd")
    @Expose
    private Integer dstEnd;
    @SerializedName("nextAbbreviation")
    @Expose
    private String nextAbbreviation;
    @SerializedName("timestamp")
    @Expose
    private Integer timestamp;
    @SerializedName("formatted")
    @Expose
    private String formatted;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public Integer getGmtOffset() {
        return gmtOffset;
    }

    public void setGmtOffset(Integer gmtOffset) {
        this.gmtOffset = gmtOffset;
    }

    public String getDst() {
        return dst;
    }

    public void setDst(String dst) {
        this.dst = dst;
    }

    public Integer getDstStart() {
        return dstStart;
    }

    public void setDstStart(Integer dstStart) {
        this.dstStart = dstStart;
    }

    public Integer getDstEnd() {
        return dstEnd;
    }

    public void setDstEnd(Integer dstEnd) {
        this.dstEnd = dstEnd;
    }

    public String getNextAbbreviation() {
        return nextAbbreviation;
    }

    public void setNextAbbreviation(String nextAbbreviation) {
        this.nextAbbreviation = nextAbbreviation;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public String getFormatted() {
        return formatted;
    }

    public void setFormatted(String formatted) {
        this.formatted = formatted;
    }

}