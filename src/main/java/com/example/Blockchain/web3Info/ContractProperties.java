package com.example.Blockchain.web3Info;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.web3j.tx.gas.StaticGasProvider;

import java.math.BigInteger;

@Configuration
@ConfigurationProperties(prefix = "lottery.contract")
@Getter
@Setter
public class ContractProperties {
    private BigInteger gasPrice;
    private BigInteger gasLimit;

    public StaticGasProvider gas() {
        return new StaticGasProvider(gasPrice, gasLimit);
    }
}