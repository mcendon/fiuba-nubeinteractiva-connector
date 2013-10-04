package org.tedxuba.social;

import java.io.IOException;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Key;

public class App {
	static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	static final JsonFactory JSON_FACTORY = new JacksonFactory();

	public static class Feed {
		@Key("like_count")
		public int likeCount;
	}

	private static void runWithFacebookSite(String site) throws Exception {
		HttpRequestFactory requestFactory = HTTP_TRANSPORT
				.createRequestFactory(new HttpRequestInitializer() {
					public void initialize(HttpRequest request)
							throws IOException {
						request.setParser(new JsonObjectParser(JSON_FACTORY));
					}
				});
		GenericUrl url = new GenericUrl(
				"http://api.facebook.com/method/fql.query?format=json&query=select%20like_count%20from%20link_stat%20WHERE%20url%20%3D%27www.facebook.com/" + site + "%27");
		HttpRequest request = requestFactory.buildGetRequest(url);
		while (true) {
			Feed[] feed = request.execute().parseAs(Feed[].class);
			System.out.println(site + " contador Me gusta: " + feed[0].likeCount);
			Thread.sleep(1000);
		}
	}

	public static void main(String[] args) {
		try {
			
		    if (args.length > 0 && !"".equals(args[0].trim())) {
		    	runWithFacebookSite(args[0].trim());
		    } else {
		    	runWithFacebookSite("TEDxUBA");		    	
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
