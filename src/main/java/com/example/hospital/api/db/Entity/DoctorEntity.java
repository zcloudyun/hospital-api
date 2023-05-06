package com.example.hospital.api.db.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("doctor")
public class DoctorEntity {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String pid;
    private String uuid;
    private String sex;
    private String photo;
    private String birthday;
    private String school;
    private String degree;
    private String tel;
    private String address;
    private String email;
    private String job;
    private String remark;
    private String description;
    private String hiredate;
    private String tag;
    private Boolean recommended;
    private Byte status;
    private String createTime;

    @TableField(exist = false)
    private Integer doctorId;

}