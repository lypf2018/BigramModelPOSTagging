package corpus;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Corpus {

	private String corpusString;
	protected List<String> wordArray;
	protected Map<String, Integer> wordCountMap;
	protected int tokensSize;
	protected int types;

	/**
	 * @return the corpusString
	 */
	public String getCorpusString() {
		return corpusString;
	}

	/**
	 * @return the wordArray
	 */
	public List<String> getWordArray() {
		return wordArray;
	}

	/**
	 * @return the tokensSize
	 */
	public int getTokensSize() {
		return tokensSize;
	}

	/**
	 * @return the types
	 */
	public int getTypes() {
		return types;
	}

	/**
	 * @return the wordCountMap
	 */
	public Map<String, Integer> getWordCountMap() {
		return wordCountMap;
	}

	/**
	 * Constructor with file name
	 * @param fileName file name with path
	 */
	public Corpus(String fileName) {
		readCorpusFromFile(fileName);
		init();
	}

	/**
	 * Initialize corpus to set member variables.
	 * This method could be inherited.
	 */
	protected void init() {
		wordCount();
	}

	/**
	 * Count word frequency and save to member variable wordCountMap
	 * Meanwhile, calculate tokensSize and types member variable
	 */
	private void wordCount() {
		wordArray = new ArrayList<>();
		wordCountMap = new HashMap<>();
		tokensSize = 0;
		String[] sentenceArray = corpusString.split(" \\. ");
		for (int i = 0; i < sentenceArray.length; i++) {
			StringBuffer sentenceStringBuffer = new StringBuffer(sentenceArray[i].trim());
			if(i != sentenceArray.length - 1) {
				sentenceStringBuffer.append(" .");
			}
        	String[] wordStringArray = sentenceStringBuffer.toString().split("\\s+");
        	for(int j = 0; j < wordStringArray.length; j++) {
        		wordArray.add(wordStringArray[j]);
        		wordCountMap.put(wordStringArray[j], (wordCountMap.getOrDefault(wordStringArray[j], 0) + 1));
        		tokensSize++;
        	}
		}
		types = wordCountMap.size();
	}

	/**
	 * Read corpus from given file
	 * @param fileName file name with path
	 * @return Corpus string
	 */
	private void readCorpusFromFile(String fileName) {
		StringBuffer corpusStringBuffer = new StringBuffer();
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
			String lineInCorpus = null;
			while((lineInCorpus = bufferedReader.readLine())!=null) {
				corpusStringBuffer.append(lineInCorpus);
				corpusStringBuffer.append("\n");
			}
			bufferedReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		corpusString = corpusStringBuffer.toString();
	}
}
