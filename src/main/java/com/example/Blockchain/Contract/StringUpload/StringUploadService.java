package com.example.Blockchain.Contract.StringUpload;

import com.example.Blockchain.Vault.VaultService;
import com.example.Blockchain.web3Info.ContractProperties;
import org.json.JSONArray;
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
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.TransactionManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.json.JSONObject;



public class StringUploadService {
    private final String contractAddress;
    private final Quorum quorum;
    private final ContractProperties config;
    private VaultService vaultService;
    private String pwd = "ziqing4884"; //若透過讀取keystore登入身分，則需要密碼
    private String keystore = "c3bf6fce044068f95715af35af96fcc7d7533bcb9c6dc38a31ebd34341843ffc";

    public StringUploadService(String contractAddress, Quorum quorum, ContractProperties config ,VaultService vaultService) {
        this.contractAddress = contractAddress;
        this.quorum = quorum;
        this.config = config;
        this.vaultService = vaultService;
    }
    private StringUpload loadContract(String msgSenderAddress) throws IOException, CipherException, JSONException {
        //取得呼叫合約者私鑰
        Map<String, Object> secretJson = vaultService.getSecret(msgSenderAddress);
        JSONObject datasJson = new JSONObject(secretJson);
        String accountPK = String.valueOf(datasJson.getJSONObject("data").getJSONObject("data").getString("ACCOUNT_PK"));

        Credentials credentials = Credentials.create(accountPK);
        return StringUpload.load(contractAddress, quorum, credentials, config.gas());
    }

    private Credentials getCredential() throws IOException, CipherException {
        return  WalletUtils.loadCredentials(pwd,keystore);
    }

    private TransactionManager txManager(String accountAddress) {
        return new ClientTransactionManager(quorum, accountAddress);
    }

    public String getStringInfo(String msgSenderAddress) throws Exception {
        StringUpload stringUpload = loadContract(msgSenderAddress);
        return stringUpload.getStringInfo().send();
    }

    public TransactionReceipt setStringInfo( String msgSenderAddress,String stringInput) throws Exception {
        StringUpload stringUpload = loadContract(msgSenderAddress);
        TransactionReceipt transactionReceipt = stringUpload.setStringInfo(stringInput).send();
        return transactionReceipt;
    }


}
