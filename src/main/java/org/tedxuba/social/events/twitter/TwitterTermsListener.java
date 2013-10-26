package org.tedxuba.social.events.twitter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.json.JsonParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Key;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

public class TwitterTermsListener extends TwitterEventListener {
	private final Logger logger = LoggerFactory
			.getLogger(TwitterTermsListener.class);

	private List<String> twitterTerms;

	private static final String TERM_EVENT = "term";
	protected static final String HASHTAG_EVENT = "hashtag";
	protected static final String MENTION_EVENT = "mention";

	public TwitterTermsListener(List<String> twitterTerms) {
		this.twitterTerms = twitterTerms;
	}
	
	public TwitterTermsListener(String twitterTerm) {
		this.twitterTerms = new ArrayList<String>();
		this.twitterTerms.add(twitterTerm);
	}

	public static class TwitterFeed {
		@Key
		public String text;
	}
	
	@Override
	public void start() {
		Thread thread = new Thread(new Runnable() {
			
			public void run() {
				BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(100000);
		
				StatusesFilterEndpoint filterEndpoint = new StatusesFilterEndpoint();
				filterEndpoint.trackTerms(TwitterTermsListener.this.twitterTerms);
		
				Authentication hosebirdAuth = new OAuth1("aeIBIUf1vVu4PWNvxlJVYg",
						"BRoMmIAozI0Hi95D344c1tk6JdtoDnkiKtS7weyw",
						"1915420819-XXj2cwXtUoSnA1E0ZQvSHg55BQYfZ2b8vBh3nUY",
						"WE0tt69igf45tXeT5peyiCQTPj03cv5TJFpqKHq3w");
		
				ClientBuilder builder = new ClientBuilder().name("Hosebird-Client-01")
						.hosts(HttpHosts.STREAM_HOST).authentication(hosebirdAuth)
						.endpoint(filterEndpoint)
						.processor(new StringDelimitedProcessor(msgQueue));
		
				Client hosebirdClient = builder.build();
				hosebirdClient.connect();
		
				while (!hosebirdClient.isDone()) {
					try {
						String msg;
						msg = msgQueue.take();
						// Avoid self logout events
						if (!msg.contains("admin logout")) {
							logger.debug(msg);
							
							JsonParser jsonParser = JacksonFactory.getDefaultInstance().createJsonParser(msg);
							TwitterFeed tf = jsonParser.parse(TwitterFeed.class);
							jsonParser.close();
							for (String twitterTerm : TwitterTermsListener.this.twitterTerms) {
								if (tf.text.toLowerCase().contains(twitterTerm.toLowerCase())) {
									if (msg.toLowerCase().contains("#" + twitterTerm.toLowerCase())) {
										setCountDiff(HASHTAG_EVENT, 1);	
									} 
									if (tf.text.toLowerCase().contains("@" + twitterTerm.toLowerCase())) {
										setCountDiff(MENTION_EVENT, 1);
									}
									String newstring = tf.text.toLowerCase().replaceAll("#"+twitterTerm.toLowerCase(), "");
									newstring = newstring.replaceAll("@"+twitterTerm.toLowerCase(), "");
									if (newstring.contains(twitterTerm.toLowerCase())) {
										setCountDiff(TERM_EVENT, 1);
									}
								}
							}
							TwitterTermsListener.this.setChanged();
							TwitterTermsListener.this.notifyObservers();
							setCountDiff(HASHTAG_EVENT, 0);	
							setCountDiff(MENTION_EVENT, 0);
							setCountDiff(TERM_EVENT, 0);
						}
					} catch (Exception e) {
						logger.error(e.toString());
					}
				}
			}
		});
		thread.start();
	}

	@Override
	public List<String> getEventNames() {
		return Arrays.asList(new String[] { TERM_EVENT, HASHTAG_EVENT, MENTION_EVENT });
	}
}
