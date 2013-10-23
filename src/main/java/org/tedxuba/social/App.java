package org.tedxuba.social;

public class App {
	private static final String mapperUrl = "http://localhost:5000/";

	public static void main(String[] args) {
		SocialMapper mapper = new SocialMapper(mapperUrl);
		SocialController controller = new SocialController(mapper);
		controller.start();
	}
}
