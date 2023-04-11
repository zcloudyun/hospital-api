package com.example.hospital.api.config.mq;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.hospital.api.db.Entity.DoctorPrescriptionEntity;
import com.example.hospital.api.db.dao.DoctorPrescriptionDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Component
@RabbitListener(queues = "presciption")
@Slf4j
public class PrescriptionReceiver {
    @Resource
    private DoctorPrescriptionDao doctorPrescriptionDao;

    @RabbitHandler
    @Transactional
    public void receive(String msg){
        //把消息转换成JSON对象
        JSONObject json= JSONUtil.parseObj(msg);
        String uuid=json.getStr("uuid");
        Integer patientCardId=json.getInt("patientCardId");
        String diagnosis=json.getStr("diagnosis");
        Integer subDeptId=json.getInt("doctorId");
        Integer doctorId=json.getInt("doctorId");
        Integer registrationId=json.getInt("registrationId");
        String rp=json.getStr("rp");
        DoctorPrescriptionEntity entity=new DoctorPrescriptionEntity();
        entity.setUuid(uuid);
        entity.setPatientCardId(patientCardId);
        entity.setDiagnosis(diagnosis);
        entity.setDoctorId(doctorId);
        entity.setSubDeptId(subDeptId);
        entity.setRegistrationId(registrationId);
        entity.setRp(rp);

        //把处方保存到数据库
        doctorPrescriptionDao.insert(entity);
        log.debug(msg);
        log.debug("成功保存到医生处方");
    }
}
