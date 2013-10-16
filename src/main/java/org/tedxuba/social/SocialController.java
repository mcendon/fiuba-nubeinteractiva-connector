package org.tedxuba.social;

import java.util.Observable;
import java.util.Observer;

import org.tedxuba.social.events.FacebookSiteLikeListener;
import org.tedxuba.social.events.FacebookSitePostsListener;
import org.tedxuba.social.events.SocialEventListener;

public class SocialController implements Observer {

	//TODO: parameter ?
	private static final String FACEBOOK_SITE_NAME = "TEDxUBA"; //official site
//	private static final String FACEBOOK_SITE_NAME = "tedxubaTest"; //test site
	
	private SocialMapper mapper;
	
	public SocialController() {
		this.mapper = new SocialMapper();
		
		SocialEventListener fbSiteLikeListener = new FacebookSiteLikeListener(FACEBOOK_SITE_NAME);
		fbSiteLikeListener.addObserver(this);
		fbSiteLikeListener.start();
		
		SocialEventListener fbSitePostsListener = new FacebookSitePostsListener(FACEBOOK_SITE_NAME);
		fbSitePostsListener.addObserver(this);
		fbSitePostsListener.start();
		
		// TODO: add the rest of social event listeners
	}
	
	public void update(Observable o, Object arg) {
		this.mapper.notify(o);
	}

}
