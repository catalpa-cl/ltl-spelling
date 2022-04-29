package utils;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.resource.ResourceInitializationException;
import org.dkpro.core.tokit.RegexSegmenter;

import de.tudarmstadt.ukp.dkpro.core.corenlp.CoreNlpSegmenter;
import de.tudarmstadt.ukp.dkpro.core.languagetool.LanguageToolSegmenter;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpPosTagger;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpSegmenter;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordNamedEntityRecognizer;
import preprocessing.POStoNEAnnotator;

public class AnalysisEngineProvider {

	public static AnalysisEngineDescription getSegmenter(String lang, String config_name) {

		AnalysisEngineDescription segmenter = null;

		try {

			if (lang.equals("it")) {

				segmenter = createEngineDescription(OpenNlpSegmenter.class);

			} else if (lang.equals("cz")) {

				segmenter = createEngineDescription(CoreNlpSegmenter.class, CoreNlpSegmenter.PARAM_LANGUAGE, "en");

			} else if (config_name.startsWith("Litkey")) {

				segmenter = createEngineDescription(RegexSegmenter.class,
//					RegexSegmenter.PARAM_WRITE_SENTENCE, false,
						RegexSegmenter.PARAM_SENTENCE_BOUNDARY_REGEX, " ([\\.!?\\n]+)|.$ ");

			} else if (lang.equals("en")) {
				segmenter = createEngineDescription(LanguageToolSegmenter.class);
			} else {
				segmenter = createEngineDescription(CoreNlpSegmenter.class);
			}

		} catch (ResourceInitializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return segmenter;
	}

	public static AnalysisEngineDescription getNamedEntityRecognizer(String lang) {

		AnalysisEngineDescription namedEntityRecognizer = null;

		try {

			if (lang.equals("it")) {
				AnalysisEngineDescription posTagger = createEngineDescription(OpenNlpPosTagger.class);
				AnalysisEngineDescription posToNeAnnotator = createEngineDescription(POStoNEAnnotator.class,
						POStoNEAnnotator.PARAM_NE_POS_TAG, "SP");
				namedEntityRecognizer = createEngineDescription(posTagger, posToNeAnnotator);
			} else if (lang.equals("cz")) {
				namedEntityRecognizer = createEngineDescription(StanfordNamedEntityRecognizer.class,
						StanfordNamedEntityRecognizer.PARAM_LANGUAGE, "en");
			} else {
				namedEntityRecognizer = createEngineDescription(StanfordNamedEntityRecognizer.class);
			}

		} catch (ResourceInitializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return namedEntityRecognizer;
	}
}
