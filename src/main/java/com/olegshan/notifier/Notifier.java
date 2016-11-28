package com.olegshan.notifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Notifier {

    private static final Logger LOGGER = LoggerFactory.getLogger(Notifier.class);

    public void notifyAdmin(String message) {
        //TODO implement the method
        LOGGER.error("Admin was notified about following issue: " + message);
    }
}
