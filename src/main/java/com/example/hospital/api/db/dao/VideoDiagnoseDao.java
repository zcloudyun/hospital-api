package com.example.hospital.api.db.dao;

import com.example.hospital.api.db.Entity.VideoDiagnoseEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Mapper
public interface VideoDiagnoseDao {
    @Transactional
    public void insert(VideoDiagnoseEntity entity);
    public HashMap searchByOutTradeNo(String outTradeNo);
    public HashMap searchPaymentStatus(int id);
    public void closePayment(Map param);
    public Map searchVideoDiagnoseInfo(int diagnoseId);
    public void updatePayment(Map param);
    public String searchPaymentResult(String outTradeNo);

    public void updateStatus(HashMap param);
}




