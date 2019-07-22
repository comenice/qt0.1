package MybatisPlus.QueryMapperTest;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zxb.qt.QtApplication;
import com.zxb.qt.exploit.common.redis.RedisUtils;
import com.zxb.qt.exploit.entity.SysRole;
import com.zxb.qt.exploit.entity.User;
import com.zxb.qt.exploit.entity.vo.BlogBasicDetailVO;
import com.zxb.qt.exploit.mapper.*;
import com.zxb.qt.service.IBlogBasicsService;
import com.zxb.qt.service.IBlogCommentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest( classes = QtApplication.class)
public class test {

   @Autowired
   private UserMapper userMapper;
   @Autowired
   private SysRoleMapper sysRoleMapper;

   @Autowired
   private BlogBasicsTagVoMapper blogBasicsTagVoMapper;

   @Autowired
   private BlogCommentMapper blogCommentMapper;

   @Qualifier("blogCommentServiceImpl")
   @Autowired
   private IBlogCommentService iBlogCommentService;

   @Autowired
   @Qualifier( "blogBasicsServiceImpl" )
   private IBlogBasicsService iBlogBasicsService;

//    @Autowired
//    private SysRightMapper sysRightMapper;
//
//
//    @Autowired
//    private RedisUtils redisUtilsp;
//    //private StringRedisTemplate redisTpl; //jdbcTemplate
//
//    // 授权
//    // 赋予用户权限
//
//
//    public void testQueryWrapper(){
//        String userName = "我是user" ;
//        //QueryWrapper 条件构造器   wrapper 装饰者设计模式 加强原有的方法
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//
//        Wrapper<User> user_name = queryWrapper.eq("USER_NAME", userName);
//        User user = userMapper.selectOne(user_name);
//        System.out.println( user );
//
//        System.out.println( sysRoleMapper.selectList( new QueryWrapper<SysRole>() ) );
//
//    }
//    @Test
//    public  void testRoleMapper(){
//
//        redisUtilsp.set( "1:2:@" , "123"  );
//
//    }



    public void save(){
        int[] arr = new int[]{9,10,11};
        blogBasicsTagVoMapper.insertBlogTag( 1 ,arr );

    }

    public void blod(){
         BlogBasicDetailVO vo =  blogBasicsTagVoMapper.queryBlogBasicDetailById( 1 );
        System.out.println( vo );
    }


    @Test
    public void service(){
        BlogBasicDetailVO vo =  iBlogBasicsService.queryBlogdetail( 1 );
        System.out.println( vo );
    }

    @Test
    public void mp(){
        //blogCommentMapper.queryCommnetByUserId("40fffd99e5972a949292a258aad3fbb5").forEach(System.out::println);
    }

}
