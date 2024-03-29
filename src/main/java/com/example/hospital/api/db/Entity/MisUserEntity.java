package com.example.hospital.api.db.Entity;

import lombok.Data;

@Data
public class MisUserEntity {
    private Integer id;
    private String username;
    private String password;
    private String name;
    private String sex;
    private String tel;
    private String email;
    private Integer deptId;
    private String job;
    private Integer refId;
    private int roleId;
    private Byte status;
    private String createTime;
}