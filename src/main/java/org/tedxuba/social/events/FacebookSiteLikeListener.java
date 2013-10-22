package org.tedxuba.social.events;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.util.Key;

public class FacebookSiteLikeListener extends FacebookEventListener {

	private final Logger logger = LoggerFactory.getLogger(FacebookSiteLikeListener.class);
	
	private static final String SITE_LIKE_EVENT = "site-like";
	
	public static class Feed {
		@Key("like_count")
		public int likeCount;
	}
	
	public FacebookSiteLikeListener(String site) {
		super(site);
	}
	
	@Override
	public List<String> getEventNames() {
		return Arrays.asList(new String[] { SITE_LIKE_EVENT });
	}

	@Override
	public String getRequestUrl(HttpRequestFactory requestFactory) {
		return "http://api.facebook.com/method/fql.query?format=json&query=select%20like_count%20from%20link_stat%20WHERE%20url%20%3D%27www.facebook.com/" + getSite() + "%27";
	}

	@Override
	public void parseResponseAndUpdateCount(HttpResponse response) {
		try {
			Feed[] feed = response.parseAs(Feed[].class);
			if (getLastCount(SITE_LIKE_EVENT) != null) {
				setCountDiff(SITE_LIKE_EVENT, feed[0].likeCount - getLastCount(SITE_LIKE_EVENT));
				setLastCount(SITE_LIKE_EVENT, feed[0].likeCount);
				if (getCountDiff(SITE_LIKE_EVENT) > 0) {
					this.setChanged();
					this.notifyObservers();
				}
			} else {
				setLastCount(SITE_LIKE_EVENT, feed[0].likeCount);
			}
		} catch (IOException iOException) {
			logger.error("Exception parsing response: " + iOException.getMessage());
		}		
	}

}
