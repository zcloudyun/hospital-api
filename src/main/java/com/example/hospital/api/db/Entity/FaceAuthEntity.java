package com.example.hospital.api.db.Entity;

import lombok.Data;

@Data
public class FaceAuthEntity {
    private Integer id;
    private Integer patientCardId;
    private String date;
}