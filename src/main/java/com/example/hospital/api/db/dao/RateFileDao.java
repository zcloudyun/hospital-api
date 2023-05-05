package com.example.hospital.api.db.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Mapper
public interface RateFileDao {
    public boolean uploadImg(HashMap param);
    public ArrayList<HashMap> searchImageByRegistrationId(int registrationId);
    public ArrayList<String> searchfilenameByRegistrationId(int registrationId);
    public void delete(Map param);
}
