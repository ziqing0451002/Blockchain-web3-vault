package com.example.Blockchain.Certificate.TVCertificate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

// 宣告為實體(@Entity)
@Entity
// 對應資料庫表名稱
@Table(name = "tv_certificate_list")
public class TVCertificate {
    @Id
    private String certId;
    private String certName;
    private String gettingTime;
    private String agenceFrom;
    private String content;
    private String certAddress;
    private Timestamp createdTime;
    private Timestamp updatedTime;

    public TVCertificate(String certId, String certName, String gettingTime, String agenceFrom, String content, String certAddress, Timestamp updatedTime) {
        this.certId = certId;
        this.certName = certName;
        this.gettingTime = gettingTime;
        this.agenceFrom = agenceFrom;
        this.content = content;
        this.certAddress = certAddress;
        this.updatedTime = updatedTime;
    }

    public TVCertificate(){

    }

    public String getCertId() {
        return certId;
    }

    public void setCertId(String certId) {
        this.certId = certId;
    }

    public String getCertName() {
        return certName;
    }

    public void setCertName(String certName) {
        this.certName = certName;
    }

    public String getGettingTime() {
        return gettingTime;
    }

    public void setGettingTime(String gettingTime) {
        this.gettingTime = gettingTime;
    }

    public String getAgenceFrom() {
        return agenceFrom;
    }

    public void setAgenceFrom(String agenceFrom) {
        this.agenceFrom = agenceFrom;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCertAddress() {
        return certAddress;
    }

    public void setCertAddress(String certAddress) {
        this.certAddress = certAddress;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public Timestamp getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Timestamp updatedTime) {
        this.updatedTime = updatedTime;
    }
}
