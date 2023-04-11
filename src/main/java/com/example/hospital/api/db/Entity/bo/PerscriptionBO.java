package com.example.hospital.api.db.Entity.bo;

import lombok.Data;

import java.util.Date;

@Data
public class PerscriptionBO {

    private Integer user_id;
    private Date date;
    private String prescription;
    private String diagnosis;
    private String name;
    private String sex;
    private Date birthday;
    private String tel;
    private String doctor_name;
    private String dept_name;

}
