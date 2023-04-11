package com.example.hospital.api.db.Entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
@Data
@TableName("medicine")
public class MedicineEntity {

    @TableId(value = "id")
    private Integer id;
    @TableField(value = "medicine_name")
    private String medicineName;
    @TableField(value = "scale")
    private String scale;
    @TableField(value = "approval_num")
    private String approvalNum;
    @TableField(value = "effects")
    private String effects;
    @TableField(value = "use")
    private String use;
    @TableField(value = "bzq")
    private String bzq;
    @TableField(value = "price")
    private BigDecimal price;
    @TableField(value = "num")
    private Integer num;
}
