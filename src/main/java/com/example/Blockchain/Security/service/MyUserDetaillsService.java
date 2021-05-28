package com.example.Blockchain.Security.service;

import com.example.Blockchain.UserInfo.UserInfo;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.Blockchain.UserInfo.UserInfoRepository;

import java.util.ArrayList;

@Service
public class MyUserDetaillsService implements UserDetailsService {

    private final UserInfoRepository userInfoRepository;

    public MyUserDetaillsService(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String connectAccount) throws UsernameNotFoundException {
       UserInfo userInfo = userInfoRepository.findUserInfoByConnectAccount(connectAccount);
       return new User(userInfo.getconnectAccount(),userInfo.getconnectPassword(), new ArrayList<>());

    }
}
