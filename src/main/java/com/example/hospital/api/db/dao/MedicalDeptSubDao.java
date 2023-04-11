package com.example.hospital.api.db.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.hospital.api.db.Entity.MedicalDeptEntity;
import com.example.hospital.api.db.Entity.MedicalDeptSubEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface MedicalDeptSubDao extends BaseMapper<MedicalDeptSubEntity> {
    public ArrayList<HashMap> searchMedicalDeptSubList(int deptId);
    public ArrayList<HashMap> searchByPages(Map param);
    public Integer searchCount(Map param);
    public ArrayList<HashMap> searchDoctorNum();
    List<MedicalDeptSubEntity> searchByDeptId(Integer deptId);
    public int insert(MedicalDeptSubEntity entity);
    public void updateById(Map param);
}




