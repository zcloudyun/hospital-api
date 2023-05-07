package com.example.hospital.api.db.dao;

import com.example.hospital.api.db.Entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Mapper
public interface UserDao {
    @Transactional
    public boolean insert(UserEntity entity);
    public ArrayList<String> searchUsername();
    public HashMap searchUserMessage(String username);
    public void updatePassword(Map param);
    public Integer searchAlreadyRegistered(String openId);
    public HashMap searchUserInfo(int userId);
    public HashMap searchOpenId(int userId);
    public HashMap searchByUserId(int userId);

}
