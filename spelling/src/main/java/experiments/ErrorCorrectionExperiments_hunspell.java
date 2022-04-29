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
import generateAndRank.GenerateAndRank_Hunspell;
import normalization.ApplyChanges;
import normalization.SpellingAnomalyReplacer;
import preprocessing.LineBreakAnnotator;
import preprocessing.MarkSentenceBeginnings;
import reader.SpellingReader;
import utils.AnalysisEngineProvider;

public class ErrorCorrectionExperiments_hunspell {

	public static void main(String[] args) throws IOException, UIMAException {

		runSkaLa_hunspell();
		runMerlinDE_hunspell();
		runMerlinCZ_hunspell();
		runMerlinIT_hunspell();
		runCIta_hunspell();
		runLitkey_hunspell();
		runTOEFL_hunspell();
	}

	private static void runSkaLa_hunspell() throws UIMAException, IOException {

		String lang = "de";
		String path = "src/main/resources/corpora/skala_spelling.xml";

		String dic = "src/main/resources/dictionaries/hunspell/German_de_DE_aux.dic";
		String aff = "src/main/resources/dictionaries/hunspell/German_de_DE.aff";

		String web1t_path = System.getenv("WEB1T_DE");

		runErrorCorrection_hunspell(lang, path, "SkaLa_hunspell_noReranking", dic, aff, 1, web1t_path, -1, false,
				false);
		runErrorCorrection_hunspell(lang, path, "SkaLa_hunspell_web1tTrigrams", dic, aff, 100, web1t_path, 3, true,
				false);
		runErrorCorrection_hunspell(lang, path, "SkaLa_hunspell_3candiates_web1tTrigrams", dic, aff, 3, web1t_path, 3,
				true, false);

	}

	private static void runLitkey_hunspell() throws UIMAException, IOException {

		String lang = "de";
		String path = "src/main/resources/corpora/litkey_spelling.xml";

		String dic = "src/main/resources/dictionaries/hunspell/German_de_DE_aux.dic";
		String aff = "src/main/resources/dictionaries/hunspell/German_de_DE.aff";

		String web1t_path = System.getenv("WEB1T_DE");

		runErrorCorrection_hunspell(lang, path, "Litkey_hunspell_noReranking", dic, aff, 1, web1t_path, -1, false,
				true);
		runErrorCorrection_hunspell(lang, path, "Litkey_hunspell_web1tTrigrams", dic, aff, 100, web1t_path, 3, true,
				true);
		runErrorCorrection_hunspell(lang, path, "Litkey_hunspell_3candidates_web1tTrigrams", dic, aff, 3, web1t_path, 3,
				true, true);
	}

	private static void runMerlinDE_hunspell() throws UIMAException, IOException {

		String lang = "de";
		String path = "src/main/resources/corpora/merlin-de_spelling.xml";

		String dic = "src/main/resources/dictionaries/hunspell/German_de_DE_aux.dic";
		String aff = "src/main/resources/dictionaries/hunspell/German_de_DE.aff";

		String web1t_path = System.getenv("WEB1T_DE");

		runErrorCorrection_hunspell(lang, path, "MERLIN-DE_hunspell_noReranking", dic, aff, 1, web1t_path, -1, false,
				false);
		runErrorCorrection_hunspell(lang, path, "MERLIN-DE_hunspell_web1tTrigrams", dic, aff, 100, web1t_path, 3, true,
				false);
		runErrorCorrection_hunspell(lang, path, "MERLIN-DE_hunspell_3candidates_web1tTrigrams", dic, aff, 3, web1t_path,
				3, true, false);
	}

	private static void runMerlinCZ_hunspell() throws UIMAException, IOException {

		String lang = "cz";
		String path = "src/main/resources/corpora/merlin-cz_spelling.xml";

		String dic = "src/main/resources/dictionaries/hunspell/Czech.dic";
		String aff = "src/main/resources/dictionaries/hunspell/Czech.aff";

		String web1t_path = System.getenv("WEB1T_CZ");

		runErrorCorrection_hunspell(lang, path, "MERLIN-CZ_hunspell_noReranking", dic, aff, 1, web1t_path, -1, false,
				false);
		runErrorCorrection_hunspell(lang, path, "MERLIN-CZ_hunspell_web1tTrigrams", dic, aff, 100, web1t_path, 3, true,
				false);
		runErrorCorrection_hunspell(lang, path, "MERLIN-CZ_hunspell_3candidates_web1tTrigrams", dic, aff, 3, web1t_path,
				3, true, false);
	}

	private static void runMerlinIT_hunspell() throws UIMAException, IOException {

		String lang = "it";
		String path = "src/main/resources/corpora/merlin-it_spelling.xml";

		String dic = "src/main/resources/dictionaries/hunspell/Italian.dic";
		String aff = "src/main/resources/dictionaries/hunspell/Italian.aff";

		String web1t_path = System.getenv("WEB1T_IT");

		runErrorCorrection_hunspell(lang, path, "MERLIN-IT_hunspell_noReranking", dic, aff, 1, web1t_path, -1, false,
				false);
		runErrorCorrection_hunspell(lang, path, "MERLIN-IT_hunspell_web1tTrigrams", dic, aff, 100, web1t_path, 3, true,
				false);
		runErrorCorrection_hunspell(lang, path, "MERLIN-IT_hunspell_3candidates_web1tTrigrams", dic, aff, 3, web1t_path,
				3, true, false);
	}

	private static void runCIta_hunspell() throws UIMAException, IOException {

		String lang = "it";
		String corpus_path = "src/main/resources/corpora/cita_spelling.xml";

		String dic = "src/main/resources/dictionaries/hunspell/Italian.dic";
		String aff = "src/main/resources/dictionaries/hunspell/Italian.aff";

		String web1t_path = System.getenv("WEB1T_IT");

		runErrorCorrection_hunspell(lang, corpus_path, "CIta_hunspell_noReranking", dic, aff, 1, web1t_path, -1, false,
				false);
		runErrorCorrection_hunspell(lang, corpus_path, "CIta_hunspell_web1tTrigrams", dic, aff, 100, web1t_path, 3,
				true, false);
		runErrorCorrection_hunspell(lang, corpus_path, "CIta_hunspell_3candidates_web1tTrigrams", dic, aff, 3,
				web1t_path, 3, true, false);

	}

	private static void runTOEFL_hunspell() throws UIMAException, IOException {

		String lang = "en";
		String path = "src/main/resources/corpora/toefl_spelling.xml";

		String dic = "src/main/resources/dictionaries/hunspell/en_US.dic";
		String aff = "src/main/resources/dictionaries/hunspell/en_US.aff";

		String web1t_path = System.getenv("WEB1T_EN");

		runErrorCorrection_hunspell(lang, path, "TOEFL_hunspell_noReranking", dic, aff, 1, web1t_path, -1, false,
				false);
		runErrorCorrection_hunspell(lang, path, "TOEFL_hunspell_web1tTrigrams", dic, aff, 100, web1t_path, 3, true,
				false);
		runErrorCorrection_hunspell(lang, path, "TOEFL_hunspell_3candidates_web1tTrigrams", dic, aff, 3, web1t_path, 3,
				true, false);

	}

	private static void runErrorCorrection_hunspell(String lang, String corpus_path, String config_name,
			String dic_path, String aff_path, int num_candidates_to_generate, String web1t_path, int n_gram_size,
			boolean reRank, boolean includePeriod) throws UIMAException, IOException {

		// Create web1t language model to set via parameter
		ExternalResourceDescription web1t = createExternalResourceDescription(Web1TFrequencyCountResource.class,
				Web1TFrequencyCountResource.PARAM_LANGUAGE, lang, Web1TFrequencyCountResource.PARAM_MIN_NGRAM_LEVEL,
				"1", Web1TFrequencyCountResource.PARAM_MAX_NGRAM_LEVEL, "3",
				Web1TFrequencyCountResource.PARAM_INDEX_PATH, web1t_path);

		CollectionReader reader = getReader(corpus_path, lang);

		AnalysisEngineDescription segmenter = AnalysisEngineProvider.getSegmenter(lang, config_name);
		AnalysisEngineDescription markSentenceBeginnings = createEngineDescription(MarkSentenceBeginnings.class,
				MarkSentenceBeginnings.PARAM_INCLUDE_PERIOD, includePeriod);
		AnalysisEngineDescription lineBreakAnnotator = createEngineDescription(LineBreakAnnotator.class);
		AnalysisEngineDescription generateRankHunspell = createEngineDescription(GenerateAndRank_Hunspell.class,
				GenerateAndRank_Hunspell.PARAM_DIC_FILE_PATH, dic_path, GenerateAndRank_Hunspell.PARAM_AFF_FILE_PATH,
				aff_path, GenerateAndRank_Hunspell.PARAM_USE_ALL_SUGGESTIONS, reRank,
				GenerateAndRank_Hunspell.PARAM_NUM_OF_CANDIDATES_TO_GENERATE, 3,
				GenerateAndRank_Hunspell.PARAM_DICTIONARIES, new String[] {});

		AnalysisEngineDescription lmReranker = createEngineDescription(LanguageModelReranker.class,
				LanguageModelReranker.RES_LANGUAGE_MODEL, web1t, LanguageModelReranker.PARAM_NGRAM_SIZE, n_gram_size);

		AnalysisEngineDescription anomalyReplacer = createEngineDescription(SpellingAnomalyReplacer.class,
				SpellingAnomalyReplacer.PARAM_TYPES_TO_COPY,
				new String[] { "de.unidue.ltl.spelling.types.ExtendedSpellingAnomaly" });
		AnalysisEngineDescription changeApplier = createEngineDescription(ApplyChanges.class);
		AnalysisEngineDescription correctionEvaluator = createEngineDescription(EvaluateErrorCorrection.class,
				EvaluateErrorCorrection.PARAM_CONFIG_NAME, config_name);

		if (reRank) {
			SimplePipeline.runPipeline(reader, segmenter, lineBreakAnnotator, markSentenceBeginnings,
					generateRankHunspell, lmReranker, anomalyReplacer, changeApplier, correctionEvaluator);
		} else {
			SimplePipeline.runPipeline(reader, segmenter, lineBreakAnnotator, markSentenceBeginnings,
					generateRankHunspell, anomalyReplacer, changeApplier, correctionEvaluator);
		}
	}

	public static CollectionReader getReader(String path, String language) throws ResourceInitializationException {

		return CollectionReaderFactory.createReader(SpellingReader.class, SpellingReader.PARAM_SOURCE_FILE, path,
				SpellingReader.PARAM_LANGUAGE_CODE, language);
	}
}