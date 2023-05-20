package com.example.hospital.api.service;

import com.example.hospital.api.db.Entity.UserEntity;
import com.example.hospital.api.db.Entity.UserInfoCardEntity;

import java.util.HashMap;
import java.util.Map;

public interface UserService {
    public HashMap  login(Map param);
    public String Register(Map param);
    public HashMap searchUserInfo(int userId);
    public String updatePassword(Map param);
    public UserInfoCardEntity getPatientById(int userId);
}
