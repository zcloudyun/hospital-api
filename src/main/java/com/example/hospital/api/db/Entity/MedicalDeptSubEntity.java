package com.example.hospital.api.db.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("medical_dept_sub")
public class MedicalDeptSubEntity {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private Integer deptId;
    private String location;
}