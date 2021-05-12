package com.example.Blockchain.UserInfo;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
        return userInfoService.getUserInfo();
    }

    @ApiOperation("新增連線帳號")
    @PostMapping(path = "createAccount")
    public void registerNewUser(@RequestBody UserInfo userInfo) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, CipherException, IOException {
        userInfoService.addUser(userInfo);
    }

    @ApiOperation("刪除連線帳號")
    @DeleteMapping(path = "deleteAccount/{userAccount}")
    public void deleteUser(@PathVariable("userAccount") String userAccount,
                          @RequestParam String userPassword
    ) throws IOException {
        userInfoService.deleteUser(userAccount,userPassword);
    }

    @ApiOperation("修改連線帳號密碼")
    @PutMapping(path = "/updatePassword/{userAccount}")
    public void updatePassword(@PathVariable("userAccount") String userAccount,
                                @RequestParam String originalUserPassword,
                                @RequestParam String newUserPassword
    ){
        userInfoService.updatePassword(userAccount,originalUserPassword,newUserPassword);

    }

    @ApiOperation("修改連線帳號資訊")
    @PutMapping(path = "/updateAccountInfo/{userAccount}")
    public void updateInfo(@PathVariable("userAccount") String userAccount,
                           @RequestParam String userName,
                           @RequestParam String userEmail,
                           @RequestParam String serviceName,
                           @RequestParam String agenciesName,
                           @RequestParam String status,
                           @RequestParam String remark
    ){
        userInfoService.updateInfo(userAccount,userName,userEmail,serviceName,agenciesName,status,remark);

    }

    @GetMapping(path = "userLogin/{userAccount}")
    public boolean userLogin(@PathVariable("userAccount") String userAccount,
                             @RequestParam String userPassword
    ){
        return userInfoService.userLogin(userAccount,userPassword);
    }
}
