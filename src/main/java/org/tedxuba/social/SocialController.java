package org.tedxuba.social;

import java.util.Observable;
import java.util.Observer;

import org.tedxuba.social.events.FacebookSiteLikeListener;
import org.tedxuba.social.events.FacebookSitePostsListener;
import org.tedxuba.social.events.SocialEventListener;
import org.tedxuba.social.events.TwitterTermsEventListener;

public class SocialController implements Observer {

	private static final String FACEBOOK_SITE_NAME = "TEDxUBA";
	private static final String TWITTER_TERMS = "TEDxUBA";

	private static final String mapperUrl = "http://localhost:5000/";
	
	private SocialMapper mapper;
	
	public SocialController() {
		this.mapper = new SocialMapper(mapperUrl);
		
		SocialEventListener fbSiteLikeListener = new FacebookSiteLikeListener(FACEBOOK_SITE_NAME);
		fbSiteLikeListener.addObserver(this);
		fbSiteLikeListener.start();
		
		SocialEventListener fbSitePostsListener = new FacebookSitePostsListener(FACEBOOK_SITE_NAME);
		fbSitePostsListener.addObserver(this);
		fbSitePostsListener.start();
		
		TwitterTermsEventListener twtTermsEventListener = new TwitterTermsEventListener(TWITTER_TERMS);
		twtTermsEventListener.addObserver(this);
		twtTermsEventListener.start();
		
		// TODO: add the rest of social event listeners
	}
	
	public void update(Observable o, Object arg) {
		this.mapper.notify(o);
	}

}
