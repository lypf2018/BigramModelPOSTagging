/**
 * 
 */
package problem2;

import java.util.Map;

/**
 * @author yzc
 *
 */
public interface Smoothing {

	/**
	 * Compute Bigram Model and save reslut into bigramCount and bigramProbability
	 * @param bigramCount Calculated bigram count with specific smoothing way, saving result into bigramCount Map
	 * @param bigramProbability Calculated bigram probability with specific smoothing way, saving result into bigramProbability Map
	 */
	public void computeBigramModel(Map<String, Double> bigramCount, Map<String, Double> bigramProbability);

	/**
	 * Calculate Default bigram probability of zero frequency bigram word pair
	 * @param bigram word pair
	 * @return Default bigram probability of zero frequency bigram word pair
	 */
	public double getZeroFrequencyDefault(String bigramWordPair);

	/**
	 * @return Smoothing way name
	 */
	public String getName();
}
