package com.example.hospital.api.service.impl;

import cn.hutool.core.map.MapUtil;
import com.example.hospital.api.db.dao.HealthDao;
import com.example.hospital.api.db.dao.HealthResultDao;
import com.example.hospital.api.service.HealthResultService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class HealthResultServiceImpl implements HealthResultService {
    @Resource
    private HealthResultDao healthResultDao;

    @Resource
    private HealthDao healthDao;

    @Override
    public HashMap searchByType(Map param){
        Integer health_id = MapUtil.getInt(param, "health_id");
        healthDao.updateByHealthId(health_id);
        HashMap map = healthResultDao.searchByType(param);
        Integer result_id = MapUtil.getInt(map, "id");
        ArrayList<HashMap> list = healthResultDao.searchScale(health_id);
        ArrayList<HashMap> list1 = healthResultDao.searchRecommed(result_id);
        map.put("recommed",list1);
        map.put("scale",list);
        return map;
    }
}
