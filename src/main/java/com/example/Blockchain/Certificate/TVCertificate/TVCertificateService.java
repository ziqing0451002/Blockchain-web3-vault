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
            throw new IllegalStateException("connectAccount:" + connectAccount + "?????????");
        }else {
            UserInfo userInfo = userInfoRepository.findUserInfoByConnectAccount(connectAccount);
            if (  !Objects.equals(userInfo.getconnectPassword(),encoder_md5.encodeMD5(connectPassword))){
                throw new IllegalStateException("????????????");
            }else if (certIdExists){
                throw new IllegalStateException("CertID:" + tvCertificate.getCertId() + "????????????");
            }else {
                //??????????????????Hash????????????hashCode
                int hashCode = hashCodeCreate(tvCertificate.getCertId(),
                        tvCertificate.getCertName(),tvCertificate.getGettingTime(),tvCertificate.getAgenceFrom(),tvCertificate.getContent());

                certificateUploadService.setCertInfo(connectAccount,connectPassword,tvCertificate.getCertId(),
                        tvCertificate.getCertName(),tvCertificate.getGettingTime(),tvCertificate.getAgenceFrom(),tvCertificate.getContent(),hashCode);
                tvCertificate.setCertAddress(certificateUploadService.getCertInfo(userInfo.getwalletAddress(),tvCertificate.getCertId()));
                //???????????????????????????????????????????????????
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
        System.out.println("certId:" + certId + ", ????????????"+tvCertificateFromDB.getCertAddress());
        int hashCode = hashCodeCreate(tvCertificateFromDB.getCertId(),
                tvCertificateFromDB.getCertName(),tvCertificateFromDB.getGettingTime(),tvCertificateFromDB.getAgenceFrom(),tvCertificateFromDB.getContent());
        //????????????
        if (!tvCertificateFromBC.component1().equals(tvCertificateFromDB.getCertId())){
            throw new IllegalStateException("CertID????????????:" + tvCertificateFromBC.component1() +"(?????????)" + tvCertificateFromDB.getCertId() +"(?????????)");
        }else if (!tvCertificateFromBC.component2().equals(tvCertificateFromDB.getCertName())){
            throw new IllegalStateException("CertName????????????:" + tvCertificateFromBC.component2() +"(?????????)" + tvCertificateFromDB.getCertName() +"(?????????)");
        }else if (!tvCertificateFromBC.component3().equals(tvCertificateFromDB.getGettingTime())){
            throw new IllegalStateException("GettingTime????????????:" + tvCertificateFromBC.component3() +"(?????????)" + tvCertificateFromDB.getGettingTime() +"(?????????)");
        }else if (!tvCertificateFromBC.component4().equals(tvCertificateFromDB.getAgenceFrom())){
            throw new IllegalStateException("AgenceFrom????????????:" + tvCertificateFromBC.component4() +"(?????????)" + tvCertificateFromDB.getAgenceFrom() +"(?????????)");
        }else if (!tvCertificateFromBC.component5().equals(tvCertificateFromDB.getContent())){
            throw new IllegalStateException("Content????????????:" + tvCertificateFromBC.component5() +"(?????????)" + tvCertificateFromDB.getContent() +"(?????????)");
        }else if (!tvCertificateFromBC.component6().equals(BigInteger.valueOf(hashCode))){
            throw new IllegalStateException("HashCode????????????:" + tvCertificateFromBC.component6() +"(?????????)" + hashCode +"(?????????)");
        }else{
            System.out.println("CertID????????????:" + tvCertificateFromBC.component1() +"(?????????)" + tvCertificateFromDB.getCertId() +"(?????????)");
            System.out.println("CertName????????????:" + tvCertificateFromBC.component2() +"(?????????)" + tvCertificateFromDB.getCertName() +"(?????????)");
            System.out.println("GettingTime????????????:" + tvCertificateFromBC.component3() +"(?????????)" + tvCertificateFromDB.getGettingTime() +"(?????????)");
            System.out.println("AgenceFrom????????????:" + tvCertificateFromBC.component4() +"(?????????)" + tvCertificateFromDB.getAgenceFrom() +"(?????????)");
            System.out.println("Content????????????:" + tvCertificateFromBC.component5() +"(?????????)" + tvCertificateFromDB.getContent() +"(?????????)");
            System.out.println("HashCode????????????:" + tvCertificateFromBC.component6() +"(?????????)" + hashCode +"(?????????)");
            System.out.println("????????????");
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
