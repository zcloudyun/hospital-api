package com.example.hospital.api.db.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.hospital.api.db.Entity.MedicalDeptSubAndDoctorEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Mapper
public interface MedicalDeptSubAndDoctorDao extends BaseMapper<MedicalDeptSubAndDoctorEntity> {
   @Transactional
    public int insert(MedicalDeptSubAndDoctorEntity entity);
    public void updateDoctorSubDept(Map params);
    List<MedicalDeptSubAndDoctorEntity> searchDeptSubId(Integer id);
}




