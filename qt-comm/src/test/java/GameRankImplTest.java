import com.zxb.qt.exploit.common.websocket.GameRank;
import com.zxb.qt.exploit.entity.Games;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class GameRankImplTest {

    @Autowired
    private GameRank gameRank;

    public void test1(){
        String key = "blog:game:rang" ;
        Games games = new Games();
//        for ( int i = 0 ; i < 10 ; i++ ){
//            games.setId( i );
//            games.setTall( i );
//        }
        gameRank.insertOrUpdate( key , games ,null );

    }



}