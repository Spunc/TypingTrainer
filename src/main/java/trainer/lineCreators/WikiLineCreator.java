package trainer.lineCreators;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

public class WikiLineCreator implements LineCreator {
	
	private static final String fixedURLPart = ".wikipedia.org/w/api.php?action=query"
			+ "&generator=random&grnnamespace=0&grnfilterredir=nonredirects"
			+ "&prop=extracts&explaintext=1&exsectionformat=plain&format=json";
	
	private TextLineCreator textLineCreator;
	
	public WikiLineCreator(String keyboardLayoutID, String languageSubdomain) throws InitException {
		initTextLineCreator(keyboardLayoutID, "https://" + languageSubdomain + fixedURLPart
				+ "&exintro");
	}

	public WikiLineCreator(String keyboardLayoutID, String languageSubdomain, int numChars) throws InitException {
		initTextLineCreator(keyboardLayoutID, "https://" + languageSubdomain + fixedURLPart
				+ "&exchars=" + numChars);
	}
	
	// Test purposes only
	public WikiLineCreator(String keyboardLayoutID, String languageSubdomain, String title) throws InitException {
		initTextLineCreator(keyboardLayoutID, "https://" + languageSubdomain
				+ ".wikipedia.org/w/api.php?action=query"
				+ "&prop=extracts&explaintext=1&exsectionformat=plain&format=json&exchars=400"
				+ "&titles=" + title);
	}
	
	private void initTextLineCreator(String keyboardLayoutID, String urlString) throws InitException {
		URL wikiURL;
		try {
			wikiURL = new URL(urlString);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
		try(JsonParser parser = Json.createParser(wikiURL.openStream())) {
			String title="", extract="";
			while(parser.hasNext()) {
				Event event = parser.next();
				if(event == Event.KEY_NAME) {
					switch(parser.getString()) {
					case "title":
						parser.next();
						title = parser.getString();
						break;	
					case "extract":
						parser.next();
						extract = parser.getString();
					}
				}
			}
			TypeableConverter converter = new TypeableConverter(keyboardLayoutID);
			String text = converter.convert(title + ": " + extract);
			textLineCreator = new TextLineCreator(
					new ByteArrayInputStream(text
							.getBytes(persistence.Constants.PROJECT_CHARSET)));
		}
		catch (IOException e) {
			throw new InitException(InitException.Type.OTHER, e.getMessage());
		}
	}
	
	@Override
	public String create(int length) {
		return textLineCreator.create(length);
	}
	
	@Override
	public boolean hasNext() {
		return textLineCreator.hasNext();
	}
	
	public static void main(String[] args) throws InitException, MalformedURLException {
		WikiLineCreator wlc = new WikiLineCreator("DE", "de", 400);
		while(wlc.hasNext())
			System.out.println(wlc.create(40));
	}

}
