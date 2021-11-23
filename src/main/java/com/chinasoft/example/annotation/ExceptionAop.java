package com.chinasoft.example.annotation;

import com.chinasoft.example.constant.Constants;
import com.chinasoft.example.exception.CommonException;
import com.chinasoft.example.exception.RequestLimitException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * 异常切面。记录异常上下文（打印发生异常时所在的方法和参数）
 */
@Aspect
@Component
@Slf4j
public class ExceptionAop {

    @Autowired
    private RedisTemplate redisTemplate;

    @Around(value = "execution(* com.chinasoft.example.controller..*.*(..))")
    public Object invoke(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        int modifiers = targetMethod.getModifiers();
        Class clazz = targetMethod.getDeclaringClass();

//        RequestLimit requestLimit = targetMethod.getDeclaredAnnotation(RequestLimit.class);
//        if (requestLimit == null)
//            requestLimit = (RequestLimit) clazz.getAnnotation(RequestLimit.class);
//        if (requestLimit != null && Modifier.isPublic(modifiers))//有注解，且方法为public
//            checkWithRedis(requestLimit);
        return joinPoint.proceed();
    }

    // 异常通知
    @AfterThrowing(value = "execution(* com.chinasoft.example.controller..*.*(..))", throwing = "throwable")
    public void afterThrowing(JoinPoint joinPoint, Throwable throwable) {
        Object[] args = joinPoint.getArgs();
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        String[] parameterNames = methodSignature.getParameterNames();
        Method method = methodSignature.getMethod();

        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes()).getRequest();
        String fromUrl = request.getHeader("url");
        log.error("exception!request page full url:{}", fromUrl);
        log.error("exception!method full name:{}", getClassAndMethodName(method));
        for (int i = 0; i < parameterNames.length; i++)
            log.error("exception!parameterName:{} = {}", parameterNames[i], args[i] == null ? "null" : args[i].toString());
        if (throwable instanceof RuntimeException)
            log.error("runtime exception.");
        else {
            log.error("not runtime exception",throwable);
            throwable.printStackTrace();
        }
        throw throwable instanceof RuntimeException ? (RuntimeException) throwable : new CommonException(throwable.getMessage());
    }

    /**
     * 请求接口次数限制
     *
     * @param limit
     */
    private void checkWithRedis(RequestLimit limit) {
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes()).getRequest();
        String authCode = request.getHeader(Constants.AUTH_CODE);
        String requestLimitKey = "requestLimit:" + authCode + ":" + request.getRequestURI();
        long count = redisTemplate.opsForValue().increment(requestLimitKey, 1);
        if (count == 1) {
            redisTemplate.expire(requestLimitKey, limit.timeout(), TimeUnit.SECONDS);
        }
        if (count > limit.maxCount()) {
            throw new RequestLimitException();
        }
    }

    //获取方法类全名+方法名
    private String getClassAndMethodName(Method method) {
        //获取类全名
        String className = method.getDeclaringClass().getName();
        //获取方法名
        String methodName = method.getName();
        Parameter[] parameters = method.getParameters();
        StringBuffer stringBuffer = new StringBuffer(className).append(".")
                .append(methodName).append("(");
        Arrays.stream(parameters).forEach(e -> stringBuffer.append(e).append(", "));
        return stringBuffer.append(")").toString();
    }
}
