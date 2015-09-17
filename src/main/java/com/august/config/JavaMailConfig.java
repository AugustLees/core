package com.august.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.config
 * Author: August
 * Update: August(2015/9/10)
 * Description:配置JavaMail配置信息
 */
@Configuration
public class JavaMailConfig {
    @Value("${smtp.host:smtp.gmail.com}")
    private String smtpHost;
    @Value("${smtp.port:465}")
    private Integer smtpPort;
    @Value("${smtp.protocol:smtps}")
    private String smtpProtocol;
    @Value("${smtp.username:sivaprasadreddy.k@gmail.com}")
    private String smtpUsername;
    @Value("${smtp.password:}")
    private String smtpPassword;
    @Bean
    public JavaMailSenderImpl javaMailSenderImpl() {
        JavaMailSenderImpl mailSenderImpl = new JavaMailSenderImpl();
        mailSenderImpl.setHost(smtpHost);
        mailSenderImpl.setPort(smtpPort);
        mailSenderImpl.setProtocol(smtpProtocol);
        mailSenderImpl.setUsername(smtpUsername);
        mailSenderImpl.setPassword(smtpPassword);

        Properties javaMailProps = new Properties();
        javaMailProps.put("mail.smtp.auth", true);
        javaMailProps.put("mail.smtp.starttls.enable", true);

        mailSenderImpl.setJavaMailProperties(javaMailProps);

        return mailSenderImpl;
    }


}
