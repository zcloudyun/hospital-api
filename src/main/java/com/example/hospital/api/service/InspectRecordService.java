package com.example.hospital.api.service;

import com.example.hospital.api.db.Entity.InspectRecordEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface InspectRecordService {
    public int insert(Map param);
    public ArrayList<HashMap> searchByStatus(Map param);
    public ArrayList<HashMap> searchInspectRecordByStatus(Map param);
    public ArrayList<HashMap> searchRecordAll(int userId);
    public HashMap searchbyRecordId(int recordId);
}
