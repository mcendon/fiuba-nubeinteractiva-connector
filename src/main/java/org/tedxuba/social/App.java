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

/**
 * Hello world!
 * 
 */
public class App {
	static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	static final JsonFactory JSON_FACTORY = new JacksonFactory();

	public static class VideoFeed {
		@Key("like_count")
		public int likeCount;
	}

	public static class DailyMotionUrl extends GenericUrl {

		public DailyMotionUrl(String encodedUrl) {
			super(encodedUrl);
		}

		@Key
		public String fields;
	}

	private static void run() throws Exception {
		HttpRequestFactory requestFactory = HTTP_TRANSPORT
				.createRequestFactory(new HttpRequestInitializer() {
					public void initialize(HttpRequest request)
							throws IOException {
						request.setParser(new JsonObjectParser(JSON_FACTORY));
					}
				});
		DailyMotionUrl url = new DailyMotionUrl(
				"https://api.facebook.com/method/fql.query?format=json&query=select%20like_count%20from%20link_stat%20WHERE%20url%20%3D%27www.facebook.com/TEDxUBA%27");
		//url.fields = "id,tags,title,url";
		HttpRequest request = requestFactory.buildGetRequest(url);
		while (true) {
			VideoFeed[] videoFeed = request.execute().parseAs(VideoFeed[].class);
			System.out.println("Like count: " + videoFeed[0].likeCount);
			Thread.sleep(1000);
		}
	}

	public static void main(String[] args) {
		try {
			run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
