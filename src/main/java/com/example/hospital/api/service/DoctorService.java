package com.example.hospital.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.hospital.api.common.PageUtils;
import com.example.hospital.api.db.Entity.DoctorEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface DoctorService extends IService<DoctorEntity> {
    public PageUtils searchByPage(Map param);
    public HashMap searchContent(int id);
    public void insert(Map param);
    public String updatePhoto(MultipartFile file,Integer doctorId);
    public HashMap searchById(int id);
    public void update(Map param);
    public void deleteByIds(Integer[] ids);
    //根据诊室ID查询下面隶属的医生
    public ArrayList<HashMap> searchByDeptSubId(int deptSubId);

    //查询医生信息
    public HashMap searchDoctorInfoById(int id);
}
