package com.example.Blockchain.Contract.StringUpload;

import com.example.Blockchain.web3Info.nodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

@RestController
public class SrtingUploadController {

//    @Value("${lottery.contract.owner-address}")
//    private String ownerAddress;
//
//    @Value("${lottery.contract.test-address}")
//    private String testAddress;

    private final StringUploadService stringUploadService;

    @Autowired
    private Web3j web3j;

    @Autowired
    public SrtingUploadController(StringUploadService stringUploadService, nodeService nodeService, Web3j web3j) {
        this.stringUploadService = stringUploadService;
        this.web3j = web3j;
    }

    @GetMapping("/StringUpload/getString")
    public String getStringInfo(String msgSenderAddress) throws Exception {
        return stringUploadService.getStringInfo(msgSenderAddress);
    }

    @PostMapping("/StringUpload/setString")
    public TransactionReceipt setStringInfo(
            @RequestParam String userAccount,
            @RequestParam String userPassword,
            @RequestBody String stringInput
    ) throws Exception {
        return stringUploadService.setStringInfo(userAccount,userPassword,stringInput);
    }


//
//    @PostMapping("/shop/setProduct")
//    public TransactionReceipt setProduct(@RequestParam BigInteger id,
//                                         @RequestParam String name,
//                                         @RequestParam BigInteger price) throws Exception {
//        return shopService.setProduct(ownerAddress, id, name, price);
//    }
//
//    @GetMapping("shop/getHistory/{id}")
//    public Map<String, Object> getProduct(@PathVariable BigInteger id) throws Exception {
//
//        return shopService.getHistory(ownerAddress, id);
//    }
//    @PostMapping("/shop/buyProduct")
//    public  HashMap<String, Object> buyProduct(
//            @RequestParam BigInteger id,
//            @RequestParam BigInteger amount)throws Exception{
//        return shopService.buyProduct(testAddress , id , amount);
//    }
//
//    @PostMapping("/shop/register")
//    public HashMap<String, Object>register()throws Exception{
//        return shopService.register(testAddress);
//    }

}
