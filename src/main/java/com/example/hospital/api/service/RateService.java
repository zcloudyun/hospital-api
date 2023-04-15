package com.example.hospital.api.service;

import com.example.hospital.api.common.R;
import com.example.hospital.api.control.form.InsertRateForm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface RateService {
    public int insert(InsertRateForm form);
    public ArrayList<HashMap> searchByUserIdAll(Integer userId);
    public HashMap searchByRateId(Integer rateId);
    public void addLikes(Integer rateId);
}
