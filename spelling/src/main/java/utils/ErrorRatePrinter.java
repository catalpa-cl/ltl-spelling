package utils;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import spelling.types.Punctuation;
import spelling.types.SpellingError;

// To print basic statistics about a dataset
public class ErrorRatePrinter extends JCasAnnotator_ImplBase {

	int numSentences = 0;
	int numTokensTotal = 0;
	int numTokensWithoutPunctuation = 0;
	int numErrorsTotal = 0;
	// To count an error consisting of multiple parts as one instead of multiple
	// errors
	int numErrorsWithoutMergeMiddleRight = 0;

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {

		numSentences += JCasUtil.select(aJCas, Sentence.class).size();
		numTokensTotal += JCasUtil.select(aJCas, Token.class).size();
		numTokensWithoutPunctuation += JCasUtil.select(aJCas, Token.class).size();
		numTokensWithoutPunctuation -= JCasUtil.select(aJCas, Punctuation.class).size();

		for (SpellingError error : JCasUtil.select(aJCas, SpellingError.class)) {
			numErrorsTotal++;
			numErrorsWithoutMergeMiddleRight++;
			if (error.getErrorType().contains("merge_middle") || error.getErrorType().contains("merge_right")) {
				numErrorsWithoutMergeMiddleRight--;
			}
		}
	}

	@Override
	public void collectionProcessComplete() throws AnalysisEngineProcessException {

		System.out.println("Total number of sentences: " + numSentences);
		System.out.println("Number of tokens (not counting punctuation):\t" + numTokensWithoutPunctuation + " ("
				+ numTokensTotal + " including punctuation)");
		System.out.println("Number or errors (not counting merge middle and merge right):\t"
				+ numErrorsWithoutMergeMiddleRight + " (" + numErrorsTotal + " counting merge middle and merge right)");
		System.out.println("Error rate (tokens w/o punctuation, but with merge middle and merge right):\t"
				+ (numErrorsTotal * 1.0) / (numTokensWithoutPunctuation * 1.0));
	}
}