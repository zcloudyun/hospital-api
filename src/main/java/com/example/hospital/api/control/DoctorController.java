package com.example.hospital.api.control;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.example.hospital.api.common.PageUtils;
import com.example.hospital.api.common.R;
import com.example.hospital.api.control.form.*;
import com.example.hospital.api.service.DoctorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/doctor")
public class DoctorController {
    @Resource
    private DoctorService doctorService;

    @PostMapping("/searchByPage")
    //验证用户是否已经登录，如果登录就能执行下面的方法，如果没有登录就不能执行该方法
    @SaCheckLogin
    //用户必须具备相关的权限，权限之间是OR的关系,管理员权限或者是医生权限
//    @SaCheckPermission(value={"ROOT","DOCTOR:SELECT"},mode= SaMode.OR)
    public R SearchByPage(@RequestBody @Valid SearchDoctorByPageForm form) {
        Map param = BeanUtil.beanToMap(form);
        int page = form.getPage();
        int length = form.getLength();
        //从当前页的第-开始
        int start = (page - 1) * length;
        param.put("start", start);
        PageUtils pageUtils = doctorService.searchByPage(param);
        log.info("{}，分发 {}", param, pageUtils);
        return R.ok().put("result", pageUtils);

    }

    @PostMapping("/searchContent")
    @SaCheckLogin
//    @SaCheckPermission(value={"ROOT","DOCTOR:SELECT"},mode=SaMode.OR)
    public R SearchContent(@RequestBody @Valid SearchDoctorContentForm form) {
        HashMap map = doctorService.searchContent(form.getId());
        return R.ok(map);
    }

    @PostMapping("/insert")
    @SaCheckLogin
//    @SaCheckPermission(value={"ROOT","DOCTOR:INSERT"},mode=SaMode.OR)
    public R insert(@RequestBody @Valid InsertDoctorForm form) {

        log.info("接收到的参数:{}", form);

        Map param = BeanUtil.beanToMap(form);
        String json = JSONUtil.parseArray(form.getTag()).toString();
        param.replace("tag", json);
        param.put("uuid", IdUtil.simpleUUID().toUpperCase());
        doctorService.insert(param);
        return R.ok();
    }

    @PostMapping("/updatePhoto")
    @SaCheckLogin
//    @SaCheckPermission(value={"ROOT","DOCTOR:UPDATE"},mode=SaMode.OR)
    public R updatePhoto(@Param("file") MultipartFile file, @Param("doctorId") Integer doctorId) {
        String filename = doctorService.updatePhoto(file, doctorId);
        return R.ok(filename);
    }

    @PostMapping("/searchById")
    @SaCheckLogin
//    @SaCheckPermission(value = {"ROOT","DOCTOR:SELECT"},mode=SaMode.OR)
    public R searchById(@RequestBody @Valid SearchDoctorByIdForm form) {
        HashMap map = doctorService.searchById(form.getId());
        return R.ok(map);
    }

    @PostMapping("/update")
    @SaCheckLogin
//    @SaCheckPermission(value={"ROOT","DOCTOR:UPDATE"},mode=SaMode.OR)
    public R update(@RequestBody @Valid UpdateDoctorForm form) {
        Map param = BeanUtil.beanToMap(form);
        String json = JSONUtil.parseArray(form.getTag()).toString();
        param.replace("tag", json);
        doctorService.update(param);
        return R.ok();
    }

    @PostMapping("/deleteByIds")
    @SaCheckLogin
//    @SaCheckPermission(value={"ROOT","DOCTOR:DELETE"},mode = SaMode.OR)
    public R deleteByIds(@RequestBody @Valid DeleteDoctorByIdsForm form) {
        doctorService.deleteByIds(form.getIds());
        return R.ok();
    }

    @PostMapping("/searchByDeptSubId")
    @SaCheckLogin
    public R searchByDeptSubId(@RequestBody @Valid SearchDoctorByDeptSubIdForm form) {
        ArrayList<HashMap> list = doctorService.searchByDeptSubId(form.getDeptSubId());
        return R.ok().put("result", list);
    }

    @PostMapping("/searchDoctorInfoById")
    public R searchDoctorInfoById(@RequestBody @Valid SearchDoctorByIdForm form) {
        HashMap map = doctorService.searchDoctorInfoById(form.getId());
        return R.ok(map);
    }
}
