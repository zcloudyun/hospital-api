package com.example.hospital.api.db.Entity;

import lombok.Data;

@Data
public class DoctorWorkPlanScheduleEntity {
    private Integer id;
    private Integer workPlanId;
    private Integer slot;
    private Integer maximum;
    private Integer num;

}