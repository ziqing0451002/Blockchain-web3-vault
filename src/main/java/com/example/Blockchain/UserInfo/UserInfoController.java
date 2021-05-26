package com.example.Blockchain.UserInfo;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.web3j.crypto.CipherException;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@Api(tags = "連線帳號 相關api")
@RestController
@RequestMapping(path = "api/account")
public class UserInfoController {

    private final UserInfoService userInfoService;

    @Autowired
    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @ApiOperation("取得連線帳號列表")
    @GetMapping(path = "getAccounts")
    public List<UserInfo> getUserInfo(){
        return userInfoService.getUserInfoAscByTime();
    }

    @ApiOperation("取得單筆連線帳號")
    @GetMapping(path = "getUserInfoByConnectAccount/{connectAccount}")
    public UserInfo getUserInfoByConnectAccount(@PathVariable("connectAccount") String connectAccount){
        return userInfoService.getUserInfoByConnectAccount(connectAccount);
    }

    @ApiOperation("新增連線帳號")
    @PostMapping(path = "createAccount")
    public void registerNewUser(@RequestBody UserInfo userInfo) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, CipherException, IOException {
        userInfoService.addUser(userInfo);
    }

    @ApiOperation("刪除連線帳號")
    @DeleteMapping(path = "deleteAccount/{connectAccount}")
    public boolean deleteUser(@PathVariable("connectAccount") String connectAccount,
                          @RequestParam String connectPassword
    ) throws IOException {
        return userInfoService.deleteUser(connectAccount,connectPassword);
    }

    @ApiOperation("修改連線帳號密碼")
    @PutMapping(path = "/updatePassword/{connectAccount}")
    public void updatePassword(@PathVariable("connectAccount") String connectAccount,
                                @RequestParam String originalConnectPassword,
                                @RequestParam String newConnectPassword
    ){
        userInfoService.updatePassword(connectAccount,originalConnectPassword,newConnectPassword);

    }

    @ApiOperation("修改連線帳號資訊")
    @PutMapping(path = "/updateAccountInfo/{connectAccount}")
    public void updateInfo(@PathVariable("connectAccount") String connectAccount,
                           @RequestParam String managerName,
                           @RequestParam String managerEmail,
                           @RequestParam String serviceName,
                           @RequestParam String orgName,
                           @RequestParam boolean status,
                           @RequestParam String remark
    ){
        userInfoService.updateInfo(connectAccount,managerName,managerEmail,serviceName,orgName,status,remark);

    }
    @PutMapping(path = "/updateAccountInfoPWD/{connectAccount}")
    public void updateInfoPWD(@PathVariable("connectAccount") String connectAccount,
                           @RequestParam String managerName,
                           @RequestParam String managerEmail,
                           @RequestParam String serviceName,
                           @RequestParam String orgName,
                           @RequestParam boolean status,
                           @RequestParam String remark,
                           @RequestParam String connectPassword
    ){
        userInfoService.updateInfo(connectAccount,managerName,managerEmail,serviceName,orgName,status,remark,connectPassword);

    }

    @GetMapping(path = "userLogin/{connectAccount}")
    public boolean userLogin(@PathVariable("connectAccount") String connectAccount,
                             @RequestParam String connectPassword
    ){
        return userInfoService.userLogin(connectAccount,connectPassword);
    }
}
