package com.example.hospital.api.service;

import java.util.HashMap;
import java.util.Map;

public interface UserService {
    public HashMap  login(Map param);
    public String Register(Map param);
    public HashMap searchUserInfo(int userId);
    public String updatePassword(Map param);
}
