package com.example.hospital.api.service;

import com.example.hospital.api.db.Entity.UserInfoCardEntity;

import java.util.HashMap;

public interface UserInfoCardService {
    public void insert(UserInfoCardEntity entity);
    public HashMap searchUserInfoCard(int userId);
    public void update(UserInfoCardEntity entity);

    public  boolean hasUserInfoCard(int userId);
}
