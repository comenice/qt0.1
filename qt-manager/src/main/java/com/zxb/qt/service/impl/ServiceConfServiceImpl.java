package com.zxb.qt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zxb.qt.exploit.entity.ServiceConf;
import com.zxb.qt.exploit.mapper.ServiceConfMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxb.qt.service.IServiceConfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author  --郑晓波-- 
 * @since 2019-04-19
 */
@Service
public class ServiceConfServiceImpl extends ServiceImpl<ServiceConfMapper, ServiceConf> implements IServiceConfService{

    @Autowired
    private ServiceConfMapper serviceConfMapper;

    @Override
    public List<ServiceConf> list() {
        return serviceConfMapper.selectList( new QueryWrapper<ServiceConf>());
    }
}
