package evaluation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.uimafit.util.JCasUtil;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import constants.SpellingConstants;
import spelling.types.ExtendedSpellingAnomaly;

public class WriteLangToolSentences extends JCasAnnotator_ImplBase {

	public static final String PARAM_CONFIG_NAME = "configName";
	@ConfigurationParameter(name = PARAM_CONFIG_NAME, mandatory = true)
	protected String configName;

	List<String> sentences = new ArrayList<String>();
	int sentenceCounter = 0;

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {

		for (Sentence sentence : JCasUtil.select(aJCas, Sentence.class)) {

			sentenceCounter += 1;

			// If everything in this sentence is correct: just add as is
			if (JCasUtil.selectCovered(ExtendedSpellingAnomaly.class, sentence).size() == 0) {

				addToListOfCorrections(sentenceCounter, sentence, null, "");

			} else {

				for (ExtendedSpellingAnomaly anomaly : JCasUtil.selectCovered(ExtendedSpellingAnomaly.class,
						sentence)) {

//					System.out.println("adding..." + anomaly.getCoveredText());
					addToListOfCorrections(sentenceCounter, sentence, anomaly, "");

				}
			}
		}
	}

	private void addToListOfCorrections(int sentIndex, Sentence sentence, ExtendedSpellingAnomaly anomaly,
			String isCorrect) {

		if (anomaly != null) {

			List<String> sentenceTokens = new ArrayList<String>();
			List<Token> tokens = JCasUtil.selectCovered(Token.class, sentence);

			// to count offset of anomaly within sentence
			int sentenceIndex = 0;
			int begin_index = -1;
			int end_index = -1;

			for (Token token : tokens) {

				// Have to replace all that are spelling anomalies in oder to normalize back to
				// initial form
				if (JCasUtil.selectCovered(ExtendedSpellingAnomaly.class, token).size() != 0) {
					ExtendedSpellingAnomaly anomalyToReplace = JCasUtil
							.selectCovered(ExtendedSpellingAnomaly.class, token).iterator().next();
					sentenceTokens.add(anomalyToReplace.getMisspelledTokenText());
					// if this is the target anomaly
					if (anomalyToReplace.equals(anomaly)) {
						begin_index = sentenceIndex;
						end_index = sentenceIndex + anomalyToReplace.getMisspelledTokenText().length();
					}
					sentenceIndex += anomalyToReplace.getMisspelledTokenText().length() + 1;
				} else {
					sentenceTokens.add(token.getCoveredText());
					sentenceIndex += token.getCoveredText().length() + 1;
				}
			}

			sentences.add(sentIndex + "\t" + String.join(" ", sentenceTokens).replaceAll("\"", "\"\"\"\"") + "\t"
					+ anomaly.getMisspelledTokenText() + "\t" + begin_index + "\t" + end_index + "\t" + "" + "\t"
					+ anomaly.getGoldStandardCorrection() + "\t" + isCorrect);
		} else {

			List<String> sentenceTokens = new ArrayList<String>();
			List<Token> tokens = JCasUtil.selectCovered(Token.class, sentence);

			for (Token t : tokens) {
				sentenceTokens.add(t.getCoveredText());
			}

			sentences.add(sentIndex + "\t" + String.join(" ", sentenceTokens).replaceAll("\"", "\"\"\"\"") + "\t" + ""
					+ "\t" + "" + "\t" + "" + "\t" + "" + "\t" + "" + "\t" + isCorrect);
		}

	}

	@Override
	public void collectionProcessComplete() throws AnalysisEngineProcessException {

		try {
			String eval_dir = SpellingConstants.EVALUATION_DATA_PATH + "LanguageTool_Sentences/";
			File dir = new File(eval_dir);
			dir.mkdir();

			BufferedWriter bw = new BufferedWriter(new FileWriter(eval_dir + configName + ".tsv"));
			bw.write(
					"SentenceID\tSentence\tinitial_word\tstart_index\tend_index\tour_correction\tgold_correction\tours_is_correct\n");
			for (String element : sentences) {
				bw.write(element + "\n");
			}
			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
