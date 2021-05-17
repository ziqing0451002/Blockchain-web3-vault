package com.example.Blockchain.UserInfo;

import com.example.Blockchain.CommentModel.Encoder_MD5;
import com.example.Blockchain.web3Info.nodeService;
import com.example.Blockchain.Vault.VaultService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.CipherException;
import org.web3j.protocol.Web3j;

import javax.transaction.Transactional;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.sql.Timestamp;
import java.util.*;

@Service
public class UserInfoService {

    private final UserInfoRepository userInfoRepository;
    private final Encoder_MD5 encoder_md5;
    private final Web3j web3j;
    private final nodeService nodeService;
    private final VaultService vaultService;


    @Autowired
    public UserInfoService(UserInfoRepository userInfoRepository, Encoder_MD5 encoder_md5, Web3j web3j, com.example.Blockchain.web3Info.nodeService nodeService, VaultService vaultService) {
        this.userInfoRepository = userInfoRepository;
        this.encoder_md5 = encoder_md5;
        this.web3j = web3j;
        this.nodeService = nodeService;
        this.vaultService = vaultService;
    }

    public List<UserInfo> getUserInfo(){
        return userInfoRepository.findAll();
    }

    public List<UserInfo> getUserInfoAscByTime(){
        return userInfoRepository.findAllByOrderByCreatedTimeAsc();
    }

    public UserInfo getUserInfoByUserAccount(String userAccount){
        return userInfoRepository.findUserInfoByUserAccount(userAccount);
    }


    public void addUser(UserInfo userInfo) throws CipherException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, IOException {
        boolean exists = userInfoRepository.existsById(userInfo.getUserAccount());
        if (exists){
            throw new IllegalStateException("userAccount:" + userInfo.getUserAccount() + "已被使用");
        }else {
            //紀錄創建時間以及初始化最終修改時間
            Long datetime = System.currentTimeMillis();
            Timestamp timestamp = new Timestamp(datetime);
            userInfo.setCreatedTime(timestamp);
            userInfo.setUpdatedTime(timestamp);
            //MD5加密
            userInfo.setUserPassword(encoder_md5.encodeMD5(userInfo.getUserPassword()));
            //取得區塊鏈錢包
            List<String> userAddress = nodeService.createAccount(userInfo.getUserPassword());
//            檢查回傳之Address型態是正確，再完成addUser
            if (userAddress.get(0).length() == 42){  // 0x + address(length=40)
                userInfo.setUserAddress(userAddress.get(0));
//                userInfo.setUserPk(userAddress.get(1));
                userInfoRepository.save(userInfo);
            }else {
                throw new IllegalStateException("區塊鏈錢包建立失敗，回傳之錯誤訊息為:" + userAddress);
            }

        }

    }

    public boolean userLogin(String  userAccount, String  userPassword){
        UserInfo userInfo = userInfoRepository.findById(userAccount).orElseThrow(
                () -> new IllegalStateException("userAccount:" + userAccount + "不存在")
        );
        if (  !Objects.equals(userInfo.getUserPassword(),encoder_md5.encodeMD5(userPassword))){
            throw new IllegalStateException("密碼錯誤");
        }else{
            return true;
        }
    }

    public boolean deleteUser(String  userAccount,String userPassword) throws IOException {
        UserInfo userInfo = userInfoRepository.findById(userAccount).orElseThrow(
                () -> new IllegalStateException("userAccount:" + userAccount + "不存在")
        );

        if ( !Objects.equals(userInfo.getUserPassword(),encoder_md5.encodeMD5(userPassword))){
            throw new IllegalStateException("密碼錯誤");

        }else{
            vaultService.deleteSecret(userInfo.getUserAddress());
            userInfoRepository.deleteById(userAccount);
            return true;
        }
    }

    @Transactional
    public void updatePassword(String userAccount,String originalUserPassword,String newUserPassword){
        UserInfo userInfo = userInfoRepository.findById(userAccount).orElseThrow(
                () -> new IllegalStateException("userAccount:" + userAccount + "不存在")
        );

        //新密碼不為空，不與先前密碼相同
        if ( !Objects.equals(userInfo.getUserPassword(),encoder_md5.encodeMD5(originalUserPassword))){
            throw new IllegalStateException("密碼錯誤");
        }else if (newUserPassword == null || newUserPassword.length() <= 0){
            throw new IllegalStateException("新密碼不得為空");
        }else if (Objects.equals(userInfo.getUserPassword(),newUserPassword)){
            throw new IllegalStateException("新密碼不得與舊密碼相同");
        }else {
            userInfo.setUserPassword(encoder_md5.encodeMD5(newUserPassword));
            //紀錄修改時間
            Long datetime = System.currentTimeMillis();
            Timestamp timestamp = new Timestamp(datetime);
            userInfo.setUpdatedTime(timestamp);
        }
//        if (Objects.equals(userInfo.getUserPassword(),originalUserPassword)
//                && newUserPassword != null
//                && newUserPassword.length() > 0
//                && !Objects.equals(userInfo.getUserPassword(),newUserPassword)){
//            userInfo.setUserPassword(newUserPassword);
//        }
    }
    @Transactional
    public void updateAddress(String userAccount,String userAddress){
        UserInfo userInfo = userInfoRepository.findById(userAccount).orElseThrow(
                () -> new IllegalStateException("userAccount:" + userAccount + "不存在")
        );

        if (userAddress == null || userAddress.length() <= 0){
            throw new IllegalStateException("Address不得為空");
        }else{
            userInfo.setUserAddress(userAddress);

        }
    }

    //個人資料update，沒修改就用原本的(serviceName,agenciesName,status,.....)
    @Transactional
    public void updateInfo(String userAccount,String userName,String userEmail, String serviceName,String agenciesName,String status,String remark){
        UserInfo userInfo = userInfoRepository.findById(userAccount).orElseThrow(
                () -> new IllegalStateException("userAccount:" + userAccount + "不存在")
        );

        if (userName == null || userName.length() <= 0){
            throw new IllegalStateException("管理者不得為空");
        }else if(userEmail == null || userEmail.length() <= 0){
            throw new IllegalStateException("信箱不得為空");
        }else if(serviceName == null || serviceName.length() <= 0){
            throw new IllegalStateException("服務名稱不得為空");
        }else if(agenciesName == null || agenciesName.length() <= 0){
            throw new IllegalStateException("機構名稱不得為空");
        }else if(status == null || status.length() <= 0){
            throw new IllegalStateException("狀態");
        }else{
            userInfo.setUserName(userName);
            userInfo.setUserEmail(userEmail);
            userInfo.setServiceName(serviceName);
            userInfo.setAgenciesName(agenciesName);
            userInfo.setStatus(status);
            userInfo.setRemark(remark);
            //紀錄修改時間
            Long datetime = System.currentTimeMillis();
            Timestamp timestamp = new Timestamp(datetime);
            userInfo.setUpdatedTime(timestamp);

        }
    }


    @Transactional
    public void updateInfo(String userAccount,String userName,String userEmail, String serviceName,String agenciesName,String status,String remark,String userPassword){
        UserInfo userInfo = userInfoRepository.findById(userAccount).orElseThrow(
                () -> new IllegalStateException("userAccount:" + userAccount + "不存在")
        );

        if (userName == null || userName.length() <= 0){
            throw new IllegalStateException("管理者不得為空");
        }else if(userEmail == null || userEmail.length() <= 0){
            throw new IllegalStateException("信箱不得為空");
        }else if(userPassword == null || userPassword.length() <= 0){
            throw new IllegalStateException("密碼不得為空");
        }else if(serviceName == null || serviceName.length() <= 0){
            throw new IllegalStateException("服務名稱不得為空");
        }else if(agenciesName == null || agenciesName.length() <= 0){
            throw new IllegalStateException("機構名稱不得為空");
        }else if(status == null || status.length() <= 0){
            throw new IllegalStateException("狀態");
        }else{
            userInfo.setServiceName(serviceName);
            userInfo.setAgenciesName(agenciesName);
            userInfo.setStatus(status);
            userInfo.setRemark(remark);
            userInfo.setUserPassword(encoder_md5.encodeMD5(userPassword));
            //紀錄修改時間
            Long datetime = System.currentTimeMillis();
            Timestamp timestamp = new Timestamp(datetime);
            userInfo.setUpdatedTime(timestamp);

        }
    }

    @Transactional
    public String exchangeAddress(String userAccount,String userPassword){
        UserInfo userInfo = userInfoRepository.findById(userAccount).orElseThrow(
                () -> new IllegalStateException("userAccount:" + userAccount + "不存在")
        );

        if ( !Objects.equals(userInfo.getUserPassword(),encoder_md5.encodeMD5(userPassword))){
            throw new IllegalStateException("密碼錯誤");

        }else{
            return userInfo.getUserAddress();
        }
    }


}
