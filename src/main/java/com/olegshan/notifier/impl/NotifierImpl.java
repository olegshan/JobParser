package com.olegshan.notifier.impl;

import com.olegshan.notifier.Notifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class NotifierImpl implements Notifier {

    @Value("${mail.recipient}")
    private String     recipient;
    private MailSender mailSender;

    @Autowired
    public NotifierImpl(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void notifyAdmin(String issue) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipient);
        message.setSubject("jParser issue");
        message.setText(issue + "\n\nhttp://www.jparser.info");

        mailSender.send(message);
        log.info("Admin was notified about following issue: " + issue + "\n");
    }

    private static final Logger log = LoggerFactory.getLogger(NotifierImpl.class);
}