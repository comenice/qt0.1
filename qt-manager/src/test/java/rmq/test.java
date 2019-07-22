package rmq;


import com.zxb.qt.QtApplication;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

public class test {

    // -------- 生产者 ------------

    // 同步发送消息
    @Test
    public void SyncProducer () throws Exception {
            //Instantiate with a producer group name.
            DefaultMQProducer producer = new
                    DefaultMQProducer("mqname_name");
            // Specify name server addresses.
            producer.setNamesrvAddr("139.224.129.134:9876");
            //Launch the instance.
            producer.start();
        System.out.println( producer.getClientIP() );
            for (int i = 0; i < 100; i++) {
                //Create a message instance, specifying topic, tag and message body.
                Message msg = new Message("TopicTest" /* Topic */,
                        "TagA" /* Tag */,
                        ("Hello RocketMQ " +
                                i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
                );
                //Call send message to deliver message to one of brokers.
                SendResult sendResult = producer.send(msg);
                System.out.printf("%s%n", sendResult);
            }
            //Shut down once the producer instance is not longer in use.
            producer.shutdown();
        }



    }




