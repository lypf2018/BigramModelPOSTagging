/**
 * 
 */
package problem2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import corpus.Corpus;
//import util.MapUtil;

/**
 * @author yzc
 *
 */
public class GoodTuringSmoothing extends AbstractSmoothing {

	/**
	 * @param corpus Corpus to be processed
	 */
	public GoodTuringSmoothing(Corpus corpus) {
		super(corpus);
	}

	/**
	 * @param stringArray An array of String
	 */
	public GoodTuringSmoothing(List<String> stringArray) {
		super(stringArray);
	}

	/* (non-Javadoc)
	 * @see problem2.Smoothing#computeBigramModel(java.util.Map, java.util.Map)
	 */
	@Override
	public void computeBigramModel(Map<String, Double> bigramCount, Map<String, Double> bigramProbability) {
		// bigramCount
//		List<Map.Entry<String, Integer>> bigramCountList = MapUtil.mapToListSortedByValue(bigramCountOriginalMap, true);
		List<Map.Entry<String, Integer>> bigramCountList = bigramCountOriginalMap.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toList());

		List<Integer> bucketBigram = new ArrayList<Integer>();
		for(int i = 0; i <= bigramCountList.get(bigramCountList.size() - 1).getValue(); i++) {
			bucketBigram.add(0);
		}

		bucketBigram.set(0, 1);
		for(int j = 0; j < bigramCountList.size(); j++) {
			int index = bigramCountList.get(j).getValue();
			bucketBigram.set(index, bucketBigram.get(index) + 1);
		}
		bucketBigram.add(1);

		for(int k = 0; k < bigramCountList.size(); k++) {
			double bigramCountSmoothed = (double)((bigramCountList.get(k).getValue() + 1) * bucketBigram.get(bigramCountList.get(k).getValue() + 1)) / (double)(bucketBigram.get(bigramCountList.get(k).getValue()));
			bigramCount.put(bigramCountList.get(k).getKey(), bigramCountSmoothed);
		}

		// bigramProbability
		List<Map.Entry<String, Double>> bigramCountSmoothedList = new ArrayList<Map.Entry<String, Double>>(bigramCount.entrySet());
		for(int l = 0; l < bigramCountSmoothedList.size(); l++) {
			double probability = (double)bigramCountSmoothedList.get(l).getValue() / (double)this.totalBigramCount;
			bigramProbability.put(bigramCountSmoothedList.get(l).getKey(), probability);
		}

		//zeroFrequencyDefault
		zeroFrequencyDefaultBase = (double)bucketBigram.get(1) / (double)this.totalBigramCount;
	}

	/* (non-Javadoc)
	 * @see problem2.Smoothing#getName()
	 */
	@Override
	public String getName() {
		return "Good-Turing Smoothing";
	}
}
