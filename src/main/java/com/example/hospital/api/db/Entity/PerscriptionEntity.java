package com.example.hospital.api.db.Entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PerscriptionEntity {
    private Integer id;
    private Integer userId;
    private Date date;
    private String prescription;
    private String diagnosis;
    private String name;
    private String sex;
    private Date birthday;
    private String tel;
    private String doctorName;
    private String deptName;

    @TableField(exist = false)
    private List<MedicineEntity> medicineEntityList;
}
