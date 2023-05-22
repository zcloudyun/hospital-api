package com.example.hospital.api.control;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.example.hospital.api.common.R;
import com.example.hospital.api.control.form.InsertInspectRecordForm;
import com.example.hospital.api.control.form.SearchByStatusInspectRecordForm;
import com.example.hospital.api.control.form.SearchImageByRecordIdForm;
import com.example.hospital.api.control.form.SearchInspectRecordByStatus;
import com.example.hospital.api.db.Entity.InspectRecordEntity;
import com.example.hospital.api.service.InspectRecordService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/inspect_record")
public class InspectRecordController {
    @Resource
    private InspectRecordService inspectRecordService;
    @PostMapping("/insert")
    public R insert(@RequestBody @Valid InsertInspectRecordForm form){
        Map param = BeanUtil.beanToMap(form);
        int result=inspectRecordService.insert(param);
        return R.ok().put("result",result);
    }

    @PostMapping("/searchByStatus")
    public R searchByStatus(@RequestBody @Valid SearchByStatusInspectRecordForm form){
        int userId=StpUtil.getLoginIdAsInt();
        Map param=BeanUtil.beanToMap(form);
        param.replace("userId",userId);
        ArrayList<HashMap> list = inspectRecordService.searchByStatus(param);
        return R.ok().put("result",list);
    }

    @PostMapping("/searchInspectRecordByStatus")
    public R searchInspectRecordByStatus(@RequestBody @Valid SearchInspectRecordByStatus form){
        Map param = BeanUtil.beanToMap(form);
        HashMap map = inspectRecordService.searchInspectRecordByStatus(param);
        return R.ok().put("result",map);
    }

    @PostMapping("/searchRecordAll")
    public R searchRecordAll(){
        int userId= StpUtil.getLoginIdAsInt();
        ArrayList<HashMap> list = inspectRecordService.searchRecordAll(userId);
        return R.ok().put("result",list);

    }

    @PostMapping("/searchByRecordId")
    public R searchByRecordId(@RequestBody @Valid SearchImageByRecordIdForm form){
        HashMap map = inspectRecordService.searchbyRecordId(form.getRecordId());
        return R.ok().put("result",map);
    }
}
