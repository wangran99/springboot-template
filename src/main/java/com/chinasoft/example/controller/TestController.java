package com.chinasoft.example.controller;

//import com.chinasoft.example.service.ILoginUserService;
import com.chinasoft.example.mapper.LoginUserMapper;
import com.chinasoft.example.service.ILoginUserService;
import com.github.wangran99.welink.api.client.openapi.OpenAPI;
import com.github.wangran99.welink.api.client.openapi.model.AuthRes;
import com.github.wangran99.welink.api.client.openapi.model.TenantInfoRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 测试服务是否正常启动
 */
@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

    @Autowired
    OpenAPI openAPI;
    @Autowired
    AuthRes authRes;
    @Autowired
    ILoginUserService iLoginUserService;

    @Autowired
    HttpMessageConverters httpMessageConverters;

    /**
     * 测试返回字符串
     * @return
     */
    @GetMapping("hello")
    public String hello(){
        return "hello world.";
    }

    /**
     * 测试后台welink调用是否成功
     * @return
     */
    @GetMapping("welink")
    public TenantInfoRes welink(){
        TenantInfoRes tenantInfoRes = openAPI.getTenantInfo(authRes.getAccess_token());
        return tenantInfoRes;
    }

    @Autowired
    LoginUserMapper loginUserMapper;
    /**
     * 测试数据库读取是否正常
     * @return
     */
    @GetMapping("time")
    public Object time(){
       return loginUserMapper.selectList(null);
//        return null;
    }
}
