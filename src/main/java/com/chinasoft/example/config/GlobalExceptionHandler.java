package com.chinasoft.example.config;


import com.chinasoft.example.exception.CommonException;
import com.chinasoft.example.exception.RequestLimitException;
import com.chinasoft.example.exception.TenantException;
import com.github.wangran99.welink.api.client.openapi.model.AuthFailOrExpiredException;
import com.github.wangran99.welink.api.client.openapi.model.OpenApiException;
import com.github.wangran99.welink.api.client.openapi.model.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;


/**
 * @author ：WangRan
 * @date ：Created in 2020/12/6 11:31
 * @description：全局异常管理
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * IO异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = IOException.class)
    public ResultVO handlerCommonException(IOException e) {
        log.error("io异常！", e);
        return ResultVO.getError("IO读写异常");
    }


    /**
     * 一般Exception
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResultVO handlerExceptionHello(Exception e) {
        log.error("exception！" + e.getMessage(), e);
        return ResultVO.getError(e.getMessage());
    }


    /**
     * 接口请求次数超过限制次数异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(RequestLimitException.class)
    public ResultVO handleRequestLimitException(RequestLimitException e) {
        log.warn("RequestLimitException--{}", e.getMessage(), e);
        return ResultVO.getError("同一个接口请求次数过多，请稍后再试");
    }

    /**
     * 无权限访问其他租户数据异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(TenantException.class)
    public ResultVO handleTenantException(TenantException e) {
        log.error("TenantException--{}", e.getMessage(), e);
        return ResultVO.getError(e.getMessage());
    }

    /**
     * 调用Welink开放平台时出现的异常处理逻辑
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = OpenApiException.class)
    public ResultVO handlerOpenApiException(OpenApiException e) {
        log.error("OpenApiException: errCode:" + e.getCode() + ",msg:" + e.getMsg(), e);
        return ResultVO.getError("开放平台接口异常");
    }

    /**
     * 用户认证失败或者认证已过期的处理逻辑
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = AuthFailOrExpiredException.class)
    public ResultVO handlerAuthFailOrExpiredException(AuthFailOrExpiredException e) {
        log.warn("AuthFailOrExpiredException: errCode:" + e.getCode() + ",msg:" + e.getMsg(), e);
        return ResultVO.getAuthFailOrExpired("登录token过期，请重新登录", e.getAuthUrl());
    }

    /**
     * 空指针异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = NullPointerException.class)
    public ResultVO handlerNullPointerException(NullPointerException e) {
        log.error("发生空指针异常！", e);
        return ResultVO.getError("空指针异常");
    }

    /**
     * 数据库字段约束异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
    public ResultVO handlerSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e) {
        log.error("数据库字段约束异常！", e);
        return ResultVO.getError("数据库字段约束异常");
    }

    /**
     * 数据库字段重复异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = DuplicateKeyException.class)
    public ResultVO handlerDuplicateKeyException(DuplicateKeyException e) {
        log.error("数据库字段重复！", e);
        return ResultVO.getError("字段值应该在同一租户/分级下唯一，不允许重复");
    }
    /**
     * 通用异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = CommonException.class)
    public ResultVO handlerCommonException(CommonException e) {
        log.error("发生通用异常！", e);
        return ResultVO.getError(e.getMessage());
    }




}