package org.tedxuba.social.events.youtube;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.util.Key;

public class YoutubeSubscribersListener extends YoutubeEventListener {
	
	private final Logger logger = LoggerFactory.getLogger(YoutubeSubscribersListener.class);

	private static final String SUBSCRIBERS_EVENT = "subscribers";

	public static class YoutubeSubscriberFeed {
		@Key
		public YoutubeEntry entry;
		
		public int getSubscriberCount() {
			return Integer.valueOf(entry.stats.subscriberCount);
		}
	}
	
	public static class YoutubeEntry {
		@Key("yt$statistics")
		public YoutubeStats stats;
	}
	
	public static class YoutubeStats {
		@Key
		public String subscriberCount;
	}
	
	public YoutubeSubscribersListener(String site) {
		super(site);
	}

	@Override
	public String getRequestUrl(HttpRequestFactory requestFactory) {
		return "http://gdata.youtube.com/feeds/api/users/" + getSite() + "?v=2&alt=json";
	}

	@Override
	public void parseResponseAndUpdateCount(HttpResponse response) {
		try {
			YoutubeSubscriberFeed feed = response.parseAs(YoutubeSubscriberFeed.class);
			if (getLastCount(SUBSCRIBERS_EVENT) != null) {
				setCountDiff(SUBSCRIBERS_EVENT, feed.getSubscriberCount() - getLastCount(SUBSCRIBERS_EVENT));
				setLastCount(SUBSCRIBERS_EVENT, feed.getSubscriberCount());
				if (getCountDiff(SUBSCRIBERS_EVENT) > 0) {
					this.setChanged();
					this.notifyObservers();
				}
			} else {
				setLastCount(SUBSCRIBERS_EVENT, feed.getSubscriberCount());
			}
		} catch (Exception e) {
			logger.error("Exception parsing response: " + e.toString());
		}	
	}

	@Override
	public List<String> getEventNames() {
		return Arrays.asList(new String[] { SUBSCRIBERS_EVENT });
	}

}
