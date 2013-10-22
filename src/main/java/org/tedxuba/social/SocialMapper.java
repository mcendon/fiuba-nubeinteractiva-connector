package org.tedxuba.social;

import java.io.IOException;
import java.util.List;
import java.util.Observable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tedxuba.social.events.SocialEventListener;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;

public class SocialMapper {
	
	private final Logger logger = LoggerFactory.getLogger(SocialMapper.class);

	static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	private HttpRequestFactory requestFactory;

	private String baseUrl;
	
	public SocialMapper(String baseUrl) {
		this.baseUrl = baseUrl;
		this.requestFactory = HTTP_TRANSPORT.createRequestFactory();
	}
	
	public void notify(Observable o) {
		SocialEventListener sel = (SocialEventListener)o;
		String socialNetworkName = sel.getSocialNetworkName();
		List<String> eventNames = sel.getEventNames();
		
		for (String eventName : eventNames) {
			Integer count = sel.getCountDiff(eventName);
			if (count > 0)
			{
				String mapperEvent = socialNetworkName + "/" + eventName + "/" + count;
				GenericUrl mapperUrl = new GenericUrl(this.baseUrl + mapperEvent);
				try {
					logger.debug("Sending event to mapper (" + mapperEvent + "): " + mapperUrl.toString());
					requestFactory.buildGetRequest(mapperUrl).execute();
				} 
				catch (IOException iOException) 
				{
					logger.error("Exception during sending event to mapper (" + mapperEvent + "): " + iOException.getMessage());
				}
			}
		}
	}

}
