package com.zxb.qt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxb.qt.exploit.common.ajax.AjaxResponse;
import com.zxb.qt.exploit.common.boot.exception.BlogException;
import com.zxb.qt.exploit.common.layui.pojo.FlowPojo;
import com.zxb.qt.exploit.common.websocket.GameRank;
import com.zxb.qt.exploit.entity.Games;
import com.zxb.qt.exploit.entity.vo.GameVO;
import com.zxb.qt.exploit.mapper.GamesMapper;
import com.zxb.qt.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class GmaeServiceImpl extends ServiceImpl<GamesMapper, Games> implements GameService {

    @Autowired
    private GamesMapper gameMapper;
    @Autowired
    private GameRank gameRank;

    private final static String rangKey = "blog:game:rank" ;

    @Override
    public AjaxResponse saveOrUpdateRank(Games games) {
       GameVO gameVO = gameRank.insertOrUpdate( rangKey , games , null );
        if ( gameVO == null ){
            return new AjaxResponse( "-1" , "排行数据更新失败" );
        }
       return new AjaxResponse( "1" , "成功更新排行数据" , gameVO );
    }

    @Override
    public FlowPojo getTopRange(long l1, long l2) {
        List list = gameRank.topRange( rangKey , l1 , l2 );
        System.out.println( "lsit size : " + list.size() );
        if ( list == null ){
            throw new BlogException( "-1" , "排行榜信息拉取失败 请重试" );
        }
        long size = gameRank.sizeRange( rangKey );
        return new FlowPojo( list ,   (long) Math.ceil(size/10 )  , size );
    }
}
