package org.tedxuba.social.events;

public abstract class FacebookEventListener extends SocialEventListener {

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
