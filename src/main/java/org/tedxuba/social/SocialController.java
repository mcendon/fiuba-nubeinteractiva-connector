package org.tedxuba.social;

import java.util.Observable;
import java.util.Observer;

import org.tedxuba.social.events.FacebookSiteLikeListener;
import org.tedxuba.social.events.SocialEventListener;

public class SocialController implements Observer {

	private SocialMapper mapper;
	
	public SocialController() {
		this.mapper = new SocialMapper();
		
		SocialEventListener fbSiteLikeListener = new FacebookSiteLikeListener("TEDxUBA");
		fbSiteLikeListener.addObserver(this);
		fbSiteLikeListener.start();
		
		// TODO: add the rest of social event listeners
	}
	
	public void update(Observable o, Object arg) {
		this.mapper.notify(o);
	}

}
