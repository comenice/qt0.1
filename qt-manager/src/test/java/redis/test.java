package redis;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zxb.qt.QtApplication;
import com.zxb.qt.exploit.common.boot.utils.EntityUtils;
import com.zxb.qt.exploit.common.redis.RedisKeys;
import com.zxb.qt.exploit.common.redis.RedisUtils;
import com.zxb.qt.exploit.entity.SysRight;
import com.zxb.qt.exploit.entity.SysRole;
import com.zxb.qt.exploit.entity.User;
import com.zxb.qt.exploit.entity.vo.BlogBasicPropVO;
import com.zxb.qt.exploit.entity.vo.UserRoleVO;
import com.zxb.qt.exploit.mapper.SysRightMapper;
import com.zxb.qt.exploit.mapper.SysRoleMapper;
import com.zxb.qt.exploit.mapper.UserMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest( classes = QtApplication.class)
public class test {

   @Autowired
   private RedisUtils redisUtilsp;
//    //private StringRedisTemplate redisTpl; //jdbcTemplate
//
//    private ZSetOperations zSet;
//
//    // 授权
//    // 赋予用户权限
//
//    @Before
//    public void aft(){
//        zSet = redisUtilsp.opsZSet();
//    }
//
//    @Test
//    public  void testRoleMapper(){
//
//        ZSetOperations.TypedTuple<Object> objectTypedTuple1 = new DefaultTypedTuple<Object>("美国队长", 2.0);//这里必须是2.0，因为那边是用Double收参
//        ZSetOperations.TypedTuple<Object> objectTypedTuple2 = new DefaultTypedTuple<Object>("蚁人", 0.0);
//        ZSetOperations.TypedTuple<Object> objectTypedTuple3 = new DefaultTypedTuple<Object>(  "雷神",0.0);
//        Set<ZSetOperations.TypedTuple<Object>> tuples = new HashSet<ZSetOperations.TypedTuple<Object>>();
//        tuples.add(objectTypedTuple1);
//        tuples.add(objectTypedTuple2);
//        tuples.add(objectTypedTuple3);
//
//        zSet.add( "k1" , tuples );
//
//        //0 , 4
//        //1 , 4
//        //a * b -1  ---
//        System.out.println("1" + zSet.range( "k1" , 1 , -1 ) );
//        System.out.println("2" + zSet.range( "k1" , 0 , -1 ) );
//        System.out.println("3" + zSet.range( "k1" , 0 , 1 ) );
//        System.out.println("4" + zSet.range( "k1" , 0 , 2 ) );
//
//    }


    @Test
    public void a(){



    }


}
