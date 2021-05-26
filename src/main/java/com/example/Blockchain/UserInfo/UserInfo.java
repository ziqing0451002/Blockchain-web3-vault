package com.example.Blockchain.UserInfo;


import java.sql.Timestamp;


import javax.persistence.*;

// 宣告為實體(@Entity)
@Entity
// 對應資料庫表名稱
@Table(name = "connect_info")
public class UserInfo {
    @Id
    //產生序列
//    @SequenceGenerator(
//            name = "userInfo_sequence",
//            sequenceName = "userInfo_sequence",
//            allocationSize = 1
//    )
//    @GeneratedValue(
//            strategy = GenerationType.SEQUENCE,
//            generator = "userInfo_sequence"
//    )

    // 可再新增編自動編號功能(或在SQL中做)
    // 主鍵由數據庫自動維護(AUTO_INCREMENT)
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "rowNum")
//
//    private Integer rowNum;
    private String connectAccount;
    private String connectPassword;
    private String walletAddress;
    private String managerName;
    private String managerEmail;
    private String remark;
//    private String userPk;
    private String serviceName;
    private String orgName;
    private boolean status;
    private Timestamp createdTime;
    private Timestamp updatedTime;



    public UserInfo(){

    }

    public UserInfo(String connectAccount, String connectPassword, String walletAddress, String managerName, String managerEmail, String remark, String serviceName, String orgName, boolean status, Timestamp createdTime, Timestamp updatedTime){
//        this.rowNum = rowNum;
        this.connectAccount = connectAccount;
        this.connectPassword = connectPassword;
        this.walletAddress = walletAddress;
        this.managerName = managerName;
        this.managerEmail = managerEmail;
        this.remark = remark;
//        this.userPk = userPk;
        this.serviceName = serviceName;
        this.orgName = orgName;
        this.status = status;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
    }

    public String getconnectAccount() {
        return connectAccount;
    }

    public void setconnectAccount(String connectAccount) {
        this.connectAccount = connectAccount;
    }

    public String getconnectPassword() {
        return connectPassword;
    }

    public void setconnectPassword(String connectPassword) {
        this.connectPassword = connectPassword;
    }

    public String getwalletAddress() {
        return walletAddress;
    }

    public void setwalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }


    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getorgName() {
        return orgName;
    }

    public void setorgName(String orgName) {
        this.orgName = orgName;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
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

//    public String getUserPk() {
//        return userPk;
//    }
//
//    public void setUserPk(String userPk) {
//        this.userPk = userPk;
//    }


//    @Override
//    public String toString(){
//        return "UserInfo{" +
//                "connectAccount=" + connectAccount + '\'' +
//                ", connectPassword='" + connectPassword + '\'' +
//                ", walletAddress='" + walletAddress + '\'' +
//                '}';
//    }

    public String getmanagerName() {
        return managerName;
    }

    public void setmanagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getmanagerEmail() {
        return managerEmail;
    }

    public void setmanagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
