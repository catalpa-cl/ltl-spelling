package preprocessing;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import spelling.types.Punctuation;

/**
 * Annotator that marks all tokens that are made up of nothing but punctuation
 * as such.
 */

public class PunctuationAnnotator extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {

		for (Token token : JCasUtil.select(aJCas, Token.class)) {

			if (token.getCoveredText().matches("^[&\\[\\];:!?\\.,=\\*\"„“_'\\-´`'()<>\\+/\\\\]+$")) {

				Punctuation punct = new Punctuation(aJCas);
				punct.setBegin(token.getBegin());
				punct.setEnd(token.getEnd());
				punct.addToIndexes();
			}
		}
	}
}