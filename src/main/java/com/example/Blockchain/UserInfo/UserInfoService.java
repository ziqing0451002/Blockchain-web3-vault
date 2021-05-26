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

    public UserInfo getUserInfoByConnectAccount(String connectAccount){
        return userInfoRepository.findUserInfoByConnectAccount(connectAccount);
    }


    public void addUser(UserInfo userInfo) throws CipherException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, IOException {
        System.out.println("getconnectAccount: " + userInfo.getconnectAccount());
        boolean exists = userInfoRepository.existsById(userInfo.getconnectAccount());
        if (exists){
            throw new IllegalStateException("connectAccount:" + userInfo.getconnectAccount() + "已被使用");
        }else {
            //紀錄創建時間以及初始化最終修改時間
            Long datetime = System.currentTimeMillis();
            Timestamp timestamp = new Timestamp(datetime);
            userInfo.setCreatedTime(timestamp);
            userInfo.setUpdatedTime(timestamp);
            //MD5加密
            userInfo.setconnectPassword(encoder_md5.encodeMD5(userInfo.getconnectPassword()));
            //取得區塊鏈錢包
            List<String> walletAddress = nodeService.createAccount(userInfo.getconnectPassword());
//            檢查回傳之Address型態是正確，再完成addUser
            if (walletAddress.get(0).length() == 42){  // 0x + address(length=40)
                userInfo.setwalletAddress(walletAddress.get(0));
//                userInfo.setUserPk(walletAddress.get(1));
                userInfoRepository.save(userInfo);
            }else {
                throw new IllegalStateException("區塊鏈錢包建立失敗，回傳之錯誤訊息為:" + walletAddress);
            }

        }

    }

    public boolean userLogin(String  connectAccount, String  connectPassword){
        UserInfo userInfo = userInfoRepository.findById(connectAccount).orElseThrow(
                () -> new IllegalStateException("connectAccount:" + connectAccount + "不存在")
        );
        if (  !Objects.equals(userInfo.getconnectPassword(),encoder_md5.encodeMD5(connectPassword))){
            throw new IllegalStateException("密碼錯誤");
        }else{
            return true;
        }
    }

    public boolean deleteUser(String  connectAccount,String connectPassword) throws IOException {
        UserInfo userInfo = userInfoRepository.findById(connectAccount).orElseThrow(
                () -> new IllegalStateException("connectAccount:" + connectAccount + "不存在")
        );

        if ( !Objects.equals(userInfo.getconnectPassword(),encoder_md5.encodeMD5(connectPassword))){
            throw new IllegalStateException("密碼錯誤");

        }else{
            vaultService.deleteSecret(userInfo.getwalletAddress());
            userInfoRepository.deleteById(connectAccount);
            return true;
        }
    }

    @Transactional
    public void updatePassword(String connectAccount,String originalconnectPassword,String newconnectPassword){
        UserInfo userInfo = userInfoRepository.findById(connectAccount).orElseThrow(
                () -> new IllegalStateException("connectAccount:" + connectAccount + "不存在")
        );

        //新密碼不為空，不與先前密碼相同
        if ( !Objects.equals(userInfo.getconnectPassword(),encoder_md5.encodeMD5(originalconnectPassword))){
            throw new IllegalStateException("密碼錯誤");
        }else if (newconnectPassword == null || newconnectPassword.length() <= 0){
            throw new IllegalStateException("新密碼不得為空");
        }else if (Objects.equals(userInfo.getconnectPassword(),newconnectPassword)){
            throw new IllegalStateException("新密碼不得與舊密碼相同");
        }else {
            userInfo.setconnectPassword(encoder_md5.encodeMD5(newconnectPassword));
            //紀錄修改時間
            Long datetime = System.currentTimeMillis();
            Timestamp timestamp = new Timestamp(datetime);
            userInfo.setUpdatedTime(timestamp);
        }
//        if (Objects.equals(userInfo.getconnectPassword(),originalconnectPassword)
//                && newconnectPassword != null
//                && newconnectPassword.length() > 0
//                && !Objects.equals(userInfo.getconnectPassword(),newconnectPassword)){
//            userInfo.setconnectPassword(newconnectPassword);
//        }
    }
    @Transactional
    public void updateAddress(String connectAccount,String walletAddress){
        UserInfo userInfo = userInfoRepository.findById(connectAccount).orElseThrow(
                () -> new IllegalStateException("connectAccount:" + connectAccount + "不存在")
        );

        if (walletAddress == null || walletAddress.length() <= 0){
            throw new IllegalStateException("Address不得為空");
        }else{
            userInfo.setwalletAddress(walletAddress);

        }
    }

    //個人資料update，沒修改就用原本的(serviceName,orgName,status,.....)
    @Transactional
    public void updateInfo(String connectAccount,String managerName,String managerEmail, String serviceName,String orgName,boolean status,String remark){
        UserInfo userInfo = userInfoRepository.findById(connectAccount).orElseThrow(
                () -> new IllegalStateException("connectAccount:" + connectAccount + "不存在")
        );

        if (managerName == null || managerName.length() <= 0){
            throw new IllegalStateException("管理者不得為空");
        }else if(managerEmail == null || managerEmail.length() <= 0){
            throw new IllegalStateException("信箱不得為空");
        }else if(serviceName == null || serviceName.length() <= 0){
            throw new IllegalStateException("服務名稱不得為空");
        }else if(orgName == null || orgName.length() <= 0){
            throw new IllegalStateException("機構名稱不得為空");
        }else{
            userInfo.setmanagerName(managerName);
            userInfo.setmanagerEmail(managerEmail);
            userInfo.setServiceName(serviceName);
            userInfo.setorgName(orgName);
            userInfo.setStatus(status);
            userInfo.setRemark(remark);
            //紀錄修改時間
            Long datetime = System.currentTimeMillis();
            Timestamp timestamp = new Timestamp(datetime);
            userInfo.setUpdatedTime(timestamp);

        }
    }


    @Transactional
    public void updateInfo(String connectAccount,String managerName,String managerEmail, String serviceName,String orgName,boolean status,String remark,String connectPassword){
        UserInfo userInfo = userInfoRepository.findById(connectAccount).orElseThrow(
                () -> new IllegalStateException("connectAccount:" + connectAccount + "不存在")
        );

        if (managerName == null || managerName.length() <= 0){
            throw new IllegalStateException("管理者不得為空");
        }else if(managerEmail == null || managerEmail.length() <= 0){
            throw new IllegalStateException("信箱不得為空");
        }else if(connectPassword == null || connectPassword.length() <= 0){
            throw new IllegalStateException("密碼不得為空");
        }else if(serviceName == null || serviceName.length() <= 0){
            throw new IllegalStateException("服務名稱不得為空");
        }else if(orgName == null || orgName.length() <= 0){
            throw new IllegalStateException("機構名稱不得為空");
        }else if(status != true || status != false){
            throw new IllegalStateException("狀態");
        }else{
            userInfo.setServiceName(serviceName);
            userInfo.setorgName(orgName);
            userInfo.setStatus(status);
            userInfo.setRemark(remark);
            userInfo.setconnectPassword(encoder_md5.encodeMD5(connectPassword));
            //紀錄修改時間
            Long datetime = System.currentTimeMillis();
            Timestamp timestamp = new Timestamp(datetime);
            userInfo.setUpdatedTime(timestamp);

        }
    }

    @Transactional
    public String exchangeAddress(String connectAccount,String connectPassword){
        UserInfo userInfo = userInfoRepository.findById(connectAccount).orElseThrow(
                () -> new IllegalStateException("connectAccount:" + connectAccount + "不存在")
        );

        if ( !Objects.equals(userInfo.getconnectPassword(),encoder_md5.encodeMD5(connectPassword))){
            throw new IllegalStateException("密碼錯誤");

        }else{
            return userInfo.getwalletAddress();
        }
    }


}
