package com.example.hospital.api.control;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import com.example.hospital.api.common.R;
import com.example.hospital.api.control.form.InsertLoginForm;
import com.example.hospital.api.control.form.InsertRegisterForm;
import com.example.hospital.api.control.form.UpdateUserPasswordForm;
import com.example.hospital.api.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/login")
    public R loginOrRegister(@RequestBody @Valid InsertLoginForm form){
        Map param= BeanUtil.beanToMap(form);
        HashMap map=userService.login(param);
        Integer id=MapUtil.getInt(map,"id");
        if(id!=null){
            StpUtil.login(id);
            String token=StpUtil.getTokenValue();
            return R.ok().put("token",token).put("userId",id);
        }
        return R.ok().put("result",map);
    }
    @PostMapping("/register")
    public R loginOrRegister(@RequestBody @Valid InsertRegisterForm form){
        Map param= BeanUtil.beanToMap(form);
        String register = userService.Register(param);
        return R.ok().put("result",register);
    }

    @GetMapping("/searchUserInfo")
    @SaCheckLogin
    public R searchUserInfo(){
        int userId=StpUtil.getLoginIdAsInt();
        HashMap map=userService.searchUserInfo(userId);
        return R.ok().put("result",map);
    }

    @PostMapping("/searchUserMessage")
    @SaCheckLogin
    public R searchUserMessage(@RequestParam("userId") Integer userId){
        HashMap map=userService.searchUserInfo(userId);
        return R.ok().put("result",map);
    }

    @PostMapping("/updatePassword")
    public R updatePassword(@RequestBody @Valid UpdateUserPasswordForm form){
        Map param=BeanUtil.beanToMap(form);
        String s = userService.updatePassword(param);
        return R.ok().put("result",s);
    }
}
