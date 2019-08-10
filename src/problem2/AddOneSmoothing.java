/**
 * 
 */
package problem2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import corpus.Corpus;

/**
 * @author yzc
 *
 */
public class AddOneSmoothing extends AbstractSmoothing {

	/**
	 * @param corpus Corpus to be processed
	 */
	public AddOneSmoothing(Corpus corpus) {
		super(corpus);
	}

	/**
	 * @param stringArray An array of String
	 */
	public AddOneSmoothing(List<String> stringArray) {
		super(stringArray);
	}

	/* (non-Javadoc)
	 * @see problem2.Smoothing#computeBigramModel(java.util.Map, java.util.Map)
	 */
	@Override
	public void computeBigramModel(Map<String, Double> bigramCount, Map<String, Double> bigramProbability) {
		// bigramCount & bigramProbability
		List<Map.Entry<String, Integer>> bigramCountList = new ArrayList<Map.Entry<String, Integer>>(bigramCountOriginalMap.entrySet());
		for(int i = 0; i < bigramCountList.size(); i++) {
			String bigramWordPair[] = bigramCountList.get(i).getKey().split(" ");
			// bigramProbability
			int formerWordCount = unigramCountOriginalMap.get(bigramWordPair[0]);
			double probability = (double)(bigramCountList.get(i).getValue() + 1) / (double)(formerWordCount + unigramCountOriginalMap.size());
			bigramProbability.put(bigramCountList.get(i).getKey(), probability);

			// bigramCount
			double bigramCountSmoothed = probability * formerWordCount;
			bigramCount.put(bigramCountList.get(i).getKey(), bigramCountSmoothed);
		}

		//zeroFrequencyDefault
		zeroFrequencyDefaultBase = 0.0;
	}

	/* (non-Javadoc)
	 * @see problem2.SmoothingImpl#getZeroFrequencyDefault(java.lang.String)
	 */
	@Override
	public double getZeroFrequencyDefault(String bigramWordPair) {
		return (zeroFrequencyDefaultBase + 1.0) / (double)(unigramCountOriginalMap.get(bigramWordPair.split(" ")[0]) + unigramCountOriginalMap.size());
	}

	/* (non-Javadoc)
	 * @see problem2.Smoothing#getName()
	 */
	@Override
	public String getName() {
		return "Add-One Smoothing";
	}
}
