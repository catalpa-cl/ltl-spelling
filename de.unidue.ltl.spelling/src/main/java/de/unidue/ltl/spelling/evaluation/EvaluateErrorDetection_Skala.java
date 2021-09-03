package de.unidue.ltl.spelling.evaluation;

import de.unidue.ltl.spelling.constants.SpellingConstants;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.dkpro.core.api.frequency.util.FrequencyDistribution;
import org.uimafit.util.JCasUtil;

import de.tudarmstadt.ukp.dkpro.core.api.anomaly.type.SpellingAnomaly;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.unidue.ltl.spelling.types.GrammarError;
import de.unidue.ltl.spelling.types.Punctuation;
import de.unidue.ltl.spelling.types.SpellingError;
import de.unidue.ltl.spelling.types.SpellingText;

public class EvaluateErrorDetection_Skala extends JCasAnnotator_ImplBase {

	public static final String PARAM_CONFIG_NAME = "configName";
	@ConfigurationParameter(name = PARAM_CONFIG_NAME, mandatory = true)
	protected String configName;

//	Map<Sentence, String> anomalySentencesToIDS = new HashMap<Sentence, String>();
//	Map<Sentence, SpellingAnomaly> anomalySentencesToAnomalies = new HashMap<Sentence, SpellingAnomaly>();
	FrequencyDistribution<String> idCount = new FrequencyDistribution<String>();
	FrequencyDistribution<String> fd = new FrequencyDistribution<String>();
	Set<String> skipped = new HashSet<String>();
	Set<String> candidateSentences = new HashSet<String>();

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {

		Map<SpellingAnomaly, Collection<Sentence>> sentenceLookup = JCasUtil.indexCovering(aJCas, SpellingAnomaly.class,
				Sentence.class);

		for (SpellingAnomaly anomaly : JCasUtil.select(aJCas, SpellingAnomaly.class)) {

			String word = anomaly.getCoveredText();

			if (word.matches(".*[0-9]+.*") || word.contains("<") || word.contains(">") || word.contains("//")
					|| word.matches("[–…{}»«•]")) {

				System.out.println("SKIP: " + word);
				skipped.add(word);

			} else {
				fd.inc(anomaly.getCoveredText());
				if (sentenceLookup.get(anomaly).size() != 1) {
					System.out.println("anomaly covered by more than one sentence");
					System.exit(0);
				}
				String id = JCasUtil.select(aJCas, SpellingText.class).iterator().next().getId();
				idCount.inc(id);
				String sentID = id + "_" + idCount.getCount(id);
				Sentence sentence = sentenceLookup.get(anomaly).iterator().next();
				String anomalyText = anomaly.getCoveredText();
				String sentenceText = sentence.getCoveredText();
				
				int sentBegin = sentence.getBegin();
				int begin = anomaly.getBegin() - sentBegin;
				int end = anomaly.getEnd() - sentBegin;
				
				System.out.println("sentID "+ sentID);
				System.out.println("anomaly "+anomalyText);
				System.out.println("begin-end "+begin+" "+end);
				System.out.println("sentence "+sentenceText);
				
				String candidate = "<text id=\"" + sentID + "\" lang=\"de\">";
				if (begin > 0) {
					candidate = candidate + sentenceText.substring(0, begin);
				}
				candidate = candidate + "<error correct=\"\" type=\"\">" +anomalyText +"</error>"+sentenceText.substring(end)+"</text>";
				candidateSentences.add(candidate);
			}
		}
	}

	@Override
	public void collectionProcessComplete() throws AnalysisEngineProcessException {

		try {
			String eval_dir = SpellingConstants.EVALUATION_DATA_PATH + "ErrorDetection_" + configName;
			File dir = new File(eval_dir);
			dir.mkdir();

			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(eval_dir + "/detectedErrors.txt")));

			for (String word : fd.getMostFrequentSamples((int) fd.getB())) {
				System.out.println(word + fd.getCount(word));
				bw.write(word + "\t" + fd.getCount(word));
				bw.newLine();
			}

			bw.close();

			bw = new BufferedWriter(new FileWriter(new File(eval_dir + "/skipped.txt")));

			for (String word : skipped) {
				bw.write(word);
				bw.newLine();
			}

			bw.close();

			bw = new BufferedWriter(new FileWriter(new File(eval_dir + "/chosenSkalaSentences.xml")));

			for (int i = 0; i < 600; i++) {

				String chosen = candidateSentences.stream()
						.skip(new Random().nextInt(candidateSentences.size())).findFirst().get();
				bw.write(chosen);
				bw.newLine();

				candidateSentences.remove(chosen);
			}

			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}