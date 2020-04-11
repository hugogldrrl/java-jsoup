package com.hgr.jsoup.service;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.internet.MimeMessage;

import static org.mockito.Mockito.*;
import javax.mail.Session;
import org.springframework.context.annotation.*;

/**
 * Mock para el servicio de envío de correos.
 */
@Profile("test")
@Configuration
public class EmailServiceTestConfiguration {

    @Bean
    @Primary
    public JavaMailSender javaMailSender() {
        JavaMailSender javaMailSender = mock(JavaMailSender.class);
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        return javaMailSender;
    }

}