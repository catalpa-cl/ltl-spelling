package preprocessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.decompounding.dictionary.LinkingMorphemes;
import de.tudarmstadt.ukp.dkpro.core.decompounding.splitter.LeftToRightSplitterAlgorithm;
import spelling.types.KnownWord;
import spelling.types.StartOfSentence;
import spelling.types.TokenToConsider;

/**
 * Checks presence of tokens within the dictionary, if present marks them as a
 * KnownWord.
 */

public class DictionaryChecker extends JCasAnnotator_ImplBase {

	public static final String PARAM_LANGUAGE = "language";
	@ConfigurationParameter(name = PARAM_LANGUAGE, mandatory = true)
	private String language;

	/**
	 * Path to a dictionary against which to check the TokensToConsider.
	 */
	public static final String PARAM_DICTIONARY_FILE = "dictionaryPath";
	@ConfigurationParameter(name = PARAM_DICTIONARY_FILE, mandatory = true)
	private String dictionaryPath;

	/**
	 * Path to an additional dictionary (optional).
	 */
	public static final String PARAM_AUXILIARY_DICTIONARY_FILE = "auxiliaryDictionaryPath";
	@ConfigurationParameter(name = PARAM_AUXILIARY_DICTIONARY_FILE, mandatory = false)
	private String auxiliaryDictionaryPath;

	public static final String PARAM_LOWERCASE = "lowercase";
	@ConfigurationParameter(name = PARAM_LOWERCASE, mandatory = true, defaultValue = "false")
	private boolean lowercase;

	private Set<String> dictionaryWords = new HashSet<String>();

	LeftToRightSplitterAlgorithm splitter = null;
	LinkingMorphemes linkingMorphemesDE = new LinkingMorphemes(new String[] { "e", "s", "es", "n", "en", "er", "ens" });

	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException {

		super.initialize(context);
		readDictionary(dictionaryPath);
		if (auxiliaryDictionaryPath != null) {
			readDictionary(auxiliaryDictionaryPath);
		}
	};

	private void readDictionary(String dictionaryPath) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(new File(dictionaryPath)));
			while (br.ready()) {
				if (lowercase) {
					dictionaryWords.add(br.readLine().toLowerCase());
				} else {
					dictionaryWords.add(br.readLine());
				}
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

		for (TokenToConsider consider : JCasUtil.select(aJCas, TokenToConsider.class)) {

//			If token is beginning of a new sentence: process lowercased as well
			if (!JCasUtil.selectCovered(StartOfSentence.class, consider).isEmpty()) {
				checkIfWordIsKnown(aJCas, consider, true);
			} else {
				checkIfWordIsKnown(aJCas, consider, false);
			}
		}
	}

	private void checkIfWordIsKnown(JCas aJCas, TokenToConsider token, boolean isBeginningOfSentence) {

		String currentWord = token.getCoveredText();
		boolean isKnown = false;

		if (lowercase) {
			currentWord = currentWord.toLowerCase();
		}

		if (dictionaryWords.contains(currentWord)
				|| (isBeginningOfSentence && dictionaryWords.contains(currentWord.toLowerCase()))) {

			KnownWord word = new KnownWord(aJCas);
			word.setPathToDictItWasFoundIn(dictionaryPath);
			word.setBegin(token.getBegin());
			word.setEnd(token.getEnd());
			word.addToIndexes();
			isKnown = true;
//			System.out.println("Marked as known:\t" + token.getCoveredText() + "\t(found in " + dictionaryPath + ")");
		}

		if (!isKnown && language.equals("it") && currentWord.contains("'")) {

			String[] wordParts = currentWord.split("'");
			if (wordParts.length == 2) {
				if ((dictionaryWords.contains(wordParts[0]) && dictionaryWords.contains(wordParts[1]))
						|| (isBeginningOfSentence && dictionaryWords.contains(wordParts[0].toLowerCase())
								&& dictionaryWords.contains(wordParts[1]))) {
					KnownWord word = new KnownWord(aJCas);
					word.setPathToDictItWasFoundIn(dictionaryPath);
					word.setBegin(token.getBegin());
					word.setEnd(token.getEnd());
					word.addToIndexes();
					isKnown = true;
//					System.out.println("Marked as known:\t" + token.getCoveredText() + "\t(found in " + dictionaryPath + ")");
				}
			}
		}

//		Strip punctuation from beginning and end of token
		if (!isKnown && currentWord.matches("^([\\W\\s]+?)([\\w\\u0080-\\uFFFF]+'?)([\\W\\s]*?)$")
				|| currentWord.matches("^([\\W\\s]*?)([\\w\\u0080-\\uFFFF]+'?)([\\W\\s]+?)$")) {

			String stripNonAlpha = currentWord.replaceAll("^([\\W\\s]*?)([\\w\\u0080-\\uFFFF]+'?)([\\W\\s]*?)$", "$2");
			if (dictionaryWords.contains(stripNonAlpha)
					|| isBeginningOfSentence && dictionaryWords.contains(stripNonAlpha.toLowerCase())) {
				KnownWord word = new KnownWord(aJCas);
				word.setPathToDictItWasFoundIn(dictionaryPath);
				word.setBegin(token.getBegin());
				word.setEnd(token.getEnd());
				word.addToIndexes();
				isKnown = true;
//				System.out.println("Marked as known:\t" + token.getCoveredText() + "\t(found in " + dictionaryPath + "as "+ stripNonAlpha+")");
			}
		}
	}
}