package com.zxb.qt.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.zxb.qt.exploit.common.ajax.AjaxResponse;
import com.zxb.qt.exploit.common.layui.pojo.FlowPojo;
import com.zxb.qt.exploit.common.websocket.GameRank;
import com.zxb.qt.exploit.entity.Games;
import com.zxb.qt.exploit.entity.vo.GameVO;
import com.zxb.qt.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping( "games" )
public class GameController {

    @Autowired
    @Qualifier( value = "gmaeServiceImpl")
    private GameService gameService;



    @RequestMapping("rank")
    public String rank(){
        return "/user/rank" ;
    }

    /**
     * 更新当前用户排行榜信息
     * @param games
     * @return
     */
    @RequestMapping( "saveRank" )
    @ResponseBody
    public AjaxResponse saveRank( Games games ){
        return gameService.saveOrUpdateRank( games );
    }

    /**
     * 分页拉取排行榜信息
     * @param left
     * @param right
     * @return
     */
    @RequestMapping( "getRank" )
    @ResponseBody
    public FlowPojo getRank(Long limit , @RequestParam(required = false , defaultValue = "10") Long size ){
        Long left = 0l;
        Long right = 0l ;
        if ( limit > 0  ){
            long mlimit = limit-1 ;
            left = mlimit * size ;
            right =  size * limit ;
        }
        return gameService.getTopRange( left , right ) ;
    }


}
