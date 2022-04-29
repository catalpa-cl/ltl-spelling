package preprocessing;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import spelling.types.Numeric;

/**
 * Annotator marking all tokens that consist of numbers.
 */

public class NumericAnnotator extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {

		for (Token token : JCasUtil.select(aJCas, Token.class)) {
			if (token.getCoveredText().matches("[:0-9-\\+\\*\\.,=x/\\\\]*[0-9]+[:0-9-\\+\\*\\.,=x/\\\\]*")) {
				Numeric numeric = new Numeric(aJCas);
				numeric.setBegin(token.getBegin());
				numeric.setEnd(token.getEnd());
				numeric.addToIndexes();
			}
		}
	}
}
