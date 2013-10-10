package org.tedxuba.social;

import java.io.IOException;
import java.util.Observable;

import org.tedxuba.social.events.SocialEventListener;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;

public class SocialMapper {

	static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	private HttpRequestFactory requestFactory;
	
	public SocialMapper() {
		this.requestFactory = HTTP_TRANSPORT.createRequestFactory();
	}
	
	public void notify(Observable o) {
		SocialEventListener sel = (SocialEventListener)o;
		String socialNetworkName = sel.getSocialNetworkName();
		String eventName = sel.getEventName();
		Integer count = sel.getCountDiff();
		
		// TODO: Mapper server IP through parameter
		GenericUrl mapperUrl = new GenericUrl("http://localhost:5000/" + socialNetworkName + "/" + eventName + "/" + count);
		try {
			requestFactory.buildGetRequest(mapperUrl).execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
