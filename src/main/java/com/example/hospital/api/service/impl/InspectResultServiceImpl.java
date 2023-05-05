package com.example.hospital.api.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.example.hospital.api.db.Entity.InspectResultEntity;
import com.example.hospital.api.db.dao.InspectRecordDao;
import com.example.hospital.api.db.dao.InspectResultDao;
import com.example.hospital.api.service.InspectResultService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class InspectResultServiceImpl implements InspectResultService {

    @Resource
    private InspectResultDao inspectResultDao;

    @Resource
    private InspectRecordDao inspectRecordDao;

    @Override
    public int insert(Map param){
        InspectResultEntity entity = BeanUtil.toBean(param, InspectResultEntity.class);
        int recordId=entity.getRecordId();
        inspectRecordDao.updateStatus(recordId);
        return inspectResultDao.insert(entity);
    }
}
