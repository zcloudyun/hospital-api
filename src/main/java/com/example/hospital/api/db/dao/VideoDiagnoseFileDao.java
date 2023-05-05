package com.example.hospital.api.db.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Mapper
public interface VideoDiagnoseFileDao {
    public boolean uploadImg(HashMap param);
    public ArrayList<HashMap> searchImageByVideoDiagnoseId(int videoDiagnoseId);
    public ArrayList<String> searchfilenameByVideoDiagnoseId(Integer videoDiagnoseId);
    public void delete(Map param);
    public ArrayList<String> searchImgByVideoDiagnoseId(Integer videoDiagnoseId);
}




