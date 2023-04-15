package com.example.hospital.api.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.example.hospital.api.db.dao.DoctorPrescriptionDao;
import com.example.hospital.api.db.dao.MedicalRecordDao;
import com.example.hospital.api.service.DoctorPrescriptionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class DoctorPrescriptionServiceImpl implements DoctorPrescriptionService {
    @Resource
    private DoctorPrescriptionDao doctorPrescriptionDao;

    @Resource
    private MedicalRecordDao medicalRecordDao;

    @Override
    public HashMap searchPrescriptionById(Map param){
        HashMap map=doctorPrescriptionDao.searchPrescriptionById(param);
        String birthday= MapUtil.getStr(map,"birthday");
        int age= DateUtil.ageOfNow(birthday);
        map.put("patientAge",age);
        String rp = MapUtil.getStr(map,"rp");
        String[] split = rp.split(",");
        if(split.length>0){
            Integer[] ids=new Integer[split.length];
            for(int i=0;i<split.length;i++){
                ids[i]=Integer.parseInt(split[i]);
            }
            ArrayList<HashMap> list=medicalRecordDao.searchRpById(ids);
            map.replace("rp",list);
        }
        return map;
    }
}
