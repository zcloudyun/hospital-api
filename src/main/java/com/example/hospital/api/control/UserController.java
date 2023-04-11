package com.example.hospital.api.control;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.map.MapUtil;
import com.example.hospital.api.common.R;
import com.example.hospital.api.control.form.LoginOrRegisterForm;
import com.example.hospital.api.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/loginOrRegister")
    public R loginOrRegister(@RequestBody @Valid LoginOrRegisterForm form){
        HashMap map=userService.loginOrRegister(form.getCode(), form.getNickname(), form.getSex());
        log.info("map的值{}",map);
        String msg= MapUtil.getStr(map,"msg");
        Integer id=MapUtil.getInt(map,"id");
        StpUtil.login(id);
        String token=StpUtil.getTokenValue();
        return R.ok(msg).put("token",token);
    }

    @GetMapping("/searchUserInfo")
    @SaCheckLogin
    public R searchUserInfo(){
        int userId=StpUtil.getLoginIdAsInt();
        HashMap map=userService.searchUserInfo(userId);
        return R.ok().put("result",map);
    }
}
