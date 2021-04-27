package com.example.Blockchain.Contract.StringUpload;
import com.example.Blockchain.Vault.VaultService;

import com.example.Blockchain.web3Info.ContractProperties;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.http.HttpService;
import org.web3j.quorum.Quorum;

@Configuration
public class ContractConfig {
    private static final Logger LOG = LoggerFactory.getLogger(ContractConfig.class);

//    @Value("${lottery.contract.owner-address}")
//    private String ownerAddress;

    @Value("${web3j.client-address}")
    private String clientAddress;

    @Autowired
    private ContractProperties config;
    private VaultService vaultService;

    @Bean
    public Quorum Quorum() {
        return Quorum.build(new HttpService(clientAddress, new OkHttpClient.Builder().build()));
    }

    @Bean
    public StringUploadService contract(Quorum quorum, @Value("${lottery.contract.address:}") String contractAddress)
            throws Exception {
        if (StringUtils.isEmpty(contractAddress)) {
            StringUpload stringUpload = deployContract(quorum);
            return initShopService(stringUpload.getContractAddress(), quorum);
        }
        return initShopService(contractAddress, quorum);
    }

    private StringUploadService initShopService(String contractAddress, Quorum quorum) {
        return new StringUploadService(contractAddress, quorum, config, vaultService);
    }

    private StringUpload deployContract(Quorum quorum) throws Exception {
        Credentials credentials = Credentials.create("b21dce597924a1b57dc10901cf8ee7cb88cd81bb623819be8f3ee8e422d8fd12");
        LOG.info("About to deploy new contract...");
        StringUpload contract = StringUpload.deploy(quorum, credentials, config.gas()).send();
        LOG.info("Deployed new contract with address '{}'", contract.getContractAddress());
        return contract;
    }

//    private TransactionManager txManager(Quorum quorum) {
//        return new ClientTransactionManager(quorum, ownerAddress);
//    }


}
