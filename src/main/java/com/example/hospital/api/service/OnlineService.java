package com.example.hospital.api.service;

import java.util.ArrayList;
import java.util.HashMap;

public interface OnlineService {
    public void online(int userId);
    public boolean offline(int userId);
    public ArrayList<HashMap> searchOnlineDoctorList(String subName, String job);
    public Boolean searchisOnline(int doctorId);
    public Boolean isStatus(int userId);
    public Boolean deitStatus(int userId);
    public Boolean searchStatus(int userId);
}
