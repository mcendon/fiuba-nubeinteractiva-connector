package org.tedxuba.social.events.facebook;

import org.tedxuba.social.events.SocialEventWithUrlListener;

public abstract class FacebookEventListener extends SocialEventWithUrlListener {

	private String site;

	public FacebookEventListener(String site) {
		this.site = site;
	}
	
	@Override
	public String getSocialNetworkName() {
		return "facebook";
	}

	public String getSite() {
		return site;
	}

}
