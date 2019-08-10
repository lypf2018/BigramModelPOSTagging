/**
 * 
 */
package problem2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import corpus.Corpus;

/**
 * @author yzc
 *
 */
public class BigramProbabilities {
	public static final int NO_SMOOTHING = 0;
	public static final int ADD_ONE_SMOOTHING = 1;
	public static final int GOOD_TURING_SMOOTHING = 2;
	public static final int[] SMOOTHING_LIST = {NO_SMOOTHING, ADD_ONE_SMOOTHING, GOOD_TURING_SMOOTHING};
	private Map<String, Double> bigramCount;
	private Map<String, Double> bigramProbability;
	private Smoothing smoothing;
	/**
	 * @return the bigramCount
	 */
	public Map<String, Double> getBigramCount() {
		return bigramCount;
	}
	/**
	 * @return the bigramProbability
	 */
	public Map<String, Double> getBigramProbability() {
		return bigramProbability;
	}

	/**
	 * @return the smoothing
	 */
	public Smoothing getSmoothing() {
		return smoothing;
	}
	/**
	 * Bigram is an inner sentence one
	 * @param corpus Corpus to be processed
	 */
	public BigramProbabilities(Corpus corpus, int smoothing) {
		if(smoothing < 0 || smoothing > SMOOTHING_LIST.length - 1) {
			throw new IllegalArgumentException();
		}
		bigramCount = new HashMap<>();
		bigramProbability = new HashMap<>();

		// Strategy Pattern Design
		switch (smoothing) {
		case NO_SMOOTHING:
			this.smoothing = new NoSmoothing(corpus);
			break;

		case ADD_ONE_SMOOTHING:
			this.smoothing = new AddOneSmoothing(corpus);
			break;

		case GOOD_TURING_SMOOTHING:
			this.smoothing = new GoodTuringSmoothing(corpus);
			break;

		default:
			break;
		}

		this.smoothing.computeBigramModel(bigramCount, bigramProbability);
	}

	/**
	 * @param stringArray An array of String
	 * @param smoothing Smoothing way
	 */
	public BigramProbabilities(List<String> stringArray, int smoothing) {
		if(smoothing < 0 || smoothing > SMOOTHING_LIST.length - 1) {
			throw new IllegalArgumentException();
		}
		bigramCount = new HashMap<>();
		bigramProbability = new HashMap<>();

		// Strategy Pattern Design
		switch (smoothing) {
		case NO_SMOOTHING:
			this.smoothing = new NoSmoothing(stringArray);
			break;

		case ADD_ONE_SMOOTHING:
			this.smoothing = new AddOneSmoothing(stringArray);
			break;

		case GOOD_TURING_SMOOTHING:
			this.smoothing = new GoodTuringSmoothing(stringArray);
			break;

		default:
			break;
		}

		this.smoothing.computeBigramModel(bigramCount, bigramProbability);
	}

	/**
	 * Static method. Computes total probability for all smoothing ways with input sentence String under specific corpus
	 * @param inputSentence Input sentence
	 * @param corpus Specific corpus
	 */
	public static void computeTotalProbabilityForAllSmoothingWays(String inputSentence, Corpus corpus) {
		// Process input sentence
		String[] wordArray = inputSentence.split("\\s+");
		List<String> bigramWordPairList = new ArrayList<>();
    	for(int i = 1; i < wordArray.length; i++) {
    		String bigramWordPair = new String(wordArray[i-1] + " " + wordArray[i]);
    		bigramWordPairList.add(bigramWordPair);
    	}

    	// Output result 
    	for (int j = 0; j < SMOOTHING_LIST.length; j++) {
    		BigramProbabilities bigramProbabilities = new BigramProbabilities(corpus, SMOOTHING_LIST[j]);
    		Map<String, Double> bigramProbability = bigramProbabilities.getBigramProbability();
    		double totalProbability = 1.0;
    		for(int k = 0; k < bigramWordPairList.size(); k++) {
    			totalProbability *= bigramProbability.getOrDefault(bigramWordPairList.get(k), bigramProbabilities.getSmoothing().getZeroFrequencyDefault(bigramWordPairList.get(k)));
    		}
    		System.out.println("The total probability for the input sentence using " + bigramProbabilities.getSmoothing().getName() + " is: " + totalProbability);
		}
	}

	/**
	 * Outputs Bigram Model into file showing Count and Probability for current BigramProbabilities instance (Smoothing Way)
	 * @param fileName Output file name with path
	 * @param append boolean value specifying file writing way, append or not
	 */
	public void outputBigramModel(String fileName, boolean append) {
		try {
			FileWriter fileWriter = new FileWriter(fileName, append);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			// Write Count
			bufferedWriter.write("Count for " + getSmoothing().getName() + " is:\n");
			List<Map.Entry<String, Double>> bigramCountList = new ArrayList<Map.Entry<String, Double>>(bigramCount.entrySet());
			for(int i = 0; i < bigramCountList.size(); i++) {
				bufferedWriter.write(bigramCountList.get(i).getKey() + ":" + bigramCountList.get(i).getValue() + "\n");
			}
			bufferedWriter.write("\n");

			// Write Probability
			bufferedWriter.write("Probability for " + getSmoothing().getName() + " is:\n");
			List<Map.Entry<String, Double>> bigramProbabilityList = new ArrayList<Map.Entry<String, Double>>(bigramProbability.entrySet());
			for(int i = 0; i < bigramProbabilityList.size(); i++) {
				bufferedWriter.write(bigramProbabilityList.get(i).getKey() + ":" + bigramProbabilityList.get(i).getValue() + "\n");
			}
			bufferedWriter.write("\n");

			bufferedWriter.flush();
			bufferedWriter.close();	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Default file writing way of not append
	 * @param fileName Output file name with path
	 */
	public void outputBigramModel(String fileName) {
		outputBigramModel(fileName, false);
	}

	/**
	 * Static method. Outputs bigram model for all smoothing ways under specific corpus
	 * @param corpus Specific corpus
	 * @param fileName Output file name with path
	 */
	public static void outputBigramModelForAllSmoothingWays(Corpus corpus, String fileName) {
    	for (int i = 0; i < SMOOTHING_LIST.length; i++) {
    		BigramProbabilities bigramProbabilities = new BigramProbabilities(corpus, SMOOTHING_LIST[i]);
    		bigramProbabilities.outputBigramModel(fileName, i != 0);
		}
	}
}
