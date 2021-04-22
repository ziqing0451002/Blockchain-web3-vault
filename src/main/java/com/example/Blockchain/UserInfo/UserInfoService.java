package com.example.Blockchain.UserInfo;

import com.example.Blockchain.CommentModel.Encoder_MD5;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthGetBalance;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class UserInfoService {

    private final UserInfoRepository userInfoRepository;
    private final Encoder_MD5 encoder_md5;
    private final Web3j web3j;


    @Autowired
    public UserInfoService(UserInfoRepository userInfoRepository, Encoder_MD5 encoder_md5, Web3j web3j) {
        this.userInfoRepository = userInfoRepository;
        this.encoder_md5 = encoder_md5;
        this.web3j = web3j;
    }

    public List<UserInfo> getUserInfo(){
        return userInfoRepository.findAll();
    }


    public void addUser(UserInfo userInfo){
        boolean exists = userInfoRepository.existsById(userInfo.getUserAccount());
        if (exists){
            throw new IllegalStateException("userAccount:" + userInfo.getUserAccount() + "已被使用");
        }else {
            //MD5加密
            userInfo.setUserPassword(encoder_md5.encodeMD5(userInfo.getUserPassword()));
            userInfoRepository.save(userInfo);

//            EthGetBalance result = web3j.ethGetBalance("0x6e61a11b9a8c9a3fd1904919ef0668cbed170fd7",DefaultBlockParameter.valueOf(s)).send();


        }

    }

    public void deletUser(String  userAccount,String userPassword){
        UserInfo userInfo = userInfoRepository.findById(userAccount).orElseThrow(
                () -> new IllegalStateException("userAccount:" + userAccount + "不存在")
        );

//        if ( !Objects.equals(userInfo.getUserPassword(),encoder_md5.encodeMD5(userPassword))){
//            throw new IllegalStateException("密碼錯誤");
//
//        }else{
//            userInfoRepository.deleteById(userAccount);
//        }
        userInfoRepository.deleteById(userAccount);

    }

    @Transactional
    public void updatePassword(String userAccount,String originalUserPassword,String newUserPassword){
        UserInfo userInfo = userInfoRepository.findById(userAccount).orElseThrow(
                () -> new IllegalStateException("userAccount:" + userAccount + "不存在")
        );

        //新密碼不為空，不與先前密碼相同
        if ( !Objects.equals(userInfo.getUserPassword(),originalUserPassword)){
            throw new IllegalStateException("密碼錯誤");
        }else if (newUserPassword == null || newUserPassword.length() <= 0){
            throw new IllegalStateException("新密碼不得為空");
        }else if (Objects.equals(userInfo.getUserPassword(),newUserPassword)){
            throw new IllegalStateException("新密碼不得與舊密碼相同");
        }else {
            userInfo.setUserPassword(newUserPassword);

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
            throw new IllegalStateException("暱稱不得為空");
        }else{
            userInfo.setUserAddress(userAddress);

        }
    }


}
