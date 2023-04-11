package com.example.hospital.api.service;

import com.example.hospital.api.db.Entity.VideoDiagnoseEntity;

import java.util.ArrayList;
import java.util.HashMap;

public interface VideoDiagnoseService {
    public void online(int userId);
    public boolean offline(int userId);
    public void updateOpenFlag(int userId,boolean open);
    public ArrayList<HashMap> searchOnlineDoctorList(String subName,String job);
    public HashMap createVideoDiagnose(int userId, VideoDiagnoseEntity entity);
}
