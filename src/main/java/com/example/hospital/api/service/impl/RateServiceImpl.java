package com.example.hospital.api.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.map.MapUtil;
import com.example.hospital.api.control.form.InsertRateForm;
import com.example.hospital.api.db.Entity.RateEntity;
import com.example.hospital.api.db.dao.MedicalRegistrationDao;
import com.example.hospital.api.db.dao.RateDao;
import com.example.hospital.api.db.dao.RateFileDao;
import com.example.hospital.api.exception.HospitalException;
import com.example.hospital.api.service.RateService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;

@Service
public class RateServiceImpl implements RateService {
    @Resource
    private RateDao rateDao;

    @Resource
    private RateFileDao rateFileDao;

    @Resource
    private MedicalRegistrationDao medicalRegistrationDao;
    @Override
    //事务的注解
    @Transactional
    public int insert(InsertRateForm form){
        Integer registrationId = form.getRegistrationId();
        medicalRegistrationDao.updateEvaluate(registrationId);
        RateEntity entity= BeanUtil.toBean(form,RateEntity.class);
        return rateDao.insert(entity);
    }

    @Override
    public ArrayList<HashMap> searchByUserIdAll(Integer userId){
        ArrayList<HashMap>list=rateDao.searchByUserIdAll(userId);
        list.forEach(item->{
            Integer registrationId = MapUtil.getInt(item, "registrationId");
            ArrayList<HashMap> list1 = rateFileDao.searchImageByRegistrationId(registrationId);
            item.put("file",list1);
        });
        return list;
    }

    @Override
    public HashMap searchByRateId(Integer rateId){
        HashMap map=rateDao.searchByRateId(rateId);
        Integer registrationId = MapUtil.getInt(map, "registrationId");
        ArrayList<HashMap> list = rateFileDao.searchImageByRegistrationId(registrationId);
        map.put("file",list);
        return map;
    }
    @Override
    public void addLikes(Integer rateId){
        rateDao.addLikes(rateId);
    }
}
