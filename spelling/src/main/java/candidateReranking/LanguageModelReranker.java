package candidateReranking;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.dkpro.core.api.frequency.provider.FrequencyCountProvider;

import de.tudarmstadt.ukp.dkpro.core.api.anomaly.type.SpellingAnomaly;
import de.tudarmstadt.ukp.dkpro.core.api.anomaly.type.SuggestedAction;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

public class LanguageModelReranker extends JCasAnnotator_ImplBase {

	public static final String RES_LANGUAGE_MODEL = "languageModel";
	@ExternalResource(key = RES_LANGUAGE_MODEL)
	private FrequencyCountProvider fcp;

//	public static final String RES_LANGUAGE_MODEL_PROMPT_SPECIFIC = "promptSpecificLangugageModel";
//	@ExternalResource(key = RES_LANGUAGE_MODEL_PROMPT_SPECIFIC)
//	private FrequencyCountProvider fcp_specific;

	public static final String PARAM_NGRAM_SIZE = "ngramSize";
	@ConfigurationParameter(name = PARAM_NGRAM_SIZE, mandatory = true, defaultValue = "3")
	private int ngramSize;

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {

		// Build index to look up the sentence an anomaly is contained in
		Map<SpellingAnomaly, Collection<Sentence>> index = JCasUtil.indexCovering(aJCas, SpellingAnomaly.class,
				Sentence.class);

		// For every anomaly
		for (SpellingAnomaly anomaly : JCasUtil.select(aJCas, SpellingAnomaly.class)) {

			if (anomaly.getSuggestions() != null) {
				Collection<Sentence> sentences = index.get(anomaly);

				if (sentences.size() != 1) {
					System.err.println("SpellingAnomaly " + anomaly.getCoveredText()
							+ " is not covered by exactly one sentence in the text " + aJCas.getDocumentText());
					for (Sentence sentence : sentences) {
						System.out.println(sentence.getCoveredText());
					}

					for (int i = 0; i < anomaly.getSuggestions().size(); i++) {
						anomaly.getSuggestions(i).setCertainty(Float.MAX_VALUE);
					}
//					System.exit(0);

					// Set certainty according to language model probability
				} else {
					for (Sentence sentence : sentences) {
						List<Token> tokens = JCasUtil.selectCovered(aJCas, Token.class, sentence);
						processAnomaly(sentence, anomaly, tokens, ngramSize);
					}
				}
			}
		}
	}

	private void processAnomaly(Sentence sentence, SpellingAnomaly anomaly, List<Token> tokens, int ngramSize_rec) {

		FSArray suggestions = anomaly.getSuggestions();
		Map<SuggestedAction, Float> LMProbabilities = new HashMap<SuggestedAction, Float>();
		double maxProbability = 0;

		// Determine index of anomaly token within sentence
		// Include n-1 tokens before and after that to determine probability
		int anomalyIndex = -1;
		for (int i = 0; i < tokens.size(); i++) {
			Token t = tokens.get(i);
			if (t.getBegin() == anomaly.getBegin() && t.getEnd() == anomaly.getEnd()) {
				anomalyIndex = i;
				break;
			}
		}

		int minIndex = -1;
		int maxIndex = -1;

		// Should never happen
		if (anomalyIndex == -1) {
			System.out.println(
					"Could not find anomaly " + anomaly.getCoveredText() + " in sentence " + sentence.getCoveredText());

			for (int i = 0; i < anomaly.getSuggestions().size(); i++) {
				anomaly.getSuggestions(i).setCertainty(Float.MAX_VALUE);
			}

		} else {
			minIndex = anomalyIndex - ngramSize_rec + 1;

			// Can at most be beginning of the sentence, but not negative
			if (minIndex < 0) {
				minIndex = 0;
			}

			maxIndex = anomalyIndex + ngramSize_rec - 1;
			// Prevent index from being out of sentence range
			if (maxIndex >= tokens.size()) {
				maxIndex = tokens.size() - 1;
			}

			// For each of the suggested corrections
			for (int k = 0; k < suggestions.size(); k++) {

				String candidate = anomaly.getSuggestions(k).getReplacement();

				// Build array with tokens for which to compute ngram probability
				String[] ngramTokens = new String[maxIndex - minIndex + StringUtils.countMatches(candidate, " ") + 1];
				int j = 0;
				for (int i = minIndex; i < anomalyIndex; i++) {
					ngramTokens[j] = tokens.get(i).getCoveredText();
					j++;
				}
				for (String part : candidate.split(" ")) {
					ngramTokens[j] = part;
					j++;
				}
				if (anomalyIndex < maxIndex) {
					for (int i = anomalyIndex + 1; i <= maxIndex; i++) {
						ngramTokens[j] = tokens.get(i).getCoveredText();
						j++;
					}
				}

				double probability = getLMprobability(fcp, ngramTokens, ngramSize_rec);

				LMProbabilities.put(anomaly.getSuggestions(k), (float) probability);

				// Check whether newly calculated ngram probability is highest for this
				// candidate
				if (probability > maxProbability) {
					maxProbability = probability;
				}
			}
		}

		if (LMProbabilities.keySet().size() > 0) {

			// Only replace the certainty of suggestions if their LM probability differs
			if (!(Collections.frequency(LMProbabilities.values(),
					LMProbabilities.values().iterator().next()) == LMProbabilities.values().size())) {

				// Certainty used inversely: max prob/prob means lower values are better
				for (SuggestedAction suggestedAction : LMProbabilities.keySet()) {

//					System.out.println("Final probability of " + suggestedAction.getReplacement() + ": "
//							+ String.format("%.12f", LMProbabilities.get(suggestedAction)) + " (normalized: "
//							+ String.format("%.12f", (float) maxProbability / LMProbabilities.get(suggestedAction)) + ")");
//						System.out.println("Probability of\t" + suggestedAction.getReplacement() + "\t"
//								+ String.format("%.12f", LMProbabilities.get(suggestedAction)) + "\t"
//								+ String.format("%.12f", (float) maxProbability / LMProbabilities.get(suggestedAction)));

					if (LMProbabilities.get(suggestedAction) > 0) {
						suggestedAction.setCertainty((float) maxProbability / LMProbabilities.get(suggestedAction));
					} else {
						suggestedAction.setCertainty(Float.MAX_VALUE);
					}
				}

			} else {
				System.out.println("Did not replace certainties for " + anomaly.getCoveredText()
						+ " because all had same value:" + LMProbabilities.values().iterator().next());
				if (ngramSize_rec > 1) {
					System.out.println("Backing off to ngram size " + (ngramSize_rec - 1));
					processAnomaly(sentence, anomaly, tokens, ngramSize_rec - 1);
				}

			}
		}
	}

	private double getLMprobability(FrequencyCountProvider fcp, String[] tokens, int ngramSize) {

		double probability = 1.0;

		if (tokens.length < ngramSize) {
			ngramSize = tokens.length;
		}

		for (int i = 0; i <= tokens.length - ngramSize; i++) {

			String[] ngramTokens = Arrays.copyOfRange(tokens, i, i + ngramSize);
			String ngram = String.join(" ", ngramTokens);

			try {
				double ngramProbability = fcp.getProbability(ngram);
				if (ngramProbability > 0) {
					probability = probability * ngramProbability;
				} else {
					probability = probability * (1 / fcp.getNrOfNgrams(ngramSize));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return probability;
	}
}