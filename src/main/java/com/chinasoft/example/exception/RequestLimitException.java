package com.chinasoft.example.exception;

import com.chinasoft.example.constant.ResultCode;

public class RequestLimitException extends CommonException{

    public RequestLimitException() {
        super(ResultCode.RequestLimitException);
    }
}
