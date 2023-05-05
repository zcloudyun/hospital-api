package com.example.hospital.api.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import com.example.hospital.api.common.PageUtils;
import com.example.hospital.api.db.Entity.DoctorEntity;
import com.example.hospital.api.db.Entity.InspectEntity;
import com.example.hospital.api.db.Entity.MedicalDeptSubAndDoctorEntity;
import com.example.hospital.api.db.dao.InspectDao;
import com.example.hospital.api.service.InspectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class InspectServiceImpl implements InspectService {

    @Resource
    private InspectDao inspectDao;
    @Override
    public ArrayList<HashMap> searchAll(){
        return inspectDao.searchAll();
    }

    @Override
    public PageUtils searchByPage(Map param){
        ArrayList<HashMap> list=null;
        //查询的记录总数
        long count=inspectDao.inspectCount(param);
        if(count>0){
            list=inspectDao.searchByPage(param);
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
    public int insert(Map param){
        InspectEntity entity= BeanUtil.toBean(param,InspectEntity.class);
        return inspectDao.insert(entity);
    }

    @Override
    @Transactional
    public void update(Map param){
        inspectDao.update(param);
    }

    @Override
    @Transactional
    public void deleteByIds(Integer[] ids){
        inspectDao.deleteByIds(ids);
    }
}
