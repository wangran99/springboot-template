package com.chinasoft.example.config;


import com.chinasoft.example.exception.CommonException;
import com.github.wangran99.welink.api.client.openapi.model.AuthFailOrExpiredException;
import com.github.wangran99.welink.api.client.openapi.model.OpenApiException;
import com.github.wangran99.welink.api.client.openapi.model.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


/**
 * @author ：WangRan
 * @date ：Created in 2020/12/6 11:31
 * @description：全局异常管理
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    /**
     * 一般Exception
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResultVO handlerExceptionHello(Exception e) {
        log.error("exception" + e.getMessage(), e);
        return ResultVO.getError(e.getMessage());
    }

    /**
     * 参数合法性校验异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultVO handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("参数校验异常-{}--{}", e.getParameter(), e.getMessage(), e);
        return ResultVO.getError(e.getMessage());
    }

    /**
     * 参数合法性校验异常-类型不匹配
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResultVO handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("参数校验异常--{}-{}", e.getParameter(), e.getMessage(), e);
        return ResultVO.getError(e.getMessage());
    }

    /**
     * 调用welink开放平台时出现的异常处理逻辑
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = OpenApiException.class)
    public ResultVO handlerOpenApiException(OpenApiException e) {
        log.error("OpenApiException: errCode:" + e.getCode() + ",msg:" + e.getMsg(), e);
        return ResultVO.getError(e.getMessage());
    }

    /**
     * 用户认证失败或者认证已过期的处理逻辑
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = AuthFailOrExpiredException.class)
    public ResultVO handlerAuthFailOrExpiredException(AuthFailOrExpiredException e) {
        log.error("AuthFailOrExpiredException: errCode:" + e.getCode() + ",msg:" + e.getMsg(), e);
        return ResultVO.getAuthFailOrExpired(e.getMessage(),e.getAuthUrl());
    }

    /**
     * 空指针异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = NullPointerException.class)
    public ResultVO handlerNullPointerException(NullPointerException e) {
        log.error("发生空指针异常！原因是:", e);
        return ResultVO.getError(e.getMessage());
    }

    /**
     * 通用异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = CommonException.class)
    public ResultVO handlerCommonException(CommonException e) {
        log.error("发生通用异常！原因是:", e);
        return ResultVO.getError(e.getMessage());
    }

}