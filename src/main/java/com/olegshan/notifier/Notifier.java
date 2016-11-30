package com.olegshan.notifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class Notifier {

    private static final Logger LOGGER = LoggerFactory.getLogger(Notifier.class);

    private MailSender mailSender;

    @Autowired
    public Notifier(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void notifyAdmin(String issue) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("***");
        message.setSubject("jParser issue");
        message.setText(issue + "\n\nhttp://www.jparser.info");

        mailSender.send(message);
        LOGGER.info("Admin was notified about following issue: " + issue + "\n");
    }
}
