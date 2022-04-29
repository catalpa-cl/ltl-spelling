package experiments;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import constants.SpellingConstants;

public class EvalOracleCondition {

	public static void main(String[] args) throws IOException {

		runTOEFL_hunspell_web1tTrigrams();
		runTOEFL_hunspell_web1tUnigrams();
		runTOEFL_hunspell_subtlexUnigrams();
		runTOEFL_hunspell_noReranking();

		runCIta_hunspell_web1tTrigrams();
		runCIta_hunspell_web1tUnigrams();
		runCIta_hunspell_subtlexUnigrams();
		runCIta_hunspell_noReranking();

		runMerlinIT_hunspell_web1tTrigrams();
		runMerlinIT_hunspell_web1tUnigrams();
		runMerlinIT_hunspell_subtlexUnigrams();
		runMerlinIT_hunspell_noReranking();

		runMerlinCZ_hunspell_web1tTrigrams();
		runMerlinCZ_hunspell_web1tUnigrams();
		runMerlinCZ_hunspell_noReranking();

		runMerlinDE_hunspell_web1tTrigrams();
		runMerlinDE_childLex_web1tTrigrams();
		runMerlinDE_hunspell_web1tUnigrams();
		runMerlinDE_hunspell_subtlexUnigrams();
		runMerlinDE_hunspell_childLexUnigrams();
		runMerlinDE_hunspell_noReranking();

		runLitkey_hunspell_web1tTrigrams();
		runLitkey_childLex_web1tTrigrams();
		runLitkey_hunspell_web1tUnigrams();
		runLitkey_hunspell_subtlexUnigrams();
		runLitkey_hunspell_childLexUnigrams();
//		runLitkey_hunspell_noReranking();

		runSkaLa_hunspell_web1tTrigrams();
//		runSkaLa_childLex_web1tTrigrams();
//		runSkaLa_hunspell_web1tUnigrams();
//		runSkaLa_hunspell_subtlexUnigrams();
//		runSkaLa_hunspell_childLexUnigrams();
//		runSkaLa_hunspell_noReranking();

	}

	private static void runSkaLa_hunspell_web1tTrigrams() throws IOException {

		int numErros = 423;
		String configName = "SkaLa_hunspell_web1tTrigrams";
		String pathMissingSpaces = "src/main/resources/results/Error_Correction/ErrorCorrection_SkaLa_missingSpaces_hunspell_web1t_numCand_3_ngramSize_3/correct_numbered.txt";
		String pathGrapheme = "src/main/resources/results/Error_Correction/ErrorCorrection_SkaLa_grapheme_hunspell_web1t_numCand_3_ngramSize_3/correct_numbered.txt";
		String pathPhoneme = "src/main/resources/Error_Correction/ErrorCorrection_SkaLa_phoneme_hunspell_web1t_numCand_3_ngramSize_3/correct_numbered.txt";
		String pathKeyboard = "src/main/resources/Error_Correction/ErrorCorrection_SkaLa_keyboard_hunspell_web1t_numCand_3_ngramSize_3/correct_numbered.txt";
		String pathFull = "src/main/resources/results/Error_Correction/ErrorCorrection_SkaLa_full_hunspell_web1t_numCand_3_ngramSize_3/correct_numbered.txt";
		determineOverlaps(configName, numErros, pathMissingSpaces, pathGrapheme, pathPhoneme, pathKeyboard, pathFull);
	}

	private static void runTOEFL_hunspell_web1tTrigrams() throws IOException {

		int numErros = 6251;
		String configName = "TOEFL_hunspell_web1tTrigrams";
		String pathMissingSpaces = "src/main/resources/Error_Correction/ErrorCorrection_TOEF_missingSpaces_hunspell_web1t_numCand_3_ngramSize_3/correct_numbered.txt";
		String pathGrapheme = "src/main/resources/results/Error_Correction/ErrorCorrection_TOEFL_grapheme_hunspell_web1t_numCand_3_ngramSize_3/correct_numbered.txt";
		String pathPhoneme = "src/main/resources/results/Error_Correction/ErrorCorrection_TOEFL_phoneme_hunspell_web1t_numCand_3_ngramSize3/correct_numbered.txt";
		String pathKeyboard = "src/main/resources/results/Error_Correction/ErrorCorrection_TOEFL_keyboard_hunspell_web1t_numCand_3_ngramSize_3/correct_numbered.txt";
		String pathFull = "src/main/resources/results/ErrorCorrection/ErrorCorrection_TOEFL_full_hunspell_web1t_numCand_3_ngramSize_3/correct_numbered.txt";
		determineOverlaps(configName, numErros, pathMissingSpaces, pathGrapheme, pathPhoneme, pathKeyboard, pathFull);
	}

	private static void runTOEFL_hunspell_web1tUnigrams() throws IOException {

		int numErros = 6251;
		String configName = "TOEFL_hunspell_web1tUnigrams";
		String pathMissingSpaces = "src/main/resources/results/Error_Correction/ErrorCorrection_TOEFL_missingSpaces_hunspell_web1t_numCand_3_ngramSize_1/correct_numbered.txt";
		String pathGrapheme = "src/main/resources/results/Error_Correction/correct_numbered.txt";
		String pathPhoneme = "src/main/resources/results/Error_Correction/ErrorCorrection_TOEFL_phoneme_hunspell_web1t_numCand_3_ngramSize_1/correct_numbered.txt";
		String pathKeyboard = "src/main/resources/results/Error_Correction/ErrorCorrection_TOEFL_keyboard_hunspell_web1t_numCand_3_ngramSize_1/correct_numbered.txt";
		String pathFull = "src/main/resources/results/Error_Correction/ErrorCorrection_TOEFL_full_hunspell_web1t_numCand_3_ngramSize_1/correct_numbered.txt";
		determineOverlaps(configName, numErros, pathMissingSpaces, pathGrapheme, pathPhoneme, pathKeyboard, pathFull);
	}

	private static void runTOEFL_hunspell_noReranking() throws IOException {

		int numErros = 6251;
		String configName = "TOEFL_hunspell_noReranking";
		String pathMissingSpaces = "src/main/resources/results/Error_Correction/ErrorCorrection_TOEFL_missingSpaces_hunspell_noReranking_numCand_3/correct_numbered.txt";
		String pathGrapheme = "src/main/resources/results/Error_Correction/ErrorCorrection_TOEFL_grapheme_hunspell_noReranking_numCand_3/correct_numbered.txt";
		String pathPhoneme = "src/main/resources/results/Error_Correction/ErrorCorrection_TOEFL_phoneme_hunspell_noReranking_numCand_3/correct_numbered.txt";
		String pathKeyboard = "src/main/resources/results/Error_Correction/ErrorCorrection_TOEFL_keyboard_hunspell_noReranking_numCand_3/correct_numbered.txt";
		String pathFull = "src/main/resources/results/Error_Correction/ErrorCorrection_TOEFL_full_hunspell_noReranking_numCand_3/correct_numbered.txt";
		determineOverlaps(configName, numErros, pathMissingSpaces, pathGrapheme, pathPhoneme, pathKeyboard, pathFull);
	}

	private static void runTOEFL_hunspell_subtlexUnigrams() throws IOException {

		int numErros = 6251;
		String configName = "TOEFL_hunspell_subtlexUnigrams";
		String pathGrapheme = "src/main/resources/results/Error_Correction/ErrorCorrection_TOEFL_grapheme_hunspell_subtlex_numCand_3_unigrams/correct_numbered.txt";
		String pathPhoneme = "src/main/resources/results/Error_Correction/ErrorCorrection_TOEFL_phoneme_hunspell_subtlex_numCand_3_unigrams/correct_numbered.txt";
		String pathKeyboard = "src/main/resources/results/Error_Correction/ErrorCorrection_TOEFL_keyboard_hunspell_subtlex_numCand_3_unigrams/correct_numbered.txt";
		String pathFull = "src/main/resources/results/Error_Correction/ErrorCorrection_TOEFL_full_hunspell_subtlex_numCand_3_unigrams/correct_numbered.txt";
		determineOverlaps_unigram(configName, numErros, pathGrapheme, pathPhoneme, pathKeyboard, pathFull);
	}

	private static void runCIta_hunspell_web1tTrigrams() throws IOException {

		int numErros = 1431;
		String configName = "CIta_hunspell_web1tTrigrams";
		String pathMissingSpaces = "src/main/resources/results/Error_Correction/ErrorCorrection_CItA_missingSpaces_hunspell_web1t_numCand_3_ngramSize_3/correct_numbered.txt";
		String pathGrapheme = "src/main/resources/results/Error_Correction/ErrorCorrection_CItA_grapheme_hunspell_web1t_numCand_3_ngramSize_3/correct_numbered.txt";
		String pathPhoneme = "src/main/resources/Error_Correction/ErrorCorrection_CItA_phoneme_hunspell_web1t_numCand_3_ngramSize_3/correct_numbered.txt";
		String pathKeyboard = "src/main/resources/Error_Correction/ErrorCorrection_CItA_keyboard_hunspell_web1t_numCand_3_ngramSize_3/correct_numbered.txt";
		String pathFull = "src/main/resources/results/Error_Correction/ErrorCorrection_CItA_full_hunspell_web1t_numCand_3_ngramSize_3/correct_numbered.txt";
		determineOverlaps(configName, numErros, pathMissingSpaces, pathGrapheme, pathPhoneme, pathKeyboard, pathFull);
	}

	private static void runCIta_hunspell_noReranking() throws IOException {

		int numErros = 1431;
		String configName = "CIta_hunspell_noReranking";
		String pathMissingSpaces = "src/main/resources/results/Error_Correction/ErrorCorrection_CItA_missingSpaces_hunspell_noReranking_numCand_3/correct_numbered.txt";
		String pathGrapheme = "src/main/resources/results/Error_Correction/ErrorCorrection_CItA_grapheme_hunspell_noReranking_numCand_3/correct_numbered.txt";
		String pathPhoneme = "src/main/resources/results/Error_Correction/ErrorCorrection_CItA_phoneme_hunspell_noReranking_numCand_3/correct_numbered.txt";
		String pathKeyboard = "src/main/resources/results/Error_Correction/ErrorCorrection_CItA_keyboard_hunspell_noReranking_numCand_3/correct_numbered.txt";
		String pathFull = "src/main/resources/results/Error_Correction/ErrorCorrection_CItA_full_hunspell_noReranking_numCand_3/correct_numbered.txt";
		determineOverlaps(configName, numErros, pathMissingSpaces, pathGrapheme, pathPhoneme, pathKeyboard, pathFull);
	}

	private static void runCIta_hunspell_web1tUnigrams() throws IOException {

		int numErros = 1431;
		String configName = "CIta_hunspell_web1tUnigrams";
		String pathMissingSpaces = "src/main/resources/results/Error_Correction/ErrorCorrection_CItA_missingSpaces_hunspell_web1t_numCand3_ngramSize_1/correct_numbered.txt";
		String pathGrapheme = "src/main/resources/results/Error_Correction/ErrorCorrection_CItA_grapheme_hunspell_web1t_numCand_3_ngramSize_1/correct_numbered.txt";
		String pathPhoneme = "src/main/resources/results/Error_Correction/ErrorCorrection_CItA_phoneme_hunspell_web1t_numCand_3_ngramSize_1/correct_numbered.txt";
		String pathKeyboard = "src/main/resources/results/Error_Correction/ErrorCorrection_CItA_keyboard_hunspell_web1t_numCand_3_ngramSize_1/correct_numbered.txt";
		String pathFull = "src/main/resources/results/Error_Correction/ErrorCorrection_CItA_full_hunspell_web1t_numCand_3_ngram_1/correct_numbered.txt";
		determineOverlaps(configName, numErros, pathMissingSpaces, pathGrapheme, pathPhoneme, pathKeyboard, pathFull);
	}

	private static void runCIta_hunspell_subtlexUnigrams() throws IOException {

		int numErros = 1431;
		String configName = "CIta_hunspell_subtlexUnigrams";
		String pathGrapheme = "src/main/resources/results/Error_Correction/ErrorCorrection_CItA_grapheme_hunspell_subtlex_numCand_3_unigrams/correct_numbered.txt";
		String pathPhoneme = "src/main/resources/results/Error_Correction/ErrorCorrection_CItA_phoneme_hunspell_subtlex_numCand_3_unigrams/correct_numbered.txt";
		String pathKeyboard = "src/main/resources/results/Error_Correction/ErrorCorrection_CItA_keyboard_hunspell_subtlex_numCand_3_unigrams/correct_numbered.txt";
		String pathFull = "src/main/resources/results/Error_Correction/ErrorCorrection_CItA_full_hunspell_subtlex_numCand_3_unigrams/correct_numbered.txt";
		determineOverlaps_unigram(configName, numErros, pathGrapheme, pathPhoneme, pathKeyboard, pathFull);
	}

	private static void runMerlinIT_hunspell_web1tTrigrams() throws IOException {

		int numErros = 2137;
		String configName = "MERLIN-IT_hunspell_web1tTrigrams";
		String pathMissingSpaces = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-IT_missingSpaces_hunspell_web1t_numCand_3_ngramSize_3/correct_numbered.txt";
		String pathGrapheme = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-IT_grapheme_hunspell_web1t_numCand_3_ngramSize_3/correct_numbered.txt";
		String pathPhoneme = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-IT_phoneme_hunspell_web1t_numCand_3_ngramSize_3/correct_numbered.txt";
		String pathKeyboard = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-IT_keyboard_hunspell_web1t_numCand_3_ngramSize_3/correct_numbered.txt";
		String pathFull = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-IT_full_hunspell_web1t_numCand_3_ngramSize_3/correct_numbered.txt";
		determineOverlaps(configName, numErros, pathMissingSpaces, pathGrapheme, pathPhoneme, pathKeyboard, pathFull);
	}

	private static void runMerlinIT_hunspell_web1tUnigrams() throws IOException {

		int numErros = 2137;
		String configName = "MERLIN-IT_hunspell_web1tUnigrams";
		String pathMissingSpaces = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-IT_missingSpaces_hunspell_web1t_numCand_3_ngramSize_1/correct_numbered.txt";
		String pathGrapheme = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-IT_grapheme_hunspell_web1t_numCand_3_ngramSize_1/correct_numbered.txt";
		String pathPhoneme = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-IT_phoneme_hunspell_web1t_numCand_3_ngramSize_1/correct_numbered.txt";
		String pathKeyboard = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-IT_keyboard_hunspell_web1t_numCand_3_ngramSize_1/correct_numbered.txt";
		String pathFull = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-IT_full_hunspell_web1t_numCand_3_ngramSize_1/correct_numbered.txt";
		determineOverlaps(configName, numErros, pathMissingSpaces, pathGrapheme, pathPhoneme, pathKeyboard, pathFull);
	}

	private static void runMerlinIT_hunspell_noReranking() throws IOException {

		int numErros = 2137;
		String configName = "MERLIN-IT_hunspell_noReranking";
		String pathMissingSpaces = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-IT_missingSpaces_hunspell_noReranking_numCand_3/correct_numbered.txt";
		String pathGrapheme = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-IT_grapheme_hunspell_noReranking_numCand_3/correct_numbered.txt";
		String pathPhoneme = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-IT_phoneme_hunspell_noReranking_numCand_3/correct_numbered.txt";
		String pathKeyboard = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-IT_keyboard_hunspell_noReranking_numCand_3/correct_numbered.txt";
		String pathFull = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-IT_full_hunspell_noReranking_numCand_3/correct_numbered.txt";
		determineOverlaps(configName, numErros, pathMissingSpaces, pathGrapheme, pathPhoneme, pathKeyboard, pathFull);
	}

	private static void runMerlinIT_hunspell_subtlexUnigrams() throws IOException {

		int numErros = 2137;
		String configName = "MERLIN-IT_hunspell_subtlexUnigrams";
		String pathGrapheme = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-IT_grapheme_hunspell_subtlex_numCand_3_unigrams/correct_numbered.txt";
		String pathPhoneme = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-IT_phoneme_hunspell_subtlex_numCand_3_unigrams/correct_numbered.txt";
		String pathKeyboard = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-IT_keyboard_hunspell_subtlex_numCand_3_unigrams/correct_numbered.txt";
		String pathFull = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLN-IT_full_hunspell_subtlex_numCand_3_unigrams/correct_numbered.txt";
		determineOverlaps_unigram(configName, numErros, pathGrapheme, pathPhoneme, pathKeyboard, pathFull);
	}

	private static void runMerlinCZ_hunspell_web1tTrigrams() throws IOException {

		int numErros = 4807;
		String configName = "MERLIN-CZ_hunspell_web1tTrigrams";
		String pathMissingSpaces = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-CZ_missingSpaces_hunspell_web1t_numCand_3_ngramSize_3/correct_numbered.txt";
		String pathGrapheme = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-CZ_grapheme_hunspell_web1t_numCand_3_ngramSize_3/correct_numbered.txt";
		String pathPhoneme = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-CZ_phoneme_hunspell_web1t_numCand_3_ngramSize_3/correct_numbered.txt";
		String pathKeyboard = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-CZ_keyboard_hunspell_web1t_numCand_3_ngramSize_3/correct_numbered.txt";
		String pathFull = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-CZ_full_hunspell_web1t_numCand_3_ngramSize_3/correct_numbered.txt";
		determineOverlaps(configName, numErros, pathMissingSpaces, pathGrapheme, pathPhoneme, pathKeyboard, pathFull);
	}

	private static void runMerlinCZ_hunspell_web1tUnigrams() throws IOException {

		int numErros = 4807;
		String configName = "MERLIN-CZ_hunspell_web1tUnigrams";
		String pathMissingSpaces = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-CZ_missingSpaces_hunspell_web1t_numCand_3_ngramSize_1/correct_numbered.txt";
		String pathGrapheme = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-CZ_grapheme_hunspell_web1t_numCand_3_ngramSize_1/correct_numbered.txt";
		String pathPhoneme = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-CZ_phoneme_hunspell_web1t_numCand_3_ngramSize_1/correct_numbered.txt";
		String pathKeyboard = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-CZ_keyboard_hunspell_web1t_numCand_3_ngramSize_1/correct_numbered.txt";
		String pathFull = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-CZ_full_hunspell_web1t_numCand_3_ngramSize_1/correct_numbered.txt";
		determineOverlaps(configName, numErros, pathMissingSpaces, pathGrapheme, pathPhoneme, pathKeyboard, pathFull);
	}

	private static void runMerlinCZ_hunspell_noReranking() throws IOException {

		int numErros = 4807;
		String configName = "MERLIN-CZ_hunspell_noReranking";
		String pathMissingSpaces = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-CZ_missingSpaces_hunspell_noReranking_numCand_3/correct_numbered.txt";
		String pathGrapheme = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-CZ_grapheme_hunspell_noReranking_numCand_3/correct_numbered.txt";
		String pathPhoneme = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-CZ_phoneme_hunspell_noReranking_numCand_3/correct_numbered.txt";
		String pathKeyboard = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-CZ_keyboard_hunspell_noReranking_numCand_3/correct_numbered.txt";
		String pathFull = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-CZ_full_hunspell_noReranking_numCand_3/correct_numbered.txt";
		determineOverlaps(configName, numErros, pathMissingSpaces, pathGrapheme, pathPhoneme, pathKeyboard, pathFull);
	}

	private static void runMerlinDE_hunspell_web1tTrigrams() throws IOException {

		int numErros = 5366;
		String configName = "MERLIN-DE_hunspell_web1tTrigrams";
		String pathMissingSpaces = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-DE_missingSpaces_hunspell_web1t_numCand_3_ngramSize_3/correct_numbered.txt";
		String pathGrapheme = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-DE_grapheme_hunspell_web1t_numCand_3_ngramSize_3/correct_numbered.txt";
		String pathPhoneme = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-DE_phoneme_hunspell_web1t_numCand_3_ngramSize_3/correct_numbered.txt";
		String pathKeyboard = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-DE_keyboard_hunspell_web1t_numCand_3_ngramSize_3/correct_numbered.txt";
		String pathFull = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-DE_full_hunspell_web1t_numCand_3_ngramSize_3/correct_numbered.txt";
		determineOverlaps(configName, numErros, pathMissingSpaces, pathGrapheme, pathPhoneme, pathKeyboard, pathFull);
	}

	private static void runMerlinDE_hunspell_web1tUnigrams() throws IOException {

		int numErros = 5366;
		String configName = "MERLIN-DE_hunspell_web1tUnigrams";
		String pathMissingSpaces = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-DE_missingSpaces_hunspell_web1t_numCand_3_ngramSize_1/correct_numbered.txt";
		String pathGrapheme = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-DE_grapheme_hunspell_web1t_numCand_3_ngramSize_1/correct_numbered.txt";
		String pathPhoneme = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-DE_phoneme_hunspell_web1t_numCand_3_ngramSize_1/correct_numbered.txt";
		String pathKeyboard = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-DE_keyboard_hunspell_web1t_numCand_3_ngramSize_1/correct_numbered.txt";
		String pathFull = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-DE_full_hunspell_web1t_numCand_3_ngramSize_1/correct_numbered.txt";
		determineOverlaps(configName, numErros, pathMissingSpaces, pathGrapheme, pathPhoneme, pathKeyboard, pathFull);
	}

	private static void runMerlinDE_hunspell_noReranking() throws IOException {

		int numErros = 5366;
		String configName = "MERLIN-DE_hunspell_noReranking";
		String pathMissingSpaces = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-DE_missingSpaces_hunspell_noReranking_numCand_3/correct_numbered.txt";
		String pathGrapheme = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-DE_grapheme_hunspell_noReranking_numCand_3/correct_numbered.txt";
		String pathPhoneme = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-DE_phoneme_hunspell_noReranking_numCand_3/correct_numbered.txt";
		String pathKeyboard = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-DE_keyboard_hunspell_noReranking_numCand_3/correct_numbered.txt";
		String pathFull = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-DE_full_hunspell_noReranking_numCand_3/correct_numbered.txt";
		determineOverlaps(configName, numErros, pathMissingSpaces, pathGrapheme, pathPhoneme, pathKeyboard, pathFull);
	}

	private static void runMerlinDE_hunspell_childLexUnigrams() throws IOException {

		int numErros = 5366;
		String configName = "MERLIN-DE_hunspell_childLexUnigrams";
		String pathGrapheme = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-DE_grapheme_hunspell_childLex_numCand_3_unigrams/correct_numbered.txt";
		String pathPhoneme = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-DE_phoneme_hunspell_childLex_numCand_3_unigrams/correct_numbered.txt";
		String pathKeyboard = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-DE_keyboard_hunspell_childLex_numCand_3_unigrams/correct_numbered.txt";
		String pathFull = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-DE_full_hunspell_childLex_numCand_3_unigrams/correct_numbered.txt";
		determineOverlaps_unigram(configName, numErros, pathGrapheme, pathPhoneme, pathKeyboard, pathFull);
	}

	private static void runMerlinDE_hunspell_subtlexUnigrams() throws IOException {

		int numErros = 5366;
		String configName = "MERLIN-DE_hunspell_subtlexUnigrams";
		String pathGrapheme = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-DE_grapheme_hunspell_subtlex_numCand_3_unigrams/correct_numbered.txt";
		String pathPhoneme = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-DE_phoneme_hunspell_subtlex_numCand_3_unigrams/correct_numbered.txt";
		String pathKeyboard = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-DE_keyboard_hunspell_subtlex_numCand_3_unigrams/correct_numbered.txt";
		String pathFull = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-DE_full_hunspell_subtlex_numCand_3_unigrams/correct_numbered.txt";
		determineOverlaps_unigram(configName, numErros, pathGrapheme, pathPhoneme, pathKeyboard, pathFull);
	}

	private static void runMerlinDE_childLex_web1tTrigrams() throws IOException {

		int numErros = 5366;
		String configName = "MERLIN-DE_childLex_web1tTrigrams";
		String pathMissingSpaces = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-DE_missingSpaces_childLex_web1t_numCand_3_ngramSize_3/correct_numbered.txt";
		String pathGrapheme = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-DE_grapheme_childLex_web1t_numCand_3_ngramSize_3/correct_numbered.txt";
		String pathPhoneme = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-DE_phoneme_childLex_web1t_numCand_3_ngramSize_3/correct_numbered.txt";
		String pathKeyboard = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-DE_keyboard_childLex_web1t_numCand_3_ngramSize_3/correct_numbered.txt";
		String pathFull = "src/main/resources/results/Error_Correction/ErrorCorrection_MERLIN-DE_full_childLex_web1t_numCand_3_ngramSize_3/correct_numbered.txt";
		determineOverlaps(configName, numErros, pathMissingSpaces, pathGrapheme, pathPhoneme, pathKeyboard, pathFull);
	}

	private static void runLitkey_hunspell_web1tTrigrams() throws IOException {

		int numErros = 38698;
		String configName = "Litkey_hunspell_web1tTrigrams";
		String pathMissingSpaces = "src/main/resources/results/Error_Correction/ErrorCorrection_Litkey_missingSpaces_hunspell_web1t_numCand_3_ngramSize_3/correct_numbered.txt";
		String pathGrapheme = "src/main/resources/results/Error_Correction/ErrorCorrection_Litkey_grapheme_hunspell_web1t_numCand_3_ngramSize_3/correct_numbered.txt";
		String pathPhoneme = "src/main/resources/results/Error_Correction/ErrorCorrection_Litkey_phoneme_hunspell_web1t_numCand_3_ngramSize_3/correct_numbered.txt";
		String pathKeyboard = "src/main/resources/results/Error_Correction/ErrorCorrection_Litkey_keyboard_hunspell_web1t_numCand_3_ngramSize_3/correct_numbered.txt";
		String pathFull = "src/main/resources/results/Error_Correction/ErrorCorrection_Litkey_full_hunspell_web1t_numCand_3_ngramSize_3/correct_numbered.txt";
		determineOverlaps(configName, numErros, pathMissingSpaces, pathGrapheme, pathPhoneme, pathKeyboard, pathFull);
	}

	private static void runLitkey_childLex_web1tTrigrams() throws IOException {

		int numErros = 38698;
		String configName = "Litkey_childLex_web1tTrigrams";
		String pathMissingSpaces = "src/main/resources/results/Error_Correction/ErrorCorrection_Litkey_missingSpaces_childLex_web1t_numCand_3_ngramSize_3/correct_numbered.txt";
		String pathGrapheme = "src/main/resources/results/Error_Correction/ErrorCorrection_Litkey_grapheme_childLex_web1t_numCand_3_ngramSize_3/correct_numbered.txt";
		String pathPhoneme = "src/main/resources/results/Error_Correction/ErrorCorrection_Litkey_phoneme_childLex_web1t_numCand_3_ngramSize_3/correct_numbered.txt";
		String pathKeyboard = "src/main/resources/results/Error_Correction/ErrorCorrection_Litkey_keyboard_childLex_web1t_numCand_3_ngramSize_3/correct_numbered.txt";
		String pathFull = "src/main/resources/results/Error_Correction/ErrorCorrection_Litkey_full_childLex_web1t_numCand_3_ngramSize_3/correct_numbered.txt";
		determineOverlaps(configName, numErros, pathMissingSpaces, pathGrapheme, pathPhoneme, pathKeyboard, pathFull);
	}

	private static void runLitkey_hunspell_web1tUnigrams() throws IOException {

		int numErros = 38698;
		String configName = "Litkey_hunspell_web1tUnigrams";
		String pathMissingSpaces = "src/main/resources/results/Error_Correction/ErrorCorrection_Litkey_missingSpaces_hunspell_web1t_numCand_3_ngramSize_1/correct_numbered.txt";
		String pathGrapheme = "src/main/resources/results/Error_Correction/ErrorCorrection_Litkey_grapheme_hunspell_web1t_numCand_3_ngramSize_1/correct_numbered.txt";
		String pathPhoneme = "src/main/resources/results/Error_Correction/ErrorCorrection_Litkey_phoneme_hunspell_web1t_numCand_3_ngramSize_1/correct_numbered.txt";
		String pathKeyboard = "src/main/resources/results/Error_Correction/ErrorCorrection_Litkey_keyboard_hunspell_web1t_numCand_3_ngramSize_1/correct_numbered.txt";
		String pathFull = "src/main/resources/results/Error_Correction/ErrorCorrection_Litkey_full_hunspell_web1t_numCand_3_ngramSize_1/correct_numbered.txt";
		determineOverlaps(configName, numErros, pathMissingSpaces, pathGrapheme, pathPhoneme, pathKeyboard, pathFull);
	}

	private static void runLitkey_hunspell_childLexUnigrams() throws IOException {

		int numErros = 38698;
		String configName = "Litkey_hunspell_childLexUnigrams";
		String pathGrapheme = "src/main/resources/results/Error_Correction/ErrorCorrection_Litkey_grapheme_hunspell_childLex_numCand_3_unigrams/correct_numbered.txt";
		String pathPhoneme = "src/main/resources/results/Error_Correction/ErrorCorrection_Litkey_phoneme_hunspell_childLex_numCand_3_unigrams/correct_numbered.txt";
		String pathKeyboard = "src/main/resources/results/Error_Correction/ErrorCorrection_Litkey_keyboard_hunspell_childLex_numCand_3_unigrams/correct_numbered.txt";
		String pathFull = "src/main/resources/results/Error_Correction/ErrorCorrection_Litkey_full_hunspell_childLex_numCand_3_unigrams/correct_numbered.txt";
		determineOverlaps_unigram(configName, numErros, pathGrapheme, pathPhoneme, pathKeyboard, pathFull);
	}

	private static void runLitkey_hunspell_subtlexUnigrams() throws IOException {

		int numErros = 38698;
		String configName = "Litkey_hunspell_subtlexUnigrams";
		String pathGrapheme = "src/main/resources/results/Error_Correction/ErrorCorrection_Litkey_grapheme_hunspell_subtlex_numCand_3_unigrams/correct_numbered.txt";
		String pathPhoneme = "src/main/resources/results/Error_Correction/ErrorCorrection_Litkey_phoneme_hunspell_subtlex_numCand_3_unigrams/correct_numbered.txt";
		String pathKeyboard = "src/main/resources/results/Error_Correction/ErrorCorrection_Litkey_keyboard_hunspell_subtlex_numCand_3_unigrams/correct_numbered.txt";
		String pathFull = "src/main/resources/results/Error_Correction/Litkey_hunspell_subtlexUnigrams/ErrorCorrection_Litkey_full_hunspell_subtlex_numCand_3_unigrams/correct_numbered.txt";
		determineOverlaps_unigram(configName, numErros, pathGrapheme, pathPhoneme, pathKeyboard, pathFull);
	}

	private static void determineOverlaps(String configName, int numErrors, String pathMissingSpaces,
			String pathGrapheme, String pathPhoneme, String pathKeyboard, String pathFull) throws IOException {

		String eval_dir = SpellingConstants.EVALUATION_DATA_PATH + "Overlaps";
		File dir = new File(eval_dir);
		dir.mkdir();

		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(eval_dir + "/" + configName + ".txt")));

		Set<String> setMissingSpaces = new HashSet<String>();
		Set<String> setGrapheme = new HashSet<String>();
		Set<String> setPhoneme = new HashSet<String>();
		Set<String> setKeyboard = new HashSet<String>();
		Set<String> setFull = new HashSet<String>();

		Map<String, Double> mapMissingSpaces = new HashMap<String, Double>();
		Map<String, Double> mapGrapheme = new HashMap<String, Double>();
		Map<String, Double> mapPhoneme = new HashMap<String, Double>();
		Map<String, Double> mapKeyboard = new HashMap<String, Double>();
//		Map<String, Double> mapFull = new HashMap<String, Double>();

		BufferedReader br = new BufferedReader(new FileReader(new File(pathMissingSpaces)));
		while (br.ready()) {
			String line = br.readLine();
			String[] parts = line.split("\t");
//			setMissingSpaces.add(parts[0]);
			setMissingSpaces.add(parts[0] + "\t" + parts[2] + "\t" + parts[3]);
			mapMissingSpaces.put(parts[0], Double.parseDouble(parts[1]));
		}
		br.close();

		br = new BufferedReader(new FileReader(new File(pathGrapheme)));
		while (br.ready()) {
			String line = br.readLine();
			String[] parts = line.split("\t");
//			setGrapheme.add(parts[0]);
			setGrapheme.add(parts[0] + "\t" + parts[2] + "\t" + parts[3]);
			mapGrapheme.put(parts[0], Double.parseDouble(parts[1]));
		}
		br.close();

		br = new BufferedReader(new FileReader(new File(pathPhoneme)));
		while (br.ready()) {
			String line = br.readLine();
			String[] parts = line.split("\t");
//			setPhoneme.add(parts[0]);
			setPhoneme.add(parts[0] + "\t" + parts[2] + "\t" + parts[3]);
			mapPhoneme.put(parts[0], Double.parseDouble(parts[1]));
		}
		br.close();

		br = new BufferedReader(new FileReader(new File(pathKeyboard)));
		while (br.ready()) {
			String line = br.readLine();
			String[] parts = line.split("\t");
//			setKeyboard.add(parts[0]);
			setKeyboard.add(parts[0] + "\t" + parts[2] + "\t" + parts[3]);
			mapKeyboard.put(parts[0], Double.parseDouble(parts[1]));
		}
		br.close();

		br = new BufferedReader(new FileReader(new File(pathFull)));
		while (br.ready()) {
			String line = br.readLine();
			String[] parts = line.split("\t");
//			setFull.add(parts[0]);
			setFull.add(parts[0] + "\t" + parts[1] + "\t" + parts[2]);
//			mapFull.put(parts[0], Double.parseDouble(parts[1]));
		}
		br.close();

		if (!passesSanityCheck(numErrors, setMissingSpaces, setGrapheme, setPhoneme, setKeyboard, setFull)) {
			System.err.println("Correction candidate sets are misaligned; did not pass sanity check!");
			System.exit(0);
		}

		// Oracle Condition
		Set<String> oracle = new HashSet<String>();
		oracle.addAll(setMissingSpaces);
		oracle.addAll(setGrapheme);
		oracle.addAll(setPhoneme);
		oracle.addAll(setKeyboard);

		Map<String, Double> oracleMap = new HashMap<String, Double>();
		oracleMap.putAll(mapGrapheme);

		for (String key : mapMissingSpaces.keySet()) {
			Double result = oracleMap.getOrDefault(key, -1.0);
			if (mapMissingSpaces.get(key) > result) {
				oracleMap.put(key, mapMissingSpaces.get(key));
			}
		}
		for (String key : mapPhoneme.keySet()) {
			Double result = oracleMap.getOrDefault(key, -1.0);
			if (mapPhoneme.get(key) > result) {
				oracleMap.put(key, mapPhoneme.get(key));
			}
		}
		for (String key : mapKeyboard.keySet()) {
			Double result = oracleMap.getOrDefault(key, -1.0);
			if (mapKeyboard.get(key) > result) {
				oracleMap.put(key, mapKeyboard.get(key));
			}
		}

		Double oracleSumIncludingWeights = 0.0;
		for (String key : oracleMap.keySet()) {
			oracleSumIncludingWeights += oracleMap.get(key);
		}

//		bw.write("Oracle condition corrects " + oracle.size() + " of " + numErrors + " total errors ("
//				+ ((oracle.size()*1.0) / (numErrors*1.0)) + ")");
//		bw.newLine();
		bw.write("Oracle condition corrects (including weighted candidates) " + oracleSumIncludingWeights + " of "
				+ numErrors + " total errors (" + ((oracleSumIncludingWeights) / (numErrors * 1.0)) + ")\n\n");

		// Missing spaces - grapheme
		Integer[] missingSpacesGrapheme = getOverlaps(setMissingSpaces, setGrapheme);
		int overlapSpacesGrapheme = missingSpacesGrapheme[0];
		int uniqueSpaces_spacesGrapheme = missingSpacesGrapheme[1];
		int uniqueGrapheme_spacesGrapheme = missingSpacesGrapheme[2];

		// Missing spaces - phoneme
		Integer[] missingSpacesPhoneme = getOverlaps(setMissingSpaces, setPhoneme);
		int overlapSpacesPhoneme = missingSpacesPhoneme[0];
		int uniqueSpaces_spacesPhoneme = missingSpacesPhoneme[1];
		int uniquePhoneme_spacesPhoneme = missingSpacesPhoneme[2];

		// Missing spaces - keyboard
		Integer[] missingSpacesKeyboard = getOverlaps(setMissingSpaces, setKeyboard);
		int overlapSpacesKeyboard = missingSpacesKeyboard[0];
		int uniqueSpaces_spacesKeyboard = missingSpacesKeyboard[1];
		int uniqueKeyboard_spacesKeyboard = missingSpacesKeyboard[2];

		// Missing spaces - full
		Integer[] missingSpacesFull = getOverlaps(setMissingSpaces, setFull);
		int overlapSpacesFull = missingSpacesFull[0];
		int uniqueSpaces_spacesFull = missingSpacesFull[1];
		int uniqueFull_spacesFull = missingSpacesFull[2];

		// Grapheme - phoneme
		Integer[] graphemePhoneme = getOverlaps(setGrapheme, setPhoneme);
		int overlapGraphemePhoneme = graphemePhoneme[0];
		int uniqueGrapheme_graphemePhoneme = graphemePhoneme[1];
		int uniquePhoneme_graphemePhoneme = graphemePhoneme[2];

		// Grapheme - keyboard
		Integer[] graphemeKeyboard = getOverlaps(setGrapheme, setKeyboard);
		int overlapGraphemeKeyboard = graphemeKeyboard[0];
		int uniqueGrapheme_graphemeKeyboard = graphemeKeyboard[1];
		int uniqueKeyboard_graphemeKeyboard = graphemeKeyboard[2];

		Set<String> keyboardButNotPhoneme = new HashSet<String>();
		keyboardButNotPhoneme.addAll(setKeyboard);
		keyboardButNotPhoneme.removeAll(setGrapheme);
		for (String element : keyboardButNotPhoneme) {
			System.out.println(element);
		}

		// Grapheme - full
		Integer[] graphemeFull = getOverlaps(setGrapheme, setFull);
		int overlapGraphemeFull = graphemeFull[0];
		int uniqueGrapheme_graphemeFull = graphemeFull[1];
		int uniqueFull_graphemeFull = graphemeFull[2];

		// Phoneme - keyboard
		Integer[] phonemeKeyboard = getOverlaps(setPhoneme, setKeyboard);
		int overlapPhonemeKeyboard = phonemeKeyboard[0];
		int uniquePhoneme_phonemeKeyboard = phonemeKeyboard[1];
		int uniqueKeyboard_phonemeKeyboard = phonemeKeyboard[2];

		// Phoneme - full
		Integer[] phonemeFull = getOverlaps(setPhoneme, setFull);
		int overlapPhonemeFull = phonemeFull[0];
		int uniquePhoneme_phonemeFull = phonemeFull[1];
		int uniqueFull_phonemeFull = phonemeFull[2];

		// Keyboard - full
		Integer[] keyboardFull = getOverlaps(setKeyboard, setFull);
		int overlapKeyboardFull = keyboardFull[0];
		int uniqueKeyboard_keyboardFull = keyboardFull[1];
		int uniqueFull_keyboardFull = keyboardFull[2];

		bw.write(
				"Table cell entry pattern is: overlap (number_of_unique_entries_ROW, number_of_unique_entries_COLUMN)");
		bw.newLine();
		bw.newLine();
		bw.write("\tMissingSpaces\tGrapheme\tPhoneme\tKeyboard\tFull");
		bw.newLine();
		bw.write("MissingSpaces\t" + setMissingSpaces.size() + "\t" + overlapSpacesGrapheme + " ("
				+ uniqueSpaces_spacesGrapheme + ", " + uniqueGrapheme_spacesGrapheme + ")\t" + overlapSpacesPhoneme
				+ " (" + uniqueSpaces_spacesPhoneme + ", " + uniquePhoneme_spacesPhoneme + ")\t" + overlapSpacesKeyboard
				+ " (" + uniqueSpaces_spacesKeyboard + ", " + uniqueKeyboard_spacesKeyboard + ")\t" + overlapSpacesFull
				+ " (" + uniqueSpaces_spacesFull + ", " + uniqueFull_spacesFull + ")");
		bw.newLine();
		bw.write("Grapheme\t\t" + setGrapheme.size() + "\t" + overlapGraphemePhoneme + " ("
				+ uniqueGrapheme_graphemePhoneme + ", " + uniquePhoneme_graphemePhoneme + ")\t"
				+ overlapGraphemeKeyboard + " (" + uniqueGrapheme_graphemeKeyboard + ", "
				+ uniqueKeyboard_graphemeKeyboard + ")\t" + overlapGraphemeFull + " (" + uniqueGrapheme_graphemeFull
				+ ", " + uniqueFull_graphemeFull + ")");
		bw.newLine();
		bw.write("Phoneme\t\t\t" + setPhoneme.size() + "\t" + overlapPhonemeKeyboard + " ("
				+ uniquePhoneme_phonemeKeyboard + ", " + uniqueKeyboard_phonemeKeyboard + ")\t" + overlapPhonemeFull
				+ " (" + uniquePhoneme_phonemeFull + ", " + uniqueFull_phonemeFull + ")");
		bw.newLine();
		bw.write("Keyboard\t\t\t\t" + setKeyboard.size() + "\t" + overlapKeyboardFull + " ("
				+ uniqueKeyboard_keyboardFull + ", " + uniqueFull_keyboardFull + ")");
		bw.newLine();
		bw.newLine();
		bw.write("Set sizes (to confirm): ");
		bw.newLine();
		bw.write("Missing spaces:\t" + setMissingSpaces.size());
		bw.newLine();
		bw.write("Grapheme:\t" + setGrapheme.size());
		bw.newLine();
		bw.write("Phoneme:\t" + setPhoneme.size());
		bw.newLine();
		bw.write("Keyboard:\t" + setKeyboard.size());
		bw.newLine();
		bw.write("Full:\t" + setFull.size());
		bw.newLine();

		bw.close();
	}

	private static void determineOverlaps_unigram(String configName, int numErrors, String pathGrapheme,
			String pathPhoneme, String pathKeyboard, String pathFull) throws IOException {

		String eval_dir = SpellingConstants.EVALUATION_DATA_PATH + "Overlaps";
		File dir = new File(eval_dir);
		dir.mkdir();

		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(eval_dir + "/" + configName + ".txt")));

		Set<String> setGrapheme = new HashSet<String>();
		Set<String> setPhoneme = new HashSet<String>();
		Set<String> setKeyboard = new HashSet<String>();
		Set<String> setFull = new HashSet<String>();

		BufferedReader br = new BufferedReader(new FileReader(new File(pathGrapheme)));
		while (br.ready()) {
			String line = br.readLine();
//			String[] parts = line.split("\t");
//			setGrapheme.add(parts[0]);
			setGrapheme.add(line);
		}
		br.close();

		br = new BufferedReader(new FileReader(new File(pathPhoneme)));
		while (br.ready()) {
			String line = br.readLine();
//			String[] parts = line.split("\t");
//			setPhoneme.add(parts[0]);
			setPhoneme.add(line);
		}
		br.close();

		br = new BufferedReader(new FileReader(new File(pathKeyboard)));
		while (br.ready()) {
			String line = br.readLine();
//			String[] parts = line.split("\t");
//			setKeyboard.add(parts[0]);
			setKeyboard.add(line);
		}
		br.close();

		br = new BufferedReader(new FileReader(new File(pathFull)));
		while (br.ready()) {
			String line = br.readLine();
//			String[] parts = line.split("\t");
//			setFull.add(parts[0]);
			setFull.add(line);
		}
		br.close();

		if (!passesSanityCheck(numErrors, new HashSet<String>(), setGrapheme, setPhoneme, setKeyboard, setFull)) {
			System.err.println("Correction candidate sets are misaligned; did not pass sanity check!");
			System.exit(0);
		}

		// Oracle Condition
		Set<String> oracle = new HashSet<String>();
		oracle.addAll(setGrapheme);
		oracle.addAll(setPhoneme);
		oracle.addAll(setKeyboard);

		bw.write("Oracle condition corrects " + oracle.size() + " of " + numErrors + " total errors ("
				+ ((oracle.size() * 1.0) / (numErrors * 1.0)) + ")");
		bw.newLine();

		// Grapheme - phoneme
		Integer[] graphemePhoneme = getOverlaps(setGrapheme, setPhoneme);
		int overlapGraphemePhoneme = graphemePhoneme[0];
		int uniqueGrapheme_graphemePhoneme = graphemePhoneme[1];
		int uniquePhoneme_graphemePhoneme = graphemePhoneme[2];

		// Grapheme - keyboard
		Integer[] graphemeKeyboard = getOverlaps(setGrapheme, setKeyboard);
		int overlapGraphemeKeyboard = graphemeKeyboard[0];
		int uniqueGrapheme_graphemeKeyboard = graphemeKeyboard[1];
		int uniqueKeyboard_graphemeKeyboard = graphemeKeyboard[2];

		Set<String> keyboardButNotPhoneme = new HashSet<String>();
		keyboardButNotPhoneme.addAll(setKeyboard);
		keyboardButNotPhoneme.removeAll(setGrapheme);
		for (String element : keyboardButNotPhoneme) {
			System.out.println(element);
		}

		// Grapheme - full
		Integer[] graphemeFull = getOverlaps(setGrapheme, setFull);
		int overlapGraphemeFull = graphemeFull[0];
		int uniqueGrapheme_graphemeFull = graphemeFull[1];
		int uniqueFull_graphemeFull = graphemeFull[2];

		// Phoneme - keyboard
		Integer[] phonemeKeyboard = getOverlaps(setPhoneme, setKeyboard);
		int overlapPhonemeKeyboard = phonemeKeyboard[0];
		int uniquePhoneme_phonemeKeyboard = phonemeKeyboard[1];
		int uniqueKeyboard_phonemeKeyboard = phonemeKeyboard[2];

		// Phoneme - full
		Integer[] phonemeFull = getOverlaps(setPhoneme, setFull);
		int overlapPhonemeFull = phonemeFull[0];
		int uniquePhoneme_phonemeFull = phonemeFull[1];
		int uniqueFull_phonemeFull = phonemeFull[2];

		// Keyboard - full
		Integer[] keyboardFull = getOverlaps(setKeyboard, setFull);
		int overlapKeyboardFull = keyboardFull[0];
		int uniqueKeyboard_keyboardFull = keyboardFull[1];
		int uniqueFull_keyboardFull = keyboardFull[2];

		bw.write(
				"Table cell entry pattern is: overlap (number_of_unique_entries_ROW, number_of_unique_entries_COLUMN)");
		bw.newLine();
		bw.newLine();
		bw.write("\tGrapheme\tPhoneme\tKeyboard\tFull");
		bw.newLine();
		bw.write("Grapheme\t" + setGrapheme.size() + "\t" + overlapGraphemePhoneme + " ("
				+ uniqueGrapheme_graphemePhoneme + ", " + uniquePhoneme_graphemePhoneme + ")\t"
				+ overlapGraphemeKeyboard + " (" + uniqueGrapheme_graphemeKeyboard + ", "
				+ uniqueKeyboard_graphemeKeyboard + ")\t" + overlapGraphemeFull + " (" + uniqueGrapheme_graphemeFull
				+ ", " + uniqueFull_graphemeFull + ")");
		bw.newLine();
		bw.write("Phoneme\t\t" + setPhoneme.size() + "\t" + overlapPhonemeKeyboard + " ("
				+ uniquePhoneme_phonemeKeyboard + ", " + uniqueKeyboard_phonemeKeyboard + ")\t" + overlapPhonemeFull
				+ " (" + uniquePhoneme_phonemeFull + ", " + uniqueFull_phonemeFull + ")");
		bw.newLine();
		bw.write("Keyboard\t\t\t" + setKeyboard.size() + "\t" + overlapKeyboardFull + " (" + uniqueKeyboard_keyboardFull
				+ ", " + uniqueFull_keyboardFull + ")");
		bw.newLine();
		bw.newLine();
		bw.write("Set sizes (to confirm): ");
		bw.newLine();
		bw.write("Grapheme:\t" + setGrapheme.size());
		bw.newLine();
		bw.write("Phoneme:\t" + setPhoneme.size());
		bw.newLine();
		bw.write("Keyboard:\t" + setKeyboard.size());
		bw.newLine();
		bw.write("Full:\t" + setFull.size());
		bw.newLine();

		bw.close();
	}

	private static Integer[] getOverlaps(Set<String> a, Set<String> b) {

		Set<String> c = new HashSet<String>();
		c.addAll(a);
		c.retainAll(b);
		int overlapAB = c.size();
		c.clear();
		c.addAll(a);
		c.removeAll(b);
		int uniqueA = c.size();
		c.clear();
		c.addAll(b);
		c.removeAll(a);
		int uniqueB = c.size();

		Integer[] results = new Integer[] { overlapAB, uniqueA, uniqueB };
		return results;
	}

	private static boolean passesSanityCheck(int numErrors, Set<String> setMissingSpaces, Set<String> setGrapheme,
			Set<String> setPhoneme, Set<String> setKeyboard, Set<String> setFull) {

		Map<String, String> missingSpaces = getMap(setMissingSpaces);
		Map<String, String> grapheme = getMap(setGrapheme);
		Map<String, String> phoneme = getMap(setPhoneme);
		Map<String, String> keyboard = getMap(setKeyboard);
		Map<String, String> full = getMap(setFull);

		for (int i = 1; i <= numErrors; i++) {

			Set<String> valueSet = new HashSet<String>();
			if (missingSpaces.containsKey(i + "")) {
				valueSet.add(missingSpaces.get(i + ""));
			}
			if (grapheme.containsKey(i + "")) {
				valueSet.add(grapheme.get(i + ""));
			}
			if (phoneme.containsKey(i + "")) {
				valueSet.add(phoneme.get(i + ""));
			}
			if (keyboard.containsKey(i + "")) {
				valueSet.add(keyboard.get(i + ""));
			}
			if (full.containsKey(i + "")) {
				valueSet.add(full.get(i + ""));
			}

			if (valueSet.size() > 0
					&& !(Collections.frequency(valueSet, valueSet.iterator().next()) == valueSet.size())) {
				System.err.println("Not all methods list the same correction for error number " + i + "! " + valueSet);
				return false;
			}
		}

		return true;
	}

	private static Map<String, String> getMap(Set<String> set) {

		Map<String, String> result = new HashMap<String, String>();
		for (String entry : set) {
			String[] parts = entry.split("\t");
			result.put(parts[0], parts[1]
			// + " " + parts[2]
			);
		}
		return result;
	}

}
