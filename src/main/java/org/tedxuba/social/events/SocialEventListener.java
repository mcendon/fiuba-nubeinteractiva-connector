package org.tedxuba.social.events;

import java.io.IOException;
import java.util.Observable;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;

public abstract class SocialEventListener extends Observable {
	static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	static final JsonFactory JSON_FACTORY = new JacksonFactory();
	
	private Integer lastCount;
	private Integer countDiff;
	
	public abstract String getSocialNetworkName();
	public abstract String getEventName();
	public abstract String getRequestUrl();
	public abstract void parseResponseAndUpdateCount(HttpResponse response);
	
	public void setLastCount(Integer lastCount) {
		this.lastCount = lastCount;
	}
	
	public Integer getLastCount() {
		return lastCount;
	}
	
	public Integer getCountDiff() {
		return countDiff;
	}
	public void setCountDiff(Integer countDiff) {
		this.countDiff = countDiff;
	}
	
	public void start() {
		Thread thread = new Thread(new Runnable() {
			
			public void run() {
				String requestUrl = SocialEventListener.this.getRequestUrl();
				HttpRequestFactory requestFactory = HTTP_TRANSPORT
						.createRequestFactory(new HttpRequestInitializer() {
							public void initialize(HttpRequest request)
									throws IOException {
								request.setParser(new JsonObjectParser(JSON_FACTORY));
							}
						});
				GenericUrl url = new GenericUrl(requestUrl);
				try {
					HttpRequest request = requestFactory.buildGetRequest(url);
				
					while (true) {
						HttpResponse response = request.execute();
						parseResponseAndUpdateCount(response);
						
						Thread.sleep(1000);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		});
		thread.start();
	}
}
