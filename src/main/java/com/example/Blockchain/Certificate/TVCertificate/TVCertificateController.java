package com.example.Blockchain.Certificate.TVCertificate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.web3j.tuples.generated.Tuple6;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping(path = "api/TVCertificate")
public class TVCertificateController {
    @Autowired
    private final TVCertificateService tvCertificateService;

    @Autowired
    public TVCertificateController(TVCertificateService tvCertificateService) {
        this.tvCertificateService = tvCertificateService;
    }

    @GetMapping(path = "getTVCertificate")
    public List<TVCertificate> getTVCertificate(){
        return tvCertificateService.getTVCertificate();
    }

    @GetMapping(path = "getTVCertificateById/{certId}")
    public TVCertificate getTVCertificateById(@PathVariable("certId") String certId){
        return tvCertificateService.getTVCertificateById(certId);
    }

    @PostMapping(path = "setTVCertificate")
    public String setTVCertificate(@RequestParam String connectAccount,@RequestParam String connectPassword, @RequestBody TVCertificate tvCertificate) throws Exception {
        return tvCertificateService.setTVCertificate(connectAccount,connectPassword,tvCertificate);
    }

    @GetMapping(path = "verifyCertificate/{certId}")
    public boolean verifyCertificate(@PathVariable("certId") String certId) throws Exception {
        return tvCertificateService.verifyCertificate(certId);
    }

    @GetMapping(path = "getTVCertificateFronBC/{certId}")
    public Tuple6<String, String, String, String, String, BigInteger> getTVCertificateFronBC(@PathVariable("certId") String certId) throws Exception {
        return tvCertificateService.getTVCertificateFronBC(certId);
    }


}
