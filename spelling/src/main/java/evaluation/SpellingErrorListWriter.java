package evaluation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import spelling.types.SpellingError;
import utils.GraphemeDictionaryToPhonemeMap;

public class SpellingErrorListWriter extends JCasAnnotator_ImplBase {

	public static final String PARAM_OUTPUT_PATH = "outputPath";
	@ConfigurationParameter(name = PARAM_OUTPUT_PATH, mandatory = true)
	protected String outPath;

	public static final String PARAM_LANGUAGE = "language";
	@ConfigurationParameter(name = PARAM_LANGUAGE, mandatory = true)
	protected String language;

	Set<String> misspellings = new HashSet<String>();

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {

		for (SpellingError error : JCasUtil.select(aJCas, SpellingError.class)) {

			if (!error.getCoveredText().equals("")) {
				misspellings.add(error.getCoveredText());
			}
		}
	}

	@Override
	public void collectionProcessComplete() throws AnalysisEngineProcessException {

		List<String> misspellingList = new ArrayList<String>();
		for (String missp : misspellings) {

			if (!missp.contains(" ")) {
				misspellingList.add(missp);
			} else {
				System.out.println("Not getting phonetic representation of misspelling string " + missp
						+ " because it contrains whitespace!");
			}
		}
		misspellingList.sort(null);

		try {
			GraphemeDictionaryToPhonemeMap.processDictionary(misspellingList, outPath, language);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}