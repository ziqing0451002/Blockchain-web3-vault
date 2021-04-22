package com.example.Blockchain.web3Info;


import com.example.Blockchain.Contract.StringUpload.StringUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthGetBalance;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class ownController {
    @Value("${lottery.contract.owner-address}")
    private String ownerAddress;

    @Autowired
    private Web3j web3j;

    @Autowired
    private StringUploadService stringUploadService;

    @GetMapping("/owner")
    public String getAddress() {
        return ownerAddress;
    }

    @GetMapping("/owner/balance")
    public BigInteger getBalance() throws IOException {
        EthGetBalance wei = web3j.ethGetBalance(ownerAddress, DefaultBlockParameterName.LATEST).send();
        return wei.getBalance();
    }

    @GetMapping("/owner/blockNum")
    public BigInteger getBlockNumber() throws IOException, ExecutionException, InterruptedException {
        EthBlockNumber ethBlockNumber = web3j.ethBlockNumber()
                .sendAsync()
                .get();
        return ethBlockNumber.getBlockNumber();
    }




}
