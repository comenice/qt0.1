package com.zxb.qt.service;

import com.zxb.qt.exploit.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author  --郑晓波-- 
 * @since 2019-04-07
 */

@Service
public interface IUserService extends IService<User> {


    Boolean userIsExist( String userName );


}
