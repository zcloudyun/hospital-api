package com.example.hospital.api.db.Entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class MedicalRegistrationEntity {
    private Integer id;
    private Integer patientCardId;
    private Integer workPlanId;
    private Integer doctorScheduleId;
    private Integer doctorId;
    private Integer deptSubId;
    private String date;
    private Integer slot;
    private BigDecimal amount;
    private String outTradeNo;
    private String prepayId;
    private String transactionId;
    private Byte paymentStatus;
    private Date createTime;
}