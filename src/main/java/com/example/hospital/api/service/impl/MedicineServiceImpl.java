package com.example.hospital.api.service.impl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import com.example.hospital.api.common.PageUtils;
import com.example.hospital.api.db.Entity.MedicineEntity;
import com.example.hospital.api.db.dao.MedicineDao;
import com.example.hospital.api.service.MedicineService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
@Service
public class MedicineServiceImpl implements MedicineService {
    @Resource
    private MedicineDao medicineDao;

    @Override
    public PageUtils searchByPage(Map param){
        ArrayList<HashMap> list=null;
        //查询的记录总数
        long count=medicineDao.searchCount(param);
        if(count>0){
            list=medicineDao.searchByPage(param);
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
    public void insert(Map param){
        //保存药品信息
        MedicineEntity entity= BeanUtil.toBean(param, MedicineEntity.class);
        medicineDao.insert(entity);
    }

    @Override
    @Transactional
    public void update(Map param){
        medicineDao.update(param);
    }

    @Override
    public ArrayList<HashMap> searchAll(){
       ArrayList<HashMap> list= medicineDao.searchAll();
       return list;
    }
}
