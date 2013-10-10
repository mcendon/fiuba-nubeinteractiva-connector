package org.tedxuba.social.events;

public abstract class TwitterEventListener extends SocialEventListener {

	@Override
	public String getSocialNetworkName() {
		return "twitter";
	}

}
