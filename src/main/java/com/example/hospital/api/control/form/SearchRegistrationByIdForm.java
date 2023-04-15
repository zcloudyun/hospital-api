package com.example.hospital.api.control.form;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class SearchRegistrationByIdForm {
    private Integer userId;
    @NotNull(message = "registrationId不能为空")
    @Min(value = 1,message = "registration不能小于1")
    private Integer registrationId;
}
