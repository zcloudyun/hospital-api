package com.example.hospital.api.db.dao;

import com.example.hospital.api.db.Entity.UserInfoCardEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Mapper
public interface UserInfoCardDao {
    public String searchUserTel(int userId);
    public int insert(UserInfoCardEntity entity);

    public HashMap searchUserInfoCard(int userId);
    public int update(UserInfoCardEntity entity);
    public Integer hasUserInfoCard(int userId);

    public Boolean searchExistFaceModel(int userId);
    public void updateExistFaceModel(Map param);

    public Integer searchIdByUserId(int userId);
    public ArrayList<HashMap> searchById(Integer id);

    @Select("select * from patient_user_info_card where user_id = #{userId} limit 1")
    public UserInfoCardEntity searchUserlist(int userId);

}
