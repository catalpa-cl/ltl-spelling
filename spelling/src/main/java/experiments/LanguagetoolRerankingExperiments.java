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
import evaluation.EvaluateErrorCorrection;
import normalization.ApplyChanges;
import normalization.SpellingAnomalyReplacer;
import preprocessing.LineBreakAnnotator;
import preprocessing.MarkSentenceBeginnings;
import reader.SpellingReader_LanguageToolSuggestions;
import utils.AnalysisEngineProvider;

public class LanguagetoolRerankingExperiments {

	public static void main(String[] args) throws UIMAException, IOException {

		runCItA(3);
		runTOEFL(3);
		runSkaLa(3);
		runMerlinIT(3);
		runMerlinDE(3);
		runLitkey(3);
	}

	private static void runLitkey(int n_gram_size) throws UIMAException, IOException {

		String lang = "de";
		String path = "src/main/resources/LanguageTool_Suggestions/Litkey_sentences_LT_correction.tsv";
		String web1t_path = System.getenv("WEB1T") + "/de";

		runErrorCorrection("LanguageToolReranking_Litkey_web1t_ngramSize_" + n_gram_size, lang, path, web1t_path,
				n_gram_size, true);
	}

	private static void runMerlinDE(int n_gram_size) throws UIMAException, IOException {

		String lang = "de";
		String path = "src/main/resources/LanguageTool_Suggestions/MERLIN-DE_sentences_LT_correction.tsv";
		String web1t_path = System.getenv("WEB1T") + "/de";

		runErrorCorrection("LanguageToolReranking_MERLIN-DE_web1t_ngramSize" + n_gram_size, lang, path, web1t_path,
				n_gram_size, false);
	}

	private static void runMerlinIT(int n_gram_size) throws UIMAException, IOException {

		String lang = "it";
		String path = "src/main/resources/LanguageTool_Suggestions/MERLIN-IT_sentences_LT_correction.tsv";
		String web1t_path = System.getenv("WEB1T") + "/it";

		runErrorCorrection("LanguageToolReranking_MERLIN-IT_web1t_ngramSize" + n_gram_size, lang, path, web1t_path,
				n_gram_size, false);

	}

	private static void runSkaLa(int n_gram_size) throws UIMAException, IOException {

		String lang = "de";
		String path = "src/main/resources/LanguageTool_Suggestions/SkaLa_sentences_LT_correction.tsv";
		String web1t_path = System.getenv("WEB1T") + "/de";

		runErrorCorrection("LanguageToolReranking_SkaLa_web1t_ngramSize_" + n_gram_size, lang, path, web1t_path,
				n_gram_size, false);
	}

	private static void runTOEFL(int n_gram_size) throws UIMAException, IOException {

		String lang = "en";
		String path = "src/main/resources/LanguageTool_Suggestions/TOEFL_sentences_LT_correction.tsv";
		String web1t_path = System.getenv("WEB1T") + "/en";
		System.out.println(web1t_path);

		runErrorCorrection("LanguageToolReranking_TOEFL_web1t_ngramSize_" + n_gram_size, lang, path, web1t_path,
				n_gram_size, false);
	}

	private static void runCItA(int n_gram_size) throws UIMAException, IOException {

		String lang = "it";
		String path = "src/main/resources/LanguageTool_Suggestions/CItA_sentences_LT_correction.tsv";
		String web1t_path = System.getenv("WEB1T") + "/it";

		runErrorCorrection("LanguageToolReranking_CItA_web1t_ngramSize_" + n_gram_size, lang, path, web1t_path,
				n_gram_size, false);
	}

	private static void runErrorCorrection(String config_name, String lang, String corpus_path, String web1t_path,
			int n_gram_size, boolean include_period) throws UIMAException, IOException {

		// Create web1t language model to set via parameter
		ExternalResourceDescription web1t = createExternalResourceDescription(Web1TFrequencyCountResource.class,
				Web1TFrequencyCountResource.PARAM_LANGUAGE, lang, Web1TFrequencyCountResource.PARAM_MIN_NGRAM_LEVEL,
				"1", Web1TFrequencyCountResource.PARAM_MAX_NGRAM_LEVEL, "4",
				Web1TFrequencyCountResource.PARAM_INDEX_PATH, web1t_path);

		CollectionReader reader = getReader(corpus_path, lang);
		AnalysisEngineDescription segmenter = AnalysisEngineProvider.getSegmenter(lang, config_name);
		AnalysisEngineDescription markSentenceBeginnings = createEngineDescription(MarkSentenceBeginnings.class,
				MarkSentenceBeginnings.PARAM_INCLUDE_PERIOD, include_period);
		AnalysisEngineDescription lineBreakAnnotator = createEngineDescription(LineBreakAnnotator.class);
		AnalysisEngineDescription lmReranker = createEngineDescription(LanguageModelReranker.class,
				LanguageModelReranker.RES_LANGUAGE_MODEL, web1t, LanguageModelReranker.PARAM_NGRAM_SIZE, n_gram_size);
		AnalysisEngineDescription anomalyReplacer = createEngineDescription(SpellingAnomalyReplacer.class,
				SpellingAnomalyReplacer.PARAM_TYPES_TO_COPY, new String[] { "spelling.types.ExtendedSpellingAnomaly" });
		AnalysisEngineDescription changeApplier = createEngineDescription(ApplyChanges.class);
		AnalysisEngineDescription correctionEvaluator = createEngineDescription(EvaluateErrorCorrection.class,
				EvaluateErrorCorrection.PARAM_CONFIG_NAME, config_name);

		SimplePipeline.runPipeline(reader, segmenter, lineBreakAnnotator, markSentenceBeginnings, lmReranker,
				anomalyReplacer, changeApplier, segmenter, correctionEvaluator);
	}

	public static CollectionReader getReader(String path, String language) throws ResourceInitializationException {

		return CollectionReaderFactory.createReader(SpellingReader_LanguageToolSuggestions.class,
				SpellingReader_LanguageToolSuggestions.PARAM_SOURCE_FILE, path,
				SpellingReader_LanguageToolSuggestions.PARAM_LANGUAGE_CODE, language);
	}

}
