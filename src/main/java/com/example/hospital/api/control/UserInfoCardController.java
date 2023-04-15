package com.example.hospital.api.control;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONNull;
import cn.hutool.json.JSONUtil;
import com.example.hospital.api.common.R;
import com.example.hospital.api.control.form.InsertUserInfoCardForm;
import com.example.hospital.api.control.form.UpdateUserInfoCardForm;
import com.example.hospital.api.db.Entity.UserInfoCardEntity;
import com.example.hospital.api.service.UserInfoCardService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;

@RestController
@RequestMapping("/user/info/card")
public class UserInfoCardController {
    @Resource
    public UserInfoCardService userInfoCardService;

    @PostMapping("/insert")
    public R insert(@RequestBody @Valid InsertUserInfoCardForm form){
      UserInfoCardEntity entity= BeanUtil.toBean(form,UserInfoCardEntity.class);
      int userId= StpUtil.getLoginIdAsInt();
      entity.setUserId(userId);
      entity.setUuid(IdUtil.simpleUUID());
      entity.setMedicalHistory(UserInfoCardEntity.convertToString(form.getMedicalHistory()));
      userInfoCardService.insert(entity);
      return R.ok();
    }

    @GetMapping("/searchUserInfoCard")
    @SaCheckLogin
    public R searchUserInfoCard(){
        int userId=StpUtil.getLoginIdAsInt();
        HashMap map=userInfoCardService.searchUserInfoCard(userId);
        if(MapUtil.isEmpty(map)){
            return R.ok("没有查询到数据");
        }
        return R.ok(map);
    }

    @PostMapping("/update")
    @SaCheckLogin
    public R update(@RequestBody @Valid UpdateUserInfoCardForm form){
        UserInfoCardEntity entity=BeanUtil.toBean(form,UserInfoCardEntity.class);
        entity.setMedicalHistory(UserInfoCardEntity.convertToString(form.getMedicalHistory()));
        userInfoCardService.update(entity);
        return R.ok();
    }

    @GetMapping("/hasUserInfoCard")
    @SaCheckLogin
    public R hasUserInfoCard(){
        int userId=StpUtil.getLoginIdAsInt();
        boolean bool=userInfoCardService.hasUserInfoCard(userId);
        return R.ok().put("result",bool);
    }
}
