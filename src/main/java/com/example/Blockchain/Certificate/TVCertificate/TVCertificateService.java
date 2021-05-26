package com.example.Blockchain.Certificate.TVCertificate;
import com.example.Blockchain.CertificateUpload.CertificateUploadService;
import com.example.Blockchain.CommentModel.Encoder_MD5;
import com.example.Blockchain.UserInfo.UserInfo;
import com.example.Blockchain.UserInfo.UserInfoRepository;
import com.example.Blockchain.CertificateUpload.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.tuples.generated.Tuple6;

import java.math.BigInteger;
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

    public String  setTVCertificate(String connectAccount, String connectPassword, TVCertificate tvCertificate) throws Exception {
        boolean certIdExists = tvCertificateRepository.existsById(tvCertificate.getCertId());
        boolean connectAccountexists = userInfoRepository.existsById(connectAccount);
        if (!connectAccountexists){
            throw new IllegalStateException("connectAccount:" + connectAccount + "不存在");
        }else {
            UserInfo userInfo = userInfoRepository.findUserInfoByConnectAccount(connectAccount);
            if (  !Objects.equals(userInfo.getconnectPassword(),encoder_md5.encodeMD5(connectPassword))){
                throw new IllegalStateException("密碼錯誤");
            }else if (certIdExists){
                throw new IllegalStateException("CertID:" + tvCertificate.getCertId() + "已被使用");
            }else {
                //把五個欄位做Hash後，傳入hashCode
                int hashCode = hashCodeCreate(tvCertificate.getCertId(),
                        tvCertificate.getCertName(),tvCertificate.getGettingTime(),tvCertificate.getAgenceFrom(),tvCertificate.getContent());

                certificateUploadService.setCertInfo(connectAccount,connectPassword,tvCertificate.getCertId(),
                        tvCertificate.getCertName(),tvCertificate.getGettingTime(),tvCertificate.getAgenceFrom(),tvCertificate.getContent(),hashCode);
                tvCertificate.setCertAddress(certificateUploadService.getCertInfo(userInfo.getwalletAddress(),tvCertificate.getCertId()));
                //紀錄創建時間以及初始化最終修改時間
                Long datetime = System.currentTimeMillis();
                Timestamp timestamp = new Timestamp(datetime);
                tvCertificate.setCreatedTime(timestamp);
                tvCertificate.setUpdatedTime(timestamp);
                tvCertificate.setHashCode(hashCode);

                tvCertificateRepository.save(tvCertificate);
                return tvCertificate.getCertAddress();
            }
        }
    }

    public boolean verifyCertificate(String certId) throws Exception {
        TVCertificate tvCertificateFromDB = getTVCertificateById(certId);
        System.out.println(tvCertificateFromDB);
        Tuple6<String, String, String, String, String, BigInteger> tvCertificateFromBC = certificateService.getCertInfo(tvCertificateFromDB.getCertAddress());
        System.out.println(tvCertificateFromBC);
        System.out.println("certId:" + certId + ", 合約地址"+tvCertificateFromDB.getCertAddress());
        int hashCode = hashCodeCreate(tvCertificateFromDB.getCertId(),
                tvCertificateFromDB.getCertName(),tvCertificateFromDB.getGettingTime(),tvCertificateFromDB.getAgenceFrom(),tvCertificateFromDB.getContent());
        //開始比對
        if (!tvCertificateFromBC.component1().equals(tvCertificateFromDB.getCertId())){
            throw new IllegalStateException("CertID比對不符:" + tvCertificateFromBC.component1() +"(區塊鏈)" + tvCertificateFromDB.getCertId() +"(資料庫)");
        }else if (!tvCertificateFromBC.component2().equals(tvCertificateFromDB.getCertName())){
            throw new IllegalStateException("CertName比對不符:" + tvCertificateFromBC.component2() +"(區塊鏈)" + tvCertificateFromDB.getCertName() +"(資料庫)");
        }else if (!tvCertificateFromBC.component3().equals(tvCertificateFromDB.getGettingTime())){
            throw new IllegalStateException("GettingTime比對不符:" + tvCertificateFromBC.component3() +"(區塊鏈)" + tvCertificateFromDB.getGettingTime() +"(資料庫)");
        }else if (!tvCertificateFromBC.component4().equals(tvCertificateFromDB.getAgenceFrom())){
            throw new IllegalStateException("AgenceFrom比對不符:" + tvCertificateFromBC.component4() +"(區塊鏈)" + tvCertificateFromDB.getAgenceFrom() +"(資料庫)");
        }else if (!tvCertificateFromBC.component5().equals(tvCertificateFromDB.getContent())){
            throw new IllegalStateException("Content比對不符:" + tvCertificateFromBC.component5() +"(區塊鏈)" + tvCertificateFromDB.getContent() +"(資料庫)");
        }else if (!tvCertificateFromBC.component6().equals(BigInteger.valueOf(hashCode))){
            throw new IllegalStateException("HashCode比對不符:" + tvCertificateFromBC.component6() +"(區塊鏈)" + hashCode +"(資料庫)");
        }else{
            System.out.println("CertID比對結果:" + tvCertificateFromBC.component1() +"(區塊鏈)" + tvCertificateFromDB.getCertId() +"(資料庫)");
            System.out.println("CertName比對結果:" + tvCertificateFromBC.component2() +"(區塊鏈)" + tvCertificateFromDB.getCertName() +"(資料庫)");
            System.out.println("GettingTime比對結果:" + tvCertificateFromBC.component3() +"(區塊鏈)" + tvCertificateFromDB.getGettingTime() +"(資料庫)");
            System.out.println("AgenceFrom比對結果:" + tvCertificateFromBC.component4() +"(區塊鏈)" + tvCertificateFromDB.getAgenceFrom() +"(資料庫)");
            System.out.println("Content比對結果:" + tvCertificateFromBC.component5() +"(區塊鏈)" + tvCertificateFromDB.getContent() +"(資料庫)");
            System.out.println("HashCode比對結果:" + tvCertificateFromBC.component6() +"(區塊鏈)" + hashCode +"(資料庫)");
            System.out.println("比對相符");
            return true;
        }
    }

    public Tuple6<String, String, String, String, String, BigInteger> getTVCertificateFronBC(String certId) throws Exception {
        TVCertificate tvCertificateFromDB = getTVCertificateById(certId);
        String contractAddress = tvCertificateFromDB.getCertAddress();
        return certificateService.getCertInfo(contractAddress);
    }

    private int hashCodeCreate(String certId, String certName, String gettingTime, String agenceFrom, String content) {
        int certIdResult = certId != null ? certId.hashCode() : 0;
        int certNameResult = certName != null ? certName.hashCode() : 0;
        int gettingTimeResult = gettingTime != null ? gettingTime.hashCode() : 0;
        int agenceFromResult = agenceFrom != null ? agenceFrom.hashCode() : 0;
        int contentResult = content != null ? content.hashCode() : 0;
        int hashCodeResult = 31 * certIdResult + certNameResult + gettingTimeResult + agenceFromResult + contentResult;
        return hashCodeResult;
    }
}
