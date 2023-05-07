package com.example.hospital.api.control.form;

import lombok.Data;

@Data
public class UpdateUserPasswordForm {
    private String nickname;
    private String password;
    private String newPassword;
}
