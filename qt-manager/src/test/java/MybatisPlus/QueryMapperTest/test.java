package MybatisPlus.QueryMapperTest;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zxb.qt.QtApplication;
import com.zxb.qt.exploit.entity.SysRight;
import com.zxb.qt.exploit.entity.SysRole;
import com.zxb.qt.exploit.entity.User;
import com.zxb.qt.exploit.entity.vo.UserRoleVO;
import com.zxb.qt.exploit.mapper.SysRightMapper;
import com.zxb.qt.exploit.mapper.SysRoleMapper;
import com.zxb.qt.exploit.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest( classes = QtApplication.class)
public class test {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysRightMapper sysRightMapper;

    // 授权
    // 赋予用户权限


    public void testQueryWrapper(){
        String userName = "我是user" ;
        //QueryWrapper 条件构造器   wrapper 装饰者设计模式 加强原有的方法
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        Wrapper<User> user_name = queryWrapper.eq("USER_NAME", userName);
        User user = userMapper.selectOne(user_name);
        System.out.println( user );

        System.out.println( sysRoleMapper.selectList( new QueryWrapper<SysRole>() ) );

    }
    @Test
    public  void testRoleMapper(){

       // UserRoleVO userRoleVO = sysRoleMapper.queryUserRole("1");
        //System.out.println( userRoleVO );
        userMapper.selectById( 1 );

    }


}
