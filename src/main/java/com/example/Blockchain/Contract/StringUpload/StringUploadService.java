package com.example.Blockchain.Contract.StringUpload;

import com.example.Blockchain.web3Info.ContractProperties;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.quorum.Quorum;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.TransactionManager;

import java.io.IOException;

public class StringUploadService {
    private final String contractAddress;
    private final Quorum quorum;
    private final ContractProperties config;
    private String pwd = "ziqing4884";
    private String keystore = "b21dce597924a1b57dc10901cf8ee7cb88cd81bb623819be8f3ee8e422d8fd12";


    public StringUploadService(String contractAddress,Quorum quorum, ContractProperties config) {
        this.contractAddress = contractAddress;
        this.quorum = quorum;
        this.config = config;

    }
    private StringUpload loadContract() throws IOException, CipherException {
        Credentials credentials = Credentials.create("b21dce597924a1b57dc10901cf8ee7cb88cd81bb623819be8f3ee8e422d8fd12");
        return StringUpload.load(contractAddress, quorum, credentials, config.gas());
    }

    private Credentials getCredential() throws IOException, CipherException {
        return  WalletUtils.loadCredentials(pwd,keystore);
    }

    private TransactionManager txManager(String accountAddress) {
        return new ClientTransactionManager(quorum, accountAddress);
    }

    public String getStringInfo() throws Exception {
        StringUpload stringUpload = loadContract();
        return stringUpload.getStringInfo().send();
    }

    public TransactionReceipt setStringInfo( String stringInput) throws Exception {
        StringUpload stringUpload = loadContract();
        TransactionReceipt transactionReceipt = stringUpload.setStringInfo(stringInput).send();
        return transactionReceipt;
    }


}
