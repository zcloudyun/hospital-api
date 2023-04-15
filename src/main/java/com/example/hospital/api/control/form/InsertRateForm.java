package com.example.hospital.api.control.form;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class InsertRateForm {
    public Integer registrationId;
    public Integer totalValue;
    public String evaluateText;
    public Integer score1;
    public Integer score2;
    public Integer score3;
    public Integer score4;
    public Integer score5;
    public Integer score6;
    public Integer score7;
    public Integer score8;
    public Integer score9;
    public Integer score10;
}
