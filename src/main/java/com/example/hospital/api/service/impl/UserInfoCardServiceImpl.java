package com.example.hospital.api.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.example.hospital.api.db.Entity.UserInfoCardEntity;
import com.example.hospital.api.db.dao.UserInfoCardDao;
import com.example.hospital.api.service.UserInfoCardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;

@Service
public class UserInfoCardServiceImpl implements UserInfoCardService {
    @Resource
    private UserInfoCardDao userInfoCardDao;

    @Override
    public void insert(UserInfoCardEntity entity) {
        userInfoCardDao.insert(entity);
    }

    @Override
    public HashMap searchUserInfoCard(int userId){
        HashMap map=userInfoCardDao.searchUserInfoCard(userId);
        if(map!=null){
            //从map中获取medicalHistory
            String medicalHistory= MapUtil.getStr(map,"medicalHistory");
            //把数组转换为json数组
            String[] array = UserInfoCardEntity.convertToArray(medicalHistory);
            map.replace("medicalHistory",array);
        }
        return map;
    }

    @Override
    @Transactional
    public void update(UserInfoCardEntity entity){
        userInfoCardDao.update(entity);
    }

    @Override
    public boolean hasUserInfoCard(int userId){
        Integer id=userInfoCardDao.hasUserInfoCard(userId);
        boolean bool=(id !=null);
        return bool;
    }

}
