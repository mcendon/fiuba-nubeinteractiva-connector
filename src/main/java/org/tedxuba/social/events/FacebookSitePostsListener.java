package org.tedxuba.social.events;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.util.Key;

public class FacebookSitePostsListener extends FacebookEventListener {

	private final Logger logger = LoggerFactory.getLogger(FacebookSitePostsListener.class);
	
	private static final String POSTS_LIKES_EVENT = "posts-likes";
	private static final String POSTS_COMMENTS_EVENT = "posts-comments";
	private static final String POSTS_SHARES_EVENT = "posts-shares";

	private String site;
	private long pageId;

	public static class PageFeed {
		@Key("page_id")
		public long pageId;
	}
	
	public static class PostFeed {
		@Key
		public PostComment comments;
		@Key("share_count")
		public int shareCount;
		@Key("likes")
		public PostLike like; 
	}	

	public static class PostComment {
		@Key
		public int count;
	}

	public static class PostLike {
		@Key
		public int count;
	}
	
	public FacebookSitePostsListener(String site) {
		this.site = site;
		this.pageId = -1;
	}
	
	@Override
	public List<String> getEventNames() {
		return Arrays.asList(new String[] { POSTS_LIKES_EVENT,
				POSTS_COMMENTS_EVENT,
				POSTS_SHARES_EVENT});
	}
	
	private long getPageId(HttpRequestFactory requestFactory)
			throws IOException {
		GenericUrl url = new GenericUrl("http://api.facebook.com/method/fql.query?format=json&query=SELECT%20page_id%20from%20page%20where%20username%20%3D%20%22" + site + "%22");
		HttpRequest request = requestFactory.buildGetRequest(url);
		PageFeed[] pageFeeds = request.execute().parseAs(PageFeed[].class);
		long pageId = -1;
		if (pageFeeds.length > 0)
		{
			pageId = pageFeeds[0].pageId;
		}
		return pageId;
	}

	private String  getAccessToken(HttpRequestFactory requestFactory) 
			throws IOException {
		//App Access Token - request by graph api (non-native app)
		GenericUrl url = new GenericUrl("https://graph.facebook.com/oauth/access_token?client_id=152513334958943&client_secret=8a7c74b36898136c791d73bdccb06976&grant_type=client_credentials");
		HttpRequest request = requestFactory.buildGetRequest(url);
		String accessToken =  request.execute().parseAsString().substring(13).replace("|", "%7C");
		//TODO: token used (review User Access Token) 
		return accessToken;
	}	

	@Override
	public String getRequestUrl(HttpRequestFactory requestFactory) {
		String accessToken = null;
		try 
		{
			if (pageId <= 0)
			{
				pageId = getPageId(requestFactory);
			}
			//TODO: avoid new access token on each call
			accessToken  = getAccessToken(requestFactory);
		} 
		catch (IOException iOException) 
		{
			logger.error("Exception during request of parameters for url: " + iOException.getMessage());
		}
		String requestUrl = null;
		if ( (pageId > 0 ) && (accessToken != null) )
		{
			requestUrl = "https://api.facebook.com/method/fql.query?format=json&query=SELECT%20comments.count,share_count,likes.count,likes.can_like%20FROM%20stream%20WHERE%20source_id%3D" + pageId + "&access_token=" + accessToken;
		}
		return requestUrl;
	}

	@Override
	public void parseResponseAndUpdateCount(HttpResponse response) {
		try {
			PostFeed[] postFeeds = response.parseAs(PostFeed[].class);
			int totalShares = 0, totalComments = 0, totalLikes = 0 ;
			for (PostFeed postFeed : postFeeds) {
				totalShares += postFeed.shareCount;
				totalComments += postFeed.comments.count;
				totalLikes += postFeed.like.count;
			}			
			boolean eventChanged = this.updateCounts(POSTS_LIKES_EVENT, totalLikes);
			eventChanged = eventChanged || this.updateCounts(POSTS_COMMENTS_EVENT, totalComments);
			eventChanged = eventChanged || this.updateCounts(POSTS_SHARES_EVENT, totalShares);
			if (eventChanged)
			{
				this.setChanged();
				this.notifyObservers();
			}
		} 
		catch (IOException iOException) 
		{
			logger.error("Exception parsing response: " + iOException.getMessage());
		}		
	}
	
	private boolean updateCounts(String eventName, int currentCount)	{
		boolean eventChanged = false;
		if (getLastCount(eventName) != null) {
			setCountDiff(eventName, currentCount - getLastCount(eventName));
			setLastCount(eventName, currentCount);
			if (getCountDiff(eventName) > 0)
			{
				eventChanged = true;
			}
		} else {
			setLastCount(eventName, currentCount);
		}
		return eventChanged;
	}

}
