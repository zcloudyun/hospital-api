package com.example.hospital.api.db.Entity;

import lombok.Data;

import java.util.Date;

@Data
public class DoctorWorkPlanEntity {
    private Integer id;
    private Integer doctorId;
    private Integer deptSubId;
    private String date;
    private Integer maximum;
    private Integer num;

}