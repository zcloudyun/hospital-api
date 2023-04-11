package com.example.hospital.api.db.dao;


import com.example.hospital.api.db.Entity.MedicineEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Mapper
public interface MedicineDao {
    public ArrayList<HashMap> searchByPage(Map param);
    public long searchCount(Map param);
    @Transactional
    public void insert(MedicineEntity entity);
    public void update(Map param);

    public ArrayList<HashMap> searchAll();
    public void updateNum(Integer[] ids);
    public List<MedicineEntity> searchByIds(@Param("ids") List<Integer> ids);
}
