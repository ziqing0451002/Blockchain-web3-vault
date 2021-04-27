package com.example.Blockchain.Vault;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Map;
import org.json.JSONObject;

//
@Component
public class VaultService {
    @Value("${spring.cloud.vault.token}")
    public String VAULT_TOKEN;

    public Map<String, Object> getSecret(String accountAddress) throws IOException {
        Map<String, Object> jsonMap=null;
        String command =
                "curl -H \"X-Vault-Token: "+ VAULT_TOKEN + "\"  -X GET http://127.0.0.1:8200/v1/secret/data/" + accountAddress;
        ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
        Process process = processBuilder.start();
        InputStream inputStream = process.getInputStream();
        ObjectMapper mapper = new ObjectMapper();
        jsonMap = mapper.readValue(inputStream, Map.class);
        inputStream.close();
        process.destroy();
        return jsonMap;
    }

    public Map<String, Object> setSecret(String accountAddress,String accountPK) throws IOException, JSONException {

        //將參數儲存成一個temp的json檔案
        JSONObject jsonData = new JSONObject();
        jsonData.put("ACCOUNT_ADDRESS",accountAddress);
        jsonData.put("ACCOUNT_PK",accountPK);
        JSONObject jsonOptions = new JSONObject();
        jsonOptions.put("cas",0);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data",jsonData);
        jsonObject.put("options",jsonOptions);
        System.out.println(jsonObject.toString());
        try(
            FileWriter file = new FileWriter("temp.json")) {
            file.write(jsonObject.toString());
            System.out.println("SUCCESS");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //讀取temp.json，上傳vault
        Map<String, Object> jsonMap=null;
        String command =
                "curl --header \"X-Vault-Token:" + VAULT_TOKEN + "\" --request POST --data @temp.json http://127.0.0.1:8200/v1/secret/data/" + accountAddress;
        System.out.println(command);
        ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
        System.out.println(processBuilder);

        Process process = processBuilder.start();
        System.out.println(process);

        InputStream inputStream = process.getInputStream();

        System.out.println(inputStream);
        ObjectMapper mapper = new ObjectMapper();
        jsonMap = mapper.readValue(inputStream, Map.class);
        inputStream.close();
//        process.destroy();
        return jsonMap;
    }
}
