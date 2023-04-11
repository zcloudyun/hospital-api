package com.example.hospital.api.db.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.hospital.api.db.Entity.MedicalDeptEntity;
import com.example.hospital.api.db.Entity.request.MedicineDeptManageRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface MedicalDeptDao extends BaseMapper<MedicalDeptEntity> {
    public ArrayList<HashMap> searchAll();
    public ArrayList<HashMap> searchDeptAndSub();
    public ArrayList<HashMap> searchMedicalDeptList(Map param);
    public ArrayList<HashMap> searchByPage(Map param);
    public int insert(MedicalDeptEntity entity);
    public void updateById(Map param);
    // 查询科室
    List<MedicalDeptEntity> selectMedicineDepartment(@Param("param") MedicineDeptManageRequest request);

    // 查询诊室


}




