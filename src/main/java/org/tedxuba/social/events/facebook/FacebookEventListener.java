package org.tedxuba.social.events.facebook;

import org.tedxuba.social.events.SocialEventWithUrlListener;

public abstract class FacebookEventListener extends SocialEventWithUrlListener {
	
	public FacebookEventListener(String site) {
		super(site);
	}

	@Override
	public String getSocialNetworkName() {
		return "facebook";
	}

}
