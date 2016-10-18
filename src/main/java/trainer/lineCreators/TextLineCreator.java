package trainer.lineCreators;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;


public class TextLineCreator implements LineCreator {
	
	private Scanner scanner;
	private String wordBuffer;
	
	public TextLineCreator(InputStream is) throws IOException {
		scanner = new Scanner(is, persistence.Constants.PROJECT_CHARSET.toString());
		wordBuffer = scanner.next();
	}

	@Override
	public String create(int length) {
		if(!hasNext())
			throw new IllegalStateException("EOF");
		if(wordBuffer.length() > length)
			throw new RuntimeException("Word longer than row.");
		
		StringBuilder sb = new StringBuilder(length+1);
		sb.append(wordBuffer);
		wordBuffer = null;
		sb.append(' ');
		length -= sb.length();
		while(length >= -1) { 	// Use of >= -1 will force wordBuffer to be filled, if there is at
								// least one remaining word. (Use -1 because of space after word
								// that will be removed at the end.)
			if(!scanner.hasNext())
				break;
			String nextWord = scanner.next();
			if(nextWord.length() > length) {
				wordBuffer = nextWord;
				break;
			}
			sb.append(nextWord);
			sb.append(' ');
			length -= nextWord.length()+1;
		}
		//delete last space before line break
		sb.setLength(sb.length()-1);
		//append a newline character
		sb.append('\n');
		return sb.toString();
	}
	
	@Override
	public boolean hasNext() {
		return wordBuffer != null;
	}
	
	@Override
	public void stop() {
		scanner.close();
	}

}
