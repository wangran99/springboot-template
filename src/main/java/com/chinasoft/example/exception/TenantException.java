package com.chinasoft.example.exception;


import com.chinasoft.example.constant.ResultCode;

/**
 * 访问非本租户数据异常
 */
public class TenantException extends CommonException{

    public TenantException() {
        super(ResultCode.TenantException);
    }
}
