package com.example.Blockchain.UserInfo;


import java.sql.Timestamp;


import javax.persistence.*;

// 宣告為實體(@Entity)
@Entity
// 對應資料庫表名稱
@Table(name = "user_info")
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

//    private Integer rowNum;
    private String userAccount;
    private String userPassword;
    private String userAddress;
    private String userName;
    private String userEmail;
//    private String userPk;
    private String serviceName;
    private String agenciesName;
    private String status;
    private Timestamp createdTime;
    private Timestamp updatedTime;



    public UserInfo(){

    }

    public UserInfo(String userAccount, String userPassword, String userAddress, String userName, String userEmail, String serviceName, String agenciesName, String status, Timestamp createdTime, Timestamp updatedTime){
//        this.rowNum = rowNum;
        this.userAccount = userAccount;
        this.userPassword = userPassword;
        this.userAddress = userAddress;
        this.userName = userName;
        this.userEmail = userEmail;
//        this.userPk = userPk;
        this.serviceName = serviceName;
        this.agenciesName = agenciesName;
        this.status = status;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }


    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getAgenciesName() {
        return agenciesName;
    }

    public void setAgenciesName(String agenciesName) {
        this.agenciesName = agenciesName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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
//                "userAccount=" + userAccount + '\'' +
//                ", userPassword='" + userPassword + '\'' +
//                ", userAddress='" + userAddress + '\'' +
//                '}';
//    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
