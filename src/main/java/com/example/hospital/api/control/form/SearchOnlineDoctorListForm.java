package com.example.hospital.api.control.form;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class SearchOnlineDoctorListForm {
    private String subName;
    @Pattern(regexp = "^主任医师$|^副主任医师$|^主治医师$|^副主治医师$",message = "job内容错误")
    private String job;
}
