package com.example.hospital.api.db.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Mapper
public interface InspectResultFileDao {
    public int uploadImg(HashMap map);
    public ArrayList<HashMap> searchImageByRecordId(int recordId);
    public void delete(Map param);
    public ArrayList<String> searchfilenameByRecordId(int recordId);

}
