package com.olegshan.social;

import com.olegshan.entity.Job;
import com.olegshan.notifier.Notifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class JTwitter {

    private Environment environment;
    private Notifier notifier;

    @Autowired
    public JTwitter(Environment environment, Notifier notifier) {
        this.environment = environment;
        this.notifier = notifier;
    }

    public void tweet(Job job) {

        if (dev()) return;

        String consumerKey = System.getProperty("CKjP");
        String consumerSecret = System.getProperty("CSjP");
        String accessToken = System.getProperty("ATjP");
        String accessTokenSecret = System.getProperty("ATSjP");

        Twitter twitter = new TwitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret);

        String tweet = String.format("%s %s More jobs here: http://jparser.info", job.getTitle(), job.getUrl());
        try {
            twitter.timelineOperations().updateStatus(tweet);
        } catch (Exception e) {
            notifier.notifyAdmin("Error while twitting following tweet:\n " + tweet +
                    "\nException was:\n" + e);
        }
    }

    private boolean dev() {
        return Arrays.stream(environment.getActiveProfiles())
                .anyMatch(env -> env.equalsIgnoreCase("dev"));
    }
}
