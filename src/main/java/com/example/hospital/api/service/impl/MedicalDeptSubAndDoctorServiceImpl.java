package com.example.hospital.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.hospital.api.db.Entity.MedicalDeptSubAndDoctorEntity;
import com.example.hospital.api.db.dao.MedicalDeptSubAndDoctorDao;
import com.example.hospital.api.service.MedicalDeptSubAndDoctorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalDeptSubAndDoctorServiceImpl extends ServiceImpl<MedicalDeptSubAndDoctorDao, MedicalDeptSubAndDoctorEntity>
        implements MedicalDeptSubAndDoctorService {


    @Override
    public List<MedicalDeptSubAndDoctorEntity> getSubDeptDoctorListBatchIds(List<Integer> subIdList) {

        List<MedicalDeptSubAndDoctorEntity> doctorList = this.lambdaQuery()
                .in(MedicalDeptSubAndDoctorEntity::getDeptSubId, subIdList)
                .list();
        return doctorList;
    }

    @Override
    public List<MedicalDeptSubAndDoctorEntity> getSubDeptDoctorList(Integer subId) {

        List<MedicalDeptSubAndDoctorEntity> doctorList = this.lambdaQuery()
                .eq(MedicalDeptSubAndDoctorEntity::getDeptSubId, subId)
                .list();
        return doctorList;
    }
}
