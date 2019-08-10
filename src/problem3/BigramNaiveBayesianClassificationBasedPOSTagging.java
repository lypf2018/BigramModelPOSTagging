/**
 * 
 */
package problem3;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import corpus.POSTaggedCorpus;
import problem2.BigramProbabilities;

/**
 * @author yzc
 *
 */
public class BigramNaiveBayesianClassificationBasedPOSTagging implements POSTagging {

	private Map<String, Integer> wordTagCountMap;
	private Map<String, Integer> tagCountMap;

	private Map<String, Double> tagBigramCountMap; 
	private Map<String, Double> tagBigramProbabilityMap;

	/**
	 * @param corpus the POS tagged corpus
	 */
	public BigramNaiveBayesianClassificationBasedPOSTagging(POSTaggedCorpus corpus) {
		wordTagCountMap = corpus.getWordTagCountMap();
		tagCountMap = corpus.getTagCountMap();
		BigramProbabilities bigramProbabilities = new BigramProbabilities(corpus.getTagArray(), BigramProbabilities.NO_SMOOTHING);
		tagBigramCountMap = bigramProbabilities.getBigramCount();
		tagBigramProbabilityMap = bigramProbabilities.getBigramProbability();
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
		for (int j = 0; j < missingTagIndex.size(); j++) {
			String bestTag = "??";
			double highestProbability = 0.0;
			List<String> tagArray = new ArrayList<>(tagCountMap.keySet());
			for (int k = 0; k < tagArray.size(); k++) {
				// P(wi|ti)=C(ti,wi)/C(ti)
				// Calculate C(ti,wi)
				int wordTagCount = wordTagCountMap.getOrDefault(wordTagList.get(missingTagIndex.get(j))[0] + "_" + tagArray.get(k), 0);
				// Calculate C(ti)
				int tagCount = tagCountMap.get(tagArray.get(k));
				// Calculate P(wi|ti)=C(ti,wi)/C(ti)
				double wordTagProbability = (double)wordTagCount / (double)tagCount;

				// P(ti|ti-1)=C(ti-1,ti)/C(ti-1)
				// Calculate P(ti|ti-1)
				double lagTagBigramProbability = 1.0;
				if (missingTagIndex.get(j) > 0) {
					lagTagBigramProbability = tagBigramProbabilityMap.getOrDefault(wordTagList.get(missingTagIndex.get(j) - 1)[1] + " " + tagArray.get(k), 0.0);
				}

				// P(ti+1|ti)=C(ti,ti+1)/C(ti)
				// Calculate P(ti+1|ti)
				double leadTagBigramProbability = 1.0;
				if (missingTagIndex.get(j) < wordTagList.size() - 1) {
					leadTagBigramProbability = tagBigramProbabilityMap.getOrDefault(tagArray.get(k) + " " + wordTagList.get(missingTagIndex.get(j) + 1)[1], 0.0);
				}

				// Calculate bigram probability P(wi|ti)*P(ti|ti-1)*P(ti+1|ti)
				double bigramProbabilityProduct = wordTagProbability * lagTagBigramProbability * leadTagBigramProbability;

				if (bigramProbabilityProduct > highestProbability) {
					highestProbability = bigramProbabilityProduct;
					bestTag = tagArray.get(k);
				}
			}

			wordTagList.get(missingTagIndex.get(j))[1] = bestTag;
		}

		System.out.println("Bigram Naive Bayesian ClassificationBased POS Tagging Result:");

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
			String taggedWordTag = wordTagList.get(missingTagIndex.get(i))[0] + "_" + wordTagList.get(missingTagIndex.get(i))[1];
			System.out.println(taggedWordTag);
		}
	}

	/* (non-Javadoc)
	 * @see problem3.POSTagging#outputModel(java.lang.String)
	 */
	@Override
	public void outputModel(String fileName) {
		outputBigramModels(fileName);
	}

	/**
	 * Output Bigram Models required by the above Naive Bayesian Classification formula
	 * @param fileName Output file name with path
	 */
	public void outputBigramModels(String fileName) {
		try {
			FileWriter filewriter = new FileWriter(fileName);
			BufferedWriter bufferedWriter = new BufferedWriter(filewriter);
			bufferedWriter.write("The bigram models (counts and probabilities) required by the Naive Bayesian Classification formula is as follow:\n");				
			bufferedWriter.write("\n");
			// Output candidate tag list in sorted order
			bufferedWriter.write("The candidate tag list is:\n");
			List<String> tagList = new ArrayList<>(tagCountMap.keySet());
			Collections.sort(tagList, String.CASE_INSENSITIVE_ORDER);
			for (int i = 0; i < tagList.size(); i++) {
				bufferedWriter.write(tagList.get(i));
				bufferedWriter.write(((i == (tagList.size() - 1)) ? ".\n" : ", "));
			}
			bufferedWriter.write("\n");

			//P(wi|ti)=C(ti,wi)/C(ti)
			// Output C(ti)
			bufferedWriter.write("The tag count is as follow:\n");
			List<Map.Entry<String, Integer>> tagCountSortedList = tagCountMap.entrySet().stream().sorted(Map.Entry.comparingByKey(String.CASE_INSENSITIVE_ORDER)).collect(Collectors.toList());
			for (int i = 0; i < tagCountSortedList.size(); i++) {
				bufferedWriter.write(tagCountSortedList.get(i).getKey() + ":" + tagCountSortedList.get(i).getValue() + "\n");
			}
			bufferedWriter.write("\n");

			// Output C(ti,wi) and P(wi|ti)
			bufferedWriter.write("The word-tag count and probability is as follow:\n");
			List<String> wordTagPairSortedList = wordTagCountMap.keySet().stream().sorted(String.CASE_INSENSITIVE_ORDER).collect(Collectors.toList());
			for (int i = 0; i < wordTagPairSortedList.size(); i++) {
				bufferedWriter.write(wordTagPairSortedList.get(i) + ":\n");
				int wordTagCount = wordTagCountMap.get(wordTagPairSortedList.get(i));
				bufferedWriter.write("Count: " + wordTagCount + "\n");
				int tagCount = tagCountMap.get(wordTagPairSortedList.get(i).split("\\_")[1]);
				double wordTagProbability = (double)wordTagCount / (double)tagCount;
				bufferedWriter.write("Probability: " + wordTagProbability + "\n");
			}
			bufferedWriter.write("\n");
			
			//P(ti|ti-1)=C(ti-1,ti)/C(ti-1)
			// Output C(ti-1,ti) and P(ti|ti-1)
			bufferedWriter.write("The tag bigram count and probability is as follow:\n");
			List<String> tagBigramPairSortedList = tagBigramCountMap.keySet().stream().sorted(String.CASE_INSENSITIVE_ORDER).collect(Collectors.toList());
			for (int i = 0; i < tagBigramPairSortedList.size(); i++) {
				String[] tagBigramPair = tagBigramPairSortedList.get(i).split(" ");
				bufferedWriter.write(tagBigramPair[0] + "->" + tagBigramPair[1] + ":\n");
				int tagBigramCount = tagBigramCountMap.get(tagBigramPairSortedList.get(i)).intValue();
				bufferedWriter.write("Count: " + tagBigramCount + "\n");
				double tagBigramProbability = tagBigramProbabilityMap.get(tagBigramPairSortedList.get(i));
				bufferedWriter.write("Probability: " + tagBigramProbability + "\n");
			}
			bufferedWriter.write("\n");
			
			bufferedWriter.flush();
			bufferedWriter.close();	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
