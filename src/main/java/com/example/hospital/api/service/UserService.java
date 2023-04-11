package com.example.hospital.api.service;

import java.util.HashMap;

public interface UserService {
    public HashMap  loginOrRegister(String code,String nickname,String sex);
    public HashMap searchUserInfo(int userId);
}
