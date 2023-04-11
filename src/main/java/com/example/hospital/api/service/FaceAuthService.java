package com.example.hospital.api.service;

import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

public interface FaceAuthService {
    public boolean hasFaceAuthInDay(Map param);
    public void createFaceModel(Map param);

    @Transactional
    public boolean verifyFaceModel(int userId,String photo);
}
