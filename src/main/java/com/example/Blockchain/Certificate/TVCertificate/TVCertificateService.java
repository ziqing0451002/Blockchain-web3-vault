package com.example.Blockchain.Certificate.TVCertificate;
import com.example.Blockchain.CertificateUpload.CertificateUploadService;
import com.example.Blockchain.CommentModel.Encoder_MD5;
import com.example.Blockchain.UserInfo.UserInfo;
import com.example.Blockchain.UserInfo.UserInfoRepository;
import com.example.Blockchain.CertificateUpload.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.tuples.generated.Tuple5;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Service
public class TVCertificateService {

    private final TVCertificateRepository tvCertificateRepository;
    private final UserInfoRepository userInfoRepository;
    private final CertificateUploadService certificateUploadService;
    private final Encoder_MD5 encoder_md5;
    private final CertificateService certificateService;

    @Autowired
    public TVCertificateService(TVCertificateRepository tvCertificateRepository, UserInfoRepository userInfoRepository, CertificateUploadService certificateUploadService, Encoder_MD5 encoder_md5, CertificateService certificateService) {
        this.tvCertificateRepository = tvCertificateRepository;
        this.userInfoRepository = userInfoRepository;
        this.certificateUploadService = certificateUploadService;
        this.encoder_md5 = encoder_md5;
        this.certificateService = certificateService;
    }

    public List<TVCertificate> getTVCertificate() {
        return tvCertificateRepository.findAll();
    }

    public TVCertificate getTVCertificateById(String certId) {
        return tvCertificateRepository.findTVCertificateByCertId(certId);
    }

    public String  setTVCertificate(String userAccount, String userPassword, TVCertificate tvCertificate) throws Exception {
        boolean certIdExists = tvCertificateRepository.existsById(tvCertificate.getCertId());
        boolean userAccountexists = userInfoRepository.existsById(userAccount);
        if (!userAccountexists){
            throw new IllegalStateException("userAccount:" + userAccount + "不存在");
        }else {
            UserInfo userInfo = userInfoRepository.findUserInfoByUserAccount(userAccount);
            if (  !Objects.equals(userInfo.getUserPassword(),encoder_md5.encodeMD5(userPassword))){
                throw new IllegalStateException("密碼錯誤");
            }else if (certIdExists){
                throw new IllegalStateException("CertID:" + tvCertificate.getCertId() + "已被使用");
            }else {
                certificateUploadService.setCertInfo(userAccount,userPassword,tvCertificate.getCertId(),
                        tvCertificate.getCertName(),tvCertificate.getGettingTime(),tvCertificate.getAgenceFrom(),tvCertificate.getContent());
                tvCertificate.setCertAddress(certificateUploadService.getCertInfo(userInfo.getUserAddress(),tvCertificate.getCertId()));
                //紀錄創建時間以及初始化最終修改時間
                Long datetime = System.currentTimeMillis();
                Timestamp timestamp = new Timestamp(datetime);
                tvCertificate.setCreatedTime(timestamp);
                tvCertificate.setUpdatedTime(timestamp);
                tvCertificateRepository.save(tvCertificate);
                return tvCertificate.getCertAddress();
            }
        }
    }

    public boolean verifyCertificate(String certId) throws Exception {
        TVCertificate tvCertificateFromDB = getTVCertificateById(certId);
        System.out.println(tvCertificateFromDB);
        Tuple5<String, String, String, String, String> tvCertificateFromBC = certificateService.getCertInfo(tvCertificateFromDB.getCertAddress());
        System.out.println(tvCertificateFromBC);
        System.out.println("certId:" + certId + ", 合約地址"+tvCertificateFromDB.getCertAddress());
        if (!tvCertificateFromBC.component1().equals(tvCertificateFromDB.getCertId())){
            throw new IllegalStateException("CertID比對不符:" + tvCertificateFromBC.component1() +"(資料庫)" + tvCertificateFromDB.getCertId() +"(區塊鏈)");
        }else if (!tvCertificateFromBC.component2().equals(tvCertificateFromDB.getCertName())){
            throw new IllegalStateException("CertName比對不符:" + tvCertificateFromBC.component2() +"(資料庫)" + tvCertificateFromDB.getCertName() +"(區塊鏈)");
        }else if (!tvCertificateFromBC.component3().equals(tvCertificateFromDB.getGettingTime())){
            throw new IllegalStateException("GettingTime比對不符:" + tvCertificateFromBC.component3() +"(資料庫)" + tvCertificateFromDB.getGettingTime() +"(區塊鏈)");
        }else if (!tvCertificateFromBC.component4().equals(tvCertificateFromDB.getAgenceFrom())){
            throw new IllegalStateException("AgenceFrom比對不符:" + tvCertificateFromBC.component4() +"(資料庫)" + tvCertificateFromDB.getAgenceFrom() +"(區塊鏈)");
        }else if (!tvCertificateFromBC.component5().equals(tvCertificateFromDB.getContent())){
            throw new IllegalStateException("Content比對不符:" + tvCertificateFromBC.component5() +"(資料庫)" + tvCertificateFromDB.getContent() +"(區塊鏈)");
        }else{
            System.out.println("CertID比對結果:" + tvCertificateFromBC.component1() +"(資料庫)" + tvCertificateFromDB.getCertId() +"(區塊鏈)");
            System.out.println("CertName比對結果:" + tvCertificateFromBC.component2() +"(資料庫)" + tvCertificateFromDB.getCertName() +"(區塊鏈)");
            System.out.println("GettingTime比對結果:" + tvCertificateFromBC.component3() +"(資料庫)" + tvCertificateFromDB.getGettingTime() +"(區塊鏈)");
            System.out.println("AgenceFrom比對結果:" + tvCertificateFromBC.component4() +"(資料庫)" + tvCertificateFromDB.getAgenceFrom() +"(區塊鏈)");
            System.out.println("Content比對結果:" + tvCertificateFromBC.component5() +"(資料庫)" + tvCertificateFromDB.getContent() +"(區塊鏈)");
            System.out.println("比對相符");
            return true;
        }
    }

    public Tuple5<String, String, String, String, String> getTVCertificateFronBC(String certId) throws Exception {
        TVCertificate tvCertificateFromDB = getTVCertificateById(certId);
        String contractAddress = tvCertificateFromDB.getCertAddress();
        return certificateService.getCertInfo(contractAddress);
    }
}
