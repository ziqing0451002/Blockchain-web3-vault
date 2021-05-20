package com.example.Blockchain.Certificate.TVCertificate;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TVCertificateRepository extends JpaRepository<TVCertificate, String> {
    TVCertificate findTVCertificateByCertId(String certId);

}
