package com.zxb.qt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zxb.qt.exploit.entity.User;
import com.zxb.qt.exploit.mapper.UserMapper;
import com.zxb.qt.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private UserMapper userMapper;

    @Override
    public Boolean userIsExist( String userName ) {
        User user = userMapper.selectOne( new QueryWrapper<User>().eq("USER_NAME",userName));
        return user == null ? false : true ;
    }

}
