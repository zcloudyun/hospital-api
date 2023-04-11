package com.example.hospital.api.db.Entity.bo;

import com.example.hospital.api.db.Entity.DoctorEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class MedicalDeptSubBO {

    // 诊室属性
    private Integer id;
    private String name;
    private Integer deptId;
    private String location;

    // 诊室下面医生
    private List<DoctorEntity> doctorList;

}
