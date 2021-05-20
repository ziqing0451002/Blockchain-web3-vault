package com.example.Blockchain.CertificateUpload;

import com.example.Blockchain.UserInfo.UserInfoService;
import com.example.Blockchain.Vault.VaultService;
import com.example.Blockchain.web3Info.ContractProperties;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.quorum.Quorum;
import org.web3j.tuples.generated.Tuple5;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.TransactionManager;

import java.io.IOException;
import java.util.Map;
@Component
public class CertificateService {
    private final Quorum quorum;
    private final ContractProperties config;
    private final UserInfoService userInfoService;
    private final VaultService vaultService;
    //驗證證書資訊，則可以不用登入，我們直接配一個公用的帳號給他
    private String pwd = "read_only"; //若透過讀取keystore登入身分，則需要密碼
    private String keystore = "f8335ca3bb5407293cee7664f4fe623561355a5ad6e12b892ffcb0aa8f0bd23";

    public CertificateService(Quorum quorum, ContractProperties config, VaultService vaultService, UserInfoService userInfoService) {
        this.quorum = quorum;
        this.config = config;
        this.vaultService = vaultService;
        this.userInfoService = userInfoService;
    }
    //===========================================================================================================
    // 使用公共read_only帳號進行書證驗證
    public Tuple5<String, String, String, String, String> getCertInfo(String contractAddress) throws Exception {
        Certificate certificate = loadContract(contractAddress);
        return certificate.getCertInfo().send();
    }

    private Certificate loadContract(String contractAddress) throws IOException, CipherException, JSONException {
        Credentials credentials = Credentials.create(keystore);
        return Certificate.load(contractAddress, quorum, credentials, config.gas());
    }

    private Credentials getCredential() throws IOException, CipherException {
        return  WalletUtils.loadCredentials(pwd,keystore);
    }

    //===========================================================================================================
    // 必須登入自己的帳號帳號，才能進行書證驗證
    public Tuple5<String, String, String, String, String> getCertInfo(String msgSenderAddress,String contractAddress) throws Exception {
        Certificate certificate = loadContract(msgSenderAddress,contractAddress);
        return certificate.getCertInfo().send();
    }

    private Certificate loadContract(String msgSenderAddress,String contractAddress) throws IOException, CipherException, JSONException {
        //取得呼叫合約者私鑰
        Map<String, Object> secretJson = vaultService.getSecret(msgSenderAddress);
        JSONObject datasJson = new JSONObject(secretJson);
        String accountPK = String.valueOf(datasJson.getJSONObject("data").getJSONObject("data").getString("ACCOUNT_PK"));
        System.out.println("accountPK: " + accountPK);
        Credentials credentials = Credentials.create(accountPK);
        return Certificate.load(contractAddress, quorum, credentials, config.gas());
    }

    //===========================================================================================================


    private TransactionManager txManager(String accountAddress) {
        return new ClientTransactionManager(quorum, accountAddress);
    }






}
