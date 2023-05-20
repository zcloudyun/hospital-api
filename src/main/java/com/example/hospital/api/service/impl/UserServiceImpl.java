package com.example.hospital.api.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.example.hospital.api.db.Entity.MisUserEntity;
import com.example.hospital.api.db.Entity.UserEntity;
import com.example.hospital.api.db.Entity.UserInfoCardEntity;
import com.example.hospital.api.db.dao.UserDao;
import com.example.hospital.api.db.dao.UserInfoCardDao;
import com.example.hospital.api.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Value("${wechat.app-id}")
    private String appId;

    @Value("${wechat.app-secret}")
    private String appSecret;

    @Resource
    private UserDao userDao;

    @Resource
    private UserInfoCardDao userInfoCardDao;

    @Override
    public HashMap login(Map param){
        String username = MapUtil.getStr(param, "nickname");
        ArrayList<String> list = userDao.searchUsername();
        HashMap map1=new HashMap();
        boolean result = list.contains(username);
        if(result){
            String password = MapUtil.getStr(param, "password");
            MD5 md5=MD5.create();
            //进行哈希加密
            String temp=md5.digestHex(username);
            //前6位字符
            String tempStart= StrUtil.subWithLength(temp,0,6);
            //后三位字符
            String tempEnd=StrUtil.subSuf(temp,temp.length()-3);
            //混淆原始密码并哈希加密
            password=md5.digestHex(tempStart+password+tempEnd);
            HashMap map = userDao.searchUserMessage(username);
            String password1=MapUtil.getStr(map,"password");
            if(password1.equals(password)){
                return map;
            }else{
                map1.put("result","密码错误");
            }
        }
        else{
            map1.put("result","用户名未存在，请去注册");
        }
        return map1;
    }

    @Override
    public String Register(Map param){
        String username = MapUtil.getStr(param, "nickname");
        ArrayList<String> list = userDao.searchUsername();
        boolean result = list.contains(username);
        if(result){
            return "已存在该用户名，请前往登录";
        }else{
            String password = MapUtil.getStr(param, "password");
            MD5 md5=MD5.create();
            //进行哈希加密
            String temp=md5.digestHex(username);
            //前6位字符
            String tempStart= StrUtil.subWithLength(temp,0,6);
            //后三位字符
            String tempEnd=StrUtil.subSuf(temp,temp.length()-3);
            //混淆原始密码并哈希加密
            password=md5.digestHex(tempStart+password+tempEnd);
            param.replace("password",password);
            UserEntity entity= BeanUtil.toBean(param,UserEntity.class);
            userDao.insert(entity);
            return "注册成功";
        }
    }
//    @Override
//    public HashMap loginOrRegister(String code,String nickname,String sex){
//        //用临时授权兑换open_id
////        String openId=this.getOpenId(code);
//
//        HashMap map=new HashMap();
//        //是否为已注册用户
//        Integer id=userDao.searchAlreadyRegistered(code);
//        if(id==null){
//            UserEntity entity=new UserEntity();
//            entity.setUuid(code);
//            entity.setNickname(nickname);
//            entity.setSex(sex);
//            entity.setStatus((byte)+1);
//            //执行新用户注册
//            userDao.insert(entity);
//            //查询新用户的主键值
//            id=userDao.searchAlreadyRegistered(entity.getUuid());
//            map.put("msg","注册成功");
//        }
//        else{
//            map.put("msg","登陆成功");
//        }
//        //查询患者信息卡中的电话号码
//        String tel=userInfoCardDao.searchUserTel(id);
//        map.put("id",id);
//        map.put("tel",tel);
//        return map;
//    }

//    private String getOpenId(String code){
//        String url="https://api.weixin.qq.com/sns/jscode2session";
//        HashMap map=new HashMap();
//        map.put("appid",appId);
//        map.put("secret",appSecret);
//        map.put("js_code",code);
//        map.put("grant_type","authorization_code");
//        String response= HttpUtil.post(url,map);
//        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(response);
//        String openId=jsonObject.getString("openid");
//        if(openId==null || openId.length()==0){
//            throw new RuntimeException("临时登陆凭证错误");
//        }
//        return openId;
//    }

    @Override
    public HashMap searchUserInfo(int userId){
        HashMap map=userDao.searchUserInfo(userId);
        String tel=userInfoCardDao.searchUserTel(userId);
        map.put("tel",tel);
        return map;
    }

    @Override
    public String updatePassword(Map param){
        //获取用户名
        String username = MapUtil.getStr(param,"nickname");
        //得到数据库中所有的用户名
        ArrayList<String> list = userDao.searchUsername();
        //查询数据库中是否存在该用户名
        boolean result = list.contains(username);
        //如果存在用户名，根据用户名查询用户名对应的密码，并判断密码是否正确
        if(result) {
            //获取用户输入的原密码
            String password = MapUtil.getStr(param, "password");
            String newPassword = MapUtil.getStr(param, "newPassword");
            MD5 md5 = MD5.create();
            //进行哈希加密
            String temp = md5.digestHex(username);
            //前6位字符
            String tempStart = StrUtil.subWithLength(temp, 0, 6);
            //后三位字符
            String tempEnd = StrUtil.subSuf(temp, temp.length() - 3);
            //混淆原始密码并哈希加密
            password = md5.digestHex(tempStart + password + tempEnd);
            newPassword = md5.digestHex(tempStart + newPassword + tempEnd);
            //从数据库中查询原密码
            HashMap map = userDao.searchUserMessage(username);
            String password1=MapUtil.getStr(map,"password");
            //如果原密码正确，则修改密码
            if (password1.equals(password)) {
                param.replace("newPassword", newPassword);
                userDao.updatePassword(param);
                return "密码修改成功";
            }
            return "原密码错误";
        }else{
            return "用户名不存在";
        }
    }
    @Override
    public UserInfoCardEntity getPatientById(int userId){
       return userInfoCardDao.searchUserlist(userId);
    }
}

