package experiments;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apache.uima.UIMAException;
import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.cas.CAS;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.util.SimpleNamedResourceManager;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.metadata.impl.ResourceManagerConfiguration_impl;
import org.dkpro.core.api.frequency.util.ConditionalFrequencyDistribution;
import org.uimafit.factory.AnalysisEngineFactory;

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
import resources.CFDFrequencyCountProvider;
import utils.AnalysisEngineProvider;

public class ErrorCorrectionExperiments_unigramReranking {

	public static void main(String[] args) throws IOException, UIMAException {

		int num_candidates_per_method = 3;

		runLitkey_unigrams(num_candidates_per_method);
		runMerlinDE_unigrams(num_candidates_per_method);
		runSkaLa_unigrams(num_candidates_per_method);
		runCItA_unigrams(num_candidates_per_method);
		runMerlinIT_unigrams(num_candidates_per_method);
		runTOEFL_unigrams(num_candidates_per_method);

		num_candidates_per_method = 10;

		runLitkey_unigrams(num_candidates_per_method);
		runMerlinDE_unigrams(num_candidates_per_method);
		runSkaLa_unigrams(num_candidates_per_method);
		runCItA_unigrams(num_candidates_per_method);
		runMerlinIT_unigrams(num_candidates_per_method);
		runTOEFL_unigrams(num_candidates_per_method);

	}

	private static void runLitkey_unigrams(int num_candidates_per_method) throws UIMAException, IOException {

		String lang = "de";
		String path = "src/main/resources/corpora/litkey_spelling.xml";
		String dict = "/dictionaries/hunspell_dict_de.txt";
		String dict_phon = "/dictionaries/hunspell_dict_de_phoneme_map.txt";
		String keyboard_distances = "/matrixes/keyboardDistance_DE-manual.txt";

		String unigram_file = "src/main/resources/language_models/childLex/childLex_0.17.01.tsv";
		ConditionalFrequencyDistribution<Integer, String> cfd = new ConditionalFrequencyDistribution<Integer, String>();
		BufferedReader br = new BufferedReader(new FileReader(new File(unigram_file)));
		String firstLine = br.readLine();
		while (br.ready()) {
			String line = br.readLine();
			String[] entries = line.split("\t");
			String word = entries[1];
			word = word.replaceAll("^\"|\"$", "");
			String frequency = entries[13];
			cfd.addSample(1, word, Integer.parseInt(frequency));
		}
		br.close();

		// childLex
		runErrorCorrectionUnigrams(
				"Litkey_missingSpaces_hunspell_childLex_numCand_" + num_candidates_per_method + "_unigrams",
				"missing_spaces", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, true,
				cfd);
		runErrorCorrectionUnigrams(
				"Litkey_grapheme_hunspell_childLex_numCand_" + num_candidates_per_method + "_unigrams", "grapheme",
				lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, true, cfd);
		runErrorCorrectionUnigrams(
				"Litkey_keyboard_hunspell_childLex_numCand_" + num_candidates_per_method + "_unigrams", "keyboard",
				lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, true, cfd);
		runErrorCorrectionUnigrams(
				"Litkey_phoneme_hunspell_childLex_numCand_" + num_candidates_per_method + "_unigrams", "phoneme", lang,
				path, dict, dict_phon, keyboard_distances, num_candidates_per_method, true, cfd);
		runErrorCorrectionUnigrams(
				"Litkey_fullUni_hunspell_childLex_numCand_" + num_candidates_per_method + "_unigrams", "full", lang,
				path, dict, dict_phon, keyboard_distances, num_candidates_per_method, true, cfd);

		unigram_file = "src/main/resources/language_models/subtlex/subtlex_de.txt";
		cfd = new ConditionalFrequencyDistribution<Integer, String>();
		br = Files.newBufferedReader(Paths.get(unigram_file), StandardCharsets.ISO_8859_1);
		firstLine = br.readLine();
		while (br.ready()) {
			String line = br.readLine();
			String[] entries = line.split("\t");
			String word = entries[0];
			word = word.replaceAll("^\"|\"$", "");
			String frequency = entries[1];
//			System.out.println(word + " " + frequency);
			cfd.addSample(1, word, Integer.parseInt(frequency));
		}
		br.close();

		// subtlex
		runErrorCorrectionUnigrams(
				"Litkey_missingSpaces_hunspell_subtlex_numCand_" + num_candidates_per_method + "_unigrams",
				"missing_spaces", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, true,
				cfd);
		runErrorCorrectionUnigrams(
				"Litkey_grapheme_hunspell_subtlex_numCand_" + num_candidates_per_method + "_unigrams", "grapheme", lang,
				path, dict, dict_phon, keyboard_distances, num_candidates_per_method, true, cfd);
		runErrorCorrectionUnigrams(
				"Litkey_keyboard_hunspell_subtlex_numCand_" + num_candidates_per_method + "_unigrams", "keyboard", lang,
				path, dict, dict_phon, keyboard_distances, num_candidates_per_method, true, cfd);
		runErrorCorrectionUnigrams("Litkey_phoneme_hunspell_subtlex_numCand_" + num_candidates_per_method + "_unigrams",
				"phoneme", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, true, cfd);
		runErrorCorrectionUnigrams("Litkey_fullUni_hunspell_subtlex_numCand_" + num_candidates_per_method + "_unigrams",
				"full", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, true, cfd);
	}

	private static void runMerlinDE_unigrams(int num_candidates_per_method) throws UIMAException, IOException {

		String lang = "de";
		String path = "src/main/resources/corpora/merlin-DE_spelling.xml";
		String dict = "/dictionaries/hunspell_dict_de.txt";
		String dict_phon = "/dictionaries/hunspell_dict_de_phoneme_map.txt";
		String keyboard_distances = "/matrixes/keyboardDistance_DE-manual.txt";

		String unigram_file = "src/main/resources/language_models/childLex/childLex_0.17.01.tsv";
		ConditionalFrequencyDistribution<Integer, String> cfd = new ConditionalFrequencyDistribution<Integer, String>();
		BufferedReader br = new BufferedReader(new FileReader(new File(unigram_file)));
		String firstLine = br.readLine();
		while (br.ready()) {
			String line = br.readLine();
			String[] entries = line.split("\t");
			String word = entries[1];
			word = word.replaceAll("^\"|\"$", "");
			String frequency = entries[13];
			cfd.addSample(1, word, Integer.parseInt(frequency));
		}
		br.close();

		// childLex
		runErrorCorrectionUnigrams(
				"MERLIN-DE_missingSpaces_childLex_numCand_" + num_candidates_per_method + "_unigrams", "missing_spaces",
				lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false, cfd);
		runErrorCorrectionUnigrams(
				"MERLIN-DE_grapheme_hunspell_childLex_numCand_" + num_candidates_per_method + "_unigrams", "grapheme",
				lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false, cfd);
		runErrorCorrectionUnigrams(
				"MERLIN-DE_phoneme_hunspell_childLex_numCand_" + num_candidates_per_method + "_unigrams", "phoneme",
				lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false, cfd);
		runErrorCorrectionUnigrams(
				"MERLIN-DE_keyboard_hunspell_childLex_numCand_" + num_candidates_per_method + "_unigrams", "keyboard",
				lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false, cfd);
		runErrorCorrectionUnigrams(
				"MERLIN-DE_full_hunspell_childLex_numCand_" + num_candidates_per_method + "_unigrams", "full", lang,
				path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false, cfd);

		unigram_file = "src/main/resources/language_models/subtlex/subtlex_de.txt";
		cfd = new ConditionalFrequencyDistribution<Integer, String>();
		br = Files.newBufferedReader(Paths.get(unigram_file), StandardCharsets.ISO_8859_1);
		firstLine = br.readLine();
		while (br.ready()) {
			String line = br.readLine();
			String[] entries = line.split("\t");
			String word = entries[0];
			word = word.replaceAll("^\"|\"$", "");
			String frequency = entries[1];
//			System.out.println(word + " " + frequency);
			cfd.addSample(1, word, Integer.parseInt(frequency));
		}
		br.close();

		// subtlex
		runErrorCorrectionUnigrams("MERLIN-DE_missingSpaces_subtlex_numCand_" + num_candidates_per_method + "_unigrams",
				"missing_spaces", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false,
				cfd);
		runErrorCorrectionUnigrams(
				"MERLIN-DE_grapheme_hunspell_subtlex_numCand_" + num_candidates_per_method + "_unigrams", "grapheme",
				lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false, cfd);
		runErrorCorrectionUnigrams(
				"MERLIN-DE_phoneme_hunspell_subtlex_numCand_" + num_candidates_per_method + "_unigrams", "phoneme",
				lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false, cfd);
		runErrorCorrectionUnigrams(
				"MERLIN-DE_keyboard_hunspell_subtlex_numCand_" + num_candidates_per_method + "_unigrams", "keyboard",
				lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false, cfd);
		runErrorCorrectionUnigrams("MERLIN-DE_full_hunspell_subtlex_numCand_" + num_candidates_per_method + "_unigrams",
				"full", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false, cfd);
	}

	private static void runSkaLa_unigrams(int num_candidates_per_method) throws UIMAException, IOException {

		String lang = "de";
		String path = "src/main/resources/corpora/skala_spelling.xml";
		String dict = "/dictionaries/hunspell_dict_de.txt";
		String dict_phon = "/dictionaries/hunspell_dict_de_phoneme_map.txt";
		String keyboard_distances = "/matrixes/keyboardDistance_DE-manual.txt";

		String unigram_file = "src/main/resources/language_models/childLex/childLex_0.17.01.tsv";
		ConditionalFrequencyDistribution<Integer, String> cfd = new ConditionalFrequencyDistribution<Integer, String>();
		BufferedReader br = new BufferedReader(new FileReader(new File(unigram_file)));
		String firstLine = br.readLine();
		while (br.ready()) {
			String line = br.readLine();
			String[] entries = line.split("\t");
			String word = entries[1];
			word = word.replaceAll("^\"|\"$", "");
			String frequency = entries[13];
			cfd.addSample(1, word, Integer.parseInt(frequency));
		}
		br.close();

		// childLex
		runErrorCorrectionUnigrams(
				"SkaLa_missingSpaces_hunspell_childLex_numCand_" + num_candidates_per_method + "_unigrams",
				"missing_spaces", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false,
				cfd);
		runErrorCorrectionUnigrams(
				"SkaLa_grapheme_hunspell_childLex_numCand_" + num_candidates_per_method + "_unigrams", "grapheme", lang,
				path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false, cfd);
		runErrorCorrectionUnigrams("SkaLa_phoneme_hunspell_childLex_numCand_" + num_candidates_per_method + "_unigrams",
				"phoneme", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false, cfd);
		runErrorCorrectionUnigrams(
				"SkaLa_keyboard_hunspell_childLex_numCand_" + num_candidates_per_method + "_unigrams", "keyboard", lang,
				path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false, cfd);
		runErrorCorrectionUnigrams("SkaLa_full_hunspell_childLex_numCand_" + num_candidates_per_method + "_unigrams",
				"full", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false, cfd);

		unigram_file = "src/main/resources/language_models/subtlex/subtlex_de.txt";
		cfd = new ConditionalFrequencyDistribution<Integer, String>();
		br = Files.newBufferedReader(Paths.get(unigram_file), StandardCharsets.ISO_8859_1);
		firstLine = br.readLine();
		while (br.ready()) {
			String line = br.readLine();
			String[] entries = line.split("\t");
			String word = entries[0];
			word = word.replaceAll("^\"|\"$", "");
			String frequency = entries[1];
//			System.out.println(word + " " + frequency);
			cfd.addSample(1, word, Integer.parseInt(frequency));
		}
		br.close();

		// subtlex
		runErrorCorrectionUnigrams(
				"SkaLa_missingSpaces_hunspell_subtlex_numCand_" + num_candidates_per_method + "_unigrams",
				"missing_spaces", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false,
				cfd);
		runErrorCorrectionUnigrams("SkaLa_grapheme_hunspell_subtlex_numCand" + num_candidates_per_method + "_unigrams",
				"grapheme", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false, cfd);
		runErrorCorrectionUnigrams("SkaLa_phoneme_hunspell_subtlex_numCand_" + num_candidates_per_method + "_unigrams",
				"phoneme", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false, cfd);
		runErrorCorrectionUnigrams("SkaLa_keyboard_hunspell_subtlex_numCand_" + num_candidates_per_method + "_unigrams",
				"keyboard", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false, cfd);
		runErrorCorrectionUnigrams("SkaLa_full_hunspell_subtlex_numCand_" + num_candidates_per_method + "_unigrams",
				"full", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false, cfd);
	}

	private static void runCItA_unigrams(int num_candidates_per_method) throws UIMAException, IOException {

		String lang = "it";
		String path = "src/main/resources/corpora/cita_spelling.xml";
		String dict = "/dictionaries/hunspell_dict_it.txt";
		String dict_phon = "/dictionaries/hunspell_dict_it_phoneme_map.txt";
		String keyboard_distances = "/matrixes/keyboardDistance_IT-manual.txt";

		String unigram_file = "src/main/resources/language_models/subtlex/subtlex_it.tsv";
		ConditionalFrequencyDistribution<Integer, String> cfd = new ConditionalFrequencyDistribution<Integer, String>();
		BufferedReader br = Files.newBufferedReader(Paths.get(unigram_file), Charset.forName("UTF-8"));
		String firstLine = br.readLine();
		while (br.ready()) {
			String line = br.readLine();
//			System.out.println(line);
			String[] entries = line.split("\t");
			String word = entries[1];
			word = word.replaceAll("^\"|\"$", "");
			String frequency = entries[2];
//			System.out.println(word + " " + frequency);
			cfd.addSample(1, word, Integer.parseInt(frequency));
		}
		br.close();

		runErrorCorrectionUnigrams(
				"CItA_missingSpaces_hunspell_subtlex_numCand_" + num_candidates_per_method + "_unigrams",
				"missing_spaces", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false,
				cfd);
		runErrorCorrectionUnigrams("CItA_grapheme_hunspell_subtlex_numCand_" + num_candidates_per_method + "_unigrams",
				"grapheme", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false, cfd);
		runErrorCorrectionUnigrams("CItA_phoneme_hunspell_subtlex_numCand_" + num_candidates_per_method + "_unigrams",
				"phoneme", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false, cfd);
		runErrorCorrectionUnigrams("CItA_keyboard_hunspell_subtlex_numCand_" + num_candidates_per_method + "_unigrams",
				"keyboard", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false, cfd);
		runErrorCorrectionUnigrams("CItA_full_hunspell_subtlex_numCand_" + num_candidates_per_method + "_unigrams",
				"full", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false, cfd);
	}

	private static void runMerlinIT_unigrams(int num_candidates_per_method) throws UIMAException, IOException {

		String lang = "it";
		String path = "src/main/resources/corpora/merlin-IT_spelling.xml";
		String dict = "/dictionaries/hunspell_dict_it.txt";
		String dict_phon = "/dictionaries/hunspell_dict_it_phoneme_map.txt";
		String keyboard_distances = "/matrixes/keyboardDistance_IT-manual.txt";

		String unigram_file = "src/main/resources/language_models/subtlex/subtlex_it.tsv";
		ConditionalFrequencyDistribution<Integer, String> cfd = new ConditionalFrequencyDistribution<Integer, String>();
		BufferedReader br = Files.newBufferedReader(Paths.get(unigram_file), Charset.forName("UTF-8"));
		String firstLine = br.readLine();
		while (br.ready()) {
			String line = br.readLine();
			String[] entries = line.split("\t");
			String word = entries[1];
			word = word.replaceAll("^\"|\"$", "");
			String frequency = entries[2];
//			System.out.println(word + " " + frequency);
			cfd.addSample(1, word, Integer.parseInt(frequency));
		}
		br.close();

		runErrorCorrectionUnigrams(
				"MERLIN-IT_missingSpaces_hunspell_subtlex_numCand_" + num_candidates_per_method + "_unigrams",
				"missing_spaces", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false,
				cfd);
		runErrorCorrectionUnigrams(
				"MERLIN-IT_grapheme_hunspell_subtlex_numCand_" + num_candidates_per_method + "_unigrams", "grapheme",
				lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false, cfd);
		runErrorCorrectionUnigrams(
				"MERLIN-IT_phoneme_hunspell_subtlex_numCand_" + num_candidates_per_method + "_unigrams", "phoneme",
				lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false, cfd);
		runErrorCorrectionUnigrams(
				"MERLIN-IT_keyboard_hunspell_subtlex_numCand_" + num_candidates_per_method + "_unigrams", "keyboard",
				lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false, cfd);
		runErrorCorrectionUnigrams(
				"MERLIN-IT_graphemePhoneme_hunspell_subtlex_numCand_" + num_candidates_per_method + "_unigrams",
				"graphemePhoneme", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false,
				cfd);
		runErrorCorrectionUnigrams("MERLIN-IT_full_hunspell_subtlex_numCand_" + num_candidates_per_method + "_unigrams",
				"full", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false, cfd);
	}

	private static void runTOEFL_unigrams(int num_candidates_per_method) throws UIMAException, IOException {

		String lang = "en";
		String path = "src/main/resources/corpora/toefl_spelling.xml";
		String dict = "/dictionaries/hunspell_dict_en_US.txt";
		String dict_phon = "/dictionaries/hunspell_dict_en_US_phoneme_map.txt";
		String keyboard_distances = "/matrixes/keyboardDistance_EN-manual.txt";

		String unigram_file = "src/main/resources/language_models/subtlex/subtlex_en_us.csv";
		ConditionalFrequencyDistribution<Integer, String> cfd = new ConditionalFrequencyDistribution<Integer, String>();
		BufferedReader br = Files.newBufferedReader(Paths.get(unigram_file), Charset.forName("UTF-8"));
		String firstLine = br.readLine();
		while (br.ready()) {
			String line = br.readLine();
			String[] entries = line.split(";");
			String word = entries[0];
			word = word.replaceAll("^\"|\"$", "");
			String frequency = entries[1];
//			System.out.println(word + " " + frequency);
			cfd.addSample(1, word, Integer.parseInt(frequency));
		}
		br.close();

		runErrorCorrectionUnigrams(
				"TOEFL_missingSpaces_hunspell_subtlex_numCand_" + num_candidates_per_method + "_unigrams",
				"missing_spaces", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false,
				cfd);
		runErrorCorrectionUnigrams("TOEFL_grapheme_hunspell_subtlex_numCand_" + num_candidates_per_method + "_unigrams",
				"grapheme", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false, cfd);
		runErrorCorrectionUnigrams("TOEFL_phoneme_hunspell_subtlex_numCand_" + num_candidates_per_method + "_unigrams",
				"phoneme", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false, cfd);
		runErrorCorrectionUnigrams("TOEF_keyboard_hunspell_subtlex_numCand_" + num_candidates_per_method + "_unigrams",
				"keyboard", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false, cfd);
		runErrorCorrectionUnigrams("TOEFL_full_hunspell_subtlex_numCand_" + num_candidates_per_method + "_unigrams",
				"fullUni", lang, path, dict, dict_phon, keyboard_distances, num_candidates_per_method, false, cfd);
	}

	private static void runErrorCorrectionUnigrams(String config_name, String setting, String lang, String corpus_path,
			String dict_path, String phonetic_dict_path, String keyboard_distance_path, int num_candidates_per_method,
			boolean include_period, ConditionalFrequencyDistribution<Integer, String> cfd)
			throws UIMAException, IOException {

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
				LanguageModelReranker.PARAM_NGRAM_SIZE, 1);
		AnalysisEngineDescription anomalyReplacer = createEngineDescription(SpellingAnomalyReplacer.class,
				SpellingAnomalyReplacer.PARAM_TYPES_TO_COPY,
				new String[] { "spelling.types.ExtendedSpellingAnomaly" });
		AnalysisEngineDescription changeApplier = createEngineDescription(ApplyChanges.class);
		AnalysisEngineDescription correctionEvaluator = createEngineDescription(EvaluateErrorCorrection.class,
				EvaluateErrorCorrection.PARAM_CONFIG_NAME, config_name);

		// Add language model resources via SimpleResourceManager
		CFDFrequencyCountProvider cfdResource = new CFDFrequencyCountProvider(cfd, lang);
		Map<String, Object> context = new HashMap<String, Object>();
		context.put(LanguageModelReranker.RES_LANGUAGE_MODEL, cfdResource);
//		context.put(LanguageModelReranker.RES_LANGUAGE_MODEL_PROMPT_SPECIFIC, cfdResource);
		SimpleNamedResourceManager resMgr = new SimpleNamedResourceManager();
		resMgr.setAutoWireEnabled(true);
		resMgr.setExternalContext(context);
		lmReranker.setResourceManagerConfiguration(new ResourceManagerConfiguration_impl());

		if (setting.equals("full")) {
			AnalysisEngineDescription spellingCorrector = AnalysisEngineFactory.createAggregateDescription(segmenter,
					lineBreakAnnotator, markSentenceBeginnings, generateRankGrapheme, generateRankPhoneme,
					generateRankKeyboard, generateRankMissingSpaces, lmReranker, anomalyReplacer, changeApplier,
					correctionEvaluator);
			AnalysisEngine ae = UIMAFramework.produceAnalysisEngine(spellingCorrector, resMgr, null);
			while (reader.hasNext()) {
				CAS cas = ae.newCAS();
				reader.getNext(cas);
				ae.process(cas);
			}
			ae.collectionProcessComplete();
		}

		if (setting.equals("graphemePhoneme")) {

			AnalysisEngineDescription spellingCorrector = AnalysisEngineFactory.createAggregateDescription(segmenter,
					lineBreakAnnotator, markSentenceBeginnings, generateRankGrapheme, generateRankPhoneme, lmReranker,
					anomalyReplacer, changeApplier, correctionEvaluator);
			AnalysisEngine ae = UIMAFramework.produceAnalysisEngine(spellingCorrector, resMgr, null);
			while (reader.hasNext()) {
				CAS cas = ae.newCAS();
				reader.getNext(cas);
				ae.process(cas);
			}
			ae.collectionProcessComplete();
		}

		else if (setting.equals("grapheme")) {

			AnalysisEngineDescription spellingCorrector = AnalysisEngineFactory.createAggregateDescription(segmenter,
					lineBreakAnnotator, markSentenceBeginnings, generateRankGrapheme, lmReranker, anomalyReplacer,
					changeApplier, correctionEvaluator);
			AnalysisEngine ae = UIMAFramework.produceAnalysisEngine(spellingCorrector, resMgr, null);
			while (reader.hasNext()) {
				CAS cas = ae.newCAS();
				reader.getNext(cas);
				ae.process(cas);
			}
			ae.collectionProcessComplete();

		} else if (setting.equals("phoneme")) {

			AnalysisEngineDescription spellingCorrector = AnalysisEngineFactory.createAggregateDescription(segmenter,
					lineBreakAnnotator, markSentenceBeginnings, generateRankPhoneme, lmReranker, anomalyReplacer,
					changeApplier, correctionEvaluator);
			AnalysisEngine ae = UIMAFramework.produceAnalysisEngine(spellingCorrector, resMgr, null);
			while (reader.hasNext()) {
				CAS cas = ae.newCAS();
				reader.getNext(cas);
				ae.process(cas);
			}
			ae.collectionProcessComplete();

		} else if (setting.equals("keyboard")) {

			AnalysisEngineDescription spellingCorrector = AnalysisEngineFactory.createAggregateDescription(segmenter,
					lineBreakAnnotator, markSentenceBeginnings, generateRankKeyboard, lmReranker, anomalyReplacer,
					changeApplier, correctionEvaluator);
			AnalysisEngine ae = UIMAFramework.produceAnalysisEngine(spellingCorrector, resMgr, null);
			while (reader.hasNext()) {
				CAS cas = ae.newCAS();
				reader.getNext(cas);
				ae.process(cas);
			}
			ae.collectionProcessComplete();

		} else if (setting.equals("missing_spaces")) {

			AnalysisEngineDescription spellingCorrector = AnalysisEngineFactory.createAggregateDescription(segmenter,
					lineBreakAnnotator, markSentenceBeginnings, generateRankMissingSpaces, lmReranker, anomalyReplacer,
					changeApplier, correctionEvaluator);
			AnalysisEngine ae = UIMAFramework.produceAnalysisEngine(spellingCorrector, resMgr, null);
			while (reader.hasNext()) {
				CAS cas = ae.newCAS();
				reader.getNext(cas);
				ae.process(cas);
			}
			ae.collectionProcessComplete();
		}
	}

	public static CollectionReader getReader(String path, String language) throws ResourceInitializationException {

		return CollectionReaderFactory.createReader(SpellingReader.class, SpellingReader.PARAM_SOURCE_FILE, path,
				SpellingReader.PARAM_LANGUAGE_CODE, language);
	}
}
