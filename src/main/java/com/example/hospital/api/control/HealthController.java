package com.example.hospital.api.control;

import com.example.hospital.api.common.R;
import com.example.hospital.api.service.HealthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/health")
public class HealthController {
    @Resource
    private HealthService healthService;

    @GetMapping("/searchAll")
    public R searchAll(){
        ArrayList<HashMap> list = healthService.searchAll();
        return R.ok().put("result",list);
    }
}
