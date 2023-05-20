package com.example.hospital.api.service;

import com.example.hospital.api.db.Entity.DoctorEntity;
import com.example.hospital.api.db.Entity.Message;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.hospital.api.db.Entity.UserInfoCardEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
* @author zhang'yun
* @description 针对表【tb_message(消息表)】的数据库操作Service
* @createDate 2023-05-05 14:44:00
*/
public interface MessageService extends IService<Message> {
    public List<DoctorEntity> searchDoctorList(Integer userId);
    public List<UserInfoCardEntity> searchUserList(Integer userId);
}
