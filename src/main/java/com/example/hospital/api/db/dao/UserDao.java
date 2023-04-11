package com.example.hospital.api.db.dao;

import com.example.hospital.api.db.Entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Mapper
public interface UserDao {
    @Transactional
    public int insert(UserEntity entity);

    public Integer searchAlreadyRegistered(String openId);
    public HashMap searchUserInfo(int userId);
    public HashMap searchOpenId(int userId);
}
