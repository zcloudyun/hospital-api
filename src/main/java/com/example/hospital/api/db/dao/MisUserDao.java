package com.example.hospital.api.db.dao;
import com.example.hospital.api.db.Entity.MisUserEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//医护人员登录
@Mapper
public interface MisUserDao {
    public ArrayList<String> searchUserPermissions(int userId);
    public Integer login(Map param);
    public HashMap searchRefId(int id);
    public ArrayList<HashMap> searchUserByPages(Map param);
    public long searchCount(Map param);
    public int insertUser(MisUserEntity entity);
    public void updateUser(Map param);
    public void deleteUser(Integer[] ids);
    public Integer searchUserId(Map param);
}




