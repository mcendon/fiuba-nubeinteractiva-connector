package org.tedxuba.social.events.youtube;

import org.tedxuba.social.events.SocialEventWithUrlListener;

public abstract class YoutubeEventListener extends SocialEventWithUrlListener {

	public YoutubeEventListener(String site) {
		super(site);
	}

	@Override
	public String getSocialNetworkName() {
		return "youtube";
	}

}
