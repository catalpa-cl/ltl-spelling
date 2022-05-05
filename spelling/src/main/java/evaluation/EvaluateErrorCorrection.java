package evaluation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.dkpro.core.api.frequency.util.FrequencyDistribution;
import org.uimafit.util.JCasUtil;

import constants.SpellingConstants;
import spelling.types.ExtendedSpellingAnomaly;
import spelling.types.StartOfSentence;

public class EvaluateErrorCorrection extends JCasAnnotator_ImplBase {

	public static final String PARAM_CONFIG_NAME = "configName";
	@ConfigurationParameter(name = PARAM_CONFIG_NAME, mandatory = true)
	protected String configName;

	FrequencyDistribution<String> correct = new FrequencyDistribution<String>();
	FrequencyDistribution<String> incorrect = new FrequencyDistribution<String>();

	List<String> numberedAndWeightedCorrect_forOracle = new ArrayList<String>();

	int numberOfAnomalies = 0;
	int numberOfMatchingCorrections = 0;
	double rAt1;
	double rAt2;
	double rAt3;
	double rAt5;
	double rAt10;

	List<String> corrections = new ArrayList<String>();

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {

		for (ExtendedSpellingAnomaly anomaly : JCasUtil.select(aJCas, ExtendedSpellingAnomaly.class)) {

			boolean isBeginningOfSentence = !JCasUtil.selectCovered(StartOfSentence.class, anomaly).isEmpty();

			numberOfAnomalies++;

			if (anomaly.getCoveredText().equals(anomaly.getGoldStandardCorrection())) {

				numberOfMatchingCorrections++;
				correct.inc(anomaly.getMisspelledTokenText() + "\t(" + anomaly.getCoveredText() + ")");

				rAt1++;
				rAt2++;
				rAt3++;
				rAt5++;
				rAt10++;
//				System.out.println("Correct: " + anomaly.getGoldStandardCorrection());

				numberedAndWeightedCorrect_forOracle.add(numberOfAnomalies + "\t1.0\t"
						+ anomaly.getMisspelledTokenText() + "\t" + anomaly.getCoveredText());

			} else {

				incorrect.inc(anomaly.getMisspelledTokenText() + "\t(" + anomaly.getCoveredText()
						+ "), should have been: " + anomaly.getGoldStandardCorrection());
//				System.out.println("Incorrect: " + anomaly.getGoldStandardCorrection());

				// R@3, 5, 10
				// Ensure that there are no duplicates in ranked candidates
				String targetCorrection = anomaly.getGoldStandardCorrection();
				Map<Float, Set<String>> suggestions = new HashMap<Float, Set<String>>();

				if (anomaly.getSuggestions() != null) {

					for (int i = 0; i < anomaly.getSuggestions().size(); i++) {
						float certainty = anomaly.getSuggestions(i).getCertainty();
						String candidate = anomaly.getSuggestions(i).getCoveredText();
						boolean alreadyPresent = false;

						for (Set<String> candidateSet : suggestions.values()) {

							if (candidateSet.contains(candidate)) {
								alreadyPresent = true;
							}
						}
						if (!alreadyPresent) {

							Set<String> suggestionRank = suggestions.get(certainty);

							if (suggestionRank == null) {

								suggestions.put(certainty, new HashSet<String>());
								suggestionRank = suggestions.get(certainty);
							}
							suggestionRank.add(anomaly.getSuggestions(i).getReplacement());
						} else {
							System.out.println("Was already present: " + candidate);
							System.exit(0);
						}
					}
				}

				List<Float> rankList = new ArrayList<Float>();
				rankList.addAll(suggestions.keySet());
				rankList.sort(null);

				Set<String> top1Candidates = new HashSet<String>();
				Set<String> top2Candidates = new HashSet<String>();
				Set<String> top3Candidates = new HashSet<String>();
				Set<String> top5Candidates = new HashSet<String>();
				Set<String> top10Candidates = new HashSet<String>();

				int currentIndex = 0;
				while (top1Candidates.size() < 1) {
					if (currentIndex < rankList.size()) {
						top1Candidates.addAll(suggestions.get(rankList.get(currentIndex)));
						currentIndex++;
					} else {
						break;
					}
				}

				// In case of too many candidates with equal rank, use probability
				if (top1Candidates.size() > 1) {

					// These candidates are all equivalent
					Set<String> equivalentCandidates = suggestions.get(rankList.get(currentIndex - 1));
					top1Candidates.clear();

					for (int i = 0; i < currentIndex - 1; i++) {
						top1Candidates.addAll(suggestions.get(rankList.get(i)));
					}

					if (top1Candidates.contains(targetCorrection)
							|| (isBeginningOfSentence && top1Candidates.contains(targetCorrection.toLowerCase()))) {

						rAt1++;
						numberedAndWeightedCorrect_forOracle.add(numberOfAnomalies + "\t1.0\t"
								+ anomaly.getMisspelledTokenText() + "\t" + anomaly.getCoveredText());
					} else if (equivalentCandidates.contains(targetCorrection) || (isBeginningOfSentence
							&& equivalentCandidates.contains(targetCorrection.toLowerCase()))) {

						rAt1 += 1.0 / (equivalentCandidates.size() * 1.0);
						double weight = 1.0 / (equivalentCandidates.size() * 1.0);
						numberedAndWeightedCorrect_forOracle.add(numberOfAnomalies + "\t" + weight + "\t"
								+ anomaly.getMisspelledTokenText() + "\t" + anomaly.getCoveredText());
					}
				} else {
					if (top1Candidates.contains(targetCorrection)
							|| (isBeginningOfSentence && top1Candidates.contains(targetCorrection.toLowerCase()))) {

						rAt1++;
						numberedAndWeightedCorrect_forOracle.add(numberOfAnomalies + "\t1.0\t"
								+ anomaly.getMisspelledTokenText() + "\t" + anomaly.getCoveredText());
					}
				}

				currentIndex = 0;
				while (top2Candidates.size() < 2) {
					if (currentIndex < rankList.size()) {

						top2Candidates.addAll(suggestions.get(rankList.get(currentIndex)));
						currentIndex++;
					} else {
						break;
					}
				}

				// In case of too many candidates with equal rank, use probability
				if (top2Candidates.size() > 2) {

					// These candidates are all equivalent
					Set<String> equivalentCandidates = suggestions.get(rankList.get(currentIndex - 1));
					top2Candidates.clear();

					for (int i = 0; i < currentIndex - 1; i++) {
						top2Candidates.addAll(suggestions.get(rankList.get(i)));
					}

					if (top2Candidates.contains(targetCorrection)
							|| (isBeginningOfSentence && top2Candidates.contains(targetCorrection.toLowerCase()))) {

						rAt2++;
					} else if (equivalentCandidates.contains(targetCorrection) || (isBeginningOfSentence
							&& equivalentCandidates.contains(targetCorrection.toLowerCase()))) {

						rAt2 += 1.0 / (equivalentCandidates.size() * 1.0);
					}
				} else {
					if (top2Candidates.contains(targetCorrection)
							|| (isBeginningOfSentence && top2Candidates.contains(targetCorrection.toLowerCase()))) {

						rAt2++;
					}
				}

				currentIndex = 0;
				while (top3Candidates.size() < 3) {
					if (currentIndex < rankList.size()) {

						top3Candidates.addAll(suggestions.get(rankList.get(currentIndex)));
						currentIndex++;
					} else {
						break;
					}
				}

				// In case of too many candidates with equal rank, use probability
				if (top3Candidates.size() > 3) {

					// These candidates are all equivalent
					Set<String> equivalentCandidates = suggestions.get(rankList.get(currentIndex - 1));
					top3Candidates.clear();

					for (int i = 0; i < currentIndex - 1; i++) {
						top3Candidates.addAll(suggestions.get(rankList.get(i)));
					}

					if (top3Candidates.contains(targetCorrection)
							|| (isBeginningOfSentence && top3Candidates.contains(targetCorrection.toLowerCase()))) {

						rAt3++;
//						System.out.println("3: above");
					} else if (equivalentCandidates.contains(targetCorrection) || (isBeginningOfSentence
							&& equivalentCandidates.contains(targetCorrection.toLowerCase()))) {

						rAt3 += 1.0 / (equivalentCandidates.size() * 1.0);
					}
				} else {
					if (top3Candidates.contains(targetCorrection)
							|| (isBeginningOfSentence && top3Candidates.contains(targetCorrection.toLowerCase()))) {

						rAt3++;
					}
				}

				currentIndex = 0;
				while (top5Candidates.size() < 5) {
					if (currentIndex < rankList.size()) {

						top5Candidates.addAll(suggestions.get(rankList.get(currentIndex)));
						currentIndex++;
					} else {
						break;
					}

				}

				// In case of too many candidates with equal rank, use probability
				if (top5Candidates.size() > 5) {

					// These candidates are all equivalent
					Set<String> equivalentCandidates = suggestions.get(rankList.get(currentIndex - 1));
					top5Candidates.clear();

					for (int i = 0; i < currentIndex - 1; i++) {
						top5Candidates.addAll(suggestions.get(rankList.get(i)));
					}

					if (top5Candidates.contains(targetCorrection)
							|| (isBeginningOfSentence && top5Candidates.contains(targetCorrection.toLowerCase()))) {

						rAt5++;
					} else if (equivalentCandidates.contains(targetCorrection) || (isBeginningOfSentence
							&& equivalentCandidates.contains(targetCorrection.toLowerCase()))) {

						rAt5 += 1.0 / (equivalentCandidates.size() * 1.0);
					}
				} else {
					if (top5Candidates.contains(targetCorrection)
							|| (isBeginningOfSentence && top5Candidates.contains(targetCorrection.toLowerCase()))) {

						rAt5++;
					}
				}

				currentIndex = 0;
				while (top10Candidates.size() < 10) {
					if (currentIndex < rankList.size()) {

						top10Candidates.addAll(suggestions.get(rankList.get(currentIndex)));
						currentIndex++;
					} else {
						break;
					}
				}

				// In case of too many candidates with equal rank, use probability
				if (top10Candidates.size() > 10) {

					// These candidates are all equivalent
					Set<String> equivalentCandidates = suggestions.get(rankList.get(currentIndex - 1));
					top10Candidates.clear();

					for (int i = 0; i < currentIndex - 1; i++) {

						top10Candidates.addAll(suggestions.get(rankList.get(i)));
					}

					if (top10Candidates.contains(targetCorrection)
							|| (isBeginningOfSentence && top10Candidates.contains(targetCorrection.toLowerCase()))) {

						rAt10++;
					} else if (equivalentCandidates.contains(targetCorrection) || (isBeginningOfSentence
							&& equivalentCandidates.contains(targetCorrection.toLowerCase()))) {

						rAt10 += (1.0 / (equivalentCandidates.size() * 1.0));
					}
				} else {

					if (top10Candidates.contains(targetCorrection)
							|| (isBeginningOfSentence && top10Candidates.contains(targetCorrection.toLowerCase()))) {

						rAt10++;
					}
				}
			}
		}
	}

	@Override
	public void collectionProcessComplete() throws AnalysisEngineProcessException {

		try {

			String eval_dir_correction = SpellingConstants.EVALUATION_DATA_PATH + "Error_Correction";
			File dir = new File(eval_dir_correction);
			dir.mkdir();

			String eval_dir = SpellingConstants.EVALUATION_DATA_PATH + "ErrorCorrection_" + configName;
			dir = new File(eval_dir);
			dir.mkdir();

			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(eval_dir + "/evaluation.txt")));
			System.out.println("Correct: " + numberOfMatchingCorrections + " Incorrect: "
					+ (numberOfAnomalies - numberOfMatchingCorrections));
			System.out.println("Accuracy:\t" + (numberOfMatchingCorrections * 1.0) / (numberOfAnomalies * 1.0));
			System.out.println("R@1:\t" + (rAt1 * 1.0) / (numberOfAnomalies * 1.0));
			System.out.println("R@2:\t" + (rAt2 * 1.0) / (numberOfAnomalies * 1.0));
			System.out.println("R@3:\t" + (rAt3 * 1.0) / (numberOfAnomalies * 1.0));
			System.out.println("R@5:\t" + (rAt5 * 1.0) / (numberOfAnomalies * 1.0));
			System.out.println("R@10:\t" + (rAt10 * 1.0) / (numberOfAnomalies * 1.0));

			bw.write("Correct: " + numberOfMatchingCorrections + " Incorrect: "
					+ (numberOfAnomalies - numberOfMatchingCorrections));
			bw.newLine();
			bw.write("Accuracy:\t" + (numberOfMatchingCorrections * 1.0) / (numberOfAnomalies * 1.0));
			bw.newLine();
			bw.write("R@1:\t" + (rAt1 * 1.0) / (numberOfAnomalies * 1.0));
			bw.newLine();
			bw.write("R@2:\t" + (rAt2 * 1.0) / (numberOfAnomalies * 1.0));
			bw.newLine();
			bw.write("R@3:\t" + (rAt3 * 1.0) / (numberOfAnomalies * 1.0));
			bw.newLine();
			bw.write("R@5:\t" + (rAt5 * 1.0) / (numberOfAnomalies * 1.0));
			bw.newLine();
			bw.write("R@10:\t" + (rAt10 * 1.0) / (numberOfAnomalies * 1.0));
			bw.newLine();
			bw.close();

			bw = new BufferedWriter(new FileWriter(new File(eval_dir + "/correct.txt")));
			List<String> correctList = new ArrayList<String>();
			correctList.addAll(correct.getKeys());
			correctList.sort(null);
			for (String c : correctList) {
				bw.write(c + "\t(" + correct.getCount(c) + " times)");
				bw.newLine();
			}
			bw.close();

			bw = new BufferedWriter(new FileWriter(new File(eval_dir + "/incorrect.txt")));
			List<String> incorrectList = new ArrayList<String>();
			incorrectList.addAll(incorrect.getKeys());
			incorrectList.sort(null);
			for (String i : incorrectList) {
				bw.write(i + "\t(" + incorrect.getCount(i) + " times)");
				bw.newLine();
			}
			bw.close();

			bw = new BufferedWriter(new FileWriter(new File(eval_dir + "/correct_numbered.txt")));
			for (String i : numberedAndWeightedCorrect_forOracle) {
				bw.write(i);
				bw.newLine();
			}
			bw.close();

//			bw = new BufferedWriter(new FileWriter(eval_dir + "/wrongSentences.txt"));
//			bw.write(
//					"Sentence\tinitial_word\tstart_index\tend_index\tour_correction\tgold_correction\tours_is_correct\n");
//			for (String element : corrections) {
//				bw.write(element + "\n");
//			}
//			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
