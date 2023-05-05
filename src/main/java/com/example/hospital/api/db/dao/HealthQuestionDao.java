package com.example.hospital.api.db.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;

@Mapper
public interface HealthQuestionDao {
    public ArrayList<HashMap> searchByType(int type);
    public ArrayList<HashMap> searchOption(int questionId);
}
