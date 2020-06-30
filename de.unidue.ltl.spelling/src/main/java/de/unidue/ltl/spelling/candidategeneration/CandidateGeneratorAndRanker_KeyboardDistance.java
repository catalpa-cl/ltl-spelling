package de.unidue.ltl.spelling.candidategeneration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.descriptor.ResourceMetaData;
import org.apache.uima.fit.descriptor.TypeCapability;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.api.anomaly.type.SpellingAnomaly;
import de.tudarmstadt.ukp.dkpro.core.api.anomaly.type.SuggestedAction;
import eu.openminted.share.annotations.api.DocumentationResource;

/**
 * Generates and ranks candidates based on keyboard distance. These are entered
 * as SuggestedActions to the SpellingAnomalies.
 */

@ResourceMetaData(name = "")
@DocumentationResource("")
@TypeCapability(inputs = { "de.unidue.ltl.spelling.types.ExtendedSpellingAnomaly" },
		// No real outputs, just SuggestedActions as entries to the SpellingAnomalies?
		outputs = { "de.tudarmstadt.ukp.dkpro.core.api.anomaly.type.SuggestedAction" })
public class CandidateGeneratorAndRanker_KeyboardDistance extends JCasAnnotator_ImplBase {

	public static final String PARAM_DICTIONARIES = "dictionaries";
	@ConfigurationParameter(name = PARAM_DICTIONARIES, mandatory = true)
	protected String[] dictionaries;

	/**
	 * A file containing tab-separated line-by-line entries of distances between
	 * characters, e.g. "a\t b\t 5".
	 */
	public static final String PARAM_KEYBOARD_DISTANCES_FILE = "keyboardDistances";
	@ConfigurationParameter(name = PARAM_KEYBOARD_DISTANCES_FILE, mandatory = true)
	protected String keyboardDistancesPath;

	/**
	 * Sets the distance to apply when it is not reasonable to assume a presumed
	 * character modification was caused by keyboard layout. This distance is
	 * applied for all insertions needed to turn the supposed wrong word into one
	 * present in the dictionary, as well as for the distance of a character to
	 * itself and when no distance information was provided via the distance file.
	 * The default value corresponds to the average distance on a QWERTZ/Y keyboard.
	 */
	public static final String PARAM_DEFAULT_DISTANCE = "defaultDistance";
	@ConfigurationParameter(name = PARAM_DEFAULT_DISTANCE, mandatory = true, defaultValue = "3.6")
	protected float defaultDistance;

	/**
	 * Number of candidates to be generated with this method. If there are more
	 * candidates with the same rank as the n-th of the top n candidates, these are
	 * included as well.
	 */
	public static final String PARAM_NUM_OF_CANDIDATES_TO_GENERATE = "numberOfCandidatesToGenerate";
	@ConfigurationParameter(name = PARAM_NUM_OF_CANDIDATES_TO_GENERATE, mandatory = true, defaultValue = "5")
	protected int numberOfCandidatesToGenerate;

	/**
	 * Whether to permit transposition as a modification operation, e.g. apply
	 * Damerau-Levenshtein distance as opposed to standard Levenshtein Distance.
	 */
	public static final String PARAM_INCLUDE_TRANSPOSITION = "includeTransposition";
	@ConfigurationParameter(name = PARAM_INCLUDE_TRANSPOSITION, mandatory = true, defaultValue = "True")
	protected boolean includeTransposition;

	private Set<String> dictionary = new HashSet<String>();
	private Map<Character, Map<Character, Float>> distanceMap = new HashMap<Character, Map<Character, Float>>();

	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException {
		super.initialize(context);
		readDictionaries(dictionaries);
		readKeyboardDistances(keyboardDistancesPath);
	}

	private void readDictionaries(String[] dictionaries) {
		for (String path : dictionaries) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(new File(path)));
				while (br.ready()) {
					dictionary.add(br.readLine());
				}
				br.close();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void readKeyboardDistances(String distanceFile) {

		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(distanceFile)));
			while (br.ready()) {
				String line = br.readLine();
				String[] distanceEntry = line.split("\t");
				Map<Character, Float> currentCharacterMap = distanceMap.get(distanceEntry[0].charAt(0));
				if (currentCharacterMap == null) {
					distanceMap.put(distanceEntry[0].charAt(0), new HashMap<Character, Float>());
					currentCharacterMap = distanceMap.get(distanceEntry[0].charAt(0));
				}
				currentCharacterMap.put(distanceEntry[1].charAt(0), Float.parseFloat(distanceEntry[2]));
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {

		for (SpellingAnomaly anomaly : JCasUtil.select(aJCas, SpellingAnomaly.class)) {
			System.out.println();
			String misspelling = anomaly.getCoveredText();
			Map<Float, List<String>> rankedCandidates = new TreeMap<Float, List<String>>();

			for (String word : dictionary) {
				float cost = calculateCost(misspelling, word);
				List<String> rankList = rankedCandidates.get(cost);
				if (rankList == null) {
					rankedCandidates.put(cost, new ArrayList<String>());
					rankList = rankedCandidates.get(cost);
				}
				rankList.add(word);
			}

			Iterator<Entry<Float, List<String>>> entries = rankedCandidates.entrySet().iterator();
			SuggestionCostTuples tuples = new SuggestionCostTuples();

			while (tuples.size() < numberOfCandidatesToGenerate) {
				if (entries.hasNext()) {
					Entry<Float, List<String>> entry = entries.next();
					List<String> currentRankList = entry.getValue();
					float rank = entry.getKey();
					for (int j = 0; j < currentRankList.size(); j++) {
						tuples.addTuple(currentRankList.get(j), rank);
					}
				} else {
					break;
				}
			}

			addSuggestedActions(aJCas, anomaly, tuples);
		}
		System.out.println();
	}

	protected void addSuggestedActions(JCas aJCas, SpellingAnomaly anomaly, SuggestionCostTuples tuples) {
		if (tuples.size() > 0) {
			FSArray actions = new FSArray(aJCas, tuples.size());
			int i = 0;
			for (SuggestionCostTuple tuple : tuples) {
				SuggestedAction action = new SuggestedAction(aJCas);
				action.setReplacement(tuple.getSuggestion());
				action.setCertainty(tuple.getCost());
				actions.set(i, action);
				i++;
				System.out.println("Added new correction candidate:\t" + anomaly.getCoveredText() + "\t"
						+ action.getReplacement() + "\t" + action.getCertainty());
			}
			anomaly.setSuggestions(actions);
		} else {
			System.out.println("No correction found for:\t" + anomaly.getCoveredText());
		}
	}

	// Assumes performing operations on 'wrong' to turn it into 'right'
	private float calculateCost(String wrong, String right) {

		float[][] distanceMatrix = new float[wrong.length() + 1][right.length() + 1];

		// Worst case: cost of starting from scratch
		for (int i = 0; i <= wrong.length(); i++) {
			distanceMatrix[i][0] = i * defaultDistance;
		}
		for (int j = 0; j <= right.length(); j++) {
			distanceMatrix[0][j] = j * defaultDistance;
		}

		for (int wrongIndex = 1; wrongIndex <= wrong.length(); wrongIndex++) {
			for (int rightIndex = 1; rightIndex <= right.length(); rightIndex++) {

				// INSERTION: Apply default cost, as no inference can be made about the
				// keyboard layout causing people to forget a certain char.
				float insertion = distanceMatrix[wrongIndex][rightIndex - 1] + defaultDistance;

				// DELETION: If b is to be deleted from abc, cost = min(delete b from ab, delete
				// b from bc)
				float compareToCharBefore = Float.MAX_VALUE;
				float compareToCharAfter = Float.MAX_VALUE;
				if (wrongIndex > 1) {
					compareToCharBefore = distanceMatrix[wrongIndex - 1][rightIndex]
							+ getDistance(wrong.charAt(wrongIndex - 2), wrong.charAt(wrongIndex - 1));
				}
				if (wrongIndex < wrong.length()) {
					compareToCharAfter = distanceMatrix[wrongIndex - 1][rightIndex]
							+ getDistance(wrong.charAt(wrongIndex - 1), wrong.charAt(wrongIndex));
				}
				float deletion = Math.min(compareToCharBefore, compareToCharAfter);

				// SUBSTITUTION
				int charsAreDifferent = 1;
				if (wrong.charAt(wrongIndex - 1) == (right.charAt(rightIndex - 1))) {
					charsAreDifferent = 0;
				}
				float substitution = distanceMatrix[wrongIndex - 1][rightIndex - 1]
						+ charsAreDifferent * getDistance(wrong.charAt(wrongIndex - 1), right.charAt(rightIndex - 1));

				// TRANSPOSITION
				if (includeTransposition && wrongIndex > 1 && rightIndex > 1
						&& wrong.charAt(wrongIndex - 1) == right.charAt(rightIndex - 2)
						&& wrong.charAt(wrongIndex - 2) == right.charAt(rightIndex - 1)) {
					float transposition = distanceMatrix[wrongIndex - 2][rightIndex - 2]
							+ getDistance(wrong.charAt(wrongIndex - 2), wrong.charAt(wrongIndex - 1));
					
					distanceMatrix[wrongIndex][rightIndex] = Math.min(deletion,
							Math.min(insertion, Math.min(substitution, transposition)));
				} else {
					distanceMatrix[wrongIndex][rightIndex] = Math.min(deletion, Math.min(insertion, substitution));
				}
			}
		}

		// To visualize cost matrix
//		for (int i = 0; i < wrong.length() + 1; i++) {
//			for (int j = 0; j < right.length() + 1; j++) {
//
//				System.out.print(distanceMatrix[i][j] + "\t");
//
//			}
//			System.out.println();
//		}
//		System.out.println("Distance between\t" + wrong + "\tand\t" + right + "\tis:\t"
//				+ distanceMatrix[wrong.length()][right.length()]);
		
		return distanceMatrix[wrong.length()][right.length()];

	}

	private float getDistance(char a, char b) {
		try {
			// It is unlikely to accidentally repeat a character; therefore deleting an o
			// from hoouse should not cost 0 because the distance between o and o is 0
			if (a == b) {
				return defaultDistance + compareCases(a, b);
			}
			return distanceMap.get(Character.toLowerCase(a)).get(Character.toLowerCase(b)) + compareCases(a, b);
		} catch (NullPointerException e) {
//			System.out.println("Not found: " + a + b); 
			return defaultDistance + compareCases(a, b);
		}
	}

	private float compareCases(char a, char b) {

		if (Character.isLowerCase(a) == Character.isLowerCase(b)) {
			return 0.0f;
		} else {
			return 0.5f;
		}

	}

	class SuggestionCostTuple {
		private final String suggestion;
		private final float cost;

		public SuggestionCostTuple(String suggestion, float cost) {
			this.suggestion = suggestion;
			this.cost = cost;
		}

		public String getSuggestion() {
			return suggestion;
		}

		public float getCost() {
			return cost;
		}
	}

	class SuggestionCostTuples implements Iterable<SuggestionCostTuple> {
		private final List<SuggestionCostTuple> tuples;

		public SuggestionCostTuples() {
			tuples = new ArrayList<SuggestionCostTuple>();
		}

		public void addTuple(String suggestion, float cost) {
			tuples.add(new SuggestionCostTuple(suggestion, cost));
		}

		public int size() {
			return tuples.size();
		}

//		@Override
		public Iterator<SuggestionCostTuple> iterator() {
			return tuples.iterator();
		}
	}

}
