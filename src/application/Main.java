/**
 * 
 */
package application;

import corpus.Corpus;
import corpus.POSTaggedCorpus;
import problem2.BigramProbabilities;
import problem3.BigramNaiveBayesianClassificationBasedPOSTagging;
import problem3.POSTagging;
import problem3.TransformationBasedPOSTagging;

/**
 * @author yzc
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Problem2:");
		runProblem2();
		System.out.println();
		System.out.println();
		System.out.println("Problem3:");
		runProblem3();
	}

	private static void runProblem2() {
		Corpus corpus = new Corpus("HW2_F18_NLP6320-NLPCorpusTreebank2Parts-CorpusA-Windows.txt");
		BigramProbabilities.outputBigramModelForAllSmoothingWays(corpus, "Problem 2 Bigram Probabilities.txt");
		
		String inputSentence = "The Fed chairman warned that the board 's decision is bad";
		BigramProbabilities.computeTotalProbabilityForAllSmoothingWays(inputSentence, corpus);
	}

	private static void runProblem3() {
		POSTaggedCorpus corpus = new POSTaggedCorpus("HW2_F18_NLP6320_POSTaggedTrainingSet-Windows.txt");
		String inputSentence = "The_DT standard_?? Turbo_NN engine_NN is_VBZ hard_JJ to_TO work_??";

		// Transformation Based POS Tagging
		POSTagging transformationBasedPOSTagging = new TransformationBasedPOSTagging(corpus);

		transformationBasedPOSTagging.outputModel("Problem 3 Question a Transformation Rules.txt");

		System.out.println("Transformation Based POS Tagging Result:");
		transformationBasedPOSTagging.fillOutMissingPOSTags(inputSentence);
		System.out.println();
		System.out.println();

		// Bigram Naive Bayesian Classification Based POS Tagging
		POSTagging bigramNaiveBayesianClassificationBasedPOSTagging= new BigramNaiveBayesianClassificationBasedPOSTagging(corpus);

		bigramNaiveBayesianClassificationBasedPOSTagging.outputModel("Problem 3 Question b Naive Bayesian Classification Based Bigram Models.txt");

		System.out.println("Transformation Based POS Tagging Result:");
		bigramNaiveBayesianClassificationBasedPOSTagging.fillOutMissingPOSTags(inputSentence);
	}
}
