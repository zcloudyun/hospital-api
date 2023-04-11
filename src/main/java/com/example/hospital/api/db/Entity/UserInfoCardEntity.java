package com.example.hospital.api.db.Entity;

import lombok.Data;

import java.util.StringJoiner;

@Data
public class UserInfoCardEntity {
    private Integer id;
    private Integer userId;
    private String uuid;
    private String name;
    private String sex;
    private String pid;
    private String tel;
    private String birthday;
    private String medicalHistory;
    private String insuranceType;
    private Boolean existFaceModel;


    public static String convertToString(String[] array) {
        StringJoiner joiner = new StringJoiner(",");
        for (String s : array) {
            joiner.add(s);
        }
        return joiner.toString();
    }

    public static String[] convertToArray(String arrStr) {
        String[] split = arrStr.split(",");
        return split;
    }


}
