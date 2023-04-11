package com.example.hospital.api.db.Entity;

import lombok.Data;

@Data
public class MedicalRecordEntity {
    private String record_code;
    private Integer user_id;
    private Integer doctor_id;
    private Integer dept_id;
    private String date;
    private String description;
    private String checkinfo;
    private String disease_history;
    private String diagnosis;
    private String prescription;
    private String comment;
    private String remark;
    private String diagnostictype;
    private Integer status;
    private String insurance_type;
    private String total_price;
}
