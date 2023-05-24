package com.example.hospital.api.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import com.example.hospital.api.db.Entity.InspectRecordEntity;
import com.example.hospital.api.db.dao.InspectRecordDao;
import com.example.hospital.api.db.dao.InspectResultFileDao;
import com.example.hospital.api.service.InspectRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class InspectRecordServiceImpl implements InspectRecordService {
    @Resource
    private InspectRecordDao inspectRecordDao;

    @Resource
    private InspectResultFileDao inspectResultFileDao;
    @Override
    public int insert(Map param){
        InspectRecordEntity entity = BeanUtil.toBean(param, InspectRecordEntity.class);
        return inspectRecordDao.insert(entity);
    }

    @Override
    public ArrayList<HashMap> searchByStatus(Map param){
        ArrayList<HashMap> list = inspectRecordDao.searchByStatus(param);
        return list;
    }

    @Override
    public ArrayList<HashMap> searchInspectRecordByStatus(Map param){
        ArrayList<HashMap> map = inspectRecordDao.searchInspectRecordByStatus(param);
        if(map.size()>0){
            map.forEach(item->{
                int recordId= MapUtil.getInt(item,"id");
                ArrayList<HashMap> list = inspectResultFileDao.searchImageByRecordId(recordId);
                item.put("file",list);
            });
        }
        return map;
    }

    @Override
    public HashMap searchbyRecordId(int recordId){
        HashMap map = inspectRecordDao.searchbyRecordId(recordId);
        ArrayList<HashMap> list = inspectResultFileDao.searchImageByRecordId(recordId);
        map.put("file",list);
        return map;
    }
    @Override
    public ArrayList<HashMap> searchRecordAll(int userId){
        ArrayList<HashMap> list = inspectRecordDao.searchRecordAll(userId);
        return list;
    }
}
