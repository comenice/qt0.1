package com.zxb.qt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxb.qt.exploit.common.ajax.AjaxResponse;
import com.zxb.qt.exploit.common.layui.pojo.FlowPojo;
import com.zxb.qt.exploit.entity.Games;


public interface GameService extends IService<Games> {

    AjaxResponse saveOrUpdateRank( Games games );

    FlowPojo getTopRange(long l1 , long l2 );


}
