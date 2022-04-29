package preprocessing;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import spelling.types.StartOfSentence;

/**
 * Annotator marking the first token after a line break as the beginning of a
 * sentence
 */

public class LineBreakAnnotator extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {

		String text = aJCas.getDocumentText();

		for (Token token : JCasUtil.select(aJCas, Token.class)) {

			if (JCasUtil.selectCovered(StartOfSentence.class, token).isEmpty()) {

				if (token.getBegin() > 0 && text.substring(token.getBegin() - 1, token.getBegin()).matches("\\n")) {

					StartOfSentence sos = new StartOfSentence(aJCas);
					sos.setBegin(token.getBegin());
					sos.setEnd(token.getEnd());
					sos.addToIndexes();
				}
			}
		}

		for (Sentence sentence : JCasUtil.select(aJCas, Sentence.class)) {
			if (sentence.getCoveredText().equals("")) {
				sentence.removeFromIndexes();
			}
		}
	}
}
