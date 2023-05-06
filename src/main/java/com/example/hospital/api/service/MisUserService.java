package com.example.hospital.api.service;

import com.example.hospital.api.common.PageUtils;
import com.example.hospital.api.db.Entity.MisUserEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface MisUserService {
    public Integer login(Map param);
    public PageUtils searchUserByPages(Map param);
    public void insertUser(Map param);
    public void updateUser(Map param);
    public void deleteUser(Integer[] ids);
    public ArrayList<String> searchUserPermissions(Integer userId);
    public MisUserEntity getUserEntityById(Integer userId);
    public String updatePassword(Map param);
}
