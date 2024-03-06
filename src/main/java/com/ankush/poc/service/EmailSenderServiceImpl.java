package com.ankush.poc.service;

import com.ankush.poc.service.interfaces.EmailSenderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static java.util.Collections.*;

@Service
@Slf4j
@EnableAsync
public class EmailSenderServiceImpl implements EmailSenderService {

    private static final String UTF_8_ENCODING = "UTF-8";
    private static final String NEW_ACCOUNT_CREATION_MESSAGE = "New Account Created !!";

    public static final String EMAIL_TEMPLATE = "emailtemplate";
    public static final String TEXT_HTML_ENCONDING = "text/html";
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.mail.verify.host}")
    private String host;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${img.path}")
    private String imgPath;

    @Override
    @Async
    public void sendHtmlEmailWithEmbeddedFiles(String name, String to, String token) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true,UTF_8_ENCODING);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setPriority(1);
            helper.setCc("ankush8802@gmail.com");
            helper.setSubject(NEW_ACCOUNT_CREATION_MESSAGE);

            // adding daat to themleaf html
            Context context = new Context();
            HashMap<String, Object> map = new HashMap<>();
            map.put("name",name);
            map.put("url",getUrl(host,token));
            context.setVariables(unmodifiableMap(map));
            String text = templateEngine.process(EMAIL_TEMPLATE, context);

            MimeMultipart mimeMultipart = new MimeMultipart("related");
            // text message
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(text,TEXT_HTML_ENCONDING);
            mimeMultipart.addBodyPart(messageBodyPart);
            // image
            BodyPart imageBodyPart = new MimeBodyPart();
            DataSource dataSource = new FileDataSource(imgPath);
            imageBodyPart.setDataHandler(new DataHandler(dataSource));
            imageBodyPart.setHeader("Content-ID","image");
            mimeMultipart.addBodyPart(imageBodyPart);

            mimeMessage.setContent(mimeMultipart);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Message execption occured {}", e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (MailException e) {
            log.error("Mail execption occured {}", e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    private Map<String, Object> unmodifiableMap(HashMap<String, Object> map) {
       return map;
    }

    private String getUrl(String host,String token) {
       return host + "/user?token=" + token;
    }

}
