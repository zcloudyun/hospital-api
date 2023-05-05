package com.example.hospital.api.db.Entity;

import lombok.Data;

@Data
public class InspectResultEntity {
    private int hitId;
    private int recordId;
    private String checkinfo;
    private String remark;
}
