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
public class NoSmoothing extends AbstractSmoothing {

	/**
	 * @param corpus Corpus to be processed
	 */
	public NoSmoothing(Corpus corpus) {
		super(corpus);
	}

	/**
	 * @param stringArray An array of String
	 */
	public NoSmoothing(List<String> stringArray) {
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
			// bigramCount
			bigramCount.put(bigramCountList.get(i).getKey(), bigramCountList.get(i).getValue().doubleValue());

			// bigramProbability
			String bigramWordPair[] = bigramCountList.get(i).getKey().split(" ");
			double probability = (double)bigramCountList.get(i).getValue() / (double)unigramCountOriginalMap.get(bigramWordPair[0]);
			bigramProbability.put(bigramCountList.get(i).getKey(), probability);
		}

		//zeroFrequencyDefault
		zeroFrequencyDefaultBase = 0.0;
	}

	/* (non-Javadoc)
	 * @see problem2.Smoothing#getName()
	 */
	@Override
	public String getName() {
		return "No Smoothing";
	}
}
