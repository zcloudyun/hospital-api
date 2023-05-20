package com.example.hospital.api.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.hospital.api.db.Entity.*;
import com.example.hospital.api.db.dao.MessageMapper;
import com.example.hospital.api.service.DoctorService;
import com.example.hospital.api.service.MessageService;
import com.example.hospital.api.service.MisUserService;
import com.example.hospital.api.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author zhang'yun
* @description 针对表【tb_message(消息表)】的数据库操作Service实现
* @createDate 2023-05-05 14:44:00
*/
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
    implements MessageService {

    @Resource
    private MessageMapper messageMapper;
    @Resource
    private MisUserService misUserService;
    @Resource
    private DoctorService doctorService;

    @Resource
    private UserService userService;



    @Override
    public List<DoctorEntity> searchDoctorList(Integer userId){
      List<Message> userAllMessage = this.messageMapper.getUserAllMessage(userId);

      // 获取所有的医生id
      Set<Integer> doctorIdSet = userAllMessage.stream().map(Message::getReceiverId).collect(Collectors.toSet());

      // 通过 医生id 查询医生信息
        List<DoctorEntity> doctorList = doctorIdSet.stream().map(doctorId -> {
            MisUserEntity misUser = this.misUserService.getUserEntityById(doctorId);
            // 获取doctor
            DoctorEntity doctorEntity = this.doctorService.getDoctorById(misUser.getRefId());
            doctorEntity.setDoctorId(doctorId);

            return doctorEntity;
        }).collect(Collectors.toList());


        return doctorList;
    }

    @Override
    public List<UserInfoCardEntity> searchUserList(Integer userId){
        //查询与医生账户id相关信息
        List<Message> userAllMessage = this.messageMapper.getDoctorAllMessage(userId);
        if(userAllMessage.size()>0){
            // 获取所有的患者的id
            Set<Integer> userIdSet = userAllMessage.stream().map(Message::getSenderId).collect(Collectors.toSet());
            // 通过 用户id 查询用户信息
            List<UserInfoCardEntity> userList = userIdSet.stream().map(patientId -> {
                // 根据用户id查询就诊卡详情
                UserInfoCardEntity patientEntity = this.userService.getPatientById(patientId);
                return patientEntity;
            }).collect(Collectors.toList());

            return userList;
        }
        return null;
    }
}




