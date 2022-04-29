package preprocessing;

import java.nio.charset.Charset;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.bridj.Pointer;

import spelling.types.KnownWord;
import spelling.types.StartOfSentence;
import spelling.types.TokenToConsider;
import dumonts.hunspell.bindings.HunspellLibrary;
import dumonts.hunspell.bindings.HunspellLibrary.Hunhandle;

/**
 * Checks which tokens hunspell deems a misspelling
 */

public class HunspellDictionaryChecker extends JCasAnnotator_ImplBase {

	public static final String PARAM_LANGUAGE = "language";
	@ConfigurationParameter(name = PARAM_LANGUAGE, mandatory = true)
	private String language;

	/**
	 * Path to a .dic - file.
	 */
	public static final String PARAM_DIC_FILE = "dicPath";
	@ConfigurationParameter(name = PARAM_DIC_FILE, mandatory = true)
	private String dicPath;

	/**
	 * Path to the corresponding .aff - file.
	 */
	public static final String PARAM_AFF_FILE = "affPath";
	@ConfigurationParameter(name = PARAM_AFF_FILE, mandatory = true)
	private String affPath;

	private Pointer<Hunhandle> handle = null;
	private Charset charset = null;

	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException {

		super.initialize(context);
		try {
			Pointer<Byte> aff = Pointer.pointerToCString(affPath.toString());
			Pointer<Byte> dic = Pointer.pointerToCString(dicPath.toString());
			handle = HunspellLibrary.Hunspell_create(aff, dic);
			charset = Charset.forName(HunspellLibrary.Hunspell_get_dic_encoding(handle).getCString());
			if (this.handle == null) {
				throw new RuntimeException("Unable to create Hunspell instance");
			}
		} catch (UnsatisfiedLinkError e) {
			throw new RuntimeException("Could not create hunspell instance. Please note that only 64 bit platforms "
					+ "(Linux, Windows, Mac) are supported by LanguageTool and that your JVM (Java) also needs to b 64 bit.",
					e);
		}
	};

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {

		for (TokenToConsider consider : JCasUtil.select(aJCas, TokenToConsider.class)) {

			String currentWord = consider.getCoveredText();
			boolean isBeginningOfSentence = !JCasUtil.selectCovered(StartOfSentence.class, consider).isEmpty();
			boolean knownWord = false;

			if (JCasUtil.selectCovered(KnownWord.class, consider).isEmpty()) {

				if (spelledCorrectly(currentWord) || (isBeginningOfSentence && spelledCorrectly(currentWord.toLowerCase()))) {

					KnownWord known = new KnownWord(aJCas);
					known.setBegin(consider.getBegin());
					known.setEnd(consider.getEnd());
					known.addToIndexes();
					knownWord = true;
				}

				if (!knownWord && language.equals("it") && consider.getCoveredText().contains("'")) {

					String[] wordParts = consider.getCoveredText().split("'");
					if (wordParts.length == 2) {
						if ((spelledCorrectly(wordParts[0]) && spelledCorrectly(wordParts[1])) || (isBeginningOfSentence
								&& spelledCorrectly(wordParts[0].toLowerCase()) && spelledCorrectly(wordParts[1]))) {
							KnownWord word = new KnownWord(aJCas);
							word.setBegin(consider.getBegin());
							word.setEnd(consider.getEnd());
							word.addToIndexes();
							knownWord = true;
						}
					}

				}

				if (!knownWord && language.equals("it") && consider.getCoveredText().contains("’")) {

					String[] wordParts = consider.getCoveredText().split("’");
					if (wordParts.length == 2) {
						if ((spelledCorrectly(wordParts[0]) && spelledCorrectly(wordParts[1])) || (isBeginningOfSentence
								&& spelledCorrectly(wordParts[0].toLowerCase()) && spelledCorrectly(wordParts[1]))) {
							KnownWord word = new KnownWord(aJCas);
							word.setBegin(consider.getBegin());
							word.setEnd(consider.getEnd());
							word.addToIndexes();
							knownWord = true;
						}
					}

				}

				if (currentWord.matches("^([\\W\\s]+?)([\\w\\u0080-\\uFFFF]+'?)([\\W\\s]*?)$")
						|| currentWord.matches("^([\\W\\s]*?)([\\w\\u0080-\\uFFFF]+'?)([\\W\\s]+?)$")) {
					String stripNonAlpha = currentWord.replaceAll("^([\\W\\s]*?)([\\w\\u0080-\\uFFFF]+'?)([\\W\\s]*?)$",
							"$2");
					if (spelledCorrectly(stripNonAlpha) || isBeginningOfSentence && spelledCorrectly(stripNonAlpha.toLowerCase())) {
						KnownWord word = new KnownWord(aJCas);
						word.setBegin(consider.getBegin());
						word.setEnd(consider.getEnd());
						word.addToIndexes();
					}
				}
			}
		}
	}

	public boolean spelledCorrectly(String word) {
		if (handle == null) {
			throw new RuntimeException("Attempt to use hunspell instance after closing");
		}

		@SuppressWarnings("unchecked")
		Pointer<Byte> str = (Pointer<Byte>) Pointer.pointerToString(word, Pointer.StringType.C, charset);
		int result = HunspellLibrary.Hunspell_spell(handle, str);
		return result != 0;
	}
}