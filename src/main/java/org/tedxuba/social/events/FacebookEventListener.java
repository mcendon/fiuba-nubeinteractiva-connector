package org.tedxuba.social.events;

public abstract class FacebookEventListener extends SocialEventListener {

	@Override
	public String getSocialNetworkName() {
		return "facebook";
	}

}
