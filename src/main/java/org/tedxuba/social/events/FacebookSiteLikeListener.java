package org.tedxuba.social.events;

import java.io.IOException;

import com.google.api.client.http.HttpResponse;
import com.google.api.client.util.Key;

public class FacebookSiteLikeListener extends FacebookEventListener {

	private String site;

	public static class Feed {
		@Key("like_count")
		public int likeCount;
	}
	
	public FacebookSiteLikeListener(String site) {
		this.site = site;
	}
	
	@Override
	public String getEventName() {
		return "site-like";
	}

	@Override
	public String getRequestUrl() {
		return "http://api.facebook.com/method/fql.query?format=json&query=select%20like_count%20from%20link_stat%20WHERE%20url%20%3D%27www.facebook.com/" + site + "%27";
	}

	@Override
	public void parseResponseAndUpdateCount(HttpResponse response) {
		try {
			Feed[] feed = response.parseAs(Feed[].class);
			if (getLastCount() != null) {
				setCountDiff(feed[0].likeCount - getLastCount());
				setLastCount(feed[0].likeCount);
				if (getCountDiff() > 0) {
					this.setChanged();
					this.notifyObservers();
				}
			} else {
				setLastCount(feed[0].likeCount);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

}
