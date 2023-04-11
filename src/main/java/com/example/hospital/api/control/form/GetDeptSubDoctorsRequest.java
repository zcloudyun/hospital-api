package com.example.hospital.api.control.form;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GetDeptSubDoctorsRequest {

    // 诊室名称
    private String deptSubName;

    // 科室ID
    private Integer deptId;

    private Integer length = 10;

    private Integer start = 1;

}
