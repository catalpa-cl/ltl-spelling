package preprocessing;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.uimafit.util.JCasUtil;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import spelling.types.StartOfSentence;

/**
 * Marks first token of each sentence to enable treating sentence beginnings
 * differently.
 */

public class MarkSentenceBeginnings extends JCasAnnotator_ImplBase {

	/*
	 * Whether periods are to be interpreted as sentence boundaries Use if there are
	 * unreliable sentence annotations
	 */
	public static final String PARAM_INCLUDE_PERIOD = "includePeriod";
	@ConfigurationParameter(name = PARAM_INCLUDE_PERIOD, mandatory = true, defaultValue = "False")
	private boolean includePeriod;

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {

		// Mark first token of every sentence as a sentence beginning
		for (Sentence sentence : JCasUtil.select(aJCas, Sentence.class)) {

			Token first = JCasUtil.selectCovered(Token.class, sentence).get(0);
			StartOfSentence startOfSentence = new StartOfSentence(aJCas);
			startOfSentence.setBegin(first.getBegin());
			startOfSentence.setEnd(first.getEnd());
			startOfSentence.addToIndexes();
		}

		// Use heuristic to find sentence beginnings
		boolean previousTokenWasPunctuation = false;
		boolean foundClosingPunctuation = true;
		for (Token token : JCasUtil.select(aJCas, Token.class)) {
			if (token.getCoveredText().matches("[\"]")) {
				if (foundClosingPunctuation) {
					previousTokenWasPunctuation = true;
					foundClosingPunctuation = false;
				} else {
					foundClosingPunctuation = true;
				}
			} else if (previousTokenWasPunctuation) {

				if (JCasUtil.selectCovered(StartOfSentence.class, token).isEmpty()) {
					StartOfSentence startOfSentence = new StartOfSentence(aJCas);
					startOfSentence.setBegin(token.getBegin());
					startOfSentence.setEnd(token.getEnd());
					startOfSentence.addToIndexes();
					previousTokenWasPunctuation = false;
				}
			}
		}

		String regex = "";
		if (includePeriod) {
			regex = "[\\.?!:„]+";
		} else {
			regex = "[?!:„]+";
		}

		previousTokenWasPunctuation = false;
		for (Token token : JCasUtil.select(aJCas, Token.class)) {

			if (token.getCoveredText().matches(regex)) {
				previousTokenWasPunctuation = true;
			} else if (previousTokenWasPunctuation) {
				if (JCasUtil.selectCovered(StartOfSentence.class, token).isEmpty()) {
					StartOfSentence startOfSentence = new StartOfSentence(aJCas);
					startOfSentence.setBegin(token.getBegin());
					startOfSentence.setEnd(token.getEnd());
					startOfSentence.addToIndexes();
					previousTokenWasPunctuation = false;
				}
			}
		}
	}
}
