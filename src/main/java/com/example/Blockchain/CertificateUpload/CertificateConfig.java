package com.example.Blockchain.CertificateUpload;

import com.example.Blockchain.UserInfo.UserInfoService;
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
public class CertificateConfig {
    private static final Logger LOG = LoggerFactory.getLogger(CertificateConfig.class);

//    @Value("${lottery.contract.owner-address}")
//    private String ownerAddress;

    @Value("${web3j.client-address}")
    private String clientAddress;

    @Autowired
    private ContractProperties config;
    @Autowired
    private VaultService vaultService;
    @Autowired
    private UserInfoService userInfoService;

    @Bean
    public Quorum Quorum() {
        return Quorum.build(new HttpService(clientAddress, new OkHttpClient.Builder().build()));
    }

    @Bean
    public CertificateUploadService contract(Quorum quorum, @Value("${lottery.contract.address:}") String contractAddress)
            throws Exception {
        if (StringUtils.isEmpty(contractAddress)) {
            CertificateUpload certificateUpload = deployContract(quorum);
            return initShopService(certificateUpload.getContractAddress(), quorum);
        }
        return initShopService(contractAddress, quorum);
    }

    private CertificateUploadService initShopService(String contractAddress, Quorum quorum) {
        return new CertificateUploadService(contractAddress, quorum, config, vaultService, userInfoService);
    }

    private CertificateUpload deployContract(Quorum quorum) throws Exception {
        Credentials credentials = Credentials.create("b21dce597924a1b57dc10901cf8ee7cb88cd81bb623819be8f3ee8e422d8fd12");
        LOG.info("About to deploy new contract...");
        CertificateUpload contract = CertificateUpload.deploy(quorum, credentials, config.gas()).send();
        LOG.info("Deployed new contract with address '{}'", contract.getContractAddress());
        return contract;
    }

//    private TransactionManager txManager(Quorum quorum) {
//        return new ClientTransactionManager(quorum, ownerAddress);
//    }


}
