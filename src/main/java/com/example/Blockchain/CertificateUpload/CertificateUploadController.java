package com.example.Blockchain.CertificateUpload;

import com.example.Blockchain.web3Info.nodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

public class CertificateUploadController {
    private final CertificateUploadService certificateUploadService;

    @Autowired
    private Web3j web3j;

    @Autowired
    public CertificateUploadController(CertificateUploadService certificateUploadService, nodeService nodeService, Web3j web3j) {
        this.certificateUploadService = certificateUploadService;
        this.web3j = web3j;
    }

    @GetMapping("/CertificateUpload/getCertInfo")
    public TransactionReceipt getCertInfo(String msgSenderAddress, String certId) throws Exception {
        return certificateUploadService.getCertInfo(msgSenderAddress,certId);
    }

    @PostMapping("/CertificateUpload/setCertInfo")
    public TransactionReceipt setCertInfo(
            @RequestParam String userAccount,
            @RequestParam String userPassword,
            @RequestParam String certId,
            @RequestParam String certName,
            @RequestParam String gettingTime,
            @RequestParam String agenceFrom,
            @RequestParam String content
    ) throws Exception {
        return certificateUploadService.setCertInfo(userAccount,userPassword,certId,certName,gettingTime,agenceFrom,content);
    }
}
