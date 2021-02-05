package com.chinasoft.example.service.impl;

import com.chinasoft.example.entity.LoginUser;
import com.chinasoft.example.mapper.LoginUserMapper;
import com.chinasoft.example.service.ILoginUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author WangRan
 * @since 2021-01-07
 */
@Service
public class LoginUserServiceImpl extends ServiceImpl<LoginUserMapper, LoginUser> implements ILoginUserService {

}
