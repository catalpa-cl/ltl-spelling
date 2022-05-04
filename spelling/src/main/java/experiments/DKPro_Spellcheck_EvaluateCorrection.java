package experiments;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.ExternalResourceFactory.createExternalResourceDescription;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.resource.ExternalResourceDescription;
import org.apache.uima.resource.ResourceInitializationException;
import org.dkpro.core.frequency.resources.Web1TFrequencyCountResource;

import candidateReranking.LanguageModelReranker;
import evaluation.EvaluateErrorCorrection_UserMode;
import generateAndRank.GenerateAndRank_LevenshteinGrapheme;
import normalization.ApplyChanges;
import normalization.SpellingAnomalyReplacer;
import preprocessing.LineBreakAnnotator;
import preprocessing.MarkSentenceBeginnings;
import reader.SpellingReader;
import utils.AnalysisEngineProvider;

public class DKPro_Spellcheck_EvaluateCorrection {

	public static void main(String[] args) {

		String lang = args[0];
		String file = args[1];

//		String lang = "it";
//		String file = "src/main/resources/corpora/cita_spelling_test.xml";

		try {
			if (lang.equals("en")) {
				runErrorCorrection(lang, file, "/dictionaries/hunspell_dict_en_US.txt",
						System.getenv("WEB1T") + "/" + lang);
			} else if (lang.equals("de")) {
				runErrorCorrection(lang, file, "/dictionaries/hunspell_dict_de.txt",
						System.getenv("WEB1T") + "/" + lang);
			} else if (lang.equals("cz")) {
				runErrorCorrection(lang, file, "/dictionaries/hunspell_dict_cz.txt",
						System.getenv("WEB1T") + "/" + lang);
			} else if (lang.equals("it")) {
				runErrorCorrection(lang, file, "/dictionaries/hunspell_dict_it.txt",
						System.getenv("WEB1T") + "/" + lang);
			} else {
				System.out.println("Cannot process language " + lang
						+ "! Supported langauges are German (\"de\"), English (\"en\"), Italian (\"it\"), and Czech (\"cz\").");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UIMAException e) {
			e.printStackTrace();
		}
	}

	private static void runErrorCorrection(String lang, String corpus_path, String dict_path, String web1t_path)
			throws UIMAException, IOException {

		// Create web1t language model to set via parameter
		ExternalResourceDescription web1t = createExternalResourceDescription(Web1TFrequencyCountResource.class,
				Web1TFrequencyCountResource.PARAM_LANGUAGE, lang, Web1TFrequencyCountResource.PARAM_MIN_NGRAM_LEVEL,
				"1", Web1TFrequencyCountResource.PARAM_MAX_NGRAM_LEVEL, "4",
				Web1TFrequencyCountResource.PARAM_INDEX_PATH, web1t_path);

		CollectionReader reader = getReader(corpus_path, lang);
		AnalysisEngineDescription segmenter = AnalysisEngineProvider.getSegmenter(lang, "");
		AnalysisEngineDescription markSentenceBeginnings = createEngineDescription(MarkSentenceBeginnings.class,
				MarkSentenceBeginnings.PARAM_INCLUDE_PERIOD, false);
		AnalysisEngineDescription lineBreakAnnotator = createEngineDescription(LineBreakAnnotator.class);
		AnalysisEngineDescription generateRankGrapheme = createEngineDescription(
				GenerateAndRank_LevenshteinGrapheme.class, GenerateAndRank_LevenshteinGrapheme.PARAM_DICTIONARIES,
				dict_path, GenerateAndRank_LevenshteinGrapheme.PARAM_LOWERCASE, false,
				GenerateAndRank_LevenshteinGrapheme.PARAM_NUM_OF_CANDIDATES_TO_GENERATE, 3,
				GenerateAndRank_LevenshteinGrapheme.PARAM_INCLUDE_TRANSPOSITION, true);
		AnalysisEngineDescription lmReranker = createEngineDescription(LanguageModelReranker.class,
				LanguageModelReranker.RES_LANGUAGE_MODEL, web1t, LanguageModelReranker.PARAM_NGRAM_SIZE, 3);
		AnalysisEngineDescription anomalyReplacer = createEngineDescription(SpellingAnomalyReplacer.class,
				SpellingAnomalyReplacer.PARAM_TYPES_TO_COPY, new String[] { "spelling.types.ExtendedSpellingAnomaly" });
		AnalysisEngineDescription changeApplier = createEngineDescription(ApplyChanges.class);
		AnalysisEngineDescription correctionEvaluator = createEngineDescription(EvaluateErrorCorrection_UserMode.class,
				EvaluateErrorCorrection_UserMode.PARAM_CONFIG_NAME,
				corpus_path);

		SimplePipeline.runPipeline(reader, segmenter, lineBreakAnnotator, markSentenceBeginnings, generateRankGrapheme,
				lmReranker, anomalyReplacer, changeApplier, segmenter, correctionEvaluator);

	}

	public static CollectionReader getReader(String path, String language) throws ResourceInitializationException {

		return CollectionReaderFactory.createReader(SpellingReader.class, SpellingReader.PARAM_SOURCE_FILE, path,
				SpellingReader.PARAM_LANGUAGE_CODE, language);
	}
}
