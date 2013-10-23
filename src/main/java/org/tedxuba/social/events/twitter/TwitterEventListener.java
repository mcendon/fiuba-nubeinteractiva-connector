package org.tedxuba.social.events.twitter;

import org.tedxuba.social.events.SocialEventListener;

public abstract class TwitterEventListener extends SocialEventListener {

	@Override
	public String getSocialNetworkName() {
		return "twitter";
	}

}
