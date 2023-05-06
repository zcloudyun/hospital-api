package com.example.hospital.api.control;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.example.hospital.api.common.PageUtils;
import com.example.hospital.api.common.R;
import com.example.hospital.api.control.form.*;
import com.example.hospital.api.service.MisUserService;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mis_user")
public class MisUserController {
    @Resource
    private MisUserService misUserService;
    //post请求
    @PostMapping("/login")
    //@RequstBody从请求里面提取数据把它保存在Form类相关的变量上
    // @Valid校验Form里面保存的数据
    public R login(@RequestBody @Valid LoginForm form){
        //调用BeanUtil工具里面的beanToMap方法把java对象转成Map对象
        Map param= BeanUtil.beanToMap(form);
        Integer userId=misUserService.login(param);
        System.out.println(userId);
        //登录成功颁发token令牌
        if(userId!=null){
            //调用SaToken框架的方法颁发令牌
            //首先调用SaToken下的工具类StpUtil下的静态方法把用户的主键传进去
            StpUtil.login(userId);
            //获取token值
            String token=StpUtil.getTokenValue();
            System.out.println(token);
            //获取用户拥有的权限
            List<String> permissions=misUserService.searchUserPermissions(userId);
            //链式调用的方式将三个数据返回给前端
            return R.ok().put("result",true).put("token",token).put("permission",permissions).put("doctorId",userId);
        }
        return R.ok().put("result",false);
    }
    @GetMapping("/logout")
    @SaCheckLogin
    public R logout(){
        StpUtil.logout();
        return R.ok();
    }

    @PostMapping("/searchUserByPages")
    @SaCheckLogin
    public R searchUserByPages(@RequestBody @Valid SearchUserBypagesForm form){
        Map param = BeanUtil.beanToMap(form);
        int page = form.getPage();
        int length = form.getLength();
        //从当前页的第-开始
        int start = (page - 1) * length;
        param.put("start", start);
        PageUtils pageUtils = misUserService.searchUserByPages(param);
        return R.ok().put("result", pageUtils);
    }

    @PostMapping("/insertUser")
    @SaCheckLogin
    public R insertUser(@RequestBody @Valid InsertUserForm form){
        Map param = BeanUtil.beanToMap(form);
        misUserService.insertUser(param);
        return R.ok();
    }

    @PostMapping("/updateUser")
    @SaCheckLogin
    public R updateUser(@RequestBody @Valid UpdateUserForm form){
        Map param = BeanUtil.beanToMap(form);
        misUserService.updateUser(param);
        return R.ok();
    }

    @PostMapping("/deleteUser")
    public R deleteUser(@RequestBody @Valid DeleteUserForm form){
        misUserService.deleteUser(form.getIds());
        return R.ok();
    }

    @PostMapping("/updatePassword")
    public R updatePassword(@RequestBody @Valid UpdatePasswordForm form){
        Integer userId = StpUtil.getLoginIdAsInt();
        form.setUserId(userId);
        Map param=BeanUtil.beanToMap(form);
        String result = misUserService.updatePassword(param);
        return R.ok().put("result",result);

    }

}
