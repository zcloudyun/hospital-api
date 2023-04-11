package com.example.hospital.api.service.impl;

import cn.hutool.core.map.MapUtil;
import com.example.hospital.api.common.PageUtils;
import com.example.hospital.api.db.dao.DoctorPriceDao;
import com.example.hospital.api.service.DoctorPriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class DoctorPriceServiceImpl implements DoctorPriceService {
    @Resource
    private DoctorPriceDao doctorPriceDao;

    public PageUtils searchByPagePrice(Map param){
        ArrayList<HashMap> list=null;
        //查询的记录总数
        long count=doctorPriceDao.searchCount(param);
        if(count>0){
            list=doctorPriceDao.searchByPagePrice(param);
        }else{
            list=new ArrayList<>();
        }
        //获取当前页数
        int page= MapUtil.getInt(param,"page");
        //获取每页记录数
        int length=MapUtil.getInt(param,"length");
        PageUtils pageUtils=new PageUtils(list,count,page,length);
        return pageUtils;
    }

    @Override
    public void updatePriceById(Map param){
        doctorPriceDao.updatePriceById(param);
    }
}
