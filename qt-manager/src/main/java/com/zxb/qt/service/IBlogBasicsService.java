package com.zxb.qt.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zxb.qt.exploit.common.ajax.AjaxResponse;
import com.zxb.qt.exploit.entity.BlogBasics;
import com.zxb.qt.exploit.entity.vo.BlogBasicDetailVO;
import com.zxb.qt.exploit.entity.vo.BlogBasicTagVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author  --郑晓波-- 
 * @since 2019-04-21
 */
public interface IBlogBasicsService extends IService<BlogBasicTagVO> {

    AjaxResponse saveBlog(BlogBasics blog, int[] tags );

    BlogBasicDetailVO queryBlogdetail(Integer blogId );


}
