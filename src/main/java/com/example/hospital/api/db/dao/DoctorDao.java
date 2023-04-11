package com.example.hospital.api.db.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.hospital.api.db.Entity.DoctorEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Mapper
public interface DoctorDao extends BaseMapper<DoctorEntity> {
   public ArrayList<HashMap> searchByPage(Map param);
   public long searchCount(Map param);
   public HashMap searchContent(int id);
   @Transactional
   public int insert(DoctorEntity entity);
   public Integer searchIdByUuid(String uuid);
   public void updatePhoto(Map param);
   public HashMap searchById(int id);
   public void update(Map param);
   public void deleteByIds(Integer[] ids);
   public ArrayList<HashMap> searchByDeptSubId(int deptSubId);
   public HashMap searchDataForOnlineCache(int doctorId);

   public HashMap searchDoctorInfoById(int id);
   public HashMap searchOpenId(int userId);
}




