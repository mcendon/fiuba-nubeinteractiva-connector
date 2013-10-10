package org.tedxuba.social.events;

public class FacebookSiteLikeListener extends FacebookEventListener {

	@Override
	public String getEventName() {
		return "site-like";
	}

}
