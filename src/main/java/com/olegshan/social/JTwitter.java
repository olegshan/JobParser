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

    private Twitter twitter;
    private Environment environment;
    private Notifier notifier;

    @Autowired
    public JTwitter(Environment environment, Notifier notifier) {
        this.environment = environment;
        this.notifier = notifier;

        if (!dev()) {
            String consumerKey = System.getProperty("CKjP");
            String consumerSecret = System.getProperty("CSjP");
            String accessToken = System.getProperty("ATjP");
            String accessTokenSecret = System.getProperty("ATSjP");

            twitter = new TwitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret);
        }
    }

    public void tweet(Job job) {

        if (twitter == null) return;

        String tweet;
        String jobTitle = job.getTitle();
        String jobUrl = job.getUrl();
        String moreJobs = " More jobs here: ";
        String jParserUrl = "http://jparser.info";
        int twitterUrlLength = 23;
        int tweetLength = jobTitle.length() + 1 + twitterUrlLength * 2 + moreJobs.length();

        if (tweetLength <= 140)
            tweet = jobTitle + " " + jobUrl + moreJobs + jParserUrl;
        else tweet = jobTitle + " " + jobUrl;

        try {
            twitter.timelineOperations().updateStatus(tweet);
        } catch (Exception e) {
            notifier.notifyAdmin(
                    "Error while twitting following tweet:\n " + tweet +
                            "\nException was:\n" + e.getMessage()
            );
        }
    }

    private boolean dev() {
        return Arrays.stream(environment.getActiveProfiles())
                .anyMatch(env -> env.equalsIgnoreCase("dev"));
    }
}
