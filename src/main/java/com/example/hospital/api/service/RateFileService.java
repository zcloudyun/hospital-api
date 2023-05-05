package com.example.hospital.api.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface RateFileService {
    public String uploadImg(MultipartFile file, int registrationId);
    public ArrayList<HashMap> searchImageByRegistrationId(int registrationId);
    public void deleteImage(Map param);
}
