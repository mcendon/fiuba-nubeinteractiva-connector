package org.tedxuba.social.events;

import java.util.Observable;

public abstract class SocialEventListener extends Observable {
	private Integer lastCount;
	
	public abstract String getSocialNetworkName();
	public abstract String getEventName();
	
	public void setLastCount(Integer lastCount) {
		this.lastCount = lastCount;
	}
	
	public Integer getLastCount() {
		return lastCount;
	}
}
