package com.example.hospital.api.control;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import com.example.hospital.api.common.R;
import com.example.hospital.api.control.form.InsertRateForm;
import com.example.hospital.api.control.form.RequestRateIdForm;
import com.example.hospital.api.control.form.RequestByUserIdForm;
import com.example.hospital.api.service.RateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/rate")
public class RateController {
    @Resource
    private RateService rateService;

    @PostMapping("/insert")
    public R insert(@RequestBody InsertRateForm formData){
        log.info("评论 form={}", formData);
        rateService.insert(formData);
        return R.ok();
    }

    @PostMapping("/searchByUserIdRateAll")
    public R searchByUserIdAll(@RequestBody @Valid RequestByUserIdForm form){
        int userId=StpUtil.getLoginIdAsInt();
        ArrayList<HashMap> list=rateService.searchByUserIdAll(userId);
        return R.ok().put("result",list);
    }

    @PostMapping("/addLikes")
    public R addLikes(@RequestBody @Valid RequestRateIdForm form){
        Map param=BeanUtil.beanToMap(form);
        Integer rateId = MapUtil.getInt(param, "rateId");
        rateService.addLikes(rateId);
        return R.ok();
    }

    @PostMapping("/searchByRateId")
    public R searchByRateId(@RequestBody @Valid RequestRateIdForm form){
        Map param=BeanUtil.beanToMap(form);
        Integer rateId = MapUtil.getInt(param, "rateId");
        HashMap map=rateService.searchByRateId(rateId);
        return R.ok().put("result",map);
    }
}
