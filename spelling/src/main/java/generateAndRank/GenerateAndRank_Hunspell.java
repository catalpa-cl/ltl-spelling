package generateAndRank;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.bridj.Pointer;
import org.uimafit.util.JCasUtil;

import de.tudarmstadt.ukp.dkpro.core.api.anomaly.type.SpellingAnomaly;
import dumonts.hunspell.bindings.HunspellLibrary;
import dumonts.hunspell.bindings.HunspellLibrary.Hunhandle;

/**
 * Uses hunspell to generate candidates
 */

public class GenerateAndRank_Hunspell extends CandidateGeneratorAndRanker {

	/**
	 * The dictionary dic file based on which to generate the correction candidates.
	 */
	public static final String PARAM_DIC_FILE_PATH = "dicFilePath";
	@ConfigurationParameter(name = PARAM_DIC_FILE_PATH, mandatory = true)
	protected String dicPath;

	/**
	 * The dictionary aff file based on which to generate the correction candidates.
	 */
	public static final String PARAM_AFF_FILE_PATH = "affFilePath";
	@ConfigurationParameter(name = PARAM_AFF_FILE_PATH, mandatory = true)
	protected String affPath;

	/*
	 * Whether to collect all suggestions returned by hunspell
	 */
	public static final String PARAM_USE_ALL_SUGGESTIONS = "useAllSuggestions";
	@ConfigurationParameter(name = PARAM_USE_ALL_SUGGESTIONS, mandatory = true)
	protected boolean useAllSuggestions;

	private Pointer<Hunhandle> handle = null;
	private Charset charset = null;

	float fixedCost = 1;

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
	}

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {

		for (SpellingAnomaly anomaly : JCasUtil.select(aJCas, SpellingAnomaly.class)) {

			Map<Float, List<String>> rankedCandidates = new TreeMap<Float, List<String>>();
			String currentWord = anomaly.getCoveredText();
			List<Object> suggestions = suggest(currentWord);
			List<String> entriesForThisCost = rankedCandidates.get(fixedCost);

			if (entriesForThisCost == null) {

				rankedCandidates.put(fixedCost, new ArrayList<String>());
				entriesForThisCost = rankedCandidates.get(fixedCost);
			}

			for (Object suggestion : suggestions) {

				String suggestedWord = (String) suggestion;
				entriesForThisCost.add(suggestedWord);
			}

			SuggestionCostTuples tuples = getSuggestionCostTuples(rankedCandidates.entrySet().iterator());
			addSuggestedActions(aJCas, anomaly, tuples);
		}
	}

	private List<Object> suggest(String word) {

		if (handle == null) {

			throw new RuntimeException("Attempt to use hunspell instance after closing");
		}

		// FROM LANGU

		@SuppressWarnings("unchecked")
		Pointer<Byte> str = (Pointer<Byte>) Pointer.pointerToString(word, Pointer.StringType.C, charset);

		// Create pointer to native string array
		Pointer<Pointer<Pointer<Byte>>> nativeSuggestionArray = Pointer.allocatePointerPointer(Byte.class);

		// Hunspell will allocate the array and fill it with suggestions
		int suggestionCount = HunspellLibrary.Hunspell_suggest(handle, nativeSuggestionArray, str);
		if (suggestionCount == 0) {
			// Return early and don't try to free the array
			return new ArrayList<Object>();
		}

		// Ask bridj for a `java.util.List` that wraps `nativeSuggestionArray`
		List<Pointer<Byte>> nativeSuggestionList = nativeSuggestionArray.get().validElements(suggestionCount).asList();

		// Convert C Strings to java strings
		List<Object> suggestions = nativeSuggestionList.stream().map(new Function<Pointer<Byte>, Object>() {

			@Override
			public Object apply(Pointer<Byte> p) {

				return p.getStringAtOffset(0, Pointer.StringType.C, charset);
			}
		}).collect(Collectors.toList());

		// We can free the underlying buffer now because Java's `String` owns it's own
		// memory
		HunspellLibrary.Hunspell_free_list(handle, nativeSuggestionArray, suggestionCount);

		if (suggestions.size() > numberOfCandidatesToGenerate) {
			return suggestions.subList(0, numberOfCandidatesToGenerate);
		}

		return suggestions;
	}
}
