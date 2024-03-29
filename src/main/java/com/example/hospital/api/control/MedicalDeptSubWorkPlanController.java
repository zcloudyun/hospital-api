package com.example.hospital.api.control;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.*;

import cn.hutool.json.JSONArray;
import com.example.hospital.api.common.R;
import com.example.hospital.api.control.form.DeleteWorkPlanForm;
import com.example.hospital.api.control.form.InsertWorkPlanForm;
import com.example.hospital.api.control.form.SearchWorkPlanInRangeForm;
import com.example.hospital.api.service.MedicalDeptSubWorkPlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
@Slf4j
@RestController
@RequestMapping("/medical/dept/sub/work_plan")
public class MedicalDeptSubWorkPlanController {
    @Resource
    private MedicalDeptSubWorkPlanService medicalDeptSubWorkPlanService;

    @PostMapping("/searchWorkPlanInRange")
    @SaCheckLogin
//    @SaCheckPermission(value={"ROOT","SCHEDULE:SELECT"},mode= SaMode.OR)
    public R searchWorkPlanInRange(@RequestBody @Valid SearchWorkPlanInRangeForm form){
        Map param= BeanUtil.beanToMap(form);
        //根据开始时间和结束时间，生成连续的日期
        DateRange range= DateUtil.range(new DateTime(form.getStartDate()),
                new DateTime(form.getEndDate()), DateField.DAY_OF_MONTH);
        ArrayList dateList=new ArrayList();
        //把连续的日期保存到集合中
        range.forEach(one->{
            dateList.add(one.toDateStr());
        });
        JSONArray array=medicalDeptSubWorkPlanService.searchWorkPlanInRange(param,dateList);
        dateList.clear();
        range.reset();
        //往dateList放入格式化的日期
        range.forEach(one->{
            dateList.add(one.toString("MM月dd日")+"(" +one.dayOfWeekEnum().toChinese() +")");
        });
        log.info("array的值",array);
        return R.ok().put("result",array).put("dateList",dateList);
    }

    @PostMapping("/insert")
    @SaCheckLogin
  //    @SaCheckPermission(value = {"ROOT","SCHEDULE:INSERT"},mode = SaMode.OR)
    public R insert(@RequestBody @Valid InsertWorkPlanForm form){
        Map param= BeanUtil.beanToMap(form);
        String result= medicalDeptSubWorkPlanService.insert(param);
        return R.ok().put("result",result);
    }

    @PostMapping("/deleteWorkPlan")
    @SaCheckLogin
//  @SaCheckPermission(value = {"ROOT","SCHEDULE:DELETE"},mode = SaMode.OR)
    public R deleteWorkPlan(@RequestBody @Valid DeleteWorkPlanForm form){
        medicalDeptSubWorkPlanService.deleteWorkPlan(form.getWorkPlanId());
        return R.ok();
    }

    @PostMapping("/searchDoctorMessage")
    @SaCheckLogin
    public R searchDoctorMessage(){
        int userId=StpUtil.getLoginIdAsInt();
        HashMap map = medicalDeptSubWorkPlanService.searchDoctorMessage(userId);
        return R.ok().put("result",map);
    }
}
