/**
 * 
 */
package problem3;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import corpus.POSTaggedCorpus;
import util.MapUtil;

/**
 * @author yzc
 *
 */
public class TransformationBasedPOSTagging implements POSTagging {

	private Map<String, String> wordMostFrequentTagMap;

	private List<String[]> correctWordTagList;
	private List<String[]> currentWordTagList;

	/**
	 * @param corpus the POS tagged corpus
	 */
	public TransformationBasedPOSTagging(POSTaggedCorpus corpus) {
		// Get wordMostFrequentTagMap
		wordMostFrequentTagMap = corpus.getWordMostFrequentTagMap();

		// Prepare correctWordTagList and currentWordTagList
		correctWordTagList = corpus.getWordTagListCopy();
		currentWordTagList = corpus.getWordTagListCopy();
		for(int i = 0; i < currentWordTagList.size(); i++) {
			currentWordTagList.get(i)[1] = wordMostFrequentTagMap.get(currentWordTagList.get(i)[0]);
		}
	}

	/* (non-Javadoc)
	 * @see problem3.POSTagging#fillOutMissingPOSTags(java.lang.String)
	 */
	@Override
	public void fillOutMissingPOSTags(String inputSentence) {
		// Process input sentence
		String[] wordTagArray = inputSentence.split("\\s+");
		List<String[]> wordTagList = new ArrayList<String[]>();
		List<Integer> missingTagIndex = new ArrayList<>();
		for(int i = 0; i < wordTagArray.length; i++) {
			String[] wordTagPair = wordTagArray[i].split("\\_");
			wordTagList.add(wordTagPair);
			if ("??".equals(wordTagPair[1])) {
				missingTagIndex.add(i);
			}
		}

		// Fill out missing tag
		for (int i = 0; i < missingTagIndex.size(); i++) {
			// Initialize
			wordTagList.get(missingTagIndex.get(i))[1] = wordMostFrequentTagMap.getOrDefault(wordTagList.get(missingTagIndex.get(i))[0], "??");

			// Apply rules
			// NN to JJ
			if (bestPreviousTag(wordTagList.get(missingTagIndex.get(i))[1], "JJ").equals((missingTagIndex.get(i) > 0) ? wordTagList.get(missingTagIndex.get(i) - 1)[1] : null)) {
				wordTagList.get(missingTagIndex.get(i))[1] = "JJ";
			}
			// NN to VB
			if (bestPreviousTag(wordTagList.get(missingTagIndex.get(i))[1], "VB").equals((missingTagIndex.get(i) > 0) ? wordTagList.get(missingTagIndex.get(i) - 1)[1] : null)) {
				wordTagList.get(missingTagIndex.get(i))[1] = "VB";
			}
		}

		// Display OriginalS Sentence
		System.out.println(inputSentence);

		// Display Tagged Sentence
		for (int i = 0; i < wordTagList.size(); i++) {
			String wordTag = wordTagList.get(i)[0] + "_" + wordTagList.get(i)[1];
			System.out.print(wordTag);
			if (i != wordTagList.size() - 1) {
				System.out.print(" ");
			}
		}
		System.out.println();
		System.out.println();

		// Display filled tag
		for (int i = 0; i < missingTagIndex.size(); i++) {
			String wordTag = wordTagList.get(missingTagIndex.get(i))[0] + "_" + wordTagList.get(missingTagIndex.get(i))[1];
			System.out.print(wordTag + " ; ");
			System.out.println("previousWordTag: " + ((missingTagIndex.get(i) > 0) ? (wordTagList.get(missingTagIndex.get(i) - 1)[0] + "_" + wordTagList.get(missingTagIndex.get(i) - 1)[1]) : null));
		}
	}

	/* (non-Javadoc)
	 * @see problem3.POSTagging#outputModel(java.lang.String)
	 */
	@Override
	public void outputModel(String fileName) {
		outputTransformationRule(fileName, "NN", "JJ");
		outputTransformationRule(fileName, true, "NN", "VB");
	}

	/**
	 * Extract the best transformation rule to Transform from fromTag to toTag.
	 * @param fromTag From tag
	 * @param toTag To tag
	 * @return The best previous tag for transformation rule
	 */
	public String bestPreviousTag(String fromTag, String toTag) {
		Map<String, Integer> previousTagScoreMap = new HashMap<String, Integer>();

		// Calculate highest score
		for(int i = 1; i < currentWordTagList.size(); i++) {
			if(correctWordTagList.get(i)[1].equals(toTag) && currentWordTagList.get(i)[1].equals(fromTag)) {
				previousTagScoreMap.put(currentWordTagList.get(i-1)[1], previousTagScoreMap.getOrDefault(currentWordTagList.get(i-1)[1], 0) + 1);
			} else if (correctWordTagList.get(i)[1].equals(fromTag) && currentWordTagList.get(i)[1].equals(fromTag)) {
				previousTagScoreMap.put(currentWordTagList.get(i-1)[1], previousTagScoreMap.getOrDefault(currentWordTagList.get(i-1)[1], 0) - 1);
			}
		}

		// Return highest score(value) word(key)
		// Traditional method
//		List<Map.Entry<String, Integer>> previousTagScoreList = MapUtil.mapToListSortedByValue(previousTagScoreMap);
//
//		return previousTagScoreList.get(0).getKey();
		// Stream method
		return previousTagScoreMap.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
	}

	/**
	 * Outputs Transformation Rule into file, default file writing way of not append
	 * @param fileName Output file name with path
	 * @param fromTag from-tag
	 * @param toTag to-tag
	 */
	public void outputTransformationRule(String fileName, String fromTag, String toTag) {
		outputTransformationRule(fileName, false, fromTag, toTag);
	}

	/**
	 * Outputs Transformation Rule into file
	 * @param fileName Output file name with path
	 * @param append boolean value specifying file writing way, append or not
	 * @param fromTag from-tag
	 * @param toTag to-tag
	 */
	public void outputTransformationRule(String fileName, boolean append, String fromTag, String toTag) {
		try {
			FileWriter fileWriter = new FileWriter(fileName, append);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write("The best transformation rule to transform " + fromTag + " to " + toTag +  " is:\n" + "Change tag from " + fromTag + " to " + toTag + " if prev tag is  " + bestPreviousTag(fromTag, toTag) + "\n");				
			bufferedWriter.flush();
			bufferedWriter.close();	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test method. Pick out indexes in wordTagList where tag equals fromTag.
	 * @param wordTagList Word tag list
	 * @param fromTag from-tag
	 * @return indexes list
	 */
	private List<Integer> pickFromTag(List<String[]> wordTagList, String fromTag) {
		List<Integer> listFromTag = new ArrayList<Integer>();
		for (int i = 0; i < wordTagList.size(); i++) {
			if (wordTagList.get(i)[1].equals(fromTag)) {
				listFromTag.add(i);
			}
		}
		return listFromTag;
	}

	/**
	 * Test method. Pick out indexes, where from-tag equals fromTag in currentWordTag and to-tag equals toTag in correctWordTag.
	 * @param currentWordTag Current word tag pair list
	 * @param correctWordTag Current word tag pair list
	 * @param fromTag from-tag
	 * @param toTag to-tag
	 * @return indexes list
	 */
	private List<Integer> pickFromTagToToTag(List<String[]> currentWordTag, List<String[]> correctWordTag, String fromTag, String toTag) {
		List<Integer> fromTagList = pickFromTag(currentWordTag, fromTag);
		List<Integer> fromTagToToTag = new ArrayList<>();
		for (int i = 0; i < fromTagList.size(); i++) {
			if (correctWordTag.get(fromTagList.get(i))[1].equals(toTag)) {
				fromTagToToTag.add(fromTagList.get(i));
			}
		}
		return fromTagToToTag;
	}

	/**
	 * Test method. Output word tag pairs, where from-tag equals fromTag in currentWordTagList and to-tag equals toTag in correctWordTagList 
	 * @param fromTag from-tag
	 * @param toTag to-tag
	 */
	public void outputFromTagToToTag(String fromTag, String toTag) {
		List<String[]> correctWordTagListFromTagToToTag = new ArrayList<>();
		List<String[]> currentWordTagListFromTagToToTag = new ArrayList<>();
		List<String[]> previousWordTagListFromTagToToTag = new ArrayList<>();
		Map<String, Integer> previousWordTagMapFromTagToToTag = new HashMap<>();
		List<Integer> indexFromTagToToTag = pickFromTagToToTag(currentWordTagList, correctWordTagList, fromTag, toTag);
		for (int i = 0; i < indexFromTagToToTag.size(); i++) {
			String[] previousWordTagFromTagToToTag = {currentWordTagList.get(indexFromTagToToTag.get(i) - 1)[1], currentWordTagList.get(indexFromTagToToTag.get(i) - 1)[0]};
			previousWordTagListFromTagToToTag.add(previousWordTagFromTagToToTag);
			previousWordTagMapFromTagToToTag.put(currentWordTagList.get(indexFromTagToToTag.get(i) - 1)[1], previousWordTagMapFromTagToToTag.getOrDefault(currentWordTagList.get(indexFromTagToToTag.get(i) - 1)[1], 0) + 1);
			String[] currentWordTagFromTagToToTag = {currentWordTagList.get(indexFromTagToToTag.get(i))[1], currentWordTagList.get(indexFromTagToToTag.get(i))[0]};
			currentWordTagListFromTagToToTag.add(currentWordTagFromTagToToTag);
			String[] correctWordTagFromTagToToTag = {correctWordTagList.get(indexFromTagToToTag.get(i))[1], correctWordTagList.get(indexFromTagToToTag.get(i))[0]};
			correctWordTagListFromTagToToTag.add(correctWordTagFromTagToToTag);
		}
		POSTaggedCorpus.outputWordTagPairListEachOneLine(previousWordTagListFromTagToToTag, "previousWordTagList" + fromTag + "To" + toTag + ".txt");
		MapUtil.outputMapValueSortedOrder(previousWordTagMapFromTagToToTag, "previousWordTagMap" + fromTag + "To" + toTag + ".txt");
		POSTaggedCorpus.outputWordTagPairListEachOneLine(currentWordTagListFromTagToToTag, "currentWordTagList" + fromTag + "To" + toTag + ".txt");
		POSTaggedCorpus.outputWordTagPairListEachOneLine(correctWordTagListFromTagToToTag, "correctWordTagList" + fromTag + "To" + toTag + ".txt");
	}
}
