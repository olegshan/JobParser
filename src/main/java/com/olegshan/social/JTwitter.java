package com.olegshan.social;

import com.olegshan.entity.Job;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Component;

@Component
public class JTwitter {

    private static final String CONSUMER_KEY = System.getProperty("CKjP");
    private static final String CONSUMER_SECRET = System.getProperty("CSjP");
    private static final String ACCESS_TOKEN = System.getProperty("ATjP");
    private static final String ACCESS_TOKEN_SECRET = System.getProperty("ATSjP");

    private Twitter twitter;

    public JTwitter() {
        twitter = new TwitterTemplate(CONSUMER_KEY, CONSUMER_SECRET, ACCESS_TOKEN, ACCESS_TOKEN_SECRET);
    }

    public void tweet(Job job) {
        try {
            twitter.timelineOperations().updateStatus(job.getTitle() + " " + job.getUrl()
                    + " More jobs here: http://jparser.info");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
