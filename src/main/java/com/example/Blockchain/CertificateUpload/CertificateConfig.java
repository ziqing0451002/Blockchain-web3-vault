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
    @Autowired

    @Bean
    public Quorum Quorum() {
        return Quorum.build(new HttpService(clientAddress, new OkHttpClient.Builder().build()));
    }

    @Bean
    public CertificateUploadService contractCertificateUploadService(Quorum quorum, @Value("${CertificateUpload.contract.address:}") String contractAddress)
            throws Exception {
        if (StringUtils.isEmpty(contractAddress)) {
            CertificateUpload certificateUpload = deployContract(quorum);
            return initCertificateUploadService(certificateUpload.getContractAddress(), quorum);
        }
        return initCertificateUploadService(contractAddress, quorum);
    }

    private CertificateUploadService initCertificateUploadService(String contractAddress, Quorum quorum) {
        return new CertificateUploadService(contractAddress, quorum, config, vaultService, userInfoService);
    }

    private CertificateUpload deployContract(Quorum quorum) throws Exception {
        Credentials credentials = Credentials.create("24d9e65899013e1a67a27d3f932b00e9e4b170114cf06740c47bb45f98c63395");
        LOG.info("About to deploy new contract...");
        CertificateUpload contract = CertificateUpload.deploy(quorum, credentials, config.gas()).send();
        LOG.info("Deployed new contract with address '{}'", contract.getContractAddress());
        return contract;
    }

//    private TransactionManager txManager(Quorum quorum) {
//        return new ClientTransactionManager(quorum, ownerAddress);
//    }

//    //加入Certificate Config
    @Bean
    public CertificateService contractCertificateService(Quorum quorum)
            throws Exception {
        return initCertificateService(quorum);
    }

    private CertificateService initCertificateService(Quorum quorum) {
        return new CertificateService(quorum, config, vaultService, userInfoService);
    }

}
