package com.example.hospital.api.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import com.example.hospital.api.common.PageUtils;
import com.example.hospital.api.db.Entity.MedicalRecordEntity;
import com.example.hospital.api.db.Entity.MedicineEntity;
import com.example.hospital.api.db.Entity.PerscriptionEntity;
import com.example.hospital.api.db.Entity.UserInfoCardEntity;
import com.example.hospital.api.db.dao.MedicalRecordDao;
import com.example.hospital.api.db.dao.MedicalRegistrationDao;
import com.example.hospital.api.db.dao.MedicineDao;
import com.example.hospital.api.service.MedicalRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MedicalRecordImpl implements MedicalRecordService {
    @Resource
    private MedicalRecordDao medicalRecordDao;

    @Resource
    private MedicineDao medicineDao;

     @Resource
     private MedicalRegistrationDao medicalRegistrationDao;
    @Override
    public void insert(Map param){
        Integer id= MapUtil.getInt(param,"registion_id");
        String prescription=MapUtil.getStr(param,"prescription");
        if(prescription!=null){
            String[] split = prescription.split(",");
            Integer[] ids=new Integer[split.length];
            for(int i=0;i<split.length;i++){
               ids[i]=Integer.parseInt(split[i]);
            }
            if(ids.length>0) {
                medicineDao.updateNum(ids);
            }
        }
        medicalRegistrationDao.updateStatus(id);
        MedicalRecordEntity entity= BeanUtil.toBean(param,MedicalRecordEntity.class);
        medicalRecordDao.insert(entity);
    }

    @Override
    public PageUtils searchAll(Map param){
        ArrayList<HashMap> list=null;
        //查询的记录总数
        long count=medicalRecordDao.searchAllCount(param);
        if(count>0){
            list=medicalRecordDao.searchAll(param);
        }else {
            list = new ArrayList<>();
        }

        //获取当前页数
        int page= MapUtil.getInt(param,"page");
        //获取每页记录数
        int length=MapUtil.getInt(param,"length");
        PageUtils pageUtils=new PageUtils(list,count,page,length);
        return pageUtils;
    }

    @Override
    public PageUtils searchPrescription(Map param){
        List<PerscriptionEntity> list=null;
        //查询的记录总数
        long count=medicalRecordDao.searchPrescriptionCount(param);
        if(count>0){
            list=medicalRecordDao.searchPrescription(param);
            for (PerscriptionEntity perscription : list) {
                String[] split = perscription.getPrescription().split(",");
                List<Integer> collect = Arrays.stream(split).map(Integer::valueOf).collect(Collectors.toList());
                List<MedicineEntity> searchByIds = this.medicineDao.searchByIds(collect);
                perscription.setMedicineEntityList(searchByIds);
            }
        }else{
            list=new ArrayList<>();
        }
        //获取当前页数
        int page= MapUtil.getInt(param,"page");
        //获取每页记录数
        int length=MapUtil.getInt(param,"length");
        PageUtils pageUtils=new PageUtils(list,count,page,length);
        return pageUtils;
    }

    @Override
    @Transactional
    public void deleteByIds(Integer[] ids){
        medicalRecordDao.deleteByIds(ids);
    }

    @Override
    @Transactional
    public void deleteIds(Integer[] ids){
        medicalRecordDao.deleteIds(ids);
    }
}
