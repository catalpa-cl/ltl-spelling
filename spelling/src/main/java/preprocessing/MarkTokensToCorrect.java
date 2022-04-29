package preprocessing;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import spelling.types.ExtendedSpellingAnomaly;
import spelling.types.KnownWord;
import spelling.types.TokenToConsider;

/**
 * Marks Tokens as SpellingAnomalies if they are a TokenToConsider and have not
 * been marked as a KnownWord.
 */

public class MarkTokensToCorrect extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {

		for (Token token : JCasUtil.select(aJCas, Token.class)) {

			if (!JCasUtil.selectCovered(TokenToConsider.class, token).isEmpty()
					&& JCasUtil.selectCovered(KnownWord.class, token).isEmpty()) {

				ExtendedSpellingAnomaly anomaly = new ExtendedSpellingAnomaly(aJCas);
				anomaly.setBegin(token.getBegin());
				anomaly.setEnd(token.getEnd());
				anomaly.setCorrected(false);
				anomaly.setMisspelledTokenText(token.getCoveredText());
				anomaly.addToIndexes();
			}
		}
	}
}
