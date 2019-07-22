package com.zxb.qt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zxb.qt.exploit.common.redis.RedisUtils;
import com.zxb.qt.exploit.entity.SysRight;
import com.zxb.qt.exploit.mapper.SysRightMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxb.qt.service.ISysRightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author  --郑晓波-- 
 * @since 2019-04-09
 */

@Service
public class SysRightServiceImpl extends ServiceImpl<SysRightMapper, SysRight> implements ISysRightService {

    private String bsl = "blog:sysRight:list" ;

    @Autowired
    private SysRightMapper sysRightMapper;

    @Autowired
    private RedisUtils redisUtils;

     @Override
    public List<SysRight> list() {
        List list = null ;
        if ( redisUtils.hasKey( bsl )  ){
            list = redisUtils.lGet( bsl , 0 , 6 );
            return list ;
        }else {
            list = sysRightMapper.selectList( new QueryWrapper<SysRight>()) ;
            redisUtils.lSet( bsl , list );
            return list;
        }
    }




}
