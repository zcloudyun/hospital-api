package com.example.hospital.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.hospital.api.common.PageUtils;
import com.example.hospital.api.common.R;
import com.example.hospital.api.control.form.GetDeptSubDoctorsRequest;
import com.example.hospital.api.db.Entity.MedicalDeptSubEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface MedicalDeptSubService extends IService<MedicalDeptSubEntity> {
    public ArrayList<HashMap> searchMedicalDeptSubList(int deptId);
    public PageUtils searchByPages(Map param);

    public List<MedicalDeptSubEntity> getMedicalDeptSubListByDeptId(Integer deptId);


    R searchMedicalDeptSubDoctorList(GetDeptSubDoctorsRequest request);

    public void insert(Map param);
    public void updateById(Map param);
}
