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
import generateAndRank.GenerateAndRank_FindMissingSpace;
import generateAndRank.GenerateAndRank_KeyboardDistance;
import generateAndRank.GenerateAndRank_LevenshteinGrapheme;
import generateAndRank.GenerateAndRank_LevenshteinPhoneme;
import normalization.ApplyChanges;
import normalization.SpellingAnomalyReplacer;
import preprocessing.LineBreakAnnotator;
import preprocessing.MarkSentenceBeginnings;
import reader.SpellingReader;
import utils.AnalysisEngineProvider;

public class ErrorCorrectionExperiments_web1tReranking {

	public static void main(String[] args) throws IOException, UIMAException {

		int n_gram_size = 3;

		int num_candidates_per_method = 3;
		runSkaLa(num_candidates_per_method, n_gram_size);
		runTOEFL(num_candidates_per_method, n_gram_size);
		runCItA(num_candidates_per_method, n_gram_size);
		runLitkey(num_candidates_per_method, n_gram_size);
		runMerlinDE(num_candidates_per_method, n_gram_size);
		runMerlinIT(num_candidates_per_method, n_gram_size);
		runMerlinCZ(num_candidates_per_method, n_gram_size);

		num_candidates_per_method = 10;
		runSkaLa(num_candidates_per_method, n_gram_size);
		runTOEFL(num_candidates_per_method, n_gram_size);
		runCItA(num_candidates_per_method, n_gram_size);
		runLitkey(num_candidates_per_method, n_gram_size);
		runMerlinDE(num_candidates_per_method, n_gram_size);
		runMerlinIT(num_candidates_per_method, n_gram_size);
		runMerlinCZ(num_candidates_per_method, n_gram_size);

		n_gram_size = 1;

		num_candidates_per_method = 3;
		runSkaLa(num_candidates_per_method, n_gram_size);
		runTOEFL(num_candidates_per_method, n_gram_size);
		runCItA(num_candidates_per_method, n_gram_size);
		runLitkey(num_candidates_per_method, n_gram_size);
		runMerlinDE(num_candidates_per_method, n_gram_size);
		runMerlinIT(num_candidates_per_method, n_gram_size);
		runMerlinCZ(num_candidates_per_method, n_gram_size);

		num_candidates_per_method = 10;
		runSkaLa(num_candidates_per_method, n_gram_size);
		runTOEFL(num_candidates_per_method, n_gram_size);
		runCItA(num_candidates_per_method, n_gram_size);
		runLitkey(num_candidates_per_method, n_gram_size);
		runMerlinDE(num_candidates_per_method, n_gram_size);
		runMerlinIT(num_candidates_per_method, n_gram_size);
		runMerlinCZ(num_candidates_per_method, n_gram_size);

	}

	private static void runSkaLa(int num_candidates_per_method, int n_gram_size) throws UIMAException, IOException {
		String lang = "de";
		String path = "src/main/resources/corpora/skala_spelling.xml";
		String dict = "src/main/resources/dictionaries/hunspell_dict_de.txt";
		String dict_phon = "src/main/resources/dictionaries/hunspell_dict_de_phoneme_map.txt";
		String dict_child = "src/main/resources/dictionaries/childlex_litkey.txt";
		String dict_child_phon = "src/main/resources/dictionaries/childlex_litkey_phoneme_map.txt";
		String keyboard_distances = "src/main/resources/matrixes/keyboardDistance_DE-manual.txt";
		String web1t_path = System.getenv("WEB1T") + "/de";

		runErrorCorrection(
				"SkaLa_missingSpaces_hunspell_web1t_numCand_" + num_candidates_per_method + "_ngramSize_" + n_gram_size,
				"missing_spaces", lang, path, dict, dict_phon, keyboard_distances, web1t_path,
				num_candidates_per_method, n_gram_size, false);
		runErrorCorrection(
				"SkaLa_grapheme_hunspell_web1t_numCand_" + num_candidates_per_method + "_ngramSize_" + n_gram_size,
				"grapheme", lang, path, dict, dict_phon, keyboard_distances, web1t_path, num_candidates_per_method,
				n_gram_size, false);
		runErrorCorrection(
				"SkaLa_phoneme_hunspell_web1t_numCand_" + num_candidates_per_method + "_ngramSize_" + n_gram_size,
				"phoneme", lang, path, dict, dict_phon, keyboard_distances, web1t_path, num_candidates_per_method,
				n_gram_size, false);
		runErrorCorrection(
				"SkaLa_keyboard_hunspell_web1t_numCand_" + num_candidates_per_method + "_ngramSize_" + n_gram_size,
				"keyboard", lang, path, dict, dict_phon, keyboard_distances, web1t_path, num_candidates_per_method,
				n_gram_size, false);
		runErrorCorrection(
				"SkaLa_full_hunspell_web1t_numCand_" + num_candidates_per_method + "_ngramSize_" + n_gram_size, "full",
				lang, path, dict, dict_phon, keyboard_distances, web1t_path, num_candidates_per_method, n_gram_size,
				false);

		runErrorCorrection(
				"SkaLa_missingSpaces_childLex_web1t_numCand_" + num_candidates_per_method + "_ngramSize_" + n_gram_size,
				"missing_spaces", lang, path, dict_child, dict_child_phon, keyboard_distances, web1t_path,
				num_candidates_per_method, n_gram_size, false);
		runErrorCorrection(
				"SkaLa_grapheme_childLex_web1t_numCand_" + num_candidates_per_method + "_ngramSize_" + n_gram_size,
				"grapheme", lang, path, dict_child, dict_child_phon, keyboard_distances, web1t_path,
				num_candidates_per_method, n_gram_size, false);
		runErrorCorrection(
				"SkaLa_phoneme_childLex_web1t_numCand_" + num_candidates_per_method + "_ngramSize_" + n_gram_size,
				"phoneme", lang, path, dict_child, dict_child_phon, keyboard_distances, web1t_path,
				num_candidates_per_method, n_gram_size, false);
		runErrorCorrection(
				"SkaLa_keyboard_childLex_web1t_numCand_" + num_candidates_per_method + "_ngramSize_" + n_gram_size,
				"keyboard", lang, path, dict_child, dict_child_phon, keyboard_distances, web1t_path,
				num_candidates_per_method, n_gram_size, false);
		runErrorCorrection(
				"SkaLa_full_childLex_web1t_numCand_" + num_candidates_per_method + "_ngramSize_" + n_gram_size, "full",
				lang, path, dict_child, dict_child_phon, keyboard_distances, web1t_path, num_candidates_per_method,
				n_gram_size, false);

	}

	private static void runTOEFL(int num_candidates_per_method, int n_gram_size) throws UIMAException, IOException {

		String lang = "en";
		String path = "src/main/resources/corpora/toefl_spelling.xml";
		String dict = "src/main/resources/dictionaries/hunspell_dict_en_US.txt";
		String dict_phon = "src/main/resources/dictionaries/hunspell_dict_en_US_phoneme_map.txt";
		String keyboard_distances = "src/main/resources/matrixes/keyboardDistance_EN-manual.txt";
		String web1t_path = System.getenv("WEB1T") + "/en";

		runErrorCorrection(
				"TOEFL_missingSpaces_hunspell_web1t_numCand_" + num_candidates_per_method + "_ngramSize_" + n_gram_size,
				"missing_spaces", lang, path, dict, dict_phon, keyboard_distances, web1t_path,
				num_candidates_per_method, n_gram_size, false);
		runErrorCorrection(
				"TOEFL_grapheme_hunspell_web1t_numCand_" + num_candidates_per_method + "_ngramSize_" + n_gram_size,
				"grapheme", lang, path, dict, dict_phon, keyboard_distances, web1t_path, num_candidates_per_method,
				n_gram_size, false);
		runErrorCorrection(
				"TOEFL_phoneme_hunspell_web1t_numCand_" + num_candidates_per_method + "_ngramSize_" + n_gram_size,
				"phoneme", lang, path, dict, dict_phon, keyboard_distances, web1t_path, num_candidates_per_method,
				n_gram_size, false);
		runErrorCorrection(
				"TOEFL_keyboard_hunspell_web1t_numCand_" + num_candidates_per_method + "_ngramSize_" + n_gram_size,
				"keyboard", lang, path, dict, dict_phon, keyboard_distances, web1t_path, num_candidates_per_method,
				n_gram_size, false);
		runErrorCorrection(
				"TOEFL_full_hunspell_web1t_numCand_" + num_candidates_per_method + "_ngramSize_" + n_gram_size, "full",
				lang, path, dict, dict_phon, keyboard_distances, web1t_path, num_candidates_per_method, n_gram_size,
				false);
	}

	private static void runCItA(int num_candidates_per_method, int n_gram_size) throws UIMAException, IOException {

		String lang = "it";
		String path = "src/main/resources/corpora/cita_spelling.xml";
		String dict = "src/main/resources/dictionaries/hunspell_dict_it.txt";
		String dict_phon = "src/main/resources/dictionaries/hunspell_dict_it_phoneme_map.txt";
		String keyboard_distances = "src/main/resources/matrixes/keyboardDistance_IT-manual.txt";
		String web1t_path = System.getenv("WEB1T") + "/it";

		runErrorCorrection(
				"CItA_missingSpaces_hunspell_web1t_numCand_" + num_candidates_per_method + "_ngramSize_" + n_gram_size,
				"missing_spaces", lang, path, dict, dict_phon, keyboard_distances, web1t_path,
				num_candidates_per_method, n_gram_size, false);
		runErrorCorrection(
				"CItA_grapheme_hunspell_web1t_numCand_" + num_candidates_per_method + "_ngramSize_" + n_gram_size,
				"grapheme", lang, path, dict, dict_phon, keyboard_distances, web1t_path, num_candidates_per_method,
				n_gram_size, false);
		runErrorCorrection(
				"CItA_phoneme_hunspell_web1t_numCand_" + num_candidates_per_method + "_ngramSize_" + n_gram_size,
				"phoneme", lang, path, dict, dict_phon, keyboard_distances, web1t_path, num_candidates_per_method,
				n_gram_size, false);
		runErrorCorrection(
				"CItA_keyboard_hunspell_web1t_numCand_" + num_candidates_per_method + "_ngramSize_" + n_gram_size,
				"keyboard", lang, path, dict, dict_phon, keyboard_distances, web1t_path, num_candidates_per_method,
				n_gram_size, false);
		runErrorCorrection(
				"CItA_full_hunspell_web1t_numCand_" + num_candidates_per_method + "_ngramSize_" + n_gram_size, "full",
				lang, path, dict, dict_phon, keyboard_distances, web1t_path, num_candidates_per_method, n_gram_size,
				false);
	}

	private static void runLitkey(int num_candidates_per_method, int n_gram_size) throws UIMAException, IOException {

		String lang = "de";
		String path = "src/main/resources/corpora/litkey_spelling.xml";
		String dict = "src/main/resources/dictionaries/hunspell_dict_de.txt";
		String dict_phon = "src/main/resources/dictionaries/hunspell_dict_de_phoneme_map.txt";
		String dict_child = "src/main/resources/dictionaries/childlex_litkey.txt";
		String dict_child_phon = "src/main/resources/dictionaries/childlex_litkey_phoneme_map.txt";
		String keyboard_distances = "src/main/resources/matrixes/keyboardDistance_DE-manual.txt";
		String web1t_path = System.getenv("WEB1T") + "/de";

		runErrorCorrection(
				"Litkey_missingSpaces_hunspell_web1t_numCand_" + num_candidates_per_method + "_ngramSize_"
						+ n_gram_size,
				"missing_spaces", lang, path, dict, dict_phon, keyboard_distances, web1t_path,
				num_candidates_per_method, n_gram_size, true);
		runErrorCorrection(
				"Litkey_grapheme_hunspell_web1t_numCand_" + num_candidates_per_method + "_ngramSize_" + n_gram_size,
				"grapheme", lang, path, dict, dict_phon, keyboard_distances, web1t_path, num_candidates_per_method,
				n_gram_size, true);
		runErrorCorrection(
				"Litkey_keyboard_hunspell_web1t_numCand_" + num_candidates_per_method + "_ngramSize_" + n_gram_size,
				"keyboard", lang, path, dict, dict_phon, keyboard_distances, web1t_path, num_candidates_per_method,
				n_gram_size, true);
		runErrorCorrection(
				"Litkey_phoneme_hunspell_web1t_numCand_" + num_candidates_per_method + "_ngramSize_" + n_gram_size,
				"phoneme", lang, path, dict, dict_phon, keyboard_distances, web1t_path, num_candidates_per_method,
				n_gram_size, true);
		runErrorCorrection(
				"Litkey_full_hunspell_web1t_numCand_" + num_candidates_per_method + "_ngramSize_" + n_gram_size, "full",
				lang, path, dict, dict_phon, keyboard_distances, web1t_path, num_candidates_per_method, n_gram_size,
				true);

		runErrorCorrection(
				"Litkey_missingSpaces_childLex_web1t_numCand_" + num_candidates_per_method + "_ngramSize_"
						+ n_gram_size,
				"missing_spaces", lang, path, dict_child, dict_child_phon, keyboard_distances, web1t_path,
				num_candidates_per_method, n_gram_size, true);
		runErrorCorrection(
				"Litkey_grapheme_childLex_web1t_numCand_" + num_candidates_per_method + "_ngramSize_" + n_gram_size,
				"grapheme", lang, path, dict_child, dict_child_phon, keyboard_distances, web1t_path,
				num_candidates_per_method, n_gram_size, true);
		runErrorCorrection(
				"Litkey_keyboard_childLex_web1t_numCand_" + num_candidates_per_method + "_ngramSize_" + n_gram_size,
				"keyboard", lang, path, dict_child, dict_child_phon, keyboard_distances, web1t_path,
				num_candidates_per_method, n_gram_size, true);
		runErrorCorrection(
				"Litkey_phoneme_childLex_web1t_numCand_" + num_candidates_per_method + "_ngramSize_" + n_gram_size,
				"phoneme", lang, path, dict_child, dict_child_phon, keyboard_distances, web1t_path,
				num_candidates_per_method, n_gram_size, true);
		runErrorCorrection(
				"Litkey_full_childLex_web1t_numCand_" + num_candidates_per_method + "_ngramSize_" + n_gram_size, "full",
				lang, path, dict_child, dict_child_phon, keyboard_distances, web1t_path, num_candidates_per_method,
				n_gram_size, true);
	}

	private static void runMerlinIT(int num_candidates_per_method, int n_gram_size) throws UIMAException, IOException {

		String lang = "it";
		String path = "src/main/resources/corpora/merlin-IT_spelling.xml";
		String dict = "src/main/resources/dictionaries/hunspell_dict_it.txt";
		String dict_phon = "src/main/resources/dictionaries/hunspell_dict_it_phoneme_map.txt";
		String keyboard_distances = "src/main/resources/matrixes/keyboardDistance_IT-manual.txt";
		String web1t_path = System.getenv("WEB1T") + "/it";

		runErrorCorrection(
				"MERLIN-IT_missingSpaces_hunspell_web1t_numCand_" + num_candidates_per_method + "_ngramSize_"
						+ n_gram_size,
				"missing_spaces", lang, path, dict, dict_phon, keyboard_distances, web1t_path,
				num_candidates_per_method, n_gram_size, false);
		runErrorCorrection(
				"MERLIN-IT_grapheme_hunspell_web1t_numCand_" + num_candidates_per_method + "_ngramSize_" + n_gram_size,
				"grapheme", lang, path, dict, dict_phon, keyboard_distances, web1t_path, num_candidates_per_method,
				n_gram_size, false);
		runErrorCorrection(
				"MERLIN-IT_phoneme_hunspell_web1t_numCand_" + num_candidates_per_method + "_ngramSize_" + n_gram_size,
				"phoneme", lang, path, dict, dict_phon, keyboard_distances, web1t_path, num_candidates_per_method,
				n_gram_size, false);
		runErrorCorrection(
				"MERLIN-IT_keyboard_hunspell_web1t_numCand_" + num_candidates_per_method + "_ngramSize_" + n_gram_size,
				"keyboard", lang, path, dict, dict_phon, keyboard_distances, web1t_path, num_candidates_per_method,
				n_gram_size, false);
		runErrorCorrection(
				"MERLIN-IT_full_hunspell_web1t_numCand_" + num_candidates_per_method + "_ngramSize_" + n_gram_size,
				"full", lang, path, dict, dict_phon, keyboard_distances, web1t_path, num_candidates_per_method,
				n_gram_size, false);
	}

	private static void runMerlinCZ(int num_candidates_per_method, int n_gram_size) throws UIMAException, IOException {

		String lang = "cz";
		String path = "src/main/resources/corpora/merlin-CZ_spelling.xml";
		String dict = "src/main/resources/dictionaries/hunspell__dict_cz.txt";
		String dict_phon = "src/main/resources/dictionaries/hunspell_dict_cz_phoneme_map.txt";
		String keyboard_distances = "src/main/resources/matrixes/keyboardDistance_CZ-manual.txt";
		String web1t_path = System.getenv("WEB1T") + "/cz";

		runErrorCorrection(
				"MERLIN-CZ_missingSpaces_hunspell_web1t_numCand_" + num_candidates_per_method + "_ngramSize_"
						+ n_gram_size,
				"missing_spaces", lang, path, dict, dict_phon, keyboard_distances, web1t_path,
				num_candidates_per_method, n_gram_size, false);
		runErrorCorrection(
				"MERLIN-CZ_grapheme_hunspell_web1t_numCand_" + num_candidates_per_method + "_ngramSize_" + n_gram_size,
				"grapheme", lang, path, dict, dict_phon, keyboard_distances, web1t_path, num_candidates_per_method,
				n_gram_size, false);
		runErrorCorrection(
				"MERLIN-CZ_phoneme_hunspell_web1t_numCand_" + num_candidates_per_method + "_ngramSize_" + n_gram_size,
				"phoneme", lang, path, dict, dict_phon, keyboard_distances, web1t_path, num_candidates_per_method,
				n_gram_size, false);
		runErrorCorrection(
				"MERLIN-CZ_keyboard_hunspell_web1t_numCand_" + num_candidates_per_method + "_ngramSize_" + n_gram_size,
				"keyboard", lang, path, dict, dict_phon, keyboard_distances, web1t_path, num_candidates_per_method,
				n_gram_size, false);
		runErrorCorrection(
				"MERLIN-CZ_graphemePhoneme_hunspell_web1t_numCand_" + num_candidates_per_method + "_ngramSize_"
						+ n_gram_size,
				"graphemePhoneme", lang, path, dict, dict_phon, keyboard_distances, web1t_path,
				num_candidates_per_method, n_gram_size, false);
		runErrorCorrection(
				"MERLIN-CZ_full_hunspell_web1t_numCand_" + num_candidates_per_method + "_ngramSize_" + n_gram_size,
				"full", lang, path, dict, dict_phon, keyboard_distances, web1t_path, num_candidates_per_method,
				n_gram_size, false);
	}

	private static void runMerlinDE(int num_candidates_per_method, int n_gram_size) throws UIMAException, IOException {

		String lang = "de";
		String path = "src/main/resources/corpora/Merlin_spelling_german.xml";
		String dict = "src/main/resources/dictionaries/hunspell_dict_de.txt";
		String dict_phon = "src/main/resources/dictionaries/hunspell_dict_de_phoneme_map.txt";
		String dict_child = "src/main/resources/dictionaries/childlex_litkey.txt";
		String dict_child_phon = "src/main/resources/dictionaries/childlex_litkey_phoneme_map.txt";
		String keyboard_distances = "src/main/resources/matrixes/keyboardDistance_DE-manual.txt";
		String web1t_path = System.getenv("WEB1T") + "/de";

		runErrorCorrection(
				"MERLIN-DE_missingSpaces_hunspell_web1t_numCand_" + num_candidates_per_method + "_ngramSize_"
						+ n_gram_size,
				"missing_spaces", lang, path, dict, dict_phon, keyboard_distances, web1t_path,
				num_candidates_per_method, n_gram_size, false);
		runErrorCorrection(
				"MERLIN-DE_grapheme_hunspell_web1t_numCand_" + num_candidates_per_method + "_ngramSize_" + n_gram_size,
				"grapheme", lang, path, dict, dict_phon, keyboard_distances, web1t_path, num_candidates_per_method,
				n_gram_size, false);
		runErrorCorrection(
				"MERLIN-DE_phoneme_hunspell_web1t_numCand_" + num_candidates_per_method + "_ngramSize_" + n_gram_size,
				"phoneme", lang, path, dict, dict_phon, keyboard_distances, web1t_path, num_candidates_per_method,
				n_gram_size, false);
		runErrorCorrection(
				"MERLIN-DE_keyboard_hunspell_web1t_numCand_" + num_candidates_per_method + "_ngramSize_" + n_gram_size,
				"keyboard", lang, path, dict, dict_phon, keyboard_distances, web1t_path, num_candidates_per_method,
				n_gram_size, false);
		runErrorCorrection(
				"MERLIN-DE_full_hunspell_web1t_numCand_" + num_candidates_per_method + "_ngramSize_" + n_gram_size,
				"full", lang, path, dict, dict_phon, keyboard_distances, web1t_path, num_candidates_per_method,
				n_gram_size, false);

		runErrorCorrection(
				"MERLIN-DE_missingSpaces_childLex_web1t_numCand_" + num_candidates_per_method + "_ngramSize_"
						+ n_gram_size,
				"missing_spaces", lang, path, dict_child, dict_child_phon, keyboard_distances, web1t_path,
				num_candidates_per_method, n_gram_size, false);
		runErrorCorrection(
				"MERLIN-DE_grapheme_childLex_web1t_numCand_" + num_candidates_per_method + "_ngramSize_" + n_gram_size,
				"grapheme", lang, path, dict_child, dict_child_phon, keyboard_distances, web1t_path,
				num_candidates_per_method, n_gram_size, false);
		runErrorCorrection(
				"MERLIN-DE_phoneme_childLex_web1t_numCand_" + num_candidates_per_method + "_ngramSize_" + n_gram_size,
				"phoneme", lang, path, dict_child, dict_child_phon, keyboard_distances, web1t_path,
				num_candidates_per_method, n_gram_size, false);
		runErrorCorrection(
				"MERLIN-DE_keyboard_childLex_web1t_numCand_" + num_candidates_per_method + "_ngramSize_" + n_gram_size,
				"keyboard", lang, path, dict_child, dict_child_phon, keyboard_distances, web1t_path,
				num_candidates_per_method, n_gram_size, false);
		runErrorCorrection(
				"MERLIN-DE_full_childLex_web1t_numCand_" + num_candidates_per_method + "_ngramSize_" + n_gram_size,
				"full", lang, path, dict_child, dict_child_phon, keyboard_distances, web1t_path,
				num_candidates_per_method, n_gram_size, false);
	}

	private static void runErrorCorrection(String config_name, String setting, String lang, String corpus_path,
			String dict_path, String phonetic_dict_path, String keyboard_distance_path, String web1t_path,
			int num_candidates_per_method, int n_gram_size, boolean include_period) throws UIMAException, IOException {

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
		AnalysisEngineDescription generateRankGrapheme = createEngineDescription(
				GenerateAndRank_LevenshteinGrapheme.class, GenerateAndRank_LevenshteinGrapheme.PARAM_DICTIONARIES,
				dict_path, GenerateAndRank_LevenshteinGrapheme.PARAM_LOWERCASE, false,
				GenerateAndRank_LevenshteinGrapheme.PARAM_NUM_OF_CANDIDATES_TO_GENERATE, num_candidates_per_method,
				GenerateAndRank_LevenshteinGrapheme.PARAM_INCLUDE_TRANSPOSITION, true);
		AnalysisEngineDescription generateRankPhoneme = createEngineDescription(
				GenerateAndRank_LevenshteinPhoneme.class, GenerateAndRank_LevenshteinPhoneme.PARAM_DICTIONARIES,
				phonetic_dict_path, GenerateAndRank_LevenshteinPhoneme.PARAM_LANGUAGE, lang,
				GenerateAndRank_LevenshteinPhoneme.PARAM_NUM_OF_CANDIDATES_TO_GENERATE, num_candidates_per_method,
				GenerateAndRank_LevenshteinPhoneme.PARAM_INCLUDE_TRANSPOSITION, true);
		AnalysisEngineDescription generateRankKeyboard = createEngineDescription(GenerateAndRank_KeyboardDistance.class,
				GenerateAndRank_KeyboardDistance.PARAM_DICTIONARIES, dict_path,
				GenerateAndRank_KeyboardDistance.PARAM_KEYBOARD_DISTANCES_FILE, keyboard_distance_path,
				GenerateAndRank_KeyboardDistance.PARAM_NUM_OF_CANDIDATES_TO_GENERATE, num_candidates_per_method,
				GenerateAndRank_KeyboardDistance.PARAM_INCLUDE_TRANSPOSITION, true);
		AnalysisEngineDescription generateRankMissingSpaces = createEngineDescription(
				GenerateAndRank_FindMissingSpace.class, GenerateAndRank_FindMissingSpace.PARAM_DICTIONARIES, dict_path,
				GenerateAndRank_FindMissingSpace.PARAM_NUM_OF_CANDIDATES_TO_GENERATE, num_candidates_per_method);
		AnalysisEngineDescription lmReranker = createEngineDescription(LanguageModelReranker.class,
				LanguageModelReranker.RES_LANGUAGE_MODEL, web1t, n_gram_size);
		AnalysisEngineDescription anomalyReplacer = createEngineDescription(SpellingAnomalyReplacer.class,
				SpellingAnomalyReplacer.PARAM_TYPES_TO_COPY,
				new String[] { "de.unidue.ltl.spelling.types.ExtendedSpellingAnomaly" });
		AnalysisEngineDescription changeApplier = createEngineDescription(ApplyChanges.class);
		AnalysisEngineDescription correctionEvaluator = createEngineDescription(EvaluateErrorCorrection.class,
				EvaluateErrorCorrection.PARAM_CONFIG_NAME, config_name);

		if (setting.equals("full")) {
			SimplePipeline.runPipeline(reader, segmenter, lineBreakAnnotator, markSentenceBeginnings,
					generateRankGrapheme, generateRankPhoneme, generateRankKeyboard, generateRankMissingSpaces,
					lmReranker, anomalyReplacer, changeApplier, segmenter, correctionEvaluator);
		}

		if (setting.equals("fullUni")) {
			SimplePipeline.runPipeline(reader, segmenter, lineBreakAnnotator, markSentenceBeginnings,
					generateRankGrapheme, generateRankPhoneme, generateRankKeyboard,
//					generateRankMissingSpaces,
					lmReranker, anomalyReplacer, changeApplier, segmenter, correctionEvaluator);
		}

		if (setting.equals("graphemePhoneme")) {
			SimplePipeline.runPipeline(reader, segmenter, lineBreakAnnotator, markSentenceBeginnings,
					generateRankGrapheme, generateRankPhoneme, lmReranker, anomalyReplacer, changeApplier, segmenter,
					correctionEvaluator);
		}

		else if (setting.equals("grapheme")) {
			SimplePipeline.runPipeline(reader, segmenter, lineBreakAnnotator, markSentenceBeginnings,
					generateRankGrapheme, lmReranker, anomalyReplacer, changeApplier, segmenter, correctionEvaluator);

		} else if (setting.equals("phoneme")) {
			SimplePipeline.runPipeline(reader, segmenter, lineBreakAnnotator, markSentenceBeginnings,
					generateRankPhoneme, lmReranker, anomalyReplacer, changeApplier, segmenter, correctionEvaluator);

		} else if (setting.equals("keyboard")) {
			SimplePipeline.runPipeline(reader, segmenter, lineBreakAnnotator, markSentenceBeginnings,
					generateRankKeyboard, lmReranker, anomalyReplacer, changeApplier, segmenter, correctionEvaluator);

		} else if (setting.equals("missing_spaces")) {
			SimplePipeline.runPipeline(reader, segmenter, lineBreakAnnotator, markSentenceBeginnings,
					generateRankMissingSpaces, lmReranker, anomalyReplacer, changeApplier, segmenter,
					correctionEvaluator);
		}
	}

	public static CollectionReader getReader(String path, String language) throws ResourceInitializationException {

		return CollectionReaderFactory.createReader(SpellingReader.class, SpellingReader.PARAM_SOURCE_FILE, path,
				SpellingReader.PARAM_LANGUAGE_CODE, language);
	}
}