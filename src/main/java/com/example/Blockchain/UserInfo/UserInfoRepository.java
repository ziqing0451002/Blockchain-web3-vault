package com.example.Blockchain.UserInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, String> {
    UserInfo findUserInfoByConnectAccount(String connectAccount);
    List<UserInfo> findAllByOrderByCreatedTimeAsc();
}
