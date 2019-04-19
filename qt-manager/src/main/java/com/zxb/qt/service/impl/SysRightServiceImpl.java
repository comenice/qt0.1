package com.zxb.qt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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

    @Autowired
    private SysRightMapper sysRightMapper;

    @Override
    public List<SysRight> list() {
        return sysRightMapper.selectList( new QueryWrapper<SysRight>());
    }
}
