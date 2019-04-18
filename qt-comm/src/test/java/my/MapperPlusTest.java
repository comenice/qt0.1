package my;


import com.zxb.qt.exploit.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MapperPlusTest {



    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
       // List<User> userList = userMapper.selectList(null);
       // userList.forEach(System.out::println);
    }


}
