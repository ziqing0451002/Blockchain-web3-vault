package com.example.Blockchain.web3Info;
import com.example.Blockchain.Vault.VaultService;
import org.springframework.stereotype.Component;
import org.web3j.crypto.*;
import org.web3j.quorum.Quorum;
import org.web3j.quorum.methods.response.raft.RaftPeer;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class nodeService {


    private final Quorum quorum;
    private final VaultService vaultService;

    public nodeService(Quorum quorum,VaultService vaultService) {
        this.quorum = quorum;
        this.vaultService = vaultService;
    }

    public BigInteger getBlockNumber() throws IOException{
        return quorum.ethBlockNumber().send().getBlockNumber();
    }

    public List<String> getAccountList() throws IOException{
        return quorum.ethAccounts().send().getAccounts();
    }

    public List<String> createAccount(String password) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, CipherException {
        try {
            ECKeyPair keyPair = Keys.createEcKeyPair();
            String pk = keyPair.getPrivateKey().toString(16);
            System.out.println("PRIVATE_KEY:");
            System.out.println(pk);

            WalletFile wallet = Wallet.createStandard(password, keyPair);
            String accountAddress = "0x" + wallet.getAddress();
            System.out.println("accountAddress");
            System.out.println(accountAddress);
            vaultService.setSecret(accountAddress,pk);
            List<String> secretKV = new ArrayList<>();
            secretKV.add(accountAddress);
            secretKV.add(pk);
            return secretKV;

            //在本機生成keystore檔案
//            String fileName = WalletUtils.generateNewWalletFile(
//                    password,
//                    new File("C:\\Users\\4884\\Desktop\\4884\\數位研發課\\JAVA\\Blockchain\\keystore"));
//            return fileName;

        }catch (Exception e){
            System.err.println("Error: " + e.getMessage());
            return Collections.singletonList(e.getMessage());
        }

    }

    public Optional<List<RaftPeer>> getNodeInfo() throws IOException{
        return quorum.raftGetCluster().send().getCluster();
    }

}
