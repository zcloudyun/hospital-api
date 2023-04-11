package com.example.hospital.api.db.Entity.vo;

import com.example.hospital.api.db.Entity.MedicalDeptEntity;
import com.example.hospital.api.db.Entity.MedicalDeptSubEntity;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MedicalDeptSubManageVO {

    // 诊室
    private MedicalDeptSubEntity deptSubEntity;
    // 科室
    private MedicalDeptEntity deptEntity;

    // 医生数量
    private Integer doctorCount;
    // 普通医生
    private Long simpleDoctorListCount;

    // 专家医生
    private Long professionalDoctorListCount;

}
