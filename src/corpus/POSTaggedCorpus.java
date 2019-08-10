/**
 * 
 */
package corpus;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yzc
 *
 */
public class POSTaggedCorpus extends Corpus {

	private List<String> tagArray;
	private Map<String, Integer> tagCountMap;
	private List<String> wordTagArray;
	private Map<String, Integer> wordTagCountMap;
	private Map<String, Map<String, Integer>> wordTagStat;
	private Map<String, String> wordMostFrequentTagMap;
	private Map<String, Map<String, Integer>> tagWordStat;
	private Map<String, String> tagMostFrequentWordMap;

	/**
	 * @return the tagArray
	 */
	public List<String> getTagArray() {
		return tagArray;
	}

	/**
	 * @return the tagCountMap
	 */
	public Map<String, Integer> getTagCountMap() {
		return tagCountMap;
	}

	/**
	 * @return the wordTagArray
	 */
	public List<String> getWordTagArray() {
		return wordTagArray;
	}

	/**
	 * @return the wordTagCountMap
	 */
	public Map<String, Integer> getWordTagCountMap() {
		return wordTagCountMap;
	}

	/**
	 * @return the wordTagStat
	 */
	public Map<String, Map<String, Integer>> getWordTagStat() {
		return wordTagStat;
	}

	/**
	 * @return the wordMostFrequentTagMap
	 */
	public Map<String, String> getWordMostFrequentTagMap() {
		return wordMostFrequentTagMap;
	}

	/**
	 * @return the tagWordStat
	 */
	public Map<String, Map<String, Integer>> getTagWordStat() {
		return tagWordStat;
	}

	/**
	 * @return the tagMostFrequentWordMap
	 */
	public Map<String, String> getTagMostFrequentWordMap() {
		return tagMostFrequentWordMap;
	}

	/**
	 * Constructor with file name
	 * @param fileName file name with path
	 */
	public POSTaggedCorpus(String fileName) {
		super(fileName);
	}

	/* (non-Javadoc)
	 * @see corpus.Corpus#init()
	 */
	@Override
	protected void init() {
		wordTagCount();
	}

	/**
	 * Parse corpus to get attribute values.
	 */
	private void wordTagCount() {
		wordArray = new ArrayList<>();
		wordCountMap = new HashMap<>();
		tagArray = new ArrayList<>();
		tagCountMap = new HashMap<>();
		wordTagArray = new ArrayList<>();
		wordTagCountMap = new HashMap<>();
		wordTagStat = new HashMap<>();
		wordMostFrequentTagMap = new HashMap<>();
		tagWordStat = new HashMap<>();
		tagMostFrequentWordMap = new HashMap<>();

		String[] wordTagStringArray = getCorpusString().split("\\s+");

		for(int i = 0; i < wordTagStringArray.length; i++) {
			// wordTagArray
			wordTagArray.add(wordTagStringArray[i]);

			// wordTagCountMap
			wordTagCountMap.put(wordTagStringArray[i], (wordTagCountMap.getOrDefault(wordTagStringArray[i], 0) + 1));

			String[] wordTagPair = wordTagStringArray[i].split("\\_");

			// wordArray
			wordArray.add(wordTagPair[0]);

			// wordCountMap
			wordCountMap.put(wordTagPair[0], (wordCountMap.getOrDefault(wordTagPair[0], 0) + 1));

			// tagArray
			tagArray.add(wordTagPair[1]);

			// tagCountMap
			tagCountMap.put(wordTagPair[1], (tagCountMap.getOrDefault(wordTagPair[1], 0) + 1));

			// wordTagStat
			if (!wordTagStat.containsKey(wordTagPair[0])) {
				Map<String, Integer> tagCountForEachWordMap = new HashMap<>();
				tagCountForEachWordMap.put(wordTagPair[1], 1);
				wordTagStat.put(wordTagPair[0], tagCountForEachWordMap);
				wordMostFrequentTagMap.put(wordTagPair[0], wordTagPair[1]);
			} else {
				wordTagStat.get(wordTagPair[0]).put(wordTagPair[1], wordTagStat.get(wordTagPair[0]).getOrDefault(wordTagPair[1], 0) + 1);
				// Put most frequent Tag into wordMostFrequentTag
				if (wordTagStat.get(wordTagPair[0]).get(wordTagPair[1]) > wordTagStat.get(wordTagPair[0]).get(wordMostFrequentTagMap.get(wordTagPair[0]))) {
					wordMostFrequentTagMap.put(wordTagPair[0], wordTagPair[1]);
				}
			}

			// tagWordStat
			if (!tagWordStat.containsKey(wordTagPair[1])) {
				Map<String, Integer> wordCountForEachTagMap = new HashMap<>();
				wordCountForEachTagMap.put(wordTagPair[0], 1);
				tagWordStat.put(wordTagPair[1], wordCountForEachTagMap);
				tagMostFrequentWordMap.put(wordTagPair[1], wordTagPair[0]);
			} else {
				tagWordStat.get(wordTagPair[1]).put(wordTagPair[0], tagWordStat.get(wordTagPair[1]).getOrDefault(wordTagPair[0], 0) + 1);
				// Put most frequent Word into tagMostFrequentWord
				if (tagWordStat.get(wordTagPair[1]).get(wordTagPair[0]) > tagWordStat.get(wordTagPair[1]).get(tagMostFrequentWordMap.get(wordTagPair[1]))) {
					tagMostFrequentWordMap.put(wordTagPair[1], wordTagPair[0]);
				}
			}
			
    		// tokensSize
    		tokensSize++;
		}

		// types
		types = wordTagArray.size();
	}

	/**
	 * Get Word-Tag list
	 * @return Word-Tag list
	 */
	public List<String[]> getWordTagListCopy() {
		String[] wordTagArray = getCorpusString().split("\\s+");
		List<String[]> wordTagList = new ArrayList<String[]>();
		for(int i = 0; i < wordTagArray.length; i++) {
			wordTagList.add(wordTagArray[i].split("\\_"));
		}
		return wordTagList;
	}

	/**
	 * Test method. Output word tag pair list
	 * @param wordTagList list of word tag as string array
	 * @param fileName file name with path
	 */
	public static void outputWordTagPairList(List<String[]> wordTagList, String fileName) {
		try {
			FileWriter fileWriter = new FileWriter(fileName);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			for (int i = 0; i < wordTagList.size(); i++) {
				String wordTag = wordTagList.get(i)[0] + "_" + wordTagList.get(i)[1];
				bufferedWriter.write(wordTag);				
				bufferedWriter.write(" ");				
				if (wordTagList.get(i)[0].equals(".")) {
					bufferedWriter.write("\n");				
				}
			}
			bufferedWriter.flush();
			bufferedWriter.close();	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test method. Output word tag pair list. Each pair per line.
	 * @param wordTagList list of word tag as string array
	 * @param fileName file name with path
	 */
	public static void outputWordTagPairListEachOneLine(List<String[]> wordTagList, String fileName) {
		try {
			FileWriter fileWriter = new FileWriter(fileName);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			for (int i = 0; i < wordTagList.size(); i++) {
				String wordTag = wordTagList.get(i)[0] + "_" + wordTagList.get(i)[1];
				bufferedWriter.write(wordTag);				
				bufferedWriter.write("\n");				
			}
			bufferedWriter.flush();
			bufferedWriter.close();	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
