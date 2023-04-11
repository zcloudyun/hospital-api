package com.example.hospital.api.db.Entity;

import lombok.Data;

@Data
public class UserEntity {
    private Integer id;
    private String uuid;
    private String nickname;
    private String sex;
    private byte status;
    private String createTime;
}
