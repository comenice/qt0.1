package com.zxb.qt.service.impl;

import com.zxb.qt.exploit.entity.User;
import com.zxb.qt.exploit.mapper.UserMapper;
import com.zxb.qt.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author  --郑晓波-- 
 * @since 2019-04-07
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
