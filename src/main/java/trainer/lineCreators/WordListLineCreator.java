package trainer.lineCreators;


import java.io.BufferedReader;
import java.util.Random;
import java.util.stream.Collectors;

public class WordListLineCreator implements LineCreator {

	private String[] wordList;
	private Random random = new Random();
	
	WordListLineCreator(BufferedReader reader) {
		wordList = reader.lines().collect(Collectors.toList()).toArray(new String[0]);
	}

	@Override
	public String create(int length) {
		StringBuilder sb = new StringBuilder(length+1);
		String word;
		while(length > 0) {
			word = wordList[random.nextInt(wordList.length)];
			if(length - word.length() < 0)
				break;
			sb.append(word);
			sb.append(' ');
			length -= (word.length()+1);
		}
		//delete last space before line break
		sb.setLength(sb.length()-1);
		//append a newline character
		sb.append('\n');
		return sb.toString();
	}

}
