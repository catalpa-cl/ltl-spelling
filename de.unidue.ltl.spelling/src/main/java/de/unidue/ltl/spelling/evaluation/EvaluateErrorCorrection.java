package de.unidue.ltl.spelling.evaluation;

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
import java.util.Set;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.dkpro.core.api.frequency.util.FrequencyDistribution;
import org.uimafit.util.JCasUtil;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.unidue.ltl.spelling.constants.SpellingConstants;
import de.unidue.ltl.spelling.types.ExtendedSpellingAnomaly;
import de.unidue.ltl.spelling.types.StartOfSentence;

public class EvaluateErrorCorrection extends JCasAnnotator_ImplBase {

	public static final String PARAM_CONFIG_NAME = "configName";
	@ConfigurationParameter(name = PARAM_CONFIG_NAME, mandatory = true)
	protected String configName;

	FrequencyDistribution<String> correct = new FrequencyDistribution<String>();
	FrequencyDistribution<String> incorrect = new FrequencyDistribution<String>();

	List<String> numberedCorrect = new ArrayList<String>();

	int numberOfAnomalies = 0;
	int numberOfMatchingCorrections = 0;
	double rAt1;
	double rAt2;
	double rAt3;
	double rAt5;
	double rAt10;
	int lessThan1in1;
	int lessThan2in2;
	int lessThan3in3;
	int lessThan5in5;
	int lessThan10in10;
	int moreThan1in1;
	int moreThan2in2;
	int moreThan3in3;
	int moreThan5in5;
	int moreThan10in10;
	
	List<String> corrections = new ArrayList<String>();

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		
		//For list of wrong corrections
		Map<ExtendedSpellingAnomaly, Collection<Sentence>> sentenceLookup = JCasUtil.indexCovering(aJCas, ExtendedSpellingAnomaly.class, Sentence.class);

		for (ExtendedSpellingAnomaly anomaly : JCasUtil.select(aJCas, ExtendedSpellingAnomaly.class)) {

			boolean isBeginningOfSentence = !JCasUtil.selectCovered(StartOfSentence.class, anomaly).isEmpty();
			if (isBeginningOfSentence) {
				System.out.println("Is beginning of sentence");
			}

			numberOfAnomalies++;
			System.out.println("Number of anomalies "+numberOfAnomalies);
			
			if (anomaly.getCoveredText().equals(anomaly.getGoldStandardCorrection())) {
				numberOfMatchingCorrections++;
				correct.inc(anomaly.getMisspelledTokenText() + "\t(" + anomaly.getCoveredText() + ")");

				rAt1++;
				rAt2++;
				rAt3++;
				rAt5++;
				rAt10++;
				System.out.println("Correct: " + anomaly.getGoldStandardCorrection());

				numberedCorrect.add(
						numberOfAnomalies + "\t" + anomaly.getMisspelledTokenText() + "\t" + anomaly.getCoveredText());
				
//				addToListOfCorrections(sentenceLookup.get(anomaly).iterator().next(),anomaly,"y");
				
			} else {
				
//				addToListOfCorrections(sentenceLookup.get(anomaly).iterator().next(),anomaly,"n");
				
				incorrect.inc(anomaly.getMisspelledTokenText() + "\t(" + anomaly.getCoveredText()
						+ "), should have been: " + anomaly.getGoldStandardCorrection());
				System.out.println("Incorrect: " + anomaly.getGoldStandardCorrection());

				// R@3, 5, 10, accuracy is same as R@1
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

//				System.out.println("Rank list: " + rankList);
				
				for(Float key : rankList) {
					for(String entry : suggestions.get(key)) {
						System.out.println(key+"\t"+entry);
					}
				}

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
					
					for(int i=0; i<currentIndex-1; i++) {
						top1Candidates.addAll(suggestions.get(rankList.get(i)));
					}
					
					System.out.println("1 without equiv");
					for(String cand: top1Candidates) {
						System.out.println(cand);
					}
					System.out.println("end");

					if (top1Candidates.contains(targetCorrection)
							|| (isBeginningOfSentence && top1Candidates.contains(targetCorrection.toLowerCase()))) {
						rAt1++;
						System.out.println("1: above");
					} else if (equivalentCandidates.contains(targetCorrection) || (isBeginningOfSentence
							&& equivalentCandidates.contains(targetCorrection.toLowerCase()))) {
						rAt1 += 1.0 / (equivalentCandidates.size() * 1.0);
						System.out.println("1: within: "+equivalentCandidates.size());
						for(String entry : equivalentCandidates) {
							System.out.println(entry);
						}
					}
				} else {
					if (top1Candidates.contains(targetCorrection)
							|| (isBeginningOfSentence && top1Candidates.contains(targetCorrection.toLowerCase()))) {
						rAt1++;
						System.out.println("1: contained");
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
					
					for(int i=0; i<currentIndex-1; i++) {
						top2Candidates.addAll(suggestions.get(rankList.get(i)));
					}
					
					System.out.println("2 without equiv");
					for(String cand: top2Candidates) {
						System.out.println(cand);
					}
					System.out.println("end");

					if (top2Candidates.contains(targetCorrection)
							|| (isBeginningOfSentence && top2Candidates.contains(targetCorrection.toLowerCase()))) {
						rAt2++;
						System.out.println("2: above");
					} else if (equivalentCandidates.contains(targetCorrection) || (isBeginningOfSentence
							&& equivalentCandidates.contains(targetCorrection.toLowerCase()))) {
						rAt2 += 1.0 / (equivalentCandidates.size() * 1.0);
						System.out.println("2: within: "+equivalentCandidates.size());
						for(String entry : equivalentCandidates) {
							System.out.println(entry);
						}
					}
				} else {
					if (top2Candidates.contains(targetCorrection)
							|| (isBeginningOfSentence && top2Candidates.contains(targetCorrection.toLowerCase()))) {
						rAt2++;
						System.out.println("2: contained");
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
					
					for(int i=0; i<currentIndex-1; i++) {
						top3Candidates.addAll(suggestions.get(rankList.get(i)));
					}
					
					System.out.println("3 without equiv");
					for(String cand: top3Candidates) {
						System.out.println(cand);
					}
					System.out.println("end");

					if (top3Candidates.contains(targetCorrection)
							|| (isBeginningOfSentence && top3Candidates.contains(targetCorrection.toLowerCase()))) {
						rAt3++;
						System.out.println("3: above");
					} else if (equivalentCandidates.contains(targetCorrection) || (isBeginningOfSentence
							&& equivalentCandidates.contains(targetCorrection.toLowerCase()))) {
						rAt3 += 1.0 / (equivalentCandidates.size() * 1.0);
						System.out.println("3: within: "+equivalentCandidates.size());
						for(String entry : equivalentCandidates) {
							System.out.println(entry);
						}
					}
				} else {
					if (top3Candidates.contains(targetCorrection)
							|| (isBeginningOfSentence && top3Candidates.contains(targetCorrection.toLowerCase()))) {
						rAt3++;
						System.out.println("3: contained");
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
					
					for(int i=0; i<currentIndex-1; i++) {
						top5Candidates.addAll(suggestions.get(rankList.get(i)));
					}
					
					System.out.println("5 without equiv");
					for(String cand: top5Candidates) {
						System.out.println(cand);
					}
					System.out.println("end");

					if (top5Candidates.contains(targetCorrection)
							|| (isBeginningOfSentence && top5Candidates.contains(targetCorrection.toLowerCase()))) {
						rAt5++;
						System.out.println("5: above");
					} else if (equivalentCandidates.contains(targetCorrection) || (isBeginningOfSentence
							&& equivalentCandidates.contains(targetCorrection.toLowerCase()))) {
						rAt5 += 1.0 / (equivalentCandidates.size() * 1.0);
						System.out.println("5: within: "+equivalentCandidates.size());
						for(String entry : equivalentCandidates) {
							System.out.println(entry);
						}
					}
				} else {
					if (top5Candidates.contains(targetCorrection)
							|| (isBeginningOfSentence && top5Candidates.contains(targetCorrection.toLowerCase()))) {
						rAt5++;
						System.out.println("5: contained");
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
					
					for(int i=0; i<currentIndex-1; i++) {
						top10Candidates.addAll(suggestions.get(rankList.get(i)));
					}
					
					System.out.println("10 without equiv");
					for(String cand: top10Candidates) {
						System.out.println(cand);
					}
					System.out.println("end");

					if (top10Candidates.contains(targetCorrection)
							|| (isBeginningOfSentence && top10Candidates.contains(targetCorrection.toLowerCase()))) {
						rAt10++;
						System.out.println("10: above");
					} else if (equivalentCandidates.contains(targetCorrection) || (isBeginningOfSentence
							&& equivalentCandidates.contains(targetCorrection.toLowerCase()))) {
						rAt10 += (1.0 / (equivalentCandidates.size() * 1.0));
						System.out.println("10: within: "+equivalentCandidates.size());
						for(String entry : equivalentCandidates) {
							System.out.println(entry);
						}
					}
				} else {
					if (top10Candidates.contains(targetCorrection)
							|| (isBeginningOfSentence && top10Candidates.contains(targetCorrection.toLowerCase()))) {
						rAt10++;
						System.out.println("10: contained");
					}
				}
			}

			System.out.println("at 1: " + rAt1);
			System.out.println("at 2: " + rAt2);
			System.out.println("at 3: " + rAt3);
			System.out.println("at 5: " + rAt5);
			System.out.println("at 10: " + rAt10);
			System.out.println("correct: " + numberOfMatchingCorrections);

		}
	}
	
	private void addToListOfCorrections(Sentence sentence, ExtendedSpellingAnomaly anomaly, String isCorrect) {
		
		List<String> sentenceTokens = new ArrayList<String>();
		List<Token> tokens = JCasUtil.selectCovered(Token.class, sentence);
		
		// to count offset of anomaly within sentence
		int sentenceIndex = 0;
		int begin_index = -1;
		int end_index = -1;
		
		for(Token token : tokens) {
//			if(token.getBegin() == anomaly.getBegin() && token.getEnd() == anomaly.getEnd()) {
//				sentenceTokens.add(anomaly.getMisspelledTokenText());
//			}
//			else {
//				sentenceTokens.add(token.getCoveredText());
//			}
			// Have to replace all that are spelling anomalies in oder to normalize back to initial form
			if(JCasUtil.selectCovered(ExtendedSpellingAnomaly.class, token).size() != 0) {
				ExtendedSpellingAnomaly anomalyToReplace = JCasUtil.selectCovered(ExtendedSpellingAnomaly.class, token).iterator().next();
				sentenceTokens.add(anomalyToReplace.getMisspelledTokenText());
				// if this is the target anomaly
				if(anomalyToReplace.equals(anomaly)) {
					begin_index = sentenceIndex;
					end_index = sentenceIndex + anomalyToReplace.getMisspelledTokenText().length();
				}
				sentenceIndex += anomalyToReplace.getMisspelledTokenText().length()+1;
			}
			else {
				sentenceTokens.add(token.getCoveredText());
				sentenceIndex += token.getCoveredText().length()+1;
			}
		}
		int sentenceBeginIndex = sentence.getBegin();
		corrections.add(String.join(" ", sentenceTokens)+"\t"+anomaly.getMisspelledTokenText()+"\t"+begin_index+"\t"+end_index+"\t"+anomaly.getCoveredText()+"\t"+anomaly.getGoldStandardCorrection()+"\t"+isCorrect);
		
	}
	

	@Override
	public void collectionProcessComplete() throws AnalysisEngineProcessException {

		try {
			String eval_dir = SpellingConstants.EVALUATION_DATA_PATH + "ErrorCorrection_" + configName;
			File dir = new File(eval_dir);
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
			bw.write("R@1:\t" + (rAt1 * 1.0) / (numberOfAnomalies * 1.0) + "\t(" + lessThan1in1
					+ " times there were less than 1 candidates to choose from, " + moreThan1in1
					+ " times there were more than 1)");
			bw.newLine();
			bw.write("R@2:\t" + (rAt2 * 1.0) / (numberOfAnomalies * 1.0) + "\t(" + lessThan2in2
					+ " times there were less than 2 candidates to choose from, " + moreThan2in2
					+ " times there were more than 2)");
			bw.newLine();
			bw.write("R@3:\t" + (rAt3 * 1.0) / (numberOfAnomalies * 1.0) + "\t(" + lessThan3in3
					+ " times there were less than 3 candidates to choose from, " + moreThan3in3
					+ " times there were more than 3)");
			bw.newLine();
			bw.write("R@5:\t" + (rAt5 * 1.0) / (numberOfAnomalies * 1.0) + "\t(" + lessThan5in5
					+ " times there were less than 5 candidates to choose from, " + moreThan5in5
					+ " times there were more than 3)");
			bw.newLine();
			bw.write("R@10:\t" + (rAt10 * 1.0) / (numberOfAnomalies * 1.0) + "\t(" + lessThan10in10
					+ " times there were less than 10 candidates to choose from, " + moreThan10in10
					+ " times there were more than 10)");
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
			for (String i : numberedCorrect) {
				bw.write(i);
				bw.newLine();
			}
			bw.close();
			
			bw = new BufferedWriter(new FileWriter(eval_dir+"/wrongSentences.txt"));
			bw.write("Sentence\tinitial_word\tstart_index\tend_index\tour_correction\tgold_correction\tours_is_correct\n");
			for(String element : corrections) {
				bw.write(element+"\n");
			}
			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
