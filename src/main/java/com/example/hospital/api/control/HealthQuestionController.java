package com.example.hospital.api.control;

import com.example.hospital.api.common.R;
import com.example.hospital.api.control.form.SearchByTypeForm;
import com.example.hospital.api.service.HealthQuestionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/health_question")
public class HealthQuestionController {
    @Resource
    private HealthQuestionService healthQuestionService;

    @PostMapping("/searchByType")
    public R searchByType(@RequestBody @Valid SearchByTypeForm form){
        ArrayList<HashMap> list = healthQuestionService.searchByType(form.getType());
        return R.ok().put("result",list);

    }
}
