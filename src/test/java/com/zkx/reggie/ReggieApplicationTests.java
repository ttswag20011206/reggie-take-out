package com.zkx.reggie;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@SpringBootTest
class ReggieApplicationTests {

    @Autowired
    JavaMailSenderImpl mailSender;

    @Test
    void contextLoads() throws MessagingException {
        while (true) {
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

            helper.setSubject("tpl");

            helper.setText("<p style='color':blue>zkx崽崽是word上最漂亮的女孩</p>", true);

            helper.setTo("2638472520@qq.com");


            helper.setFrom("2722829330@qq.com");

            mailSender.send(mimeMessage);
        }
    }

}

