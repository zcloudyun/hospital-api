package com.example.hospital.api.db.Entity.request;

import lombok.Data;
import lombok.experimental.Accessors;


/**
 * 医疗科室管理查询请求
 */
@Data
@Accessors(chain = true)
public class MedicineDeptManageRequest {

    // 科室名称
    private String name;

    // 科室类型
    private Boolean outpatient;

    // 优先
    private Boolean recommended;

    // 每页多少条
    private Integer length;

    // 第几页
    private Integer start;

}
