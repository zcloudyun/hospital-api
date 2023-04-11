package com.example.hospital.api.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.hospital.api.db.Entity.MedicalDeptEntity;
import com.example.hospital.api.db.Entity.MedicalDeptSubAndDoctorEntity;
import com.example.hospital.api.db.Entity.MedicalDeptSubEntity;
import com.example.hospital.api.db.Entity.request.MedicineDeptManageRequest;
import com.example.hospital.api.db.Entity.vo.MedicalDepartmentManagementVO;
import com.example.hospital.api.db.dao.MedicalDeptDao;
import com.example.hospital.api.db.dao.MedicalDeptSubAndDoctorDao;
import com.example.hospital.api.service.DoctorPriceService;
import com.example.hospital.api.service.MedicalDeptService;
import com.example.hospital.api.service.MedicalDeptSubAndDoctorService;
import com.example.hospital.api.service.MedicalDeptSubService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MedicalDeptServiceImpl extends ServiceImpl<MedicalDeptDao, MedicalDeptEntity> implements MedicalDeptService {
    @Resource
    private MedicalDeptDao medicalDeptDao;
    @Resource
    private MedicalDeptSubService medicalDeptSubService;
    @Resource
    private MedicalDeptSubAndDoctorService medicalDeptSubAndDoctorService;
    @Override
    public ArrayList<HashMap> searchAll(){
        ArrayList<HashMap> list=medicalDeptDao.searchAll();
        return list;
    }

    @Override
    public HashMap searchDeptAndSub(){
        ArrayList<HashMap> list=medicalDeptDao.searchDeptAndSub();
        LinkedHashMap map=new LinkedHashMap();
        for(HashMap one :list){
            Integer deptId= MapUtil.getInt(one,"deptId");
            Integer subId=MapUtil.getInt(one,"subId");
            String deptName=MapUtil.getStr(one,"deptName");
            String subName=MapUtil.getStr(one,"subName");
            if(map.containsKey(deptName)){
                ArrayList<HashMap> subList=(ArrayList<HashMap>) map.get(deptName);
                subList.add(new HashMap(){{
                    put("subId",subId);
                    put("subName",subName);
                }});
            }else{
                map.put(deptName,new ArrayList(){{
                    add(new HashMap(){{
                        put("subId",subId);
                        put("subName",subName);
                    }});
                }});
            }
        }
        return map;
    }

    @Override
    public ArrayList<HashMap> searchMedicalDeptList(Map param){
        ArrayList<HashMap> list=medicalDeptDao.searchMedicalDeptList(param);
        return list;
    }

    /**
     * 医疗科室管理
     * @param request
     * @return
     */
    @Override
    public List<MedicalDepartmentManagementVO> getMedicineDepartmentManagement(MedicineDeptManageRequest request) {

        // 参数校验
        log.info("getMedicineDepartmentManagement: {}", request);

        // 查询科室
        List<MedicalDeptEntity> medicalDeptEntities = this.medicalDeptDao.selectMedicineDepartment(request);
        // 查询科室下诊室，医生
        List<MedicalDepartmentManagementVO> res = medicalDeptEntities.stream().map(dept -> {
            MedicalDepartmentManagementVO vo = new MedicalDepartmentManagementVO();
            // 获取诊室
            List<MedicalDeptSubEntity> subDeptList = this.medicalDeptSubService.getMedicalDeptSubListByDeptId(dept.getId());
            // 获取诊室下医生
            List<Integer> subIdList = subDeptList.stream().map(MedicalDeptSubEntity::getId).collect(Collectors.toList());
            List<MedicalDeptSubAndDoctorEntity> doctorEntityList = this.medicalDeptSubAndDoctorService.getSubDeptDoctorListBatchIds(subIdList);
            // 设置VO对象
            vo.setSubDeptCount(subDeptList.size()).setDoctorCount(doctorEntityList.size())
                    .setId(dept.getId()).setName(dept.getName()).setDescription(dept.getDescription())
                    .setOutpatient(dept.getOutpatient()).setRecommended(dept.getRecommended());
            return vo;
        }).collect(Collectors.toList());

        return res;
    }
    @Override
    public void insert(Map param){
        MedicalDeptEntity entity= BeanUtil.toBean(param,MedicalDeptEntity.class);
        medicalDeptDao.insert(entity);
    }

    @Override
    public void updateById(Map param){
        medicalDeptDao.updateById(param);
    }



}
