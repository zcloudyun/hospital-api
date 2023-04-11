package com.example.hospital.api.db.Entity.vo;

import com.example.hospital.api.db.Entity.MedicalDeptSubEntity;
import com.example.hospital.api.db.Entity.bo.MedicalDeptSubBO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 医疗科室管理
 */
@Data
@Accessors(chain = true)
public class MedicalDepartmentManagementVO {

    // 科室属性
    private Integer id;
    private String name;
    private Boolean outpatient;
    private String description;
    private Boolean recommended;


    // 科室数量
    private Integer subDeptCount;

    // 科室下医生数量
    private Integer doctorCount;


    // 科室下诊室
    private List<MedicalDeptSubBO> medicalDeptSubList;


}
