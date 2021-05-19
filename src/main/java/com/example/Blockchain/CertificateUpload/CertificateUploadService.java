package com.example.Blockchain.CertificateUpload;

import com.example.Blockchain.UserInfo.UserInfoService;
import com.example.Blockchain.Vault.VaultService;
import com.example.Blockchain.web3Info.ContractProperties;
import org.json.JSONException;
import org.json.JSONObject;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.quorum.Quorum;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.TransactionManager;

import java.io.IOException;
import java.util.Map;

public class CertificateUploadService {
    private final String contractAddress;
    private final Quorum quorum;
    private final ContractProperties config;
    private final UserInfoService userInfoService;
    private final VaultService vaultService;
//    private String pwd = "ziqing4884"; //若透過讀取keystore登入身分，則需要密碼
//    private String keystore = "c3bf6fce044068f95715af35af96fcc7d7533bcb9c6dc38a31ebd34341843ffc";

    public CertificateUploadService(String contractAddress, Quorum quorum, ContractProperties config ,VaultService vaultService, UserInfoService userInfoService) {
        this.contractAddress = contractAddress;
        this.quorum = quorum;
        this.config = config;
        this.vaultService = vaultService;
        this.userInfoService = userInfoService;
        System.out.println("contractAddress:" + contractAddress);
    }
    private CertificateUpload loadContract(String msgSenderAddress) throws IOException, CipherException, JSONException {
        //取得呼叫合約者私鑰
        Map<String, Object> secretJson = vaultService.getSecret(msgSenderAddress);
        JSONObject datasJson = new JSONObject(secretJson);
        String accountPK = String.valueOf(datasJson.getJSONObject("data").getJSONObject("data").getString("ACCOUNT_PK"));
        System.out.println("accountPK: " + accountPK);
        Credentials credentials = Credentials.create(accountPK);
        return CertificateUpload.load(contractAddress, quorum, credentials, config.gas());
    }

//    private Credentials getCredential() throws IOException, CipherException {
//        return  WalletUtils.loadCredentials(pwd,keystore);
//    }

    private TransactionManager txManager(String accountAddress) {
        return new ClientTransactionManager(quorum, accountAddress);
    }

    public String getCertInfo(String msgSenderAddress, String certId) throws Exception {
        CertificateUpload certificateUpload = loadContract(msgSenderAddress);
        return certificateUpload.getCertContract(certId).send();
    }

    public TransactionReceipt setCertInfo(String userAccount, String userPassword, String certId, String certName, String gettingTime, String agenceFrom, String content) throws Exception {
        String msgSenderAddress = userInfoService.exchangeAddress(userAccount, userPassword);
        System.out.println(msgSenderAddress);
        CertificateUpload certificateUpload = loadContract(msgSenderAddress);
        TransactionReceipt transactionReceipt = certificateUpload.setCertContract(certId,certName,gettingTime,agenceFrom,content).send();
        return transactionReceipt;
    }
}
