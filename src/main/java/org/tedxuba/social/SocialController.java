package org.tedxuba.social;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.tedxuba.social.events.SocialEventListener;
import org.tedxuba.social.events.facebook.FacebookSiteLikeListener;
import org.tedxuba.social.events.facebook.FacebookSitePostsListener;
import org.tedxuba.social.events.twitter.TwitterTermsListener;
import org.tedxuba.social.events.youtube.YoutubeSubscribersListener;

public class SocialController implements Observer {

	private static final String FACEBOOK_SITE_NAME = "TEDxUBA";
	private static final String TWITTER_TERMS = "TEDxUBA";
	private static final String YOUTUBE_SITE_NAME = "TEDxUBA";
	
	private SocialMapper mapper;
	private List<SocialEventListener> eventListeners;
	
	public SocialController(SocialMapper mapper) {
		this.mapper = mapper;
		
		setupEventListeners();
	}
	
	private void setupEventListeners() {
		this.eventListeners = new ArrayList<SocialEventListener>();
		
//		SocialEventListener fbSiteLikeListener = new FacebookSiteLikeListener(FACEBOOK_SITE_NAME);
//		fbSiteLikeListener.addObserver(this);
//		this.eventListeners.add(fbSiteLikeListener);
//		
//		SocialEventListener fbSitePostsListener = new FacebookSitePostsListener(FACEBOOK_SITE_NAME);
//		fbSitePostsListener.addObserver(this);
//		this.eventListeners.add(fbSitePostsListener);
//		
//		SocialEventListener twtTermsListener = new TwitterTermsListener(TWITTER_TERMS);
//		twtTermsListener.addObserver(this);
//		this.eventListeners.add(twtTermsListener);
		
		SocialEventListener ytSubscribersListener = new YoutubeSubscribersListener(YOUTUBE_SITE_NAME);
		ytSubscribersListener.addObserver(this);
		this.eventListeners.add(ytSubscribersListener);
		
		// TODO: add the rest of social event listeners
	}

	public void start() {
		for (SocialEventListener listener : this.eventListeners) {
			listener.start();
		}
	}
	
	public void update(Observable o, Object arg) {
		this.mapper.notify(o);
	}

}
