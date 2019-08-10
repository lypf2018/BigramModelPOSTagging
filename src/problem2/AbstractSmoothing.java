/**
 * 
 */
package problem2;

import java.util.Map;

import corpus.Corpus;

import java.util.HashMap;
import java.util.List;

/**
 * @author yzc
 *
 */
public abstract class AbstractSmoothing implements Smoothing {

	protected Map<String, Integer> unigramCountOriginalMap;

	protected int totalBigramCount;
	protected Map<String, Integer> bigramCountOriginalMap;
	protected double zeroFrequencyDefaultBase;

	/**
	 * @param corpus Corpus to be processed
	 */
	public AbstractSmoothing(Corpus corpus) {
		unigramCountOriginalMap = corpus.getWordCountMap();
		bigramCountOriginalMap = new HashMap<>();
		computeBigramCountOriginal(corpus);
	}
	
	/**
	 * @param stringArray An array of String
	 */
	public AbstractSmoothing(List<String> stringArray) {
		unigramCountOriginalMap = new HashMap<>();
		bigramCountOriginalMap = new HashMap<>();
		computeBigramCountOriginal(stringArray);
	}
	
	/* (non-Javadoc)
	 * @see problem2.Smoothing#getZeroFrequencyDefault(java.lang.String)
	 */
	@Override
	public double getZeroFrequencyDefault(String bigramWordPair) {
		return zeroFrequencyDefaultBase;
	}

	/**
	 * Compute original bigram count and save into bigramCountOriginalMap for a String array
	 * @param stringArray An array of String
	 */
	protected void computeBigramCountOriginal(List<String> stringArray) {
		for (int i = 0; i < stringArray.size() - 1; i++) {
			String bigramWordPair = new String(stringArray.get(i) + " " + stringArray.get(i+1));
			unigramCountOriginalMap.put(stringArray.get(i), unigramCountOriginalMap.getOrDefault(stringArray.get(i), 0) + 1);
			bigramCountOriginalMap.put(bigramWordPair, (bigramCountOriginalMap.getOrDefault(bigramWordPair, 0) + 1));
			totalBigramCount++;
		}
		unigramCountOriginalMap.put(stringArray.get(stringArray.size() - 1), unigramCountOriginalMap.getOrDefault(stringArray.get(stringArray.size() - 1), 0) + 1);
	}

	/**
	 * Compute original bigram count and save into bigramCountOriginalMap under specific corpus
	 * Bigram is an inner sentence one
	 * @param corpus Specific corpus
	 */
	protected void computeBigramCountOriginal(Corpus corpus) {
		String[] sentenceArray = corpus.getCorpusString().split(" \\. ");
		for (int i = 0; i < sentenceArray.length; i++) {
			StringBuffer sentenceStringBuffer = new StringBuffer(sentenceArray[i].trim());
			if(i != sentenceArray.length - 1) {
				sentenceStringBuffer.append(" .");
			}
        	String[] wordArray = sentenceStringBuffer.toString().split("\\s+");
        	for(int j = 1; j < wordArray.length; j++) {
        		String bigramWordPair = new String(wordArray[j-1] + " " + wordArray[j]);
        		bigramCountOriginalMap.put(bigramWordPair, (bigramCountOriginalMap.getOrDefault(bigramWordPair, 0) + 1));
				totalBigramCount++;
        	}
		}
	}
}
