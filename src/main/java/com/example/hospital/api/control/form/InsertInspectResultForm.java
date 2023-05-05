package com.example.hospital.api.control.form;

import lombok.Data;

@Data
public class InsertInspectResultForm {
    private int hitId;
    private int recordId;
    private String checkinfo;
    private String remark;
}
