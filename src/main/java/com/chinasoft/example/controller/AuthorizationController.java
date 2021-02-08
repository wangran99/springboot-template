package com.chinasoft.example.controller;

import com.chinasoft.example.constant.Constants;
import com.chinasoft.example.redis.RedisService;
import com.github.wangran99.welink.api.client.openapi.OpenAPI;
import com.github.wangran99.welink.api.client.openapi.model.AuthRes;
import com.github.wangran99.welink.api.client.openapi.model.UserBasicInfoRes;
import com.github.wangran99.welink.api.client.openapi.model.UserIdInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/authorization")
@Slf4j
public class AuthorizationController {

    //welink轻应用扫码鉴权地址前缀
    private static final String managerAuthUrl = "https://login.welink.huaweicloud.com/sso/oauth2/authorize?";

    @Value("${welink.openapi.client-id}")
    String clientId = "20200403100832764837661";

    @Value("${welink.openapi.client-secret}")
    String clientSecret;

    //welink鉴权成功后跳转到的地址
    @Value("${welink.authorization.callback.url}")
    String redirectUri;

    @Autowired
    AuthRes authRes;

    @Autowired
    private RedisService redisService;

    @Autowired
    private OpenAPI openAPI;

    /**
     * 手机We码认证
      * @param authCode 前端we码获取的authCode
     * @param response
     * @return
     */
    @GetMapping(value = "/wecode")
    public UserBasicInfoRes authorization(String authCode, HttpServletResponse response) {
        //获取用户id和租户id
        UserIdInfo userInfo = openAPI.getUserBasicIdInfo(authRes.getAccess_token(), authCode);
        //获取个人信息。
        UserBasicInfoRes userBasicInfoRes = openAPI.getUserInfoById(authRes.getAccess_token(), userInfo.getUserId());
        redisService.saveUserInfo(authCode, userBasicInfoRes);
        response.setHeader(Constants.AUTH_CODE, authCode);
        return userBasicInfoRes;
    }

    /**
     * 用户退出(删除token)
     * @param request
     * @return
     */
    @GetMapping(value = "/logout")
    public boolean logout(HttpServletRequest request) {
        redisService.delUserInfo(request.getHeader(Constants.AUTH_CODE));
        return true;
    }
}
