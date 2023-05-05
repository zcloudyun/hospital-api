package com.example.hospital.api.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface InspectResultFileService {
    public HashMap uploadImg(MultipartFile file, int recordId);
    public ArrayList<HashMap> searchImageByRecordId(int recordId);
    public void deleteImage(Map param);
}
