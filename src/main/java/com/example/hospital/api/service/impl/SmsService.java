package com.example.hospital.api.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.RandomUtil;
import com.example.hospital.api.util.SmsUtil;
import kim.wind.sms.api.SmsBlend;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SmsService {

    @Resource
    private SmsUtil smsUtil;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 发送验证码
     * @param phone
     * @return
     */
    public String sendMessage(String phone) {

        // 生成验证码
        String code = RandomUtil.randomNumbers(6);

        // 存到redis: phone-code, 五分钟有效期
        String key = "code:phone:" + phone;
        this.stringRedisTemplate.opsForValue().set(key, code, 5, TimeUnit.MINUTES);

        // 发送验证码
        this.smsUtil.sendMessage(phone, code);

        // 返回验证码
        return code;
    }

    /**
     * 校验验证码是否正确
     * @param phone
     * @param code
     * @return
     */
    public boolean checkCode(String phone, String code) {

        // 获取redis 验证码
        String key = "code:phone:" + phone;
        String redisCode = this.stringRedisTemplate.opsForValue().get(key);

        // 比对验证码
        if (BooleanUtils.isFalse(Objects.equals(redisCode, code))) {
            // 验证码错误
            return Boolean.FALSE;
        }

        // 验证码正确：删除验证码，返回正确
        this.stringRedisTemplate.delete(key);

        return Boolean.TRUE;
    }



}
