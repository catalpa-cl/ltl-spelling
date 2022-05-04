package normalization;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import spelling.types.ExtendedSpellingAnomaly;
import spelling.types.ExtendedSpellingAnomaly_ForTSV;

/**
 * Annotator that marks all tokens that are made up of nothing but punctuation
 * as such.
 */

public class TransferSpellingAnomalies_ForTSV extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {

		for (ExtendedSpellingAnomaly anomaly : JCasUtil.select(aJCas, ExtendedSpellingAnomaly.class)) {

			String replacements = "";
			float best_cert = Float.MAX_VALUE;
			String best_cand = "";

			if (anomaly.getSuggestions() != null) {
				for (int i = 0; i < anomaly.getSuggestions().size(); i++) {
					float cert = anomaly.getSuggestions(i).getCertainty();
					String sugg = anomaly.getSuggestions(i).getReplacement();

					replacements += sugg + " (" + cert + "),";

					if (cert < best_cert) {
						best_cert = cert;
						best_cand = sugg;
					}
				}
			} else {
				best_cand = "*";
			}

			ExtendedSpellingAnomaly_ForTSV anomaly_tsv = new ExtendedSpellingAnomaly_ForTSV(aJCas);
			anomaly_tsv.setBegin(anomaly.getBegin());
			anomaly_tsv.setEnd(anomaly.getEnd());
			anomaly_tsv.setSuggestions(replacements);
			anomaly_tsv.setBest_candidate(best_cand);
			anomaly_tsv.addToIndexes();
		}
	}
}