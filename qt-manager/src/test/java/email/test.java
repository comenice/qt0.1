package email;


import com.zxb.qt.QtApplication;
import com.zxb.qt.exploit.common.email.MailService;
import com.zxb.qt.exploit.common.email.imp.MailServiceImpl;
import com.zxb.qt.exploit.common.email.imp.MailServiceUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

@RunWith(SpringRunner.class)
@SpringBootTest( classes = QtApplication.class)
public class test {

//
//    private String to =  "571503431@qq.com";
//    private String subJet = "Test";
//    private String context  = "spring bott jicheng email Test";
//
//    @Autowired
//    private MailService email;
//
//
//    public void TestMsg (){
//
//
//
//
//    }
//    @Test
//    public void TestHtml(){
//        Multipart mainPart = new MimeMultipart();
//        email.sendHtmlMail( to , subJet , MailServiceUtils.content);
//    }

    public static void main(String[] args) {

        int a = 20;
        int b =0 ;
        boolean h = true ;
        String str = "";
        if ( a == 20 && !h   ){

        }

        System.out.println( h );
    }



}
