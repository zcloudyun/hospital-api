package com.example.hospital.api.service;

import com.example.hospital.api.db.Entity.VideoDiagnoseEntity;
import org.apache.hadoop.util.hash.Hash;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface VideoDiagnoseService {
    public void online(int userId);
    public boolean offline(int userId);
    public void updateOpenFlag(int userId,boolean open);
    public ArrayList<HashMap> searchOnlineDoctorList(String subName,String job);
    public HashMap createVideoDiagnose(int userId, VideoDiagnoseEntity entity);
    //查询问诊信息接口
    public HashMap searchVideoDiagnoseInfo(int userId);
    //查询上线缓存
    public HashMap refreshInfo(int userId);
    public boolean updatePayment(Map param);
    public boolean searchPaymentResult(String outTradeNo);
    public String uploadImg(MultipartFile file, Integer videoDiagnoseId);
    public ArrayList<HashMap> searchImageByVideoDiagnoseId(int videoDiagnoseId);
    public void deleteImage(Map param);
    public String searchRoomId(int doctorId);
    public ArrayList<String> searchImgByVideoDiagnoseId(int videoDiagnoseId);
}
