package org.tedxuba.social.events;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

public abstract class SocialEventWithUrlListener extends SocialEventListener {
	private final Logger logger = LoggerFactory.getLogger(SocialEventWithUrlListener.class);
	
	static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	static final JsonFactory JSON_FACTORY = new JacksonFactory();
	
	public abstract String getRequestUrl(HttpRequestFactory requestFactory);
	public abstract void parseResponseAndUpdateCount(HttpResponse response);
	
	public void start() {
		Thread thread = new Thread(new Runnable() {
			
			public void run() {
				HttpRequestFactory requestFactory = HTTP_TRANSPORT
						.createRequestFactory(new HttpRequestInitializer() {
							public void initialize(HttpRequest request)
									throws IOException {
								request.setParser(new JsonObjectParser(JSON_FACTORY));
							}
						});
				String requestUrl = SocialEventWithUrlListener.this.getRequestUrl(requestFactory);
				GenericUrl url = new GenericUrl(requestUrl);
				try {
					HttpRequest request = requestFactory.buildGetRequest(url);
				
					while (true) {
						logger.debug("Requesting to " + url.toString());
						HttpResponse response;
						try {
							response = request.execute();
							parseResponseAndUpdateCount(response);
							Thread.sleep(1000);
						} 
						catch (IOException iOException) {
							logger.warn("Exception during request execution", iOException);
						}
						catch (InterruptedException interruptedException) {
							logger.warn("Exception during sleep", interruptedException);
						}
					}
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}
			
		});
		thread.start();
	}
}
