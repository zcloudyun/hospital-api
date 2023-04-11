package com.example.hospital.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.hospital.api.db.Entity.MedicalDeptSubAndDoctorEntity;

import java.util.List;

public interface MedicalDeptSubAndDoctorService extends IService<MedicalDeptSubAndDoctorEntity> {
    List<MedicalDeptSubAndDoctorEntity> getSubDeptDoctorListBatchIds(List<Integer> subIdList);
    List<MedicalDeptSubAndDoctorEntity> getSubDeptDoctorList(Integer subId);

}