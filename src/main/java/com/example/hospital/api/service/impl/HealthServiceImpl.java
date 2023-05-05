package com.example.hospital.api.service.impl;

import com.example.hospital.api.db.dao.HealthDao;
import com.example.hospital.api.service.HealthService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;

@Service
public class HealthServiceImpl implements HealthService {
    @Resource
    private HealthDao healthDao;
    @Override
    public ArrayList<HashMap> searchAll(){
        return healthDao.searchAll();
    }
}
