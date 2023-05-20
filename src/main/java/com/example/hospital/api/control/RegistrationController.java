package com.example.hospital.api.control;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import com.example.hospital.api.common.R;
import com.example.hospital.api.control.form.*;
import com.example.hospital.api.service.RegistrationService;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/registration")
public class RegistrationController {
  @Resource
  private RegistrationService registrationService;

  @PostMapping("/searchCanRegisterInDateRange")
  @SaCheckLogin
  public R searchCanRegisterInDateRange(@RequestBody @Valid searchCanRegisterInDateRangeForm form){
      Map<String,Object> param= BeanUtil.beanToMap(form);
      ArrayList<HashMap> list=registrationService.searchCanRegisterInDateRange(param);
      return R.ok().put("result",list);
  }
//查询诊室可挂号医生的信息
  @PostMapping("/searchDeptSubDoctorPlanInDay")
  @SaCheckLogin
  public R searchDeptSubDoctorPlanInDay(@RequestBody @Valid searchDeptSubDoctorPlanInDayForm form){
    Map param=BeanUtil.beanToMap(form);
    ArrayList<HashMap> list=registrationService.searchDeptSubDoctorPlanInDay(param);
    return R.ok().put("result",list);
  }
//检测患者是否有挂号资格
  @PostMapping("/checkRegisterCondition")
  @SaCheckLogin
  public R checkRegisterCondition(@RequestBody @Valid CheckRegisterConditionForm form){
    int userId= StpUtil.getLoginIdAsInt();
    form.setUserId(userId);
    Map param=BeanUtil.beanToMap(form);
    String result=registrationService.checkRegisterCondition(param);
    return R.ok().put("result",result);
  }
//出诊时间段
  @PostMapping("/searchDoctorWorkPlanSchedule")
  @SaCheckLogin
  public R searchDoctorWorkPlanSchedule(@RequestBody @Valid SearchDoctorWorkPlanScheduleForm form){
    Map param=BeanUtil.beanToMap(form);
    ArrayList<HashMap> list=registrationService.searchDoctorWorkPlanSchedule(param);
    return R.ok().put("result",list);
  }
//微信支付
  @PostMapping("/registerMedicalAppointment")
  @SaCheckLogin
  public R registerMedicalAppointment(@RequestBody @Valid RegisterMedicalAppointmentForm form){
    int userId=StpUtil.getLoginIdAsInt();
    Map param=BeanUtil.beanToMap(form);
    param.put("userId",userId);
    HashMap result=registrationService.registerMedicalAppointment(param);
    if(result==null){
      return R.ok();
    }
    return R.ok(result);
  }
  //付款结果查询
  @PostMapping("/searchPaymentResult")
  @SaCheckLogin
  public R searchPaymentResult(@RequestBody @Valid SearchPaymentResultForm form){
    //创建支付单ID
    String transactionId= IdUtil.simpleUUID().toUpperCase();
    Map param=BeanUtil.beanToMap(form);
    String outTradeNo=MapUtil.getStr(param,"outTradeNo");
    //更新挂号记录的付款状态和付款账单ID
    registrationService.updatePayment(new HashMap(){{
      put("outTradeNo",outTradeNo);
      put("transactionId",transactionId);
      put("paymentStatus",2);
    }});
    boolean bool=registrationService.searchPaymentResult(form.getOutTradeNo());
    return R.ok().put("result",bool);
  }
  //挂号单
  @PostMapping("/searchRegistrationInfo")
  @SaCheckLogin
  public R searchRegistrationInfo(@RequestBody @Valid SearchRegistrationInfoForm form){
    int userId=StpUtil.getLoginIdAsInt();
    return R.ok();
  }

  //查询挂号成功患者信息
  @PostMapping ("/searchBystatus")
  @SaCheckLogin
  public R searchBystatus(@RequestBody @Valid SearchBystatusForm form){
    int userId=StpUtil.getLoginIdAsInt();
    Map param= BeanUtil.beanToMap(form);
    param.replace("userId",userId);
    ArrayList<HashMap> list=registrationService.searchByStatus(param);
    return R.ok().put("result",list);
  }
  //根据用户id和付款状态查看
  @PostMapping("/searchByUserId")
//  @SaCheckLogin
  public R searchByUserId(@RequestBody @Valid SearchByUserIdForm form){
    int userId = StpUtil.getLoginIdAsInt();
    Map param = BeanUtil.beanToMap(form);
    param.replace("userId",userId);
    ArrayList<HashMap> list = registrationService.searchByUserId(param);
    return R.ok().put("result",list);
  }

  //取消订单
  @PostMapping("/updateByRegistrationId")
//  @SaCheckLogin
  public R updateByRegistrationId(@RequestBody @Valid UpdateByRegistrationForm form){
    Integer registrationId = form.getRegistrationId();
    registrationService.updateByRegistrationId(registrationId);
    return R.ok();
  }
  //查看订单详情
  @PostMapping("/searchRegistrationById")
//  @SaCheckLogin
  public R searchRegistrationById(@RequestBody @Valid SearchRegistrationByIdForm form){
    Map param=BeanUtil.beanToMap(form);
    HashMap map = registrationService.searchRegistrationById(param);
    return R.ok().put("result",map);
  }
}
