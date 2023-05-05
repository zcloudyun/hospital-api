package com.example.hospital.api.service.impl;

import cn.hutool.core.map.MapUtil;
import com.example.hospital.api.db.dao.HealthQuestionDao;
import com.example.hospital.api.service.HealthQuestionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;

@Service
public class HealthQuestionServiceImpl implements HealthQuestionService {
    @Resource
    public HealthQuestionDao healthQuestionDao;

    public ArrayList<HashMap> searchByType(int type){
        ArrayList<HashMap> list = healthQuestionDao.searchByType(type);
        list.forEach(item->{
            int id= MapUtil.getInt(item,"id");
            ArrayList<HashMap> list1 = healthQuestionDao.searchOption(id);
            item.put("options",list1);
        });
        return list;
    }
}
