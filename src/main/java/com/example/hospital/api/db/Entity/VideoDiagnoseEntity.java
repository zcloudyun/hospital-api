package com.example.hospital.api.db.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("video_diagnose")
public class VideoDiagnoseEntity {
    private Integer id;
    private Integer patientCardId;
    private Integer doctorId;
    private String outTradeNo;
    private BigDecimal amount;
    private Byte paymentStatus;
    private String prepayId;
    private String transactionId;

    @TableField(value = "expect_start")
    private String expectStart;
    @TableField(value = "expect_end")
    private String expectEnd;
    private String realStart;
    private String realEnd;
    private Byte status;

    private String createTime;

}