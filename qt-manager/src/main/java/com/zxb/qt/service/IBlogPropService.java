package com.zxb.qt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zxb.qt.exploit.entity.vo.BlogBasicPropVO;
import com.zxb.qt.exploit.entity.vo.BlogBasicPropVO;

import java.util.Map;

public interface IBlogPropService extends IService<BlogBasicPropVO> {

    /**
     * @param page
     * @return 博客属性 浏览量 ...
     */
//    List<BlogBasicPropVo> queryBlogProp(Page page ); mybatisplus  分页方法代替

   // public Map<Object, Object> queryBlogPropCache (Page page   );


}
