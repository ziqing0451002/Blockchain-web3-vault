package com.example.Blockchain.Vault;

import org.json.JSONException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@RestController
public class VaultController {
    private final VaultService vaultService;

    public VaultController(VaultService vaultService) {
        this.vaultService = vaultService;
    }

    @GetMapping("/getSecret")
    public Map<String, Object> getSecret(@RequestParam String accountAddress) throws IOException {
        return vaultService.getSecret(accountAddress);
    }

    @PostMapping("/setSecret")
    public Map<String, Object> setSecret(
            @RequestParam String accountAddress,
            @RequestParam String accountPK
    ) throws IOException, JSONException {
        return vaultService.setSecret(accountAddress,accountPK);
    }

    @DeleteMapping("/deleteSecret")
    public boolean deleteSecret(
            @RequestParam String accountAddress
    ) throws IOException, JSONException {
        return vaultService.deleteSecret(accountAddress);
    }

}
