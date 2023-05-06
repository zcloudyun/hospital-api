package com.example.hospital.api.control.form;

import lombok.Data;

@Data
public class UpdatePasswordForm {
    public String password;
    public String newPassword;
    public Integer userId;
}
