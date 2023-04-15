package com.example.hospital.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.hospital.api.common.PageUtils;
import com.example.hospital.api.db.Entity.MedicalDeptEntity;
import com.example.hospital.api.db.Entity.request.MedicineDeptManageRequest;
import com.example.hospital.api.db.Entity.vo.MedicalDepartmentManagementVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface MedicalDeptService extends IService<MedicalDeptEntity> {
    public ArrayList<HashMap> searchAll();
    public HashMap searchDeptAndSub();
    public void insert(Map param);
    public void updateById(Map param);
    public ArrayList<HashMap> searchMedicalDeptList(Map param);

    PageUtils getMedicineDepartmentManagement(MedicineDeptManageRequest request);
}
