package com.example.Blockchain.UserInfo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/UserInfo")
public class UserInfoController {

    private final UserInfoService userInfoService;

    @Autowired
    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @GetMapping
    public List<UserInfo> getUserInfo(){
        return userInfoService.getUserInfo();
    }

    @PostMapping
    public void registerNewUser(@RequestBody UserInfo userInfo){
        userInfoService.addUser(userInfo);
    }

    @DeleteMapping(path = "{userAccount}")
    public void deletUser(@PathVariable("userAccount") String userAccount,
                          @RequestParam String userPassword
    ){
        userInfoService.deletUser(userAccount,userPassword);
    }


    @PutMapping(path = "/updatePassword/{userAccount}")
    public void updatePassword(@PathVariable("userAccount") String userAccount,
                                @RequestParam String originalUserPassword,
                                @RequestParam String newUserPassword
    ){
        userInfoService.updatePassword(userAccount,originalUserPassword,newUserPassword);

    }

//    @PutMapping(path = "/updateAddress/{userAccount}")
//    public void updateName(@PathVariable("userAccount") String userAccount,
//                            @RequestParam String userAddress
//    ){
//        userInfoService.updateAddress(userAccount,userAddress);
//
//    }
}
