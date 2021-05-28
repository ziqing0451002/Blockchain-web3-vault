package com.example.Blockchain.Security;

import com.example.Blockchain.Security.models.AuthenticationRequest;
import com.example.Blockchain.Security.models.AuthenticationResponse;
import com.example.Blockchain.Security.service.MyUserDetaillsService;
import com.example.Blockchain.Security.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.example.Blockchain.UserInfo.UserInfoService;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class HelloResource {

    @Autowired
    private MyUserDetaillsService userDetaillsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private JwtUtil jwtTokenUtil;

//    @RequestMapping({"/hello"})
//    public String hello(){
//        return "Hello world";
//    }
//
//    @RequestMapping({"/getNum"})
//    public String getNum(){
//        return "1";
//    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{
        System.out.println("可以呼叫");

        boolean isLogin = userInfoService.userLogin(authenticationRequest.getUsername(),authenticationRequest.getPassword());
        System.out.println(isLogin);
//        if (isLogin){
//            try {
//                authenticationManager.authenticate(
//                        new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),authenticationRequest.getPassword())
//                );
//            }catch (BadCredentialsException e){
//                throw new Exception("帳號密碼錯誤",e);
//            }
//        }else {
//            throw new Exception("帳號密碼錯誤");
//        }
        if (isLogin){
            System.out.println("帳號驗證成功，即將產生JWT");

            final UserDetails userDetails = userDetaillsService.loadUserByUsername(authenticationRequest.getUsername());

            final String jwt = jwtTokenUtil.generateToken(userDetails);

            return ResponseEntity.ok(new AuthenticationResponse(jwt));
        }else {
            throw new Exception("帳號密碼錯誤");
        }


    }



}
