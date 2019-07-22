package comm.web;

import com.zxb.qt.QtApplication;
import com.zxb.qt.exploit.common.websocket.GameRank;
import com.zxb.qt.exploit.entity.Games;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest( classes = QtApplication.class)
public class GameRankImplTest {

    @Autowired
    private GameRank gameRank;


    @Test
    public void myt(){
        String key = "blog:game:rang" ;
        Games games = new Games();

        gameRank.insertOrUpdate( key , games ,null );

    }







}