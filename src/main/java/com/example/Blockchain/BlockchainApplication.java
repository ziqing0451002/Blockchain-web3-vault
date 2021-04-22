package com.example.Blockchain;

//import com.example.Blockchain.Vault.VaultController;
//import com.example.Blockchain.configuration.VaultConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class BlockchainApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlockchainApplication.class, args);
//		ConfigurableApplicationContext context = SpringApplication.run(BlockchainApplication.class, args);
//		VaultConfiguration vaultConfiguration = context.getBean(VaultConfiguration.class);
//		System.out.println("account: " + vaultConfiguration.getLogin());
//		System.out.println("Password: " + vaultConfiguration.getPassword());



		

	}

}
