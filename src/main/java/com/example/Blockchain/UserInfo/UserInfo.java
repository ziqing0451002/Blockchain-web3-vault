package com.example.Blockchain.UserInfo;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
//    @Column(name = "id")
//    private Long id;

    private String userAccount;
    private String userPassword;
    private String userAddress;

    public UserInfo(){

    }
    public UserInfo(String userAccount,String userPassword,String userAddress){
        this.userAccount = userAccount;
        this.userPassword = userPassword;
        this.userAddress = userAddress;
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

    @Override
    public String toString(){
        return "UserInfo{" +
                "userAccount=" + userAccount + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userAddress='" + userAddress + '\'' +
                '}';
    }
}
