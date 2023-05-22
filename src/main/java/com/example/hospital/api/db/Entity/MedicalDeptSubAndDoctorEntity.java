package com.example.hospital.api.db.Entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("medical_dept_sub_and_doctor")
public class MedicalDeptSubAndDoctorEntity {
//    private Integer id;
    private Integer deptSubId;
    private Integer doctorId;

}