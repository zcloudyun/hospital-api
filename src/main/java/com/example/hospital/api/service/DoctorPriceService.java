package com.example.hospital.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.hospital.api.common.PageUtils;
import com.example.hospital.api.db.Entity.MedicalDeptSubAndDoctorEntity;

import java.util.Map;

public interface DoctorPriceService {
    public PageUtils searchByPagePrice(Map param);
    public void updatePriceById(Map param);

}
