package com.example.hospital.api.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.hospital.api.db.Entity.DoctorEntity;
import com.example.hospital.api.db.Entity.Message;
import com.example.hospital.api.db.Entity.MisUserEntity;
import com.example.hospital.api.db.dao.MessageMapper;
import com.example.hospital.api.service.DoctorService;
import com.example.hospital.api.service.MessageService;
import com.example.hospital.api.service.MisUserService;
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
}




