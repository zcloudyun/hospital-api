package com.example.hospital.api.db.dao;
import com.example.hospital.api.db.Entity.MisUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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

    @Select("select * from mis_user where id = #{userId} limit 1")
    public MisUserEntity selectDoctorUserById(@Param("userId") Integer doctorId);

}




