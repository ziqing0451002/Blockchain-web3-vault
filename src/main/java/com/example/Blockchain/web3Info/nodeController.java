package com.example.Blockchain.web3Info;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.crypto.CipherException;
import org.web3j.quorum.methods.response.raft.RaftPeer;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;
import java.util.Optional;

@RestController
public class nodeController {

    private final nodeService nodeService;


    public nodeController(com.example.Blockchain.web3Info.nodeService nodeService) {
        this.nodeService = nodeService;
    }

    @GetMapping("/node/BlockNumber")
    public BigInteger getBlockNumber() throws IOException {
        return nodeService.getBlockNumber();
    }

    @GetMapping("/node/AccountList")
    public List<String> getAccountList() throws IOException {
        return nodeService.getAccountList();
    }

    @PostMapping("/node/createAccount")
    public List<String> createAccount(@RequestParam String password
    ) throws IOException, CipherException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {

        return nodeService.createAccount(password);
    }
    @GetMapping("/node/getNodeInfo")
    public Optional<List<RaftPeer>> getNodeInfo() throws IOException {
        return nodeService.getNodeInfo();
    }

}
