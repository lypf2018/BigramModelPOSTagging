/**
 * 
 */
package problem3;

/**
 * @author yzc
 *
 */
public interface POSTagging {

	/**
	 * Fill out missing POS tags in inputSentence
	 * @param inputSentence Input sentence
	 */
	public void fillOutMissingPOSTags(String inputSentence);

	/**
	 * Output tagging model into file
	 * @param fileName Output file name with path
	 */
	public void outputModel(String fileName);
}
