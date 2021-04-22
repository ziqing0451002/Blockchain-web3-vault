package com.example.Blockchain.CommentModel;

import org.apache.tomcat.util.security.ConcurrentMessageDigest;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.stereotype.Component;

@Component
public class Encoder_MD5 {

    public String encodeMD5(String userPWD){
        return MD5Encoder.encode(ConcurrentMessageDigest.digestMD5(userPWD.getBytes()));
    }




}

