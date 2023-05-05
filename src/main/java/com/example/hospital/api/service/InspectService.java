package com.example.hospital.api.service;

import com.example.hospital.api.common.PageUtils;
import com.example.hospital.api.db.Entity.InspectEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface InspectService{
    public ArrayList<HashMap> searchAll();
    public PageUtils searchByPage(Map param);
    public int insert(Map param);
    public void update(Map param);
    public void deleteByIds(Integer[] ids);
}
