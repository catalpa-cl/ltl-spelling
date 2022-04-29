package experiments;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.resource.ResourceInitializationException;

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

public class ErrorCorrectionExperiments_noReranking {

	public static void main(String[] args) throws IOException, UIMAException {

		int num_candidates_per_method = 3;

		runTOEFL_noReranking(num_candidates_per_method);
		runMerlinDE_noReranking(num_candidates_per_method);
		runMerlinCZ_noReranking(num_candidates_per_method);
		runMerlinIT_noReranking(num_candidates_per_method);
		runLitkey_noReranking(num_candidates_per_method);
		runCItA_noReranking(num_candidates_per_method);
		runSkaLa_noReranking(num_candidates_per_method);

//		int num_candidates_per_method = 10;

		runTOEFL_noReranking(num_candidates_per_method);
		runMerlinDE_noReranking(num_candidates_per_method);
		runMerlinCZ_noReranking(num_candidates_per_method);
		runMerlinIT_noReranking(num_candidates_per_method);
		runLitkey_noReranking(num_candidates_per_method);
		runCItA_noReranking(num_candidates_per_method);
		runSkaLa_noReranking(num_candidates_per_method);

	}

	private static void runTOEFL_noReranking(int num_candidates_per_method) throws UIMAException, IOException {

		String lang = "en";
		String path = "src/main/resources/corpora/toefl_spelling.xml";
		String dict = "src/main/resources/dictionaries/hunspell_dict_en_US.txt";
		String dict_phon = "src/main/resources/dictionaries/hunspell_dict_en_US_phoneme_map.txt";
		String keyboard_distances = "src/main/resources/matrixes/keyboardDistance_EN-manual.txt";

		runErrorCorrection_noReranking("TOEFL_missingSpaces_hunspell_noReranking_numCand_" + num_candidates_per_method,
				"missing_spaces", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false);
		runErrorCorrection_noReranking("TOEFL_grapheme_hunspell_noReranking_numCand_" + num_candidates_per_method,
				"grapheme", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false);
		runErrorCorrection_noReranking("TOEFL_phoneme_hunspell_noReranking_numCand_" + num_candidates_per_method,
				"phoneme", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false);
		runErrorCorrection_noReranking("TOEFL_keyboard_hunspell_noReranking_numCand_" + num_candidates_per_method,
				"keyboard", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false);
		runErrorCorrection_noReranking("TOEFL_full_hunspell_noReranking_numCand_" + num_candidates_per_method, "full",
				lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false);
	}

	private static void runCItA_noReranking(int num_candidates_per_method) throws UIMAException, IOException {

		String lang = "it";
		String path = "src/main/resources/corpora/cita_spelling.xml";
		String dict = "src/main/resources/dictionaries/hunspell_dict_it.txt";
		String dict_phon = "src/main/resources/dictionaries/hunspell_dict_it_phoneme_map.txt";
		String keyboard_distances = "src/main/resources/matrixes/keyboardDistance_IT-manual.txt";

		runErrorCorrection_noReranking("CItA_missingSpaces_hunspell_noReranking_numCand_" + num_candidates_per_method,
				"missing_spaces", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false);
		runErrorCorrection_noReranking("CItA_grapheme_hunspell_noReranking_numCand_" + num_candidates_per_method,
				"grapheme", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false);
		runErrorCorrection_noReranking("CItA_phoneme_hunspell_noReranking_numCand_" + num_candidates_per_method,
				"phoneme", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false);
		runErrorCorrection_noReranking("CItA_keyboard_hunspell_noReranking_numCand_" + num_candidates_per_method,
				"keyboard", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false);
		runErrorCorrection_noReranking("CItA_full_hunspell_noReranking_numCand_" + num_candidates_per_method, "full",
				lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false);
	}

	private static void runLitkey_noReranking(int num_candidates_per_method) throws UIMAException, IOException {

		String lang = "de";
		String path = "src/main/resources/corpora/litkey_spelling.xml";
		String dict = "src/main/resources/dictionaries/hunspell_dict_de.txt";
		String dict_phon = "src/main/resources/dictionaries/hunspell_dict_de_phoneme_map.txt";
		String keyboard_distances = "src/main/resources/matrixes/keyboardDistance_DE-manual.txt";

		runErrorCorrection_noReranking("Litkey_missingSpaces_hunspell_noReranking_numCand_" + num_candidates_per_method,
				"missing_spaces", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, true);
		runErrorCorrection_noReranking("Litkey_grapheme_hunspell_noReranking_numCand_" + num_candidates_per_method,
				"grapheme", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, true);
		runErrorCorrection_noReranking("Litkey_keyboard_hunspell_noReranking_numCand_" + num_candidates_per_method,
				"keyboard", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, true);
		runErrorCorrection_noReranking("Litkey_phoneme_hunspell_noReranking_numCand_" + num_candidates_per_method,
				"phoneme", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, true);
		runErrorCorrection_noReranking("Litkey_full_hunspell_noReranking_numCand_" + num_candidates_per_method, "full",
				lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, true);
	}

	private static void runMerlinIT_noReranking(int num_candidates_per_method) throws UIMAException, IOException {

		String lang = "it";
		String path = "src/main/resources/corpora/merlin-IT_spelling.xml";
		String dict = "src/main/resources/dictionaries/hunspell__dict_it.txt";
		String dict_phon = "src/main/resources/dictionaries/hunspell_dict_it_phoneme_map.txt";
		String keyboard_distances = "src/main/resources/matrixes/keyboardDistance_IT-manual.txt";

		runErrorCorrection_noReranking(
				"MERLIN-IT_missingSpaces_hunspell_noReranking_numCand_" + num_candidates_per_method, "missing_spaces",
				lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false);
		runErrorCorrection_noReranking("MERLIN-IT_grapheme_hunspell_noReranking_numCand_" + num_candidates_per_method,
				"grapheme", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false);
		runErrorCorrection_noReranking("MERLIN-IT_phoneme_hunspell_noReranking_numCand_" + num_candidates_per_method,
				"phoneme", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false);
		runErrorCorrection_noReranking("MERLIN-IT_keyboard_hunspell_noReranking_numCand_" + num_candidates_per_method,
				"keyboard", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false);
		runErrorCorrection_noReranking("MERLIN-IT_full_hunspell_noReranking_numCand_" + num_candidates_per_method,
				"full", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false);
	}

	private static void runMerlinCZ_noReranking(int num_candidates_per_method) throws UIMAException, IOException {
		String lang = "cz";
		String path = "src/main/resources/corpora/merlin-CZ_spelling.xml";
		String dict = "src/main/resources/dictionaries/hunspell_dict_cz.txt";
		String dict_phon = "src/main/resources/dictionaries/hunspell_dict_cz_phoneme_map.txt";
		String keyboard_distances = "src/main/resources/matrixes/keyboardDistance_CZ-manual.txt";

		runErrorCorrection_noReranking(
				"MERLIN-CZ_missingSpaces_hunspell_noReranking_numCand_" + num_candidates_per_method, "missing_spaces",
				lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false);
		runErrorCorrection_noReranking("MERLIN-CZ_grapheme_hunspell_noReranking_numCand_" + num_candidates_per_method,
				"grapheme", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false);
		runErrorCorrection_noReranking("MERLIN-CZ_phoneme_hunspell_noReranking_numCand_" + num_candidates_per_method,
				"phoneme", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false);
		runErrorCorrection_noReranking("MERLIN-CZ_keyboard_hunspell_noReranking_numCand_" + num_candidates_per_method,
				"keyboard", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false);
		runErrorCorrection_noReranking("MERLIN-CZ_full_hunspell_noReranking_numCand_" + num_candidates_per_method,
				"full", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false);
	}

	private static void runMerlinDE_noReranking(int num_candidates_per_method) throws UIMAException, IOException {

		String lang = "de";
		String path = "src/main/resources/corpora/merlin-DE_spelling.xml";
		String dict = "src/main/resources/dictionaries/hunspell_dict_de.txt";
		String dict_phon = "src/main/resources/dictionaries/hunspell_dict_de_phoneme_map.txt";
		String keyboard_distances = "src/main/resources/matrixes/keyboardDistance_DE-manual.txt";

		runErrorCorrection_noReranking(
				"MERLIN-DE_missingSpaces_hunspell_noReranking_numCand_" + num_candidates_per_method, "missing_spaces",
				lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false);
		runErrorCorrection_noReranking("MERLIN-DE_grapheme_hunspell_noReranking_numCand_" + num_candidates_per_method,
				"grapheme", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false);
		runErrorCorrection_noReranking("MERLIN-DE_phoneme_hunspell_noReranking_numCand_" + num_candidates_per_method,
				"phoneme", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false);
		runErrorCorrection_noReranking("MERLIN-DE_keyboard_hunspell_noReranking_numCand_" + num_candidates_per_method,
				"keyboard", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false);
		runErrorCorrection_noReranking("MERLIN-DE_full_hunspell_noReranking_numCand_" + num_candidates_per_method,
				"full", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false);
	}

	private static void runSkaLa_noReranking(int num_candidates_per_method) throws UIMAException, IOException {

		String lang = "de";
		String path = "src/main/resources/corpora/skala_spelling.xml";
		String dict = "src/main/resources/dictionaries/hunspell_dict_de.txt";
		String dict_phon = "src/main/resources/dictionaries/hunspell_dict_de_phoneme_map.txt";
		String keyboard_distances = "src/main/resources/matrixes/keyboardDistance_DE-manual.txt";

		runErrorCorrection_noReranking("SkaLa_missingSpaces_hunspell_noReranking_numCand_" + num_candidates_per_method,
				"missing_spaces", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false);
		runErrorCorrection_noReranking("SkaLa_grapheme_hunspell_noReranking_numCand_" + num_candidates_per_method,
				"grapheme", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false);
		runErrorCorrection_noReranking("SkaLa_phoneme_hunspell_noReranking_numCand_" + num_candidates_per_method,
				"phoneme", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false);
		runErrorCorrection_noReranking("SkaLa_keyboard_hunspell_web1t_noReranking_numCand_" + num_candidates_per_method,
				"keyboard", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false);
		runErrorCorrection_noReranking("SkaLa_full_hunspell_noReranking_numCand_" + num_candidates_per_method, "full",
				lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false);
	}

	private static void runErrorCorrection_noReranking(String config_name, String setting, String lang,
			String corpus_path, String dict_path, String phonetic_dict_path, String keyboard_distance_path,
			int num_candidates_per_method, boolean include_period) throws UIMAException, IOException {

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
		AnalysisEngineDescription anomalyReplacer = createEngineDescription(SpellingAnomalyReplacer.class,
				SpellingAnomalyReplacer.PARAM_TYPES_TO_COPY,
				new String[] { "de.unidue.ltl.spelling.types.ExtendedSpellingAnomaly" });
		AnalysisEngineDescription changeApplier = createEngineDescription(ApplyChanges.class);
		AnalysisEngineDescription correctionEvaluator = createEngineDescription(EvaluateErrorCorrection.class,
				EvaluateErrorCorrection.PARAM_CONFIG_NAME, config_name);

		if (setting.equals("full")) {
			SimplePipeline.runPipeline(reader, segmenter, lineBreakAnnotator, markSentenceBeginnings,
					generateRankGrapheme, generateRankPhoneme, generateRankKeyboard, generateRankMissingSpaces,
					anomalyReplacer, changeApplier, correctionEvaluator);
		}

		if (setting.equals("fullUni")) {
			SimplePipeline.runPipeline(reader, segmenter, lineBreakAnnotator, markSentenceBeginnings,
					generateRankGrapheme, generateRankPhoneme, generateRankKeyboard, anomalyReplacer, changeApplier,
					correctionEvaluator);
		}

		if (setting.equals("graphemePhoneme")) {
			SimplePipeline.runPipeline(reader, segmenter, lineBreakAnnotator, markSentenceBeginnings,
					generateRankGrapheme, generateRankPhoneme, anomalyReplacer, changeApplier, correctionEvaluator);
		}

		else if (setting.equals("grapheme")) {
			SimplePipeline.runPipeline(reader, segmenter, lineBreakAnnotator, markSentenceBeginnings,
					generateRankGrapheme, anomalyReplacer, changeApplier, correctionEvaluator);

		} else if (setting.equals("phoneme")) {
			SimplePipeline.runPipeline(reader, segmenter, lineBreakAnnotator, markSentenceBeginnings,
					generateRankPhoneme, anomalyReplacer, changeApplier, correctionEvaluator);

		} else if (setting.equals("keyboard")) {
			SimplePipeline.runPipeline(reader, segmenter, lineBreakAnnotator, markSentenceBeginnings,
					generateRankKeyboard, anomalyReplacer, changeApplier, correctionEvaluator);

		} else if (setting.equals("missing_spaces")) {
			SimplePipeline.runPipeline(reader, segmenter, lineBreakAnnotator, markSentenceBeginnings,
					generateRankMissingSpaces, anomalyReplacer, changeApplier, correctionEvaluator);
		}
	}

	public static CollectionReader getReader(String path, String language) throws ResourceInitializationException {

		return CollectionReaderFactory.createReader(SpellingReader.class, SpellingReader.PARAM_SOURCE_FILE, path,
				SpellingReader.PARAM_LANGUAGE_CODE, language);
	}
}