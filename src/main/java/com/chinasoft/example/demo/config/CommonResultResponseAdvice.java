package com.chinasoft.example.demo.config;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 为所有的rest接口返回的数据自动构建统一的数据格式
 * @author WangRan
 */
@EnableWebMvc
@Configuration
@RestControllerAdvice(basePackages = "com.chinasoft.example.demo")
public class CommonResultResponseAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof Exception) {
            return ResultVO.getError(((Exception) body).getMessage());
        }
        if (body instanceof String) {
            ObjectMapper om = new ObjectMapper();
            try {
                return om.writeValueAsString(ResultVO.getSuccess(body));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return ResultVO.getSuccess(body);
    }
}