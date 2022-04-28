package experiments;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.ExternalResourceFactory.createExternalResourceDescription;

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
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.SimpleNamedResourceManager;
import org.apache.uima.resource.ExternalResourceDescription;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.metadata.impl.ResourceManagerConfiguration_impl;
import org.dkpro.core.api.frequency.util.ConditionalFrequencyDistribution;
import org.dkpro.core.frequency.resources.Web1TFrequencyCountResource;
import org.dkpro.core.tokit.RegexSegmenter;
import org.uimafit.factory.AnalysisEngineFactory;

import candidateReranking.LanguageModelReranker;
import de.tudarmstadt.ukp.dkpro.core.corenlp.CoreNlpSegmenter;
import de.tudarmstadt.ukp.dkpro.core.languagetool.LanguageToolSegmenter;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpSegmenter;
import evaluation.EvaluateErrorCorrection;
import generateAndRank.GenerateAndRank_FindMissingSpace;
import generateAndRank.GenerateAndRank_Hunspell;
import generateAndRank.GenerateAndRank_KeyboardDistance;
import generateAndRank.GenerateAndRank_LevenshteinGrapheme;
import generateAndRank.GenerateAndRank_LevenshteinPhoneme;
import normalization.ApplyChanges;
import normalization.SpellingAnomalyReplacer;
import preprocessing.LineBreakAnnotator;
import preprocessing.MarkSentenceBeginnings;
import reader.SpellingReader;
import resources.CFDFrequencyCountProvider;
import resources.DummyFrequencyCountProvider;

public class ErrorCorrectionExperiments{

	public static void main(String[] args) throws IOException, UIMAException{
		int num_candidates_per_method = 3;
//		int num_candidates_per_method = 10;
		int n_gram_size = 3;
		
//		runSkala(num_candidates_per_method,n_gram_size);
//		runETS(num_candidates_per_method, n_gram_size);
//		runCItA(num_candidates_per_method, n_gram_size);
		runLitkey(num_candidates_per_method, n_gram_size);
//		runMerlinDE(num_candidates_per_method, n_gram_size);
//		runMerlinIT(num_candidates_per_method, n_gram_size);
//		runMerlinCZ(num_candidates_per_method, n_gram_size);
		
//		num_candidates_per_method = 10;
		n_gram_size=1;
//		runETS(num_candidates_per_method, n_gram_size);
//		runCItA(num_candidates_per_method, n_gram_size);
//		runLitkey(num_candidates_per_method, n_gram_size);
//		runMerlinDE(num_candidates_per_method, n_gram_size);
//		runMerlinIT(num_candidates_per_method, n_gram_size);
//		runMerlinCZ(num_candidates_per_method, n_gram_size);
//		runSkala(num_candidates_per_method, n_gram_size);

//		runETS_noReranking(num_candidates_per_method);
//		runMerlinDE_noReranking(num_candidates_per_method);
//		runMerlinCZ_noReranking(num_candidates_per_method);
//		runMerlinIT_noReranking(num_candidates_per_method);
//		runLitkey_noReranking(num_candidates_per_method);
//		runCItA_noReranking(num_candidates_per_method);
//		runSkala_noReranking(num_candidates_per_method);
		
		n_gram_size = 1;
//		runLitkey_childlex_unigrams(num_candidates_per_method, n_gram_size);
//		runMerlinDE_childlex_unigrams(num_candidates_per_method, n_gram_size);
//		runLitkey_subtlex_unigrams(num_candidates_per_method, n_gram_size);
//		runMerlinDE_subtlex_unigrams(num_candidates_per_method, n_gram_size);
//		runCITA_subtlex_unigrams(num_candidates_per_method, n_gram_size);
//		runMerlinIT_subtlex_unigrams(num_candidates_per_method, n_gram_size);
//		runETS_subtlex_unigrams(num_candidates_per_method, n_gram_size);
//		runSkala_childlex_unigrams(num_candidates_per_method, n_gram_size);
//		runSkala_subtlex_unigrams(num_candidates_per_method, n_gram_size);
		
		//Hunspell
//		runSkala_hunspell();
//		runMerlinDE_hunspell();
//		runMerlinCZ_hunspell();
//		runMerlinIT_hunspell();
//		runCita_hunspell();
//		runLitkey_hunspell();
		runTOEFLSPELL_hunspell();
//		runTOEFLSPELLhigh_hunspell();

		// ASAP?
	}
	
	private static void runSkala_hunspell() throws UIMAException, IOException {
		int ngram_size = 3;
		String lang = "de";
		String corpus_path = "src/main/resources/corpora/skalaErrors_merged.xml";

		String hunspell_dic = "src/main/resources/dictionaries/hunspell/German_de_DE_aux.dic";
		String hunspell_aff = "src/main/resources/dictionaries/hunspell/German_de_DE.aff";
		
		String web1t_path_de = System.getenv("WEB1T_DE");
		
//		runErrorCorrection_hunspell(lang, corpus_path, "Skala_hunspell_noReranking", hunspell_dic, hunspell_aff, 1, web1t_path_de, ngram_size, false, false);
//		runErrorCorrection_hunspell(lang, corpus_path, "Skala_hunspell_web1tTrigrams", hunspell_dic, hunspell_aff, 100, web1t_path_de, ngram_size, true, false);
		runErrorCorrection_hunspell(lang, corpus_path, "Skala_hunspell_3candiates_web1tTrigrams", hunspell_dic, hunspell_aff, 3, web1t_path_de, ngram_size, true, false);

	}
	
	private static void runLitkey_hunspell() throws UIMAException, IOException {
		int ngram_size = 3;
		String lang = "de";
		String corpus_path = "src/main/resources/corpora/litkey_spelling.xml";

		String hunspell_dic = "src/main/resources/dictionaries/hunspell/German_de_DE_aux.dic";
		String hunspell_aff = "src/main/resources/dictionaries/hunspell/German_de_DE.aff";
		
		String web1t_path_de = System.getenv("WEB1T_DE");
		
//		runErrorCorrection_hunspell(lang, corpus_path, "Litkey_hunspell_noReranking", hunspell_dic, hunspell_aff, 1, web1t_path_de, ngram_size, false, true);
//		runErrorCorrection_hunspell(lang, corpus_path, "Litkey_hunspell_web1tTrigrams", hunspell_dic, hunspell_aff, 100, web1t_path_de, ngram_size, true, true);
		runErrorCorrection_hunspell(lang, corpus_path, "Litkey_hunspell_3candidates_web1tTrigrams", hunspell_dic, hunspell_aff, 3, web1t_path_de, ngram_size, true, true);
	}
	
	private static void runMerlinDE_hunspell() throws UIMAException, IOException {
		int ngram_size = 3;
		String lang = "de";
		String corpus_path = "src/main/resources/corpora/Merlin_spelling_german.xml";

		String hunspell_dic = "src/main/resources/dictionaries/hunspell/German_de_DE_aux.dic";
		String hunspell_aff = "src/main/resources/dictionaries/hunspell/German_de_DE.aff";
		
		String web1t_path_de = System.getenv("WEB1T_DE");
		
//		runErrorCorrection_hunspell(lang, corpus_path, "MerlinDE_hunspell_noReranking", hunspell_dic, hunspell_aff, 1, web1t_path_de, ngram_size, false, false);
//		runErrorCorrection_hunspell(lang, corpus_path, "MerlinDE_hunspell_web1tTrigrams", hunspell_dic, hunspell_aff, 100, web1t_path_de, ngram_size, true, false);
		runErrorCorrection_hunspell(lang, corpus_path, "MerlinDE_hunspell_3candidates_web1tTrigrams", hunspell_dic, hunspell_aff, 3, web1t_path_de, ngram_size, true, false);
	}
	
	private static void runMerlinCZ_hunspell() throws UIMAException, IOException {
		int ngram_size = 3;
		String lang = "cz";
		String corpus_path = "src/main/resources/corpora/Merlin_spelling_czech.xml";

		String hunspell_dic = "src/main/resources/dictionaries/hunspell/Czech.dic";
		String hunspell_aff = "src/main/resources/dictionaries/hunspell/Czech.aff";
		
		String web1t_path = System.getenv("WEB1T_CZ");
		
//		runErrorCorrection_hunspell(lang, corpus_path, "MerlinCZ_hunspell_noReranking", hunspell_dic, hunspell_aff, 1, web1t_path, ngram_size, false, false);
//		runErrorCorrection_hunspell(lang, corpus_path, "MerlinCZ_hunspell_web1tTrigrams", hunspell_dic, hunspell_aff, 100, web1t_path, ngram_size, true, false);
		runErrorCorrection_hunspell(lang, corpus_path, "MerlinCZ_hunspell_3candidates_web1tTrigrams", hunspell_dic, hunspell_aff, 3, web1t_path, ngram_size, true, false);
	}
	
	private static void runMerlinIT_hunspell() throws UIMAException, IOException {
		int ngram_size = 3;
		String lang = "it";
		String corpus_path = "src/main/resources/corpora/Merlin_spelling_italian.xml";

		String hunspell_dic = "src/main/resources/dictionaries/hunspell/Italian.dic";
		String hunspell_aff = "src/main/resources/dictionaries/hunspell/Italian.aff";
		
		String web1t_path = System.getenv("WEB1T_IT");
		
//		runErrorCorrection_hunspell(lang, corpus_path, "MerlinIT_hunspell_noReranking", hunspell_dic, hunspell_aff, 1, web1t_path, ngram_size, false, false);
//		runErrorCorrection_hunspell(lang, corpus_path, "MerlinIT_hunspell_web1tTrigrams", hunspell_dic, hunspell_aff, 100, web1t_path, ngram_size, true, false);
		runErrorCorrection_hunspell(lang, corpus_path, "MerlinIT_hunspell_3candidates_web1tTrigrams", hunspell_dic, hunspell_aff, 3, web1t_path, ngram_size, true, false);
	}
	
	private static void runCita_hunspell() throws UIMAException, IOException {
		int ngram_size = 3;
		String lang = "it";
		String corpus_path = "src/main/resources/corpora/cita_spelling.xml";

		String hunspell_dic = "src/main/resources/dictionaries/hunspell/Italian.dic";
		String hunspell_aff = "src/main/resources/dictionaries/hunspell/Italian.aff";
		
		String web1t_path = System.getenv("WEB1T_IT");
		
//		runErrorCorrection_hunspell(lang, corpus_path, "Cita_hunspell_noReranking", hunspell_dic, hunspell_aff, 1, web1t_path, ngram_size, false, false);
//		runErrorCorrection_hunspell(lang, corpus_path, "Cita_hunspell_web1tTrigrams", hunspell_dic, hunspell_aff, 100, web1t_path, ngram_size, true, false);
		runErrorCorrection_hunspell(lang, corpus_path, "Cita_hunspell_3candidates_web1tTrigrams", hunspell_dic, hunspell_aff, 3, web1t_path, ngram_size, true, false);

	}
	
	private static void runTOEFLSPELL_hunspell() throws UIMAException, IOException {
		int ngram_size = 3;
		String lang = "en";
		String corpus_path = "src/main/resources/corpora/ETS_spelling.xml";

		String hunspell_dic = "src/main/resources/dictionaries/hunspell/en_US.dic";
		String hunspell_aff = "src/main/resources/dictionaries/hunspell/en_US.aff";
		
		String web1t_path = System.getenv("WEB1T_EN");
		
//		runErrorCorrection_hunspell(lang, corpus_path, "TOEFLSPELL_hunspell_noReranking", hunspell_dic, hunspell_aff, 1, web1t_path, ngram_size, false, false);
//		runErrorCorrection_hunspell(lang, corpus_path, "TOEFLSPELL_hunspell_web1tTrigrams", hunspell_dic, hunspell_aff, 100, web1t_path, ngram_size, true, false);
		runErrorCorrection_hunspell(lang, corpus_path, "TOEFLSPELL_hunspell_3candidates_web1tTrigrams", hunspell_dic, hunspell_aff, 3, web1t_path, ngram_size, true, false);

	}
	
	private static void runTOEFLSPELLhigh_hunspell() throws UIMAException, IOException {
		int ngram_size = 3;
		String lang = "en";
		String corpus_path = "src/main/resources/corpora/ETS_spelling_highScore.xml";

		String hunspell_dic = "src/main/resources/dictionaries/hunspell/en_US.dic";
		String hunspell_aff = "src/main/resources/dictionaries/hunspell/en_US.aff";
		
		String web1t_path = System.getenv("WEB1T_EN");
		
//		runErrorCorrection_hunspell(lang, corpus_path, "TOEFLSPELLhigh_hunspell_noReranking", hunspell_dic, hunspell_aff, 1, web1t_path, ngram_size, false, false);
//		runErrorCorrection_hunspell(lang, corpus_path, "TOEFLSPELLhigh_hunspell_web1tTrigrams", hunspell_dic, hunspell_aff, 100, web1t_path, ngram_size, true, false);
		runErrorCorrection_hunspell(lang, corpus_path, "TOEFLSPELLhigh_hunspell_3candidates_web1tTrigrams", hunspell_dic, hunspell_aff, 3, web1t_path, ngram_size, true, false);
	}
	
	private static void runSkala(int num_candidates_per_method, int n_gram_size) throws UIMAException, IOException {
		String merlin_lang = "de";
		String merlin_path = "src/main/resources/corpora/skalaErrors_merged.xml";
		String hunspell_de = "src/main/resources/dictionaries/hunspell_DE.txt";
		String phonetic_hunspell_de = "src/main/resources/dictionaries/hunspell_DE_phoneme_map.txt";
//		String hunspell_de = "src/main/resources/dictionaries/childlex_litkey.txt";
//		String phonetic_hunspell_de = "src/main/resources/dictionaries/childlex_litkey_phoneme_map.txt";
		String keyboard_distances_de = "src/main/resources/matrixes/keyboardDistance_DE-manual.txt";
		String web1t_path_de = System.getenv("WEB1T_DE");
		
		num_candidates_per_method = 3;
		runErrorCorrection("Skala_missingSpaces_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "missing_spaces", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, web1t_path_de, num_candidates_per_method, n_gram_size, false);
		runErrorCorrection("Skala_grapheme_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "grapheme", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, web1t_path_de, num_candidates_per_method, n_gram_size, false);
		runErrorCorrection("Skala_phoneme_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "phoneme", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, web1t_path_de, num_candidates_per_method, n_gram_size, false);
		runErrorCorrection("Skala_keyboard_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "keyboard", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, web1t_path_de, num_candidates_per_method, n_gram_size, false);
//		runErrorCorrection("Skala_graphemePhoneme_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "graphemePhoneme", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, web1t_path_de, num_candidates_per_method, n_gram_size, false);
//		runErrorCorrection("Skala_full_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "full", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, web1t_path_de, num_candidates_per_method, n_gram_size, false);
		
		num_candidates_per_method = 10;
//		runErrorCorrection("Skala_missingSpaces_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "missing_spaces", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, web1t_path_de, num_candidates_per_method, n_gram_size, false);
//		runErrorCorrection("Skala_grapheme_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "grapheme", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, web1t_path_de, num_candidates_per_method, n_gram_size, false);
//		runErrorCorrection("Skala_phoneme_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "phoneme", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, web1t_path_de, num_candidates_per_method, n_gram_size, false);
//		runErrorCorrection("Skala_keyboard_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "keyboard", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, web1t_path_de, num_candidates_per_method, n_gram_size, false);
//		runErrorCorrection("Skala_graphemePhoneme_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "graphemePhoneme", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, web1t_path_de, num_candidates_per_method, n_gram_size, false);
//		runErrorCorrection("Skala_full_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "full", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, web1t_path_de, num_candidates_per_method, n_gram_size, false);

	}
	
	private static void runETS(int num_candidates_per_method, int n_gram_size) throws UIMAException, IOException {
		
		String ets_lang = "en";
		String ets_path = "src/main/resources/corpora/ETS_spelling.xml";
		String hunspell_en = "src/main/resources/dictionaries/hunspell_en_US.txt";
		String phonetic_hunspell_en = "src/main/resources/dictionaries/hunspell_en_US_phoneme_map.txt";
		String keyboard_distances_en = "src/main/resources/matrixes/keyboardDistance_EN-manual.txt";
		String web1t_path_en = System.getenv("WEB1T_EN");

		num_candidates_per_method = 3;
		runErrorCorrection("TOEFLSPELL_missingSpaces_hunspell_web1t_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "missing_spaces", ets_lang, ets_path, hunspell_en, phonetic_hunspell_en, keyboard_distances_en, web1t_path_en, num_candidates_per_method, n_gram_size, false);
		runErrorCorrection("TOEFLSPELL_grapheme_hunspell_web1t_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "grapheme", ets_lang, ets_path, hunspell_en, phonetic_hunspell_en, keyboard_distances_en, web1t_path_en, num_candidates_per_method, n_gram_size, false);
		runErrorCorrection("TOEFLSPELL_phoneme_hunspell_web1t_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "phoneme", ets_lang, ets_path, hunspell_en, phonetic_hunspell_en, keyboard_distances_en, web1t_path_en, num_candidates_per_method, n_gram_size, false);
		runErrorCorrection("TOEFLSPELL_keyboard_hunspell_web1t_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "keyboard", ets_lang, ets_path, hunspell_en, phonetic_hunspell_en, keyboard_distances_en, web1t_path_en, num_candidates_per_method, n_gram_size, false);
//		runErrorCorrection("TOEFLSPELL_graphemePhoneme_hunspell_web1t_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "graphemePhoneme", ets_lang, ets_path, hunspell_en, phonetic_hunspell_en, keyboard_distances_en, web1t_path_en, num_candidates_per_method, n_gram_size, false);
//		runErrorCorrection("TOEFLSPELL_full_hunspell_web1t_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "full", ets_lang, ets_path, hunspell_en, phonetic_hunspell_en, keyboard_distances_en, web1t_path_en, num_candidates_per_method, n_gram_size, false);
		
		num_candidates_per_method = 10;
//		runErrorCorrection("TOEFLSPELL_missingSpaces_hunspell_web1t_"+num_candidates_per_method+n_gram_size, "missing_spaces", ets_lang, ets_path, hunspell_en, phonetic_hunspell_en, keyboard_distances_en, web1t_path_en, num_candidates_per_method, n_gram_size, false);
//		runErrorCorrection("TOEFLSPELL_grapheme_hunspell_web1t_"+num_candidates_per_method+n_gram_size, "grapheme", ets_lang, ets_path, hunspell_en, phonetic_hunspell_en, keyboard_distances_en, web1t_path_en, num_candidates_per_method, n_gram_size, false);
//		runErrorCorrection("TOEFLSPELL_phoneme_hunspell_web1t_"+num_candidates_per_method+n_gram_size, "phoneme", ets_lang, ets_path, hunspell_en, phonetic_hunspell_en, keyboard_distances_en, web1t_path_en, num_candidates_per_method, n_gram_size, false);
//		runErrorCorrection("TOEFLSPELL_keyboard_hunspell_web1t_"+num_candidates_per_method+n_gram_size, "keyboard", ets_lang, ets_path, hunspell_en, phonetic_hunspell_en, keyboard_distances_en, web1t_path_en, num_candidates_per_method, n_gram_size, false);
//		runErrorCorrection("TOEFLSPELL_graphemePhoneme_hunspell_web1t_"+num_candidates_per_method+n_gram_size, "graphemePhoneme", ets_lang, ets_path, hunspell_en, phonetic_hunspell_en, keyboard_distances_en, web1t_path_en, num_candidates_per_method, n_gram_size, false);
//		runErrorCorrection("TOEFLSPELL_full_hunspell_web1t_"+num_candidates_per_method+n_gram_size, "full", ets_lang, ets_path, hunspell_en, phonetic_hunspell_en, keyboard_distances_en, web1t_path_en, num_candidates_per_method, n_gram_size, false);

	}
	
	private static void runCItA(int num_candidates_per_method, int n_gram_size) throws UIMAException, IOException {
		
		String cita_lang = "it";
		String cita_path = "src/main/resources/corpora/cita_spelling.xml";
		String hunspell_it = "src/main/resources/dictionaries/hunspell_Italian_dict.txt";
		String phonetic_hunspell_it = "src/main/resources/dictionaries/hunspell_Italian_phoneme_map.txt";
		String keyboard_distances_it = "src/main/resources/matrixes/keyboardDistance_IT-manual.txt";
		String web1t_path_it = System.getenv("WEB1T_IT");

		num_candidates_per_method = 3;
		runErrorCorrection("CItA_missingSpaces_hunspell_web1t_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "missing_spaces", cita_lang, cita_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, web1t_path_it, num_candidates_per_method, n_gram_size, false);
		runErrorCorrection("CItA_grapheme_hunspell_web1t_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "grapheme", cita_lang, cita_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, web1t_path_it, num_candidates_per_method, n_gram_size, false);
		runErrorCorrection("CItA_phoneme_hunspell_web1t_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "phoneme", cita_lang, cita_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, web1t_path_it, num_candidates_per_method, n_gram_size, false);
		runErrorCorrection("CItA_keyboard_hunspell_web1t_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "keyboard", cita_lang, cita_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, web1t_path_it, num_candidates_per_method, n_gram_size, false);
//		runErrorCorrection("CItA_graphemePhoneme_hunspell_web1t_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "graphemePhoneme", cita_lang, cita_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, web1t_path_it, num_candidates_per_method, n_gram_size, false);
//		runErrorCorrection("CItA_full_hunspell_web1t_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "full", cita_lang, cita_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, web1t_path_it, num_candidates_per_method, n_gram_size, false);
		
		num_candidates_per_method = 10;
//		runErrorCorrection("CItA_missingSpaces_hunspell_web1t_"+num_candidates_per_method+n_gram_size, "missing_spaces", cita_lang, cita_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, web1t_path_it, num_candidates_per_method, n_gram_size, false);
//		runErrorCorrection("CItA_grapheme_hunspell_web1t_"+num_candidates_per_method+n_gram_size, "grapheme", cita_lang, cita_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, web1t_path_it, num_candidates_per_method, n_gram_size, false);
//		runErrorCorrection("CItA_phoneme_hunspell_web1t_"+num_candidates_per_method+n_gram_size, "phoneme", cita_lang, cita_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, web1t_path_it, num_candidates_per_method, n_gram_size, false);
//		runErrorCorrection("CItA_keyboard_hunspell_web1t_"+num_candidates_per_method+n_gram_size, "keyboard", cita_lang, cita_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, web1t_path_it, num_candidates_per_method, n_gram_size, false);
//		runErrorCorrection("CItA_graphemePhoneme_hunspell_web1t_"+num_candidates_per_method+n_gram_size, "graphemePhoneme", cita_lang, cita_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, web1t_path_it, num_candidates_per_method, n_gram_size, false);
//		runErrorCorrection("CItA_full_hunspell_web1t_"+num_candidates_per_method+n_gram_size, "full", cita_lang, cita_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, web1t_path_it, num_candidates_per_method, n_gram_size, false);

	}
	
	private static void runLitkey(int num_candidates_per_method, int n_gram_size) throws UIMAException, IOException {
		String litkey_lang = "de";
		String litkey_path = "src/main/resources/corpora/litkey_spelling.xml";
		String hunspell_de = "src/main/resources/dictionaries/hunspell_DE.txt";
		String phonetic_hunspell_de = "src/main/resources/dictionaries/hunspell_DE_phoneme_map.txt";
//		String hunspell_de = "src/main/resources/dictionaries/childlex_litkey.txt";
//		String phonetic_hunspell_de = "src/main/resources/dictionaries/childlex_litkey_phoneme_map.txt";
		String keyboard_distances_de = "src/main/resources/matrixes/keyboardDistance_DE-manual.txt";
		String web1t_path_de = System.getenv("WEB1T_DE");
		
		num_candidates_per_method = 3;
		runErrorCorrection("Litkey_missingSpaces_hunspell_web1t_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "missing_spaces", litkey_lang, litkey_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, web1t_path_de, num_candidates_per_method, n_gram_size, true);
//		runErrorCorrection("Litkey_grapheme_hunspell_web1t_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "grapheme", litkey_lang, litkey_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, web1t_path_de, num_candidates_per_method, n_gram_size, true);
//		runErrorCorrection("Litkey_keyboard_hunspell_web1t_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "keyboard", litkey_lang, litkey_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, web1t_path_de, num_candidates_per_method, n_gram_size, true);
//		runErrorCorrection("Litkey_phoneme_hunspell_web1t_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "phoneme", litkey_lang, litkey_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, web1t_path_de, num_candidates_per_method, n_gram_size, true);
//		runErrorCorrection("Litkey_graphemePhoneme_hunspell_web1t_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "graphemePhoneme", litkey_lang, litkey_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, web1t_path_de, num_candidates_per_method, n_gram_size, true);
//		runErrorCorrection("Litkey_full_hunspell_web1t_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "full", litkey_lang, litkey_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, web1t_path_de, num_candidates_per_method, n_gram_size, true);

		num_candidates_per_method = 10;
//		runErrorCorrection("Litkey_missingSpaces_hunspell_web1t_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "missing_spaces", litkey_lang, litkey_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, web1t_path_de, num_candidates_per_method, n_gram_size, true);
//		runErrorCorrection("Litkey_grapheme_hunspell_web1t_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "grapheme", litkey_lang, litkey_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, web1t_path_de, num_candidates_per_method, n_gram_size, true);
//		runErrorCorrection("Litkey_keyboard_hunspell_web1t_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "keyboard", litkey_lang, litkey_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, web1t_path_de, num_candidates_per_method, n_gram_size, true);
//		runErrorCorrection("Litkey_phoneme_hunspell_web1t_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "phoneme", litkey_lang, litkey_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, web1t_path_de, num_candidates_per_method, n_gram_size, true);
//		runErrorCorrection("Litkey_graphemePhoneme_hunspell_web1t_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "graphemePhoneme", litkey_lang, litkey_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, web1t_path_de, num_candidates_per_method, n_gram_size, true);	
//		runErrorCorrection("Litkey_full_hunspell_web1t_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "full", litkey_lang, litkey_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, web1t_path_de, num_candidates_per_method, n_gram_size, true);	

	}
	
	private static void runMerlinIT(int num_candidates_per_method, int n_gram_size) throws UIMAException, IOException {
		String merlin_lang = "it";
		String merlin_path = "src/main/resources/corpora/Merlin_spelling_italian.xml";
		String hunspell_it = "src/main/resources/dictionaries/hunspell_Italian_dict.txt";
		String phonetic_hunspell_it = "src/main/resources/dictionaries/hunspell_Italian_phoneme_map.txt";
		String keyboard_distances_it = "src/main/resources/matrixes/keyboardDistance_IT-manual.txt";
		String web1t_path_it = System.getenv("WEB1T_IT");
		
		num_candidates_per_method = 3;
		runErrorCorrection("MerlinIT_missingSpaces_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "missing_spaces", merlin_lang, merlin_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, web1t_path_it, num_candidates_per_method, n_gram_size, false);
		runErrorCorrection("MerlinIT_grapheme_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "grapheme", merlin_lang, merlin_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, web1t_path_it, num_candidates_per_method, n_gram_size, false);
		runErrorCorrection("MerlinIT_phoneme_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "phoneme", merlin_lang, merlin_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, web1t_path_it, num_candidates_per_method, n_gram_size, false);
		runErrorCorrection("MerlinIT_keyboard_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "keyboard", merlin_lang, merlin_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, web1t_path_it, num_candidates_per_method, n_gram_size, false);
//		runErrorCorrection("MerlinIT_graphemePhoneme_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "graphemePhoneme", merlin_lang, merlin_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, web1t_path_it, num_candidates_per_method, n_gram_size, false);	
//		runErrorCorrection("MerlinIT_full_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "full", merlin_lang, merlin_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, web1t_path_it, num_candidates_per_method, n_gram_size, false);	

		
		num_candidates_per_method = 10;
//		runErrorCorrection("MerlinIT_missingSpaces_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "missing_spaces", merlin_lang, merlin_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, web1t_path_it, num_candidates_per_method, n_gram_size, false);
//		runErrorCorrection("MerlinIT_grapheme_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "grapheme", merlin_lang, merlin_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, web1t_path_it, num_candidates_per_method, n_gram_size, false);
//		runErrorCorrection("MerlinIT_phoneme_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "phoneme", merlin_lang, merlin_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, web1t_path_it, num_candidates_per_method, n_gram_size, false);
//		runErrorCorrection("MerlinIT_keyboard_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "keyboard", merlin_lang, merlin_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, web1t_path_it, num_candidates_per_method, n_gram_size, false);
//		runErrorCorrection("MerlinIT_graphemePhoneme_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "graphemePhoneme", merlin_lang, merlin_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, web1t_path_it, num_candidates_per_method, n_gram_size, false);
//		runErrorCorrection("MerlinIT_full_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "full", merlin_lang, merlin_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, web1t_path_it, num_candidates_per_method, n_gram_size, false);

	}
	
	
	private static void runMerlinCZ(int num_candidates_per_method, int n_gram_size) throws UIMAException, IOException {
		String merlin_lang = "cz";
		String merlin_path = "src/main/resources/corpora/Merlin_spelling_czech.xml";
		String hunspell_cz = "src/main/resources/dictionaries/hunspell_Czech_dict.txt";
		String phonetic_hunspell_cz = "src/main/resources/dictionaries/hunspell_Czech_phoneme_map.txt";
		String keyboard_distances_cz = "src/main/resources/matrixes/keyboardDistance_CZ-manual.txt";
		String web1t_path_cz = System.getenv("WEB1T_CZ");
		
		num_candidates_per_method = 3;
		runErrorCorrection("MerlinCZ_missingSpaces_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "missing_spaces", merlin_lang, merlin_path, hunspell_cz, phonetic_hunspell_cz, keyboard_distances_cz, web1t_path_cz, num_candidates_per_method, n_gram_size, false);
		runErrorCorrection("MerlinCZ_grapheme_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "grapheme", merlin_lang, merlin_path, hunspell_cz, phonetic_hunspell_cz, keyboard_distances_cz, web1t_path_cz, num_candidates_per_method, n_gram_size, false);
		runErrorCorrection("MerlinCZ_phoneme_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "phoneme", merlin_lang, merlin_path, hunspell_cz, phonetic_hunspell_cz, keyboard_distances_cz, web1t_path_cz, num_candidates_per_method, n_gram_size, false);
		runErrorCorrection("MerlinCZ_keyboard_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "keyboard", merlin_lang, merlin_path, hunspell_cz, phonetic_hunspell_cz, keyboard_distances_cz, web1t_path_cz, num_candidates_per_method, n_gram_size, false);
//		runErrorCorrection("MerlinCZ_graphemePhoneme_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "graphemePhoneme", merlin_lang, merlin_path, hunspell_cz, phonetic_hunspell_cz, keyboard_distances_cz, web1t_path_cz, num_candidates_per_method, n_gram_size, false);
//		runErrorCorrection("MerlinCZ_full_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "full", merlin_lang, merlin_path, hunspell_cz, phonetic_hunspell_cz, keyboard_distances_cz, web1t_path_cz, num_candidates_per_method, n_gram_size, false);
		
		num_candidates_per_method = 10;
//		runErrorCorrection("MerlinCZ_missingSpaces_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "missing_spaces", merlin_lang, merlin_path, hunspell_cz, phonetic_hunspell_cz, keyboard_distances_cz, web1t_path_cz, num_candidates_per_method, n_gram_size, false);
//		runErrorCorrection("MerlinCZ_grapheme_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "grapheme", merlin_lang, merlin_path, hunspell_cz, phonetic_hunspell_cz, keyboard_distances_cz, web1t_path_cz, num_candidates_per_method, n_gram_size, false);
//		runErrorCorrection("MerlinCZ_phoneme_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "phoneme", merlin_lang, merlin_path, hunspell_cz, phonetic_hunspell_cz, keyboard_distances_cz, web1t_path_cz, num_candidates_per_method, n_gram_size, false);
//		runErrorCorrection("MerlinCZ_keyboard_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "keyboard", merlin_lang, merlin_path, hunspell_cz, phonetic_hunspell_cz, keyboard_distances_cz, web1t_path_cz, num_candidates_per_method, n_gram_size, false);
//		runErrorCorrection("MerlinCZ_graphemePhoneme_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "graphemePhoneme", merlin_lang, merlin_path, hunspell_cz, phonetic_hunspell_cz, keyboard_distances_cz, web1t_path_cz, num_candidates_per_method, n_gram_size, false);
//		runErrorCorrection("MerlinCZ_full_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "full", merlin_lang, merlin_path, hunspell_cz, phonetic_hunspell_cz, keyboard_distances_cz, web1t_path_cz, num_candidates_per_method, n_gram_size, false);

	}
	
	
	private static void runMerlinDE(int num_candidates_per_method, int n_gram_size) throws UIMAException, IOException {
		String merlin_lang = "de";
		String merlin_path = "src/main/resources/corpora/Merlin_spelling_german.xml";
//		String merlin_path = "src/main/resources/corpora/merlinDE_firstText.xml";
		String hunspell_de = "src/main/resources/dictionaries/hunspell_DE.txt";
		String phonetic_hunspell_de = "src/main/resources/dictionaries/hunspell_DE_phoneme_map.txt";
//		String hunspell_de = "src/main/resources/dictionaries/childlex_litkey.txt";
//		String phonetic_hunspell_de = "src/main/resources/dictionaries/childlex_litkey_phoneme_map.txt";
		String keyboard_distances_de = "src/main/resources/matrixes/keyboardDistance_DE-manual.txt";
		String web1t_path_de = System.getenv("WEB1T_DE");
		
		num_candidates_per_method = 3;
		runErrorCorrection("MerlinDE_missingSpaces_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "missing_spaces", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, web1t_path_de, num_candidates_per_method, n_gram_size, false);
		runErrorCorrection("MerlinDE_grapheme_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "grapheme", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, web1t_path_de, num_candidates_per_method, n_gram_size, false);
		runErrorCorrection("MerlinDE_phoneme_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "phoneme", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, web1t_path_de, num_candidates_per_method, n_gram_size, false);
		runErrorCorrection("MerlinDE_keyboard_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "keyboard", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, web1t_path_de, num_candidates_per_method, n_gram_size, false);
//		runErrorCorrection("MerlinDE_graphemePhoneme_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "graphemePhoneme", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, web1t_path_de, num_candidates_per_method, n_gram_size, false);
//		runErrorCorrection("MerlinDE_full_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "full", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, web1t_path_de, num_candidates_per_method, n_gram_size, false);
		
		num_candidates_per_method = 10;
//		runErrorCorrection("MerlinDE_missingSpaces_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "missing_spaces", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, web1t_path_de, num_candidates_per_method, n_gram_size, false);
//		runErrorCorrection("MerlinDE_grapheme_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "grapheme", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, web1t_path_de, num_candidates_per_method, n_gram_size, false);
//		runErrorCorrection("MerlinDE_phoneme_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "phoneme", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, web1t_path_de, num_candidates_per_method, n_gram_size, false);
//		runErrorCorrection("MerlinDE_keyboard_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "keyboard", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, web1t_path_de, num_candidates_per_method, n_gram_size, false);
//		runErrorCorrection("MerlinDE_graphemePhoneme_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "graphemePhoneme", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, web1t_path_de, num_candidates_per_method, n_gram_size, false);
//		runErrorCorrection("MerlinDE_full_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "full", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, web1t_path_de, num_candidates_per_method, n_gram_size, false);

	}
	
	
private static void runETS_noReranking(int num_candidates_per_method) throws UIMAException, IOException {
		
		String ets_lang = "en";
		String ets_path = "src/main/resources/corpora/ETS_spelling.xml";
		String hunspell_en = "src/main/resources/dictionaries/hunspell_en_US.txt";
		String phonetic_hunspell_en = "src/main/resources/dictionaries/hunspell_en_US_phoneme_map.txt";
		String keyboard_distances_en = "src/main/resources/matrixes/keyboardDistance_EN-manual.txt";

		num_candidates_per_method = 3;
		runErrorCorrection_noReranking("TOEFLSPELL_missingSpaces_hunspell_noReranking_"+num_candidates_per_method, "missing_spaces", ets_lang, ets_path, hunspell_en, phonetic_hunspell_en, keyboard_distances_en, num_candidates_per_method, false);
		runErrorCorrection_noReranking("TOEFLSPELL_grapheme_hunspell_noReranking_"+num_candidates_per_method, "grapheme", ets_lang, ets_path, hunspell_en, phonetic_hunspell_en, keyboard_distances_en, num_candidates_per_method, false);
		runErrorCorrection_noReranking("TOEFLSPELL_phoneme_hunspell_noReranking_"+num_candidates_per_method, "phoneme", ets_lang, ets_path, hunspell_en, phonetic_hunspell_en, keyboard_distances_en, num_candidates_per_method, false);
		runErrorCorrection_noReranking("TOEFLSPELL_keyboard_hunspell_noReranking_"+num_candidates_per_method, "keyboard", ets_lang, ets_path, hunspell_en, phonetic_hunspell_en, keyboard_distances_en, num_candidates_per_method, false);
		runErrorCorrection_noReranking("TOEFLSPELL_graphemePhoneme_hunspell_noReranking_"+num_candidates_per_method, "graphemePhoneme", ets_lang, ets_path, hunspell_en, phonetic_hunspell_en, keyboard_distances_en, num_candidates_per_method, false);
		runErrorCorrection_noReranking("TOEFLSPELL_full_hunspell_noReranking_"+num_candidates_per_method, "full", ets_lang, ets_path, hunspell_en, phonetic_hunspell_en, keyboard_distances_en, num_candidates_per_method, false);

		num_candidates_per_method = 10;
		runErrorCorrection_noReranking("TOEFLSPELL_missingSpaces_hunspell_noReranking_"+num_candidates_per_method, "missing_spaces", ets_lang, ets_path, hunspell_en, phonetic_hunspell_en, keyboard_distances_en, num_candidates_per_method, false);
		runErrorCorrection_noReranking("TOEFLSPELL_grapheme_hunspell_noReranking_"+num_candidates_per_method, "grapheme", ets_lang, ets_path, hunspell_en, phonetic_hunspell_en, keyboard_distances_en, num_candidates_per_method, false);
		runErrorCorrection_noReranking("TOEFLSPELL_phoneme_hunspell_noReranking_"+num_candidates_per_method, "phoneme", ets_lang, ets_path, hunspell_en, phonetic_hunspell_en, keyboard_distances_en, num_candidates_per_method, false);
		runErrorCorrection_noReranking("TOEFLSPELL_keyboard_hunspell_noReranking_"+num_candidates_per_method, "keyboard", ets_lang, ets_path, hunspell_en, phonetic_hunspell_en, keyboard_distances_en, num_candidates_per_method, false);
		runErrorCorrection_noReranking("TOEFLSPELL_graphemePhoneme_hunspell_noReranking_"+num_candidates_per_method, "graphemePhoneme", ets_lang, ets_path, hunspell_en, phonetic_hunspell_en, keyboard_distances_en, num_candidates_per_method, false);
		runErrorCorrection_noReranking("TOEFLSPELL_full_hunspell_noReranking_"+num_candidates_per_method, "full", ets_lang, ets_path, hunspell_en, phonetic_hunspell_en, keyboard_distances_en, num_candidates_per_method, false);

	}
	
	
	private static void runCItA_noReranking(int num_candidates_per_method) throws UIMAException, IOException {
		
		String cita_lang = "it";
		String cita_path = "src/main/resources/corpora/cita_spelling.xml";
//		String cita_path = "src/main/resources/corpora/cita_example.xml";
		String hunspell_it = "src/main/resources/dictionaries/hunspell_Italian_dict.txt";
		String phonetic_hunspell_it = "src/main/resources/dictionaries/hunspell_Italian_phoneme_map.txt";
		String keyboard_distances_it = "src/main/resources/matrixes/keyboardDistance_IT-manual.txt";

		num_candidates_per_method = 3;
//		runErrorCorrection_noReranking("CItA_missingSpaces_hunspell_noReranking_"+num_candidates_per_method, "missing_spaces", cita_lang, cita_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, num_candidates_per_method, false);
//		runErrorCorrection_noReranking("CItA_grapheme_hunspell_noReranking_"+num_candidates_per_method, "grapheme", cita_lang, cita_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, num_candidates_per_method, false);
//		runErrorCorrection_noReranking("CItA_phoneme_hunspell_noReranking_"+num_candidates_per_method, "phoneme", cita_lang, cita_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, num_candidates_per_method, false);
//		runErrorCorrection_noReranking("CItA_keyboard_hunspell_noReranking_"+num_candidates_per_method, "keyboard", cita_lang, cita_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, num_candidates_per_method, false);
//		runErrorCorrection_noReranking("CItA_graphemePhoneme_hunspell_noReranking_"+num_candidates_per_method, "graphemePhoneme", cita_lang, cita_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, num_candidates_per_method, false);
//		runErrorCorrection_noReranking("CItA_full_hunspell_noReranking_"+num_candidates_per_method, "full", cita_lang, cita_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, num_candidates_per_method, false);

		num_candidates_per_method = 10;
//		runErrorCorrection_noReranking("CItA_missingSpaces_hunspell_noReranking_"+num_candidates_per_method, "missing_spaces", cita_lang, cita_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, num_candidates_per_method, false);
//		runErrorCorrection_noReranking("CItA_grapheme_hunspell_noReranking_"+num_candidates_per_method, "grapheme", cita_lang, cita_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, num_candidates_per_method, false);
//		runErrorCorrection_noReranking("CItA_phoneme_hunspell_noReranking_"+num_candidates_per_method, "phoneme", cita_lang, cita_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, num_candidates_per_method, false);
		runErrorCorrection_noReranking("CItA_keyboard_hunspell_noReranking_"+num_candidates_per_method, "keyboard", cita_lang, cita_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, num_candidates_per_method, false);
		runErrorCorrection_noReranking("CItA_graphemePhoneme_hunspell_noReranking_"+num_candidates_per_method, "graphemePhoneme", cita_lang, cita_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, num_candidates_per_method, false);
		runErrorCorrection_noReranking("CItA_full_hunspell_noReranking_"+num_candidates_per_method, "full", cita_lang, cita_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, num_candidates_per_method, false);

	}
	
	private static void runLitkey_noReranking(int num_candidates_per_method) throws UIMAException, IOException {
		String litkey_lang = "de";
		String litkey_path = "src/main/resources/corpora/litkey_spelling.xml";
//		String litkey_path = "src/main/resources/corpora/litkey_example.xml";
		String hunspell_de = "src/main/resources/dictionaries/hunspell_DE.txt";
		String phonetic_hunspell_de = "src/main/resources/dictionaries/hunspell_DE_phoneme_map.txt";
		String keyboard_distances_de = "src/main/resources/matrixes/keyboardDistance_DE-manual.txt";
		
		num_candidates_per_method = 3;
		runErrorCorrection_noReranking("Litkey_missingSpaces_hunspell__noReranking_numCand"+num_candidates_per_method, "missing_spaces", litkey_lang, litkey_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, true);
		runErrorCorrection_noReranking("Litkey_grapheme_hunspell_noReranking_numCand"+num_candidates_per_method, "grapheme", litkey_lang, litkey_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, true);
		runErrorCorrection_noReranking("Litkey_keyboard_hunspell_noReranking_numCand"+num_candidates_per_method, "keyboard", litkey_lang, litkey_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, true);
		runErrorCorrection_noReranking("Litkey_phoneme_hunspell_noReranking_numCand"+num_candidates_per_method, "phoneme", litkey_lang, litkey_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, true);
		runErrorCorrection_noReranking("Litkey_graphemePhoneme_hunspell_noReranking_numCand"+num_candidates_per_method, "graphemePhoneme", litkey_lang, litkey_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, true);
		runErrorCorrection_noReranking("Litkey_full_hunspell_noReranking_numCand"+num_candidates_per_method, "full", litkey_lang, litkey_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, true);
		
		num_candidates_per_method = 10;
		runErrorCorrection_noReranking("Litkey_missingSpaces_hunspell_noReranking_numCand"+num_candidates_per_method, "missing_spaces", litkey_lang, litkey_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, true);
		runErrorCorrection_noReranking("Litkey_grapheme_hunspell_noReranking_numCand"+num_candidates_per_method, "grapheme", litkey_lang, litkey_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, true);
		runErrorCorrection_noReranking("Litkey_keyboard_hunspell_noReranking_numCand"+num_candidates_per_method, "keyboard", litkey_lang, litkey_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, true);
		runErrorCorrection_noReranking("Litkey_phoneme_hunspell_noReranking_numCand"+num_candidates_per_method, "phoneme", litkey_lang, litkey_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, true);
		runErrorCorrection_noReranking("Litkey_graphemePhoneme_hunspell_noReranking_numCand"+num_candidates_per_method, "graphemePhoneme", litkey_lang, litkey_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, true);	
		runErrorCorrection_noReranking("Litkey_full_hunspell_noReranking_numCand"+num_candidates_per_method, "full", litkey_lang, litkey_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, true);	

	}
	
	private static void runMerlinIT_noReranking(int num_candidates_per_method) throws UIMAException, IOException {
		String merlin_lang = "it";
		String merlin_path = "src/main/resources/corpora/Merlin_spelling_italian.xml";
		String hunspell_it = "src/main/resources/dictionaries/hunspell_Italian_dict.txt";
		String phonetic_hunspell_it = "src/main/resources/dictionaries/hunspell_Italian_phoneme_map.txt";
		String keyboard_distances_it = "src/main/resources/matrixes/keyboardDistance_IT-manual.txt";
		
		num_candidates_per_method = 3;
//		runErrorCorrection_noReranking("MerlinIT_missingSpaces_hunspell_noReranking_"+num_candidates_per_method, "missing_spaces", merlin_lang, merlin_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, num_candidates_per_method, false);
//		runErrorCorrection_noReranking("MerlinIT_grapheme_hunspell_noReranking_"+num_candidates_per_method, "grapheme", merlin_lang, merlin_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, num_candidates_per_method, false);
//		runErrorCorrection_noReranking("MerlinIT_phoneme_hunspell_noReranking_"+num_candidates_per_method, "phoneme", merlin_lang, merlin_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, num_candidates_per_method, false);
//		runErrorCorrection_noReranking("MerlinIT_keyboard_hunspell_noReranking_"+num_candidates_per_method, "keyboard", merlin_lang, merlin_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, num_candidates_per_method, false);
//		runErrorCorrection_noReranking("MerlinIT_graphemePhoneme_hunspell_noReranking_"+num_candidates_per_method, "graphemePhoneme", merlin_lang, merlin_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, num_candidates_per_method, false);	
		runErrorCorrection_noReranking("MerlinIT_full_hunspell_noReranking_"+num_candidates_per_method, "full", merlin_lang, merlin_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, num_candidates_per_method, false);	
		
		num_candidates_per_method = 10;
		runErrorCorrection_noReranking("MerlinIT_missingSpaces_hunspell_noReranking_"+num_candidates_per_method, "missing_spaces", merlin_lang, merlin_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, num_candidates_per_method, false);
		runErrorCorrection_noReranking("MerlinIT_grapheme_hunspell_noReranking_"+num_candidates_per_method, "grapheme", merlin_lang, merlin_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, num_candidates_per_method, false);
		runErrorCorrection_noReranking("MerlinIT_phoneme_hunspell_noReranking_"+num_candidates_per_method, "phoneme", merlin_lang, merlin_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, num_candidates_per_method, false);
		runErrorCorrection_noReranking("MerlinIT_keyboard_hunspell_noReranking_"+num_candidates_per_method, "keyboard", merlin_lang, merlin_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, num_candidates_per_method, false);
		runErrorCorrection_noReranking("MerlinIT_graphemePhoneme_hunspell_noReranking_"+num_candidates_per_method, "graphemePhoneme", merlin_lang, merlin_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, num_candidates_per_method, false);
		runErrorCorrection_noReranking("MerlinIT_full_hunspell_noReranking_"+num_candidates_per_method, "full", merlin_lang, merlin_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, num_candidates_per_method, false);

	}
	
	private static void runMerlinCZ_noReranking(int num_candidates_per_method) throws UIMAException, IOException {
		String merlin_lang = "cz";
		String merlin_path = "src/main/resources/corpora/Merlin_spelling_czech.xml";
		String hunspell_cz = "src/main/resources/dictionaries/hunspell_Czech_dict.txt";
		String phonetic_hunspell_cz = "src/main/resources/dictionaries/hunspell_Czech_phoneme_map.txt";
		String keyboard_distances_cz = "src/main/resources/matrixes/keyboardDistance_CZ-manual.txt";
		
		num_candidates_per_method = 3;
		runErrorCorrection_noReranking("MerlinCZ_missingSpaces_hunspell_noReranking_"+num_candidates_per_method, "missing_spaces", merlin_lang, merlin_path, hunspell_cz, phonetic_hunspell_cz, keyboard_distances_cz, num_candidates_per_method, false);
		runErrorCorrection_noReranking("MerlinCZ_grapheme_hunspell_noReranking_"+num_candidates_per_method, "grapheme", merlin_lang, merlin_path, hunspell_cz, phonetic_hunspell_cz, keyboard_distances_cz, num_candidates_per_method, false);
		runErrorCorrection_noReranking("MerlinCZ_phoneme_hunspell_noReranking_"+num_candidates_per_method, "phoneme", merlin_lang, merlin_path, hunspell_cz, phonetic_hunspell_cz, keyboard_distances_cz, num_candidates_per_method, false);
		runErrorCorrection_noReranking("MerlinCZ_keyboard_hunspell_noReranking_"+num_candidates_per_method, "keyboard", merlin_lang, merlin_path, hunspell_cz, phonetic_hunspell_cz, keyboard_distances_cz, num_candidates_per_method, false);
		runErrorCorrection_noReranking("MerlinCZ_graphemePhoneme_hunspell_noReranking_"+num_candidates_per_method, "graphemePhoneme", merlin_lang, merlin_path, hunspell_cz, phonetic_hunspell_cz, keyboard_distances_cz, num_candidates_per_method, false);
		runErrorCorrection_noReranking("MerlinCZ_full_hunspell_noReranking_"+num_candidates_per_method, "full", merlin_lang, merlin_path, hunspell_cz, phonetic_hunspell_cz, keyboard_distances_cz, num_candidates_per_method, false);
		
		num_candidates_per_method = 10;
		runErrorCorrection_noReranking("MerlinCZ_missingSpaces_hunspell_noReranking_"+num_candidates_per_method, "missing_spaces", merlin_lang, merlin_path, hunspell_cz, phonetic_hunspell_cz, keyboard_distances_cz, num_candidates_per_method, false);
		runErrorCorrection_noReranking("MerlinCZ_grapheme_hunspell_noReranking_"+num_candidates_per_method, "grapheme", merlin_lang, merlin_path, hunspell_cz, phonetic_hunspell_cz, keyboard_distances_cz, num_candidates_per_method, false);
		runErrorCorrection_noReranking("MerlinCZ_phoneme_hunspell_noReranking_"+num_candidates_per_method, "phoneme", merlin_lang, merlin_path, hunspell_cz, phonetic_hunspell_cz, keyboard_distances_cz, num_candidates_per_method, false);
		runErrorCorrection_noReranking("MerlinCZ_keyboard_hunspell_noReranking_"+num_candidates_per_method, "keyboard", merlin_lang, merlin_path, hunspell_cz, phonetic_hunspell_cz, keyboard_distances_cz, num_candidates_per_method, false);
		runErrorCorrection_noReranking("MerlinCZ_graphemePhoneme_hunspell_noReranking_"+num_candidates_per_method, "graphemePhoneme", merlin_lang, merlin_path, hunspell_cz, phonetic_hunspell_cz, keyboard_distances_cz, num_candidates_per_method, false);
		runErrorCorrection_noReranking("MerlinCZ_full_hunspell_noReranking_"+num_candidates_per_method, "full", merlin_lang, merlin_path, hunspell_cz, phonetic_hunspell_cz, keyboard_distances_cz, num_candidates_per_method, false);

	}
	
	private static void runMerlinDE_noReranking(int num_candidates_per_method) throws UIMAException, IOException {
		String merlin_lang = "de";
		String merlin_path = "src/main/resources/corpora/Merlin_spelling_german.xml";
//		String merlin_path = "src/main/resources/corpora/merlinDE_firstText.xml";
		String hunspell_de = "src/main/resources/dictionaries/hunspell_DE.txt";
		String phonetic_hunspell_de = "src/main/resources/dictionaries/hunspell_DE_phoneme_map.txt";
		String keyboard_distances_de = "src/main/resources/matrixes/keyboardDistance_DE-manual.txt";
		
		num_candidates_per_method = 3;
		runErrorCorrection_noReranking("MerlinDE_missingSpaces_hunspell_noReranking_"+num_candidates_per_method, "missing_spaces", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, false);
		runErrorCorrection_noReranking("MerlinDE_grapheme_hunspell_noReranking_"+num_candidates_per_method, "grapheme", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, false);
		runErrorCorrection_noReranking("MerlinDE_phoneme_hunspell_noReranking_"+num_candidates_per_method, "phoneme", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, false);
		runErrorCorrection_noReranking("MerlinDE_keyboard_hunspell_web1t_noReranking"+num_candidates_per_method, "keyboard", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, false);
		runErrorCorrection_noReranking("MerlinDE_graphemePhoneme_hunspell_noReranking_"+num_candidates_per_method, "graphemePhoneme", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, false);
		runErrorCorrection_noReranking("MerlinDE_full_hunspell_noReranking_"+num_candidates_per_method, "full", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, false);
		
		num_candidates_per_method = 10;
		runErrorCorrection_noReranking("MerlinDE_missingSpaces_hunspell_noReranking_"+num_candidates_per_method, "missing_spaces", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, false);
		runErrorCorrection_noReranking("MerlinDE_grapheme_hunspell_noReranking_"+num_candidates_per_method, "grapheme", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, false);
		runErrorCorrection_noReranking("MerlinDE_phoneme_hunspell_noReranking_"+num_candidates_per_method, "phoneme", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, false);
		runErrorCorrection_noReranking("MerlinDE_keyboard_hunspell_noReranking_"+num_candidates_per_method, "keyboard", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, false);
		runErrorCorrection_noReranking("MerlinDE_graphemePhoneme_hunspell_noReranking_"+num_candidates_per_method, "graphemePhoneme", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, false);
		runErrorCorrection_noReranking("MerlinDE_full_hunspell_noReranking_"+num_candidates_per_method, "full", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, false);
	}
	
	private static void runSkala_noReranking(int num_candidates_per_method) throws UIMAException, IOException{
		String merlin_lang = "de";
		String merlin_path = "src/main/resources/corpora/skalaErrors_merged.xml";
		String hunspell_de = "src/main/resources/dictionaries/hunspell_DE.txt";
		String phonetic_hunspell_de = "src/main/resources/dictionaries/hunspell_DE_phoneme_map.txt";
		String keyboard_distances_de = "src/main/resources/matrixes/keyboardDistance_DE-manual.txt";
		
		num_candidates_per_method = 3;
//		runErrorCorrection_noReranking("Skala_missingSpaces_hunspell_noReranking_"+num_candidates_per_method, "missing_spaces", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, false);
		runErrorCorrection_noReranking("Skala_grapheme_hunspell_noReranking_"+num_candidates_per_method, "grapheme", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, false);
//		runErrorCorrection_noReranking("Skala_phoneme_hunspell_noReranking_"+num_candidates_per_method, "phoneme", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, false);
//		runErrorCorrection_noReranking("Skala_keyboard_hunspell_web1t_noReranking"+num_candidates_per_method, "keyboard", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, false);
//		runErrorCorrection_noReranking("Skala_graphemePhoneme_hunspell_noReranking_"+num_candidates_per_method, "graphemePhoneme", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, false);
//		runErrorCorrection_noReranking("Skala_full_hunspell_noReranking_"+num_candidates_per_method, "full", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, false);
		
		num_candidates_per_method = 10;
//		runErrorCorrection_noReranking("Skala_missingSpaces_hunspell_noReranking_"+num_candidates_per_method, "missing_spaces", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, false);
		runErrorCorrection_noReranking("Skala_grapheme_hunspell_noReranking_"+num_candidates_per_method, "grapheme", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, false);
//		runErrorCorrection_noReranking("Skala_phoneme_hunspell_noReranking_"+num_candidates_per_method, "phoneme", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, false);
//		runErrorCorrection_noReranking("Skala_keyboard_hunspell_noReranking_"+num_candidates_per_method, "keyboard", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, false);
//		runErrorCorrection_noReranking("Skala_graphemePhoneme_hunspell_noReranking_"+num_candidates_per_method, "graphemePhoneme", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, false);
//		runErrorCorrection_noReranking("Skala_full_hunspell_noReranking_"+num_candidates_per_method, "full", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, false);

	}
	
	private static void runLitkey_childlex_unigrams(int num_candidates_per_method, int n_gram_size) throws UIMAException, IOException {
		String litkey_lang = "de";
		String litkey_path = "src/main/resources/corpora/litkey_spelling.xml";
		String hunspell_de = "src/main/resources/dictionaries/hunspell_DE.txt";
		String phonetic_hunspell_de = "src/main/resources/dictionaries/hunspell_DE_phoneme_map.txt";
		String keyboard_distances_de = "src/main/resources/matrixes/keyboardDistance_DE-manual.txt";
		String unigram_file = "src/main/resources/dictionaries/childLex_0.17.01_clean.tsv";
		
		ConditionalFrequencyDistribution<Integer, String> cfd = new ConditionalFrequencyDistribution<Integer, String>();
		BufferedReader br = new BufferedReader(new FileReader(new File(unigram_file)));
		String firstLine = br.readLine();
		while(br.ready()) {
			String line = br.readLine();
			String[] entries = line.split("\t");
			String word = entries[1];
			word = word.replaceAll("^\"|\"$", "");
			String frequency = entries[13];
//			System.out.println(word+" "+frequency);
			cfd.addSample(1, word, Integer.parseInt(frequency));
		}
		br.close();
		n_gram_size = 1;
		
		num_candidates_per_method = 3;
//		runErrorCorrectionUnigrams("Litkey_missingSpaces_hunspell_web1t_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "missing_spaces", litkey_lang, litkey_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, true);
		runErrorCorrectionUnigrams("Litkey_grapheme_hunspell_childlex_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "grapheme", litkey_lang, litkey_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, true, cfd);
		runErrorCorrectionUnigrams("Litkey_keyboard_hunspell_childlex_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "keyboard", litkey_lang, litkey_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, true, cfd);
		runErrorCorrectionUnigrams("Litkey_phoneme_hunspell_childlex_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "phoneme", litkey_lang, litkey_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, true, cfd);
		runErrorCorrectionUnigrams("Litkey_graphemePhoneme_hunspell_childlex_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "graphemePhoneme", litkey_lang, litkey_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, true, cfd);
		runErrorCorrectionUnigrams("Litkey_fullUni_hunspell_childlex_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "fullUni", litkey_lang, litkey_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, true, cfd);

		num_candidates_per_method = 10;
//		runErrorCorrectionUnigrams("Litkey_missingSpaces_hunspell_web1t_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "missing_spaces", litkey_lang, litkey_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, true);
		runErrorCorrectionUnigrams("Litkey_grapheme_hunspell_childlex_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "grapheme", litkey_lang, litkey_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, true, cfd);
		runErrorCorrectionUnigrams("Litkey_keyboard_hunspell_childlex_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "keyboard", litkey_lang, litkey_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, true, cfd);
		runErrorCorrectionUnigrams("Litkey_phoneme_hunspell_childlex_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "phoneme", litkey_lang, litkey_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, true, cfd);
		runErrorCorrectionUnigrams("Litkey_graphemePhoneme_hunspell_childlex_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "graphemePhoneme", litkey_lang, litkey_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, true, cfd);	
		runErrorCorrectionUnigrams("Litkey_fullUni_hunspell_childlex_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "fullUni", litkey_lang, litkey_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, true, cfd);	

	}
	
	private static void runMerlinDE_childlex_unigrams(int num_candidates_per_method, int n_gram_size) throws UIMAException, IOException {
		String merlin_lang = "de";
		String merlin_path = "src/main/resources/corpora/Merlin_spelling_german.xml";
		String hunspell_de = "src/main/resources/dictionaries/hunspell_DE.txt";
		String phonetic_hunspell_de = "src/main/resources/dictionaries/hunspell_DE_phoneme_map.txt";
		String keyboard_distances_de = "src/main/resources/matrixes/keyboardDistance_DE-manual.txt";
		String unigram_file = "src/main/resources/dictionaries/childLex_0.17.01_clean.tsv";
		
		ConditionalFrequencyDistribution<Integer, String> cfd = new ConditionalFrequencyDistribution<Integer, String>();
		BufferedReader br = new BufferedReader(new FileReader(new File(unigram_file)));
		String firstLine = br.readLine();
		while(br.ready()) {
			String line = br.readLine();
			String[] entries = line.split("\t");
			String word = entries[1];
			word = word.replaceAll("^\"|\"$", "");
			String frequency = entries[13];
//			System.out.println(word+" "+frequency);
			cfd.addSample(1, word, Integer.parseInt(frequency));
		}
		br.close();
		n_gram_size = 1;
		
		num_candidates_per_method = 3;
//		runErrorCorrectionUnigrams("MerlinDE_missingSpaces_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "missing_spaces", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("MerlinDE_grapheme_hunspell_childlex_"+num_candidates_per_method+"_"+n_gram_size, "grapheme", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("MerlinDE_phoneme_hunspell_childlex_"+num_candidates_per_method+"_"+n_gram_size, "phoneme", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("MerlinDE_keyboard_hunspell_childlex_"+num_candidates_per_method+"_"+n_gram_size, "keyboard", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("MerlinDE_graphemePhoneme_hunspell_childlex_"+num_candidates_per_method+"_"+n_gram_size, "graphemePhoneme", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("MerlinDE_full_hunspell_childlex_"+num_candidates_per_method+"_"+n_gram_size, "fullUni", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, false, cfd);
		
		num_candidates_per_method = 10;
//		runErrorCorrectionUnigrams("MerlinDE_missingSpaces_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "missing_spaces", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("MerlinDE_grapheme_hunspell_childlex_"+num_candidates_per_method+"_"+n_gram_size, "grapheme", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("MerlinDE_phoneme_hunspell_childlex_"+num_candidates_per_method+"_"+n_gram_size, "phoneme", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("MerlinDE_keyboard_hunspell_childlex_"+num_candidates_per_method+"_"+n_gram_size, "keyboard", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("MerlinDE_graphemePhoneme_hunspell_childlex_"+num_candidates_per_method+"_"+n_gram_size, "graphemePhoneme", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("MerlinDE_full_hunspell_childlex_"+num_candidates_per_method+"_"+n_gram_size, "fullUni", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, false, cfd);
	}
	
	private static void runSkala_childlex_unigrams(int num_candidates_per_method, int n_gram_size) throws UIMAException, IOException {
		String merlin_lang = "de";
		String merlin_path = "src/main/resources/corpora/SkalaErrors_merged.xml";
		String hunspell_de = "src/main/resources/dictionaries/hunspell_DE.txt";
		String phonetic_hunspell_de = "src/main/resources/dictionaries/hunspell_DE_phoneme_map.txt";
		String keyboard_distances_de = "src/main/resources/matrixes/keyboardDistance_DE-manual.txt";
		String unigram_file = "src/main/resources/dictionaries/childLex_0.17.01_clean.tsv";
		
		ConditionalFrequencyDistribution<Integer, String> cfd = new ConditionalFrequencyDistribution<Integer, String>();
		BufferedReader br = new BufferedReader(new FileReader(new File(unigram_file)));
		String firstLine = br.readLine();
		while(br.ready()) {
			String line = br.readLine();
			String[] entries = line.split("\t");
			String word = entries[1];
			word = word.replaceAll("^\"|\"$", "");
			String frequency = entries[13];
//			System.out.println(word+" "+frequency);
			cfd.addSample(1, word, Integer.parseInt(frequency));
		}
		br.close();
		n_gram_size = 1;
		
		num_candidates_per_method = 3;
//		runErrorCorrectionUnigrams("Skala_missingSpaces_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "missing_spaces", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("Skala_grapheme_hunspell_childlex_"+num_candidates_per_method+"_"+n_gram_size, "grapheme", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, false, cfd);
//		runErrorCorrectionUnigrams("Skala_phoneme_hunspell_childlex_"+num_candidates_per_method+"_"+n_gram_size, "phoneme", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, false, cfd);
//		runErrorCorrectionUnigrams("Skala_keyboard_hunspell_childlex_"+num_candidates_per_method+"_"+n_gram_size, "keyboard", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, false, cfd);
//		runErrorCorrectionUnigrams("Skala_graphemePhoneme_hunspell_childlex_"+num_candidates_per_method+"_"+n_gram_size, "graphemePhoneme", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, false, cfd);
//		runErrorCorrectionUnigrams("Skala_full_hunspell_childlex_"+num_candidates_per_method+"_"+n_gram_size, "fullUni", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, false, cfd);
		
		num_candidates_per_method = 10;
//		runErrorCorrectionUnigrams("Skala_missingSpaces_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "missing_spaces", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("Skala_grapheme_hunspell_childlex_"+num_candidates_per_method+"_"+n_gram_size, "grapheme", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, false, cfd);
//		runErrorCorrectionUnigrams("Skala_phoneme_hunspell_childlex_"+num_candidates_per_method+"_"+n_gram_size, "phoneme", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, false, cfd);
//		runErrorCorrectionUnigrams("Skala_keyboard_hunspell_childlex_"+num_candidates_per_method+"_"+n_gram_size, "keyboard", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, false, cfd);
//		runErrorCorrectionUnigrams("Skala_graphemePhoneme_hunspell_childlex_"+num_candidates_per_method+"_"+n_gram_size, "graphemePhoneme", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, false, cfd);
//		runErrorCorrectionUnigrams("Skala_full_hunspell_childlex_"+num_candidates_per_method+"_"+n_gram_size, "fullUni", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, false, cfd);
	}
	
	private static void runLitkey_subtlex_unigrams(int num_candidates_per_method, int n_gram_size) throws UIMAException, IOException {
		String litkey_lang = "de";
		String litkey_path = "src/main/resources/corpora/litkey_spelling.xml";
		String hunspell_de = "src/main/resources/dictionaries/hunspell_DE.txt";
		String phonetic_hunspell_de = "src/main/resources/dictionaries/hunspell_DE_phoneme_map.txt";
		String keyboard_distances_de = "src/main/resources/matrixes/keyboardDistance_DE-manual.txt";
		String unigram_file = "src/main/resources/dictionaries/subtlex_de.txt";
		
		ConditionalFrequencyDistribution<Integer, String> cfd = new ConditionalFrequencyDistribution<Integer, String>();
//		BufferedReader br = new BufferedReader(new File(unigram_file),"UFT-8");
		BufferedReader br = Files.newBufferedReader(Paths.get(unigram_file),StandardCharsets.ISO_8859_1);
		String firstLine = br.readLine();
		while(br.ready()) {
			String line = br.readLine();
//			System.out.println(line);
			String[] entries = line.split("\t");
			String word = entries[0];
			word = word.replaceAll("^\"|\"$", "");
			String frequency = entries[1];
			System.out.println(word+" "+frequency);
			cfd.addSample(1, word, Integer.parseInt(frequency));
		}
		br.close();
		n_gram_size = 1;
		
		num_candidates_per_method = 3;
//		runErrorCorrectionUnigrams("Litkey_missingSpaces_hunspell_web1t_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "missing_spaces", litkey_lang, litkey_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, web1t_path_de, num_candidates_per_method, n_gram_size, true);
		runErrorCorrectionUnigrams("Litkey_grapheme_hunspell_subtlex_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "grapheme", litkey_lang, litkey_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, true, cfd);
		runErrorCorrectionUnigrams("Litkey_keyboard_hunspell_subtlex_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "keyboard", litkey_lang, litkey_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, true, cfd);
		runErrorCorrectionUnigrams("Litkey_phoneme_hunspell_subtlex_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "phoneme", litkey_lang, litkey_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, true, cfd);
		runErrorCorrectionUnigrams("Litkey_graphemePhoneme_hunspell_subtlex_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "graphemePhoneme", litkey_lang, litkey_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, true, cfd);
		runErrorCorrectionUnigrams("Litkey_fullUni_hunspell_subtlex_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "fullUni", litkey_lang, litkey_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, true, cfd);

		num_candidates_per_method = 10;
//		runErrorCorrectionUnigrams("Litkey_missingSpaces_hunspell_web1t_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "missing_spaces", litkey_lang, litkey_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, web1t_path_de, num_candidates_per_method, n_gram_size, true);
		runErrorCorrectionUnigrams("Litkey_grapheme_hunspell_subtlex_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "grapheme", litkey_lang, litkey_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, true, cfd);
		runErrorCorrectionUnigrams("Litkey_keyboard_hunspell_subtlex_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "keyboard", litkey_lang, litkey_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, true, cfd);
		runErrorCorrectionUnigrams("Litkey_phoneme_hunspell_subtlex_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "phoneme", litkey_lang, litkey_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, true, cfd);
		runErrorCorrectionUnigrams("Litkey_graphemePhoneme_hunspell_subtlex_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "graphemePhoneme", litkey_lang, litkey_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, true, cfd);	
		runErrorCorrectionUnigrams("Litkey_fullUni_hunspell_subtlex_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "fullUni", litkey_lang, litkey_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, true, cfd);	

	}
	
	private static void runMerlinDE_subtlex_unigrams(int num_candidates_per_method, int n_gram_size) throws UIMAException, IOException {
		String merlin_lang = "de";
		String merlin_path = "src/main/resources/corpora/Merlin_spelling_german.xml";
		String hunspell_de = "src/main/resources/dictionaries/hunspell_DE.txt";
		String phonetic_hunspell_de = "src/main/resources/dictionaries/hunspell_DE_phoneme_map.txt";
		String keyboard_distances_de = "src/main/resources/matrixes/keyboardDistance_DE-manual.txt";
		String unigram_file = "src/main/resources/dictionaries/subtlex_de.txt";
		
		ConditionalFrequencyDistribution<Integer, String> cfd = new ConditionalFrequencyDistribution<Integer, String>();
//		BufferedReader br = new BufferedReader(new File(unigram_file),"UFT-8");
		BufferedReader br = Files.newBufferedReader(Paths.get(unigram_file),StandardCharsets.ISO_8859_1);
		String firstLine = br.readLine();
		while(br.ready()) {
			String line = br.readLine();
//			System.out.println(line);
			String[] entries = line.split("\t");
			String word = entries[0];
			word = word.replaceAll("^\"|\"$", "");
			String frequency = entries[1];
			System.out.println(word+" "+frequency);
			cfd.addSample(1, word, Integer.parseInt(frequency));
		}
		br.close();
		n_gram_size = 1;
		
		num_candidates_per_method = 3;
//		runErrorCorrectionUnigrams("MerlinDE_missingSpaces_hunspell_subtlex_"+num_candidates_per_method+"_"+n_gram_size, "missing_spaces", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("MerlinDE_grapheme_hunspell_subtlex_"+num_candidates_per_method+"_"+n_gram_size, "grapheme", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("MerlinDE_phoneme_hunspell_subtlex_"+num_candidates_per_method+"_"+n_gram_size, "phoneme", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("MerlinDE_keyboard_hunspell_subtlex_"+num_candidates_per_method+"_"+n_gram_size, "keyboard", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("MerlinDE_graphemePhoneme_hunspell_subtlex_"+num_candidates_per_method+"_"+n_gram_size, "graphemePhoneme", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("MerlinDE_full_hunspell_subtlex_"+num_candidates_per_method+"_"+n_gram_size, "fullUni", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, false, cfd);
		
		num_candidates_per_method = 10;
//		runErrorCorrectionUnigrams("MerlinDE_missingSpaces_subtlex_web1t_"+num_candidates_per_method+"_"+n_gram_size, "missing_spaces", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("MerlinDE_grapheme_hunspell_subtlex_"+num_candidates_per_method+"_"+n_gram_size, "grapheme", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("MerlinDE_phoneme_hunspell_subtlex_"+num_candidates_per_method+"_"+n_gram_size, "phoneme", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("MerlinDE_keyboard_hunspell_subtlex_"+num_candidates_per_method+"_"+n_gram_size, "keyboard", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("MerlinDE_graphemePhoneme_hunspell_subtlex_"+num_candidates_per_method+"_"+n_gram_size, "graphemePhoneme", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("MerlinDE_full_hunspell_subtlex_"+num_candidates_per_method+"_"+n_gram_size, "fullUni", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, false, cfd);

	}
	
	private static void runSkala_subtlex_unigrams(int num_candidates_per_method, int n_gram_size) throws UIMAException, IOException {
		String merlin_lang = "de";
		String merlin_path = "src/main/resources/corpora/skalaErrors_merged.xml";
		String hunspell_de = "src/main/resources/dictionaries/hunspell_DE.txt";
		String phonetic_hunspell_de = "src/main/resources/dictionaries/hunspell_DE_phoneme_map.txt";
		String keyboard_distances_de = "src/main/resources/matrixes/keyboardDistance_DE-manual.txt";
		String unigram_file = "src/main/resources/dictionaries/subtlex_de.txt";
		
		ConditionalFrequencyDistribution<Integer, String> cfd = new ConditionalFrequencyDistribution<Integer, String>();
//		BufferedReader br = new BufferedReader(new File(unigram_file),"UFT-8");
		BufferedReader br = Files.newBufferedReader(Paths.get(unigram_file),StandardCharsets.ISO_8859_1);
		String firstLine = br.readLine();
		while(br.ready()) {
			String line = br.readLine();
//			System.out.println(line);
			String[] entries = line.split("\t");
			String word = entries[0];
			word = word.replaceAll("^\"|\"$", "");
			String frequency = entries[1];
			System.out.println(word+" "+frequency);
			cfd.addSample(1, word, Integer.parseInt(frequency));
		}
		br.close();
		n_gram_size = 1;
		
		num_candidates_per_method = 3;
//		runErrorCorrectionUnigrams(Skala_missingSpaces_hunspell_subtlex_"+num_candidates_per_method+"_"+n_gram_size, "missing_spaces", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("Skala_grapheme_hunspell_subtlex_"+num_candidates_per_method+"_"+n_gram_size, "grapheme", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, false, cfd);
//		runErrorCorrectionUnigrams("Skala_phoneme_hunspell_subtlex_"+num_candidates_per_method+"_"+n_gram_size, "phoneme", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, false, cfd);
//		runErrorCorrectionUnigrams("Skala_keyboard_hunspell_subtlex_"+num_candidates_per_method+"_"+n_gram_size, "keyboard", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, false, cfd);
//		runErrorCorrectionUnigrams("Skala_graphemePhoneme_hunspell_subtlex_"+num_candidates_per_method+"_"+n_gram_size, "graphemePhoneme", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, false, cfd);
//		runErrorCorrectionUnigrams("Skala_full_hunspell_subtlex_"+num_candidates_per_method+"_"+n_gram_size, "fullUni", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, false, cfd);
		
		num_candidates_per_method = 10;
//		runErrorCorrectionUnigrams("Skala_missingSpaces_subtlex_web1t_"+num_candidates_per_method+"_"+n_gram_size, "missing_spaces", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("Skala_grapheme_hunspell_subtlex_"+num_candidates_per_method+"_"+n_gram_size, "grapheme", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, false, cfd);
//		runErrorCorrectionUnigrams("Skala_phoneme_hunspell_subtlex_"+num_candidates_per_method+"_"+n_gram_size, "phoneme", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, false, cfd);
//		runErrorCorrectionUnigrams("Skala_keyboard_hunspell_subtlex_"+num_candidates_per_method+"_"+n_gram_size, "keyboard", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, false, cfd);
//		runErrorCorrectionUnigrams("Skala_graphemePhoneme_hunspell_subtlex_"+num_candidates_per_method+"_"+n_gram_size, "graphemePhoneme", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, false, cfd);
//		runErrorCorrectionUnigrams("Skala_full_hunspell_subtlex_"+num_candidates_per_method+"_"+n_gram_size, "fullUni", merlin_lang, merlin_path, hunspell_de, phonetic_hunspell_de, keyboard_distances_de, num_candidates_per_method, n_gram_size, false, cfd);

	}
	
	private static void runCITA_subtlex_unigrams(int num_candidates_per_method, int n_gram_size) throws UIMAException, IOException {
		
		String cita_lang = "it";
		String cita_path = "src/main/resources/corpora/cita_spelling.xml";
		String hunspell_it = "src/main/resources/dictionaries/hunspell_Italian_dict.txt";
		String phonetic_hunspell_it = "src/main/resources/dictionaries/hunspell_Italian_phoneme_map.txt";
		String keyboard_distances_it = "src/main/resources/matrixes/keyboardDistance_IT-manual.txt";
		String unigram_file = "src/main/resources/dictionaries/subtlex_it.tsv";
		
		ConditionalFrequencyDistribution<Integer, String> cfd = new ConditionalFrequencyDistribution<Integer, String>();
		BufferedReader br = Files.newBufferedReader(Paths.get(unigram_file), Charset.forName("UTF-8"));
		String firstLine = br.readLine();
		while(br.ready()) {
			String line = br.readLine();
//			System.out.println(line);
			String[] entries = line.split("\t");
			String word = entries[1];
			word = word.replaceAll("^\"|\"$", "");
			String frequency = entries[2];
			System.out.println(word+" "+frequency);
			cfd.addSample(1, word, Integer.parseInt(frequency));
		}
		br.close();
		n_gram_size = 1;

		num_candidates_per_method = 3;
//		runErrorCorrectionUnigrams("CItA_missingSpaces_hunspell_subtlex_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "missing_spaces", cita_lang, cita_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("CItA_grapheme_hunspell_subtlex_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "grapheme", cita_lang, cita_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("CItA_phoneme_hunspell_subtlex_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "phoneme", cita_lang, cita_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("CItA_keyboard_hunspell_subtlex_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "keyboard", cita_lang, cita_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("CItA_graphemePhoneme_hunspell_subtlex_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "graphemePhoneme", cita_lang, cita_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("CItA_full_hunspell_subtlex_numCand"+num_candidates_per_method+"_ngram"+n_gram_size, "fullUni", cita_lang, cita_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, num_candidates_per_method, n_gram_size, false, cfd);

		num_candidates_per_method = 10;
//		runErrorCorrectionUnigrams("CItA_missingSpaces_hunspell_subtlex_"+num_candidates_per_method+n_gram_size, "missing_spaces", cita_lang, cita_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("CItA_grapheme_hunspell_subtlex_"+num_candidates_per_method+n_gram_size, "grapheme", cita_lang, cita_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("CItA_phoneme_hunspell_subtlex_"+num_candidates_per_method+n_gram_size, "phoneme", cita_lang, cita_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("CItA_keyboard_hunspell_subtlex_"+num_candidates_per_method+n_gram_size, "keyboard", cita_lang, cita_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("CItA_graphemePhoneme_hunspell_subtlex_"+num_candidates_per_method+n_gram_size, "graphemePhoneme", cita_lang, cita_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("CItA_full_hunspell_subtlex_"+num_candidates_per_method+n_gram_size, "fullUni", cita_lang, cita_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, num_candidates_per_method, n_gram_size, false, cfd);

	}
	
	private static void runMerlinIT_subtlex_unigrams(int num_candidates_per_method, int n_gram_size) throws UIMAException, IOException {
		String merlin_lang = "it";
		String merlin_path = "src/main/resources/corpora/Merlin_spelling_italian.xml";
		String hunspell_it = "src/main/resources/dictionaries/hunspell_Italian_dict.txt";
		String phonetic_hunspell_it = "src/main/resources/dictionaries/hunspell_Italian_phoneme_map.txt";
		String keyboard_distances_it = "src/main/resources/matrixes/keyboardDistance_IT-manual.txt";
		String unigram_file = "src/main/resources/dictionaries/subtlex_it.tsv";
		
		ConditionalFrequencyDistribution<Integer, String> cfd = new ConditionalFrequencyDistribution<Integer, String>();
		BufferedReader br = Files.newBufferedReader(Paths.get(unigram_file), Charset.forName("UTF-8"));
		String firstLine = br.readLine();
		while(br.ready()) {
			String line = br.readLine();
//			System.out.println(line);
			String[] entries = line.split("\t");
			String word = entries[1];
			word = word.replaceAll("^\"|\"$", "");
			String frequency = entries[2];
			System.out.println(word+" "+frequency);
			cfd.addSample(1, word, Integer.parseInt(frequency));
		}
		br.close();
		n_gram_size = 1;
		
		num_candidates_per_method = 3;
//		runErrorCorrectionUnigrams("MerlinIT_missingSpaces_hunspell_subtlex_"+num_candidates_per_method+"_"+n_gram_size, "missing_spaces", merlin_lang, merlin_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("MerlinIT_grapheme_hunspell_subtlex_"+num_candidates_per_method+"_"+n_gram_size, "grapheme", merlin_lang, merlin_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("MerlinIT_phoneme_hunspell_subtlex_"+num_candidates_per_method+"_"+n_gram_size, "phoneme", merlin_lang, merlin_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("MerlinIT_keyboard_hunspell_subtlex_"+num_candidates_per_method+"_"+n_gram_size, "keyboard", merlin_lang, merlin_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("MerlinIT_graphemePhoneme_hunspell_subtlex_"+num_candidates_per_method+"_"+n_gram_size, "graphemePhoneme", merlin_lang, merlin_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, num_candidates_per_method, n_gram_size, false, cfd);	
		runErrorCorrectionUnigrams("MerlinIT_full_hunspell_subtlex_"+num_candidates_per_method+"_"+n_gram_size, "fullUni", merlin_lang, merlin_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, num_candidates_per_method, n_gram_size, false, cfd);	
		
		num_candidates_per_method = 10;
//		runErrorCorrectionUnigrams("MerlinIT_missingSpaces_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "missing_spaces", merlin_lang, merlin_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("MerlinIT_grapheme_hunspell_subtlex_"+num_candidates_per_method+"_"+n_gram_size, "grapheme", merlin_lang, merlin_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("MerlinIT_phoneme_hunspell_subtlex_"+num_candidates_per_method+"_"+n_gram_size, "phoneme", merlin_lang, merlin_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("MerlinIT_keyboard_hunspell_subtlex_"+num_candidates_per_method+"_"+n_gram_size, "keyboard", merlin_lang, merlin_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("MerlinIT_graphemePhoneme_hunspell_subtlex_"+num_candidates_per_method+"_"+n_gram_size, "graphemePhoneme", merlin_lang, merlin_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("MerlinIT_full_hunspell_subtlex_"+num_candidates_per_method+"_"+n_gram_size, "fullUni", merlin_lang, merlin_path, hunspell_it, phonetic_hunspell_it, keyboard_distances_it, num_candidates_per_method, n_gram_size, false, cfd);

	}
	
	private static void runETS_subtlex_unigrams(int num_candidates_per_method, int n_gram_size) throws UIMAException, IOException {
		String ets_lang = "en";
		String ets_path = "src/main/resources/corpora/ETS_spelling.xml";
		String hunspell_en = "src/main/resources/dictionaries/hunspell_en_US.txt";
		String phonetic_hunspell_en = "src/main/resources/dictionaries/hunspell_en_US_phoneme_map.txt";
		String keyboard_distances_en = "src/main/resources/matrixes/keyboardDistance_EN-manual.txt";
		String unigram_file = "src/main/resources/dictionaries/subtlex_en_us.csv";
		
		ConditionalFrequencyDistribution<Integer, String> cfd = new ConditionalFrequencyDistribution<Integer, String>();
		BufferedReader br = Files.newBufferedReader(Paths.get(unigram_file), Charset.forName("UTF-8"));
		String firstLine = br.readLine();
		while(br.ready()) {
			String line = br.readLine();
//			System.out.println(line);
			String[] entries = line.split(";");
			String word = entries[0];
			word = word.replaceAll("^\"|\"$", "");
			String frequency = entries[1];
			System.out.println(word+" "+frequency);
			cfd.addSample(1, word, Integer.parseInt(frequency));
		}
		br.close();
		n_gram_size = 1;
		
		num_candidates_per_method = 3;
//		runErrorCorrectionUnigrams("TOEFLSPELL_missingSpaces_hunspell_subtlex_"+num_candidates_per_method+"_"+n_gram_size, "missing_spaces", ets_lang, ets_path, hunspell_en, phonetic_hunspell_en, keyboard_distances_en, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("TOEFLSPELL_grapheme_hunspell_subtlex_"+num_candidates_per_method+"_"+n_gram_size, "grapheme", ets_lang, ets_path, hunspell_en, phonetic_hunspell_en, keyboard_distances_en, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("TOEFLSPELL_phoneme_hunspell_subtlex_"+num_candidates_per_method+"_"+n_gram_size, "phoneme", ets_lang, ets_path, hunspell_en, phonetic_hunspell_en, keyboard_distances_en, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("TOEFLSPELL_keyboard_hunspell_subtlex_"+num_candidates_per_method+"_"+n_gram_size, "keyboard", ets_lang, ets_path, hunspell_en, phonetic_hunspell_en, keyboard_distances_en, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("TOEFLSPELL_graphemePhoneme_hunspell_subtlex_"+num_candidates_per_method+"_"+n_gram_size, "graphemePhoneme", ets_lang, ets_path, hunspell_en, phonetic_hunspell_en, keyboard_distances_en, num_candidates_per_method, n_gram_size, false, cfd);	
		runErrorCorrectionUnigrams("TOEFLSPELL_full_hunspell_subtlex_"+num_candidates_per_method+"_"+n_gram_size, "fullUni", ets_lang, ets_path, hunspell_en, phonetic_hunspell_en, keyboard_distances_en, num_candidates_per_method, n_gram_size, false, cfd);	
		
		num_candidates_per_method = 10;
//		runErrorCorrectionUnigrams("TOEFLSPELL_missingSpaces_hunspell_web1t_"+num_candidates_per_method+"_"+n_gram_size, "missing_spaces", ets_lang, ets_path, hunspell_en, phonetic_hunspell_en, keyboard_distances_en, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("TOEFLSPELL_grapheme_hunspell_subtlex_"+num_candidates_per_method+"_"+n_gram_size, "grapheme", ets_lang, ets_path, hunspell_en, phonetic_hunspell_en, keyboard_distances_en, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("TOEFLSPELL_phoneme_hunspell_subtlex_"+num_candidates_per_method+"_"+n_gram_size, "phoneme", ets_lang, ets_path, hunspell_en, phonetic_hunspell_en, keyboard_distances_en, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("TOEFLSPELL_keyboard_hunspell_subtlex_"+num_candidates_per_method+"_"+n_gram_size, "keyboard", ets_lang, ets_path, hunspell_en, phonetic_hunspell_en, keyboard_distances_en, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("TOEFLSPELL_graphemePhoneme_hunspell_subtlex_"+num_candidates_per_method+"_"+n_gram_size, "graphemePhoneme", ets_lang, ets_path, hunspell_en, phonetic_hunspell_en, keyboard_distances_en, num_candidates_per_method, n_gram_size, false, cfd);
		runErrorCorrectionUnigrams("TOEFLSPELL_full_hunspell_subtlex_"+num_candidates_per_method+"_"+n_gram_size, "fullUni", ets_lang, ets_path, hunspell_en, phonetic_hunspell_en, keyboard_distances_en, num_candidates_per_method, n_gram_size, false, cfd);

	}

	private static void runErrorCorrection(String config_name, String setting, String lang, String corpus_path, String dict_path, String phonetic_dict_path,
			String keyboard_distance_path, String web1t_path, int num_candidates_per_method, int n_gram_size, boolean include_period) throws UIMAException, IOException {

		// Create web1t language model to set via parameter
		ExternalResourceDescription web1t = createExternalResourceDescription(Web1TFrequencyCountResource.class,
				Web1TFrequencyCountResource.PARAM_LANGUAGE, lang,
				Web1TFrequencyCountResource.PARAM_MIN_NGRAM_LEVEL, "1",
				Web1TFrequencyCountResource.PARAM_MAX_NGRAM_LEVEL, "4",
				Web1TFrequencyCountResource.PARAM_INDEX_PATH, web1t_path);
		
		ExternalResourceDescription dummy = createExternalResourceDescription(DummyFrequencyCountProvider.class);

		CollectionReader reader = getReader(corpus_path, lang);
		AnalysisEngineDescription segmenter;
			if(lang.equals("it")) {
				segmenter = createEngineDescription(OpenNlpSegmenter.class);
			}
			else if (lang.equals("cz")){
				segmenter = createEngineDescription(CoreNlpSegmenter.class, CoreNlpSegmenter.PARAM_LANGUAGE, "en");
			}
			else if (config_name.startsWith("Litkey")) {
				segmenter = createEngineDescription(RegexSegmenter.class,
//						RegexSegmenter.PARAM_WRITE_SENTENCE, false,
						RegexSegmenter.PARAM_SENTENCE_BOUNDARY_REGEX, " ([\\.!?\\n]+)|.$ "
						);
			}
			else if (lang.equals("en")) {
				segmenter = createEngineDescription(LanguageToolSegmenter.class);
			}
			else {
				segmenter = createEngineDescription(CoreNlpSegmenter.class);			
			}
		AnalysisEngineDescription markSentenceBeginnings = createEngineDescription(MarkSentenceBeginnings.class,
				MarkSentenceBeginnings.PARAM_INCLUDE_PERIOD, include_period);
		AnalysisEngineDescription lineBreakAnnotator = createEngineDescription(LineBreakAnnotator.class);
		AnalysisEngineDescription generateRankGrapheme = createEngineDescription(
				GenerateAndRank_LevenshteinGrapheme.class,
				GenerateAndRank_LevenshteinGrapheme.PARAM_DICTIONARIES, dict_path,
				GenerateAndRank_LevenshteinGrapheme.PARAM_LOWERCASE, false,
				GenerateAndRank_LevenshteinGrapheme.PARAM_NUM_OF_CANDIDATES_TO_GENERATE, num_candidates_per_method,
//				GenerateAndRank_LevenshteinGrapheme.PARAM_WEIGHT_FILE_DELETION,
//				"src/main/resources/matrixes/RDMatrix_deletion_EN.tsv",
//				GenerateAndRank_LevenshteinGrapheme.PARAM_WEIGHT_FILE_INSERTION,
//				"src/main/resources/matrixes/RDMatrix_insertion_EN.tsv",
//				GenerateAndRank_LevenshteinGrapheme.PARAM_WEIGHT_FILE_SUBSTITUTION,
//				"src/main/resources/matrixes/RDMatrix_substitution_EN.tsv",
//				GenerateAndRank_LevenshteinGrapheme.PARAM_WEIGHT_FILE_TRANSPOSITION,
//				"src/main/resources/matrixes/RDMatrix_transposition_EN.tsv",
				GenerateAndRank_LevenshteinGrapheme.PARAM_INCLUDE_TRANSPOSITION, true);
		AnalysisEngineDescription generateRankPhoneme = createEngineDescription(
				GenerateAndRank_LevenshteinPhoneme.class,
				GenerateAndRank_LevenshteinPhoneme.PARAM_DICTIONARIES, phonetic_dict_path,
				GenerateAndRank_LevenshteinPhoneme.PARAM_LANGUAGE, lang,
				GenerateAndRank_LevenshteinPhoneme.PARAM_NUM_OF_CANDIDATES_TO_GENERATE, num_candidates_per_method,
//				GenerateAndRank_LevenshteinPhoneme.PARAM_WEIGHT_FILE_DELETION,
//				"src/main/resources/matrixes/RDMatrix_deletion_Sampa.tsv",
//				GenerateAndRank_LevenshteinPhoneme.PARAM_WEIGHT_FILE_INSERTION,
//				"src/main/resources/matrixes/RDMatrix_insertion_Sampa.tsv",
//				GenerateAndRank_LevenshteinPhoneme.PARAM_WEIGHT_FILE_SUBSTITUTION,
//				"src/main/resources/matrixes/RDMatrix_substitution_Sampa.tsv",
//				GenerateAndRank_LevenshteinPhoneme.PARAM_WEIGHT_FILE_TRANSPOSITION,
//				"src/main/resources/matrixes/RDMatrix_transposition_Sampa.tsv",
				GenerateAndRank_LevenshteinPhoneme.PARAM_INCLUDE_TRANSPOSITION, true);
		AnalysisEngineDescription generateRankKeyboard = createEngineDescription(
				GenerateAndRank_KeyboardDistance.class,
				GenerateAndRank_KeyboardDistance.PARAM_DICTIONARIES, dict_path,
				GenerateAndRank_KeyboardDistance.PARAM_KEYBOARD_DISTANCES_FILE, keyboard_distance_path,
				GenerateAndRank_KeyboardDistance.PARAM_NUM_OF_CANDIDATES_TO_GENERATE, num_candidates_per_method,
				GenerateAndRank_KeyboardDistance.PARAM_INCLUDE_TRANSPOSITION, true);
		AnalysisEngineDescription generateRankMissingSpaces = createEngineDescription(
				GenerateAndRank_FindMissingSpace.class,
				GenerateAndRank_FindMissingSpace.PARAM_DICTIONARIES, dict_path,
				GenerateAndRank_FindMissingSpace.PARAM_NUM_OF_CANDIDATES_TO_GENERATE, num_candidates_per_method);
		AnalysisEngineDescription lmReranker = createEngineDescription(
				LanguageModelReranker.class,
				LanguageModelReranker.RES_LANGUAGE_MODEL, web1t,
//				LanguageModelReranker.RES_LANGUAGE_MODEL_PROMPT_SPECIFIC, dummy,
				LanguageModelReranker.PARAM_SPECIFIC_LM_WEIGHT, 0.0f,
				LanguageModelReranker.PARAM_NGRAM_SIZE, n_gram_size);
		AnalysisEngineDescription anomalyReplacer = createEngineDescription(
				SpellingAnomalyReplacer.class,
				SpellingAnomalyReplacer.PARAM_TYPES_TO_COPY, new String[] { "de.unidue.ltl.spelling.types.ExtendedSpellingAnomaly" });
		AnalysisEngineDescription changeApplier = createEngineDescription(ApplyChanges.class);
		AnalysisEngineDescription correctionEvaluator = createEngineDescription(
				EvaluateErrorCorrection.class,
				EvaluateErrorCorrection.PARAM_CONFIG_NAME, config_name);

		if(setting.equals("full")) {
			SimplePipeline.runPipeline(
					reader,
					segmenter,
					lineBreakAnnotator,
					markSentenceBeginnings,
					generateRankGrapheme,
					generateRankPhoneme,
					generateRankKeyboard,
					generateRankMissingSpaces,
					lmReranker,
					anomalyReplacer,
					changeApplier,
					segmenter,
					correctionEvaluator);
		}
		
		if(setting.equals("fullUni")) {
			SimplePipeline.runPipeline(
					reader,
					segmenter,
					lineBreakAnnotator,
					markSentenceBeginnings,
					generateRankGrapheme,
					generateRankPhoneme,
					generateRankKeyboard,
//					generateRankMissingSpaces,
					lmReranker,
					anomalyReplacer,
					changeApplier,
					segmenter,
					correctionEvaluator);
		}
		
		if(setting.equals("graphemePhoneme")) {
			SimplePipeline.runPipeline(
					reader,
					segmenter,
					lineBreakAnnotator,
					markSentenceBeginnings,
					generateRankGrapheme,
					generateRankPhoneme,
					lmReranker,
					anomalyReplacer,
					changeApplier,
					segmenter,
					correctionEvaluator);
		}
		
		else if(setting.equals("grapheme")) {
			SimplePipeline.runPipeline(
					reader,
					segmenter,
					lineBreakAnnotator,
					markSentenceBeginnings,
					generateRankGrapheme,
					lmReranker,
					anomalyReplacer,
					changeApplier,
					segmenter,
					correctionEvaluator);
			
		}
		else if(setting.equals("phoneme")) {
			SimplePipeline.runPipeline(
					reader,
					segmenter,
					lineBreakAnnotator,
					markSentenceBeginnings,
					generateRankPhoneme,
					lmReranker,
					anomalyReplacer,
					changeApplier,
					segmenter,
					correctionEvaluator);
			
		}
		else if(setting.equals("keyboard")) {
			SimplePipeline.runPipeline(
					reader,
					segmenter,
					lineBreakAnnotator,
					markSentenceBeginnings,
					generateRankKeyboard,
					lmReranker,
					anomalyReplacer,
					changeApplier,
					segmenter,
					correctionEvaluator);
			
		}
		else if(setting.equals("missing_spaces")) {
			SimplePipeline.runPipeline(
					reader,
					segmenter,
					lineBreakAnnotator,
					markSentenceBeginnings,
					generateRankMissingSpaces,
					lmReranker,
					anomalyReplacer,
					changeApplier,
					segmenter,
					correctionEvaluator);
		}
	}
	
	private static void runErrorCorrection_noReranking(String config_name, String setting, String lang, String corpus_path, String dict_path, String phonetic_dict_path,
			String keyboard_distance_path, int num_candidates_per_method, boolean include_period) throws UIMAException, IOException {
		
		CollectionReader reader = getReader(corpus_path, lang);
		AnalysisEngineDescription segmenter;
			if(lang.equals("it")) {
				segmenter = createEngineDescription(OpenNlpSegmenter.class);
			}
			else if (lang.equals("cz")){
				segmenter = createEngineDescription(CoreNlpSegmenter.class, CoreNlpSegmenter.PARAM_LANGUAGE, "en");
			}
			else if (config_name.startsWith("Litkey")) {
				segmenter = createEngineDescription(RegexSegmenter.class,
//						RegexSegmenter.PARAM_WRITE_SENTENCE, false,
						RegexSegmenter.PARAM_SENTENCE_BOUNDARY_REGEX, " ([\\.!?\\n]+)|.$ "
						);
			}
			else if (lang.equals("en")) {
				segmenter = createEngineDescription(LanguageToolSegmenter.class);
			}
			else {
				segmenter = createEngineDescription(CoreNlpSegmenter.class);			
			}
		AnalysisEngineDescription markSentenceBeginnings = createEngineDescription(MarkSentenceBeginnings.class,
				MarkSentenceBeginnings.PARAM_INCLUDE_PERIOD, include_period);
		AnalysisEngineDescription lineBreakAnnotator = createEngineDescription(LineBreakAnnotator.class);
		AnalysisEngineDescription generateRankGrapheme = createEngineDescription(
				GenerateAndRank_LevenshteinGrapheme.class,
				GenerateAndRank_LevenshteinGrapheme.PARAM_DICTIONARIES, dict_path,
				GenerateAndRank_LevenshteinGrapheme.PARAM_LOWERCASE, false,
				GenerateAndRank_LevenshteinGrapheme.PARAM_NUM_OF_CANDIDATES_TO_GENERATE, num_candidates_per_method,
//				GenerateAndRank_LevenshteinGrapheme.PARAM_WEIGHT_FILE_DELETION,
//				"src/main/resources/matrixes/RDMatrix_deletion_EN.tsv",
//				GenerateAndRank_LevenshteinGrapheme.PARAM_WEIGHT_FILE_INSERTION,
//				"src/main/resources/matrixes/RDMatrix_insertion_EN.tsv",
//				GenerateAndRank_LevenshteinGrapheme.PARAM_WEIGHT_FILE_SUBSTITUTION,
//				"src/main/resources/matrixes/RDMatrix_substitution_EN.tsv",
//				GenerateAndRank_LevenshteinGrapheme.PARAM_WEIGHT_FILE_TRANSPOSITION,
//				"src/main/resources/matrixes/RDMatrix_transposition_EN.tsv",
				GenerateAndRank_LevenshteinGrapheme.PARAM_INCLUDE_TRANSPOSITION, true);
		AnalysisEngineDescription generateRankPhoneme = createEngineDescription(
				GenerateAndRank_LevenshteinPhoneme.class,
				GenerateAndRank_LevenshteinPhoneme.PARAM_DICTIONARIES, phonetic_dict_path,
				GenerateAndRank_LevenshteinPhoneme.PARAM_LANGUAGE, lang,
				GenerateAndRank_LevenshteinPhoneme.PARAM_NUM_OF_CANDIDATES_TO_GENERATE, num_candidates_per_method,
//				GenerateAndRank_LevenshteinPhoneme.PARAM_WEIGHT_FILE_DELETION,
//				"src/main/resources/matrixes/RDMatrix_deletion_Sampa.tsv",
//				GenerateAndRank_LevenshteinPhoneme.PARAM_WEIGHT_FILE_INSERTION,
//				"src/main/resources/matrixes/RDMatrix_insertion_Sampa.tsv",
//				GenerateAndRank_LevenshteinPhoneme.PARAM_WEIGHT_FILE_SUBSTITUTION,
//				"src/main/resources/matrixes/RDMatrix_substitution_Sampa.tsv",
//				GenerateAndRank_LevenshteinPhoneme.PARAM_WEIGHT_FILE_TRANSPOSITION,
//				"src/main/resources/matrixes/RDMatrix_transposition_Sampa.tsv",
				GenerateAndRank_LevenshteinPhoneme.PARAM_INCLUDE_TRANSPOSITION, true);
		AnalysisEngineDescription generateRankKeyboard = createEngineDescription(
				GenerateAndRank_KeyboardDistance.class,
				GenerateAndRank_KeyboardDistance.PARAM_DICTIONARIES, dict_path,
				GenerateAndRank_KeyboardDistance.PARAM_KEYBOARD_DISTANCES_FILE, keyboard_distance_path,
				GenerateAndRank_KeyboardDistance.PARAM_NUM_OF_CANDIDATES_TO_GENERATE, num_candidates_per_method,
				GenerateAndRank_KeyboardDistance.PARAM_INCLUDE_TRANSPOSITION, true);
		AnalysisEngineDescription generateRankMissingSpaces = createEngineDescription(
				GenerateAndRank_FindMissingSpace.class,
				GenerateAndRank_FindMissingSpace.PARAM_DICTIONARIES, dict_path,
				GenerateAndRank_FindMissingSpace.PARAM_NUM_OF_CANDIDATES_TO_GENERATE, num_candidates_per_method);
		AnalysisEngineDescription anomalyReplacer = createEngineDescription(
				SpellingAnomalyReplacer.class,
				SpellingAnomalyReplacer.PARAM_TYPES_TO_COPY, new String[] { "de.unidue.ltl.spelling.types.ExtendedSpellingAnomaly" });
		AnalysisEngineDescription changeApplier = createEngineDescription(ApplyChanges.class);
		AnalysisEngineDescription correctionEvaluator = createEngineDescription(
				EvaluateErrorCorrection.class,
				EvaluateErrorCorrection.PARAM_CONFIG_NAME, config_name);

		if(setting.equals("full")) {
			SimplePipeline.runPipeline(
					reader,
					segmenter,
					lineBreakAnnotator,
					markSentenceBeginnings,
					generateRankGrapheme,
					generateRankPhoneme,
					generateRankKeyboard,
					generateRankMissingSpaces,
					anomalyReplacer,
					changeApplier,
					correctionEvaluator);
		}
		
		if(setting.equals("fullUni")) {
			SimplePipeline.runPipeline(
					reader,
					segmenter,
					lineBreakAnnotator,
					markSentenceBeginnings,
					generateRankGrapheme,
					generateRankPhoneme,
					generateRankKeyboard,
					anomalyReplacer,
					changeApplier,
					correctionEvaluator);
		}
		
		if(setting.equals("graphemePhoneme")) {
			SimplePipeline.runPipeline(
					reader,
					segmenter,
					lineBreakAnnotator,
					markSentenceBeginnings,
					generateRankGrapheme,
					generateRankPhoneme,
					anomalyReplacer,
					changeApplier,
					correctionEvaluator);
		}
		
		else if(setting.equals("grapheme")) {
			SimplePipeline.runPipeline(
					reader,
					segmenter,
					lineBreakAnnotator,
					markSentenceBeginnings,
					generateRankGrapheme,
					anomalyReplacer,
					changeApplier,
					correctionEvaluator);
			
		}
		else if(setting.equals("phoneme")) {
			SimplePipeline.runPipeline(
					reader,
					segmenter,
					lineBreakAnnotator,
					markSentenceBeginnings,
					generateRankPhoneme,
					anomalyReplacer,
					changeApplier,
					correctionEvaluator);
			
		}
		else if(setting.equals("keyboard")) {
			SimplePipeline.runPipeline(
					reader,
					segmenter,
					lineBreakAnnotator,
					markSentenceBeginnings,
					generateRankKeyboard,
					anomalyReplacer,
					changeApplier,
					correctionEvaluator);
			
		}
		else if(setting.equals("missing_spaces")) {
			SimplePipeline.runPipeline(
					reader,
					segmenter,
					lineBreakAnnotator,
					markSentenceBeginnings,
					generateRankMissingSpaces,
					anomalyReplacer,
					changeApplier,
					correctionEvaluator);
		}
	}
	
	private static void runErrorCorrectionUnigrams(String config_name, String setting, String lang, String corpus_path, String dict_path, String phonetic_dict_path,
			String keyboard_distance_path, int num_candidates_per_method, int n_gram_size, boolean include_period, ConditionalFrequencyDistribution<Integer, String> cfd) throws UIMAException, IOException {

		ExternalResourceDescription dummy = createExternalResourceDescription(DummyFrequencyCountProvider.class);
		
		CollectionReader reader = getReader(corpus_path, lang);
		AnalysisEngineDescription segmenter;
			if(lang.equals("it")) {
				segmenter = createEngineDescription(OpenNlpSegmenter.class);
			}
			else if (lang.equals("cz")){
				segmenter = createEngineDescription(CoreNlpSegmenter.class, CoreNlpSegmenter.PARAM_LANGUAGE, "en");
			}
			else if (config_name.startsWith("Litkey")) {
				segmenter = createEngineDescription(RegexSegmenter.class,
//						RegexSegmenter.PARAM_WRITE_SENTENCE, false,
						RegexSegmenter.PARAM_SENTENCE_BOUNDARY_REGEX, " ([\\.!?\\n]+)|.$ "
						);
			}
			else if (lang.equals("en")) {
				segmenter = createEngineDescription(LanguageToolSegmenter.class);
			}
			else {
				segmenter = createEngineDescription(CoreNlpSegmenter.class);			
			}
		AnalysisEngineDescription markSentenceBeginnings = createEngineDescription(MarkSentenceBeginnings.class,
				MarkSentenceBeginnings.PARAM_INCLUDE_PERIOD, include_period);
		AnalysisEngineDescription lineBreakAnnotator = createEngineDescription(LineBreakAnnotator.class);
		AnalysisEngineDescription generateRankGrapheme = createEngineDescription(
				GenerateAndRank_LevenshteinGrapheme.class,
				GenerateAndRank_LevenshteinGrapheme.PARAM_DICTIONARIES, dict_path,
				GenerateAndRank_LevenshteinGrapheme.PARAM_LOWERCASE, false,
				GenerateAndRank_LevenshteinGrapheme.PARAM_NUM_OF_CANDIDATES_TO_GENERATE, num_candidates_per_method,
//				GenerateAndRank_LevenshteinGrapheme.PARAM_WEIGHT_FILE_DELETION,
//				"src/main/resources/matrixes/RDMatrix_deletion_EN.tsv",
//				GenerateAndRank_LevenshteinGrapheme.PARAM_WEIGHT_FILE_INSERTION,
//				"src/main/resources/matrixes/RDMatrix_insertion_EN.tsv",
//				GenerateAndRank_LevenshteinGrapheme.PARAM_WEIGHT_FILE_SUBSTITUTION,
//				"src/main/resources/matrixes/RDMatrix_substitution_EN.tsv",
//				GenerateAndRank_LevenshteinGrapheme.PARAM_WEIGHT_FILE_TRANSPOSITION,
//				"src/main/resources/matrixes/RDMatrix_transposition_EN.tsv",
				GenerateAndRank_LevenshteinGrapheme.PARAM_INCLUDE_TRANSPOSITION, true);
		AnalysisEngineDescription generateRankPhoneme = createEngineDescription(
				GenerateAndRank_LevenshteinPhoneme.class,
				GenerateAndRank_LevenshteinPhoneme.PARAM_DICTIONARIES, phonetic_dict_path,
				GenerateAndRank_LevenshteinPhoneme.PARAM_LANGUAGE, lang,
				GenerateAndRank_LevenshteinPhoneme.PARAM_NUM_OF_CANDIDATES_TO_GENERATE, num_candidates_per_method,
//				GenerateAndRank_LevenshteinPhoneme.PARAM_WEIGHT_FILE_DELETION,
//				"src/main/resources/matrixes/RDMatrix_deletion_Sampa.tsv",
//				GenerateAndRank_LevenshteinPhoneme.PARAM_WEIGHT_FILE_INSERTION,
//				"src/main/resources/matrixes/RDMatrix_insertion_Sampa.tsv",
//				GenerateAndRank_LevenshteinPhoneme.PARAM_WEIGHT_FILE_SUBSTITUTION,
//				"src/main/resources/matrixes/RDMatrix_substitution_Sampa.tsv",
//				GenerateAndRank_LevenshteinPhoneme.PARAM_WEIGHT_FILE_TRANSPOSITION,
//				"src/main/resources/matrixes/RDMatrix_transposition_Sampa.tsv",
				GenerateAndRank_LevenshteinPhoneme.PARAM_INCLUDE_TRANSPOSITION, true);
		AnalysisEngineDescription generateRankKeyboard = createEngineDescription(
				GenerateAndRank_KeyboardDistance.class,
				GenerateAndRank_KeyboardDistance.PARAM_DICTIONARIES, dict_path,
				GenerateAndRank_KeyboardDistance.PARAM_KEYBOARD_DISTANCES_FILE, keyboard_distance_path,
				GenerateAndRank_KeyboardDistance.PARAM_NUM_OF_CANDIDATES_TO_GENERATE, num_candidates_per_method,
				GenerateAndRank_KeyboardDistance.PARAM_INCLUDE_TRANSPOSITION, true);
		AnalysisEngineDescription generateRankMissingSpaces = createEngineDescription(
				GenerateAndRank_FindMissingSpace.class,
				GenerateAndRank_FindMissingSpace.PARAM_DICTIONARIES, dict_path,
				GenerateAndRank_FindMissingSpace.PARAM_NUM_OF_CANDIDATES_TO_GENERATE, num_candidates_per_method);
		AnalysisEngineDescription lmReranker = createEngineDescription(
				LanguageModelReranker.class,
//				LanguageModelReranker.RES_LANGUAGE_MODEL, web1t,
//				LanguageModelReranker.RES_LANGUAGE_MODEL_PROMPT_SPECIFIC, dummy,
				LanguageModelReranker.PARAM_SPECIFIC_LM_WEIGHT, 0.0f,
				LanguageModelReranker.PARAM_NGRAM_SIZE, n_gram_size);
		AnalysisEngineDescription anomalyReplacer = createEngineDescription(
				SpellingAnomalyReplacer.class,
				SpellingAnomalyReplacer.PARAM_TYPES_TO_COPY, new String[] { "de.unidue.ltl.spelling.types.ExtendedSpellingAnomaly" });
		AnalysisEngineDescription changeApplier = createEngineDescription(ApplyChanges.class);
		AnalysisEngineDescription correctionEvaluator = createEngineDescription(
				EvaluateErrorCorrection.class,
				EvaluateErrorCorrection.PARAM_CONFIG_NAME, config_name);
		
		// Adding language model resources via SimpleResourceManager
		// TODO: Does not work if resources for more than one annotator are added
		CFDFrequencyCountProvider cfdResource = new CFDFrequencyCountProvider(cfd, lang);
		Map<String, Object> context = new HashMap<String, Object>();
		context.put(LanguageModelReranker.RES_LANGUAGE_MODEL, cfdResource);
//		context.put(LanguageModelReranker.RES_LANGUAGE_MODEL_PROMPT_SPECIFIC, cfdResource);
		SimpleNamedResourceManager resMgr = new SimpleNamedResourceManager();
		resMgr.setAutoWireEnabled(true);
		resMgr.setExternalContext(context);
		lmReranker.setResourceManagerConfiguration(new ResourceManagerConfiguration_impl());

		if(setting.equals("full")) {
			AnalysisEngineDescription spellingCorrector = AnalysisEngineFactory.createAggregateDescription(
					segmenter,
					lineBreakAnnotator,
					markSentenceBeginnings,
					generateRankGrapheme,
					generateRankPhoneme,
					generateRankKeyboard,
					generateRankMissingSpaces,
					lmReranker,
					anomalyReplacer,
					changeApplier,
					correctionEvaluator);
			AnalysisEngine ae = UIMAFramework.produceAnalysisEngine(spellingCorrector, resMgr, null);
			while (reader.hasNext()) {
				CAS cas = ae.newCAS();
				reader.getNext(cas);
				ae.process(cas);
			}
			ae.collectionProcessComplete();
					
//			SimplePipeline.runPipeline(
//					reader,
//					segmenter,
//					markSentenceBeginnings,
//					lineBreakAnnotator,
//					generateRankGrapheme,
//					generateRankPhoneme,
//					generateRankKeyboard,
//					generateRankMissingSpaces,
//					lmReranker,
//					anomalyReplacer,
//					changeApplier,
//					correctionEvaluator);
		}
		
		if(setting.equals("fullUni")) {
			
			AnalysisEngineDescription spellingCorrector = AnalysisEngineFactory.createAggregateDescription(
					segmenter,
					lineBreakAnnotator,
					markSentenceBeginnings,
					generateRankGrapheme,
					generateRankPhoneme,
					generateRankKeyboard,
					lmReranker,
					anomalyReplacer,
					changeApplier,
					correctionEvaluator);
			AnalysisEngine ae = UIMAFramework.produceAnalysisEngine(spellingCorrector, resMgr, null);
			while (reader.hasNext()) {
				CAS cas = ae.newCAS();
				reader.getNext(cas);
				ae.process(cas);
			}
			ae.collectionProcessComplete();
			
//			SimplePipeline.runPipeline(
//					reader,
//					segmenter,
//					markSentenceBeginnings,
//					lineBreakAnnotator,
//					generateRankGrapheme,
//					generateRankPhoneme,
//					generateRankKeyboard,
//					lmReranker,
//					anomalyReplacer,
//					changeApplier,
//					correctionEvaluator);
		}
		
		if(setting.equals("graphemePhoneme")) {
			
			AnalysisEngineDescription spellingCorrector = AnalysisEngineFactory.createAggregateDescription(
					segmenter,
					lineBreakAnnotator,
					markSentenceBeginnings,
					generateRankGrapheme,
					generateRankPhoneme,
					lmReranker,
					anomalyReplacer,
					changeApplier,
					correctionEvaluator);
			AnalysisEngine ae = UIMAFramework.produceAnalysisEngine(spellingCorrector, resMgr, null);
			while (reader.hasNext()) {
				CAS cas = ae.newCAS();
				reader.getNext(cas);
				ae.process(cas);
			}
			ae.collectionProcessComplete();
			
//			SimplePipeline.runPipeline(
//					reader,
//					segmenter,
//					markSentenceBeginnings,
//					lineBreakAnnotator,
//					generateRankGrapheme,
//					generateRankPhoneme,
//					lmReranker,
//					anomalyReplacer,
//					changeApplier,
//					correctionEvaluator);
		}
		
		else if(setting.equals("grapheme")) {
			
			AnalysisEngineDescription spellingCorrector = AnalysisEngineFactory.createAggregateDescription(
					segmenter,
					lineBreakAnnotator,
					markSentenceBeginnings,
					generateRankGrapheme,
					lmReranker,
					anomalyReplacer,
					changeApplier,
					correctionEvaluator);
			AnalysisEngine ae = UIMAFramework.produceAnalysisEngine(spellingCorrector, resMgr, null);
			while (reader.hasNext()) {
				CAS cas = ae.newCAS();
				reader.getNext(cas);
				ae.process(cas);
			}
			ae.collectionProcessComplete();
			
//			SimplePipeline.runPipeline(
//					reader,
//					segmenter,
//					markSentenceBeginnings,
//					lineBreakAnnotator,
//					generateRankGrapheme,
//					lmReranker,
//					anomalyReplacer,
//					changeApplier,
//					correctionEvaluator);
			
		}
		else if(setting.equals("phoneme")) {
			
			AnalysisEngineDescription spellingCorrector = AnalysisEngineFactory.createAggregateDescription(
					segmenter,
					lineBreakAnnotator,
					markSentenceBeginnings,
					generateRankPhoneme,
					lmReranker,
					anomalyReplacer,
					changeApplier,
					correctionEvaluator);
			AnalysisEngine ae = UIMAFramework.produceAnalysisEngine(spellingCorrector, resMgr, null);
			while (reader.hasNext()) {
				CAS cas = ae.newCAS();
				reader.getNext(cas);
				ae.process(cas);
			}
			ae.collectionProcessComplete();
			
//			SimplePipeline.runPipeline(
//					reader,
//					segmenter,
//					markSentenceBeginnings,
//					lineBreakAnnotator,
//					generateRankPhoneme,
//					lmReranker,
//					anomalyReplacer,
//					changeApplier,
//					correctionEvaluator);
			
		}
		else if(setting.equals("keyboard")) {
			
			AnalysisEngineDescription spellingCorrector = AnalysisEngineFactory.createAggregateDescription(
					segmenter,
					lineBreakAnnotator,
					markSentenceBeginnings,
					generateRankKeyboard,
					lmReranker,
					anomalyReplacer,
					changeApplier,
					correctionEvaluator);
			AnalysisEngine ae = UIMAFramework.produceAnalysisEngine(spellingCorrector, resMgr, null);
			while (reader.hasNext()) {
				CAS cas = ae.newCAS();
				reader.getNext(cas);
				ae.process(cas);
			}
			ae.collectionProcessComplete();
			
//			SimplePipeline.runPipeline(
//					reader,
//					segmenter,
//					markSentenceBeginnings,
//					lineBreakAnnotator,
//					generateRankKeyboard,
//					lmReranker,
//					anomalyReplacer,
//					changeApplier,
//					correctionEvaluator);
			
		}
		else if(setting.equals("missing_spaces")) {
			
			AnalysisEngineDescription spellingCorrector = AnalysisEngineFactory.createAggregateDescription(
					segmenter,
					lineBreakAnnotator,
					markSentenceBeginnings,
					generateRankMissingSpaces,
					lmReranker,
					anomalyReplacer,
					changeApplier,
					correctionEvaluator);
			AnalysisEngine ae = UIMAFramework.produceAnalysisEngine(spellingCorrector, resMgr, null);
			while (reader.hasNext()) {
				CAS cas = ae.newCAS();
				reader.getNext(cas);
				ae.process(cas);
			}
			ae.collectionProcessComplete();
			
//			SimplePipeline.runPipeline(
//					reader,
//					segmenter,
//					markSentenceBeginnings,
//					lineBreakAnnotator,
//					generateRankMissingSpaces,
//					lmReranker,
//					anomalyReplacer,
//					changeApplier,
//					correctionEvaluator);
		}
	}
	
	
	private static void runErrorCorrection_hunspell(String lang, String corpus_path, String config_name, String dic_path, String aff_path, int num_candidates_to_generate, String web1t_path, int n_gram_size, boolean reRank, boolean includePeriod) throws UIMAException, IOException {
		
		// Create web1t language model to set via parameter
		ExternalResourceDescription web1t = createExternalResourceDescription(Web1TFrequencyCountResource.class,
				Web1TFrequencyCountResource.PARAM_LANGUAGE, lang,
				Web1TFrequencyCountResource.PARAM_MIN_NGRAM_LEVEL, "1",
				Web1TFrequencyCountResource.PARAM_MAX_NGRAM_LEVEL, "3",
				Web1TFrequencyCountResource.PARAM_INDEX_PATH, web1t_path);
		
		CollectionReader reader = getReader(corpus_path, lang);
		
		AnalysisEngineDescription segmenter;
			if(lang.equals("it")) {
				segmenter = createEngineDescription(OpenNlpSegmenter.class);
			}
			else if (lang.equals("cz")){
				segmenter = createEngineDescription(CoreNlpSegmenter.class, CoreNlpSegmenter.PARAM_LANGUAGE, "en");
			}
			else if (config_name.startsWith("Litkey")) {
				segmenter = createEngineDescription(RegexSegmenter.class,
//						RegexSegmenter.PARAM_WRITE_SENTENCE, false,
						RegexSegmenter.PARAM_SENTENCE_BOUNDARY_REGEX, " ([\\.!?\\n]+)|.$ "
						);
			}
			else if (lang.equals("en")) {
				segmenter = createEngineDescription(LanguageToolSegmenter.class);
			}
			else {
				segmenter = createEngineDescription(CoreNlpSegmenter.class);			
			}
		AnalysisEngineDescription markSentenceBeginnings = createEngineDescription(MarkSentenceBeginnings.class,
				MarkSentenceBeginnings.PARAM_INCLUDE_PERIOD, includePeriod);
		AnalysisEngineDescription lineBreakAnnotator = createEngineDescription(LineBreakAnnotator.class);
		AnalysisEngineDescription generateRankHunspell = createEngineDescription(
				GenerateAndRank_Hunspell.class,
				GenerateAndRank_Hunspell.PARAM_DIC_FILE_PATH, dic_path,
				GenerateAndRank_Hunspell.PARAM_AFF_FILE_PATH, aff_path,
				GenerateAndRank_Hunspell.PARAM_USE_ALL_SUGGESTIONS, reRank,
				GenerateAndRank_Hunspell.PARAM_NUM_OF_CANDIDATES_TO_GENERATE,3,
				GenerateAndRank_Hunspell.PARAM_DICTIONARIES, new String[] {});
		
		AnalysisEngineDescription lmReranker = createEngineDescription(
				LanguageModelReranker.class,
				LanguageModelReranker.RES_LANGUAGE_MODEL, web1t,
//				LanguageModelReranker.RES_LANGUAGE_MODEL_PROMPT_SPECIFIC, dummy,
				LanguageModelReranker.PARAM_SPECIFIC_LM_WEIGHT, 0.0f,
				LanguageModelReranker.PARAM_NGRAM_SIZE, n_gram_size);
		
		AnalysisEngineDescription anomalyReplacer = createEngineDescription(
				SpellingAnomalyReplacer.class,
				SpellingAnomalyReplacer.PARAM_TYPES_TO_COPY, new String[] { "de.unidue.ltl.spelling.types.ExtendedSpellingAnomaly" });
		AnalysisEngineDescription changeApplier = createEngineDescription(ApplyChanges.class);
		AnalysisEngineDescription correctionEvaluator = createEngineDescription(
				EvaluateErrorCorrection.class,
				EvaluateErrorCorrection.PARAM_CONFIG_NAME, config_name);

		if(reRank) {
			SimplePipeline.runPipeline(
					reader,
					segmenter,
					lineBreakAnnotator,
					markSentenceBeginnings,
					generateRankHunspell,
					lmReranker,
					anomalyReplacer,
					changeApplier,
					correctionEvaluator);
		}
		else {
			SimplePipeline.runPipeline(
					reader,
					segmenter,
					lineBreakAnnotator,
					markSentenceBeginnings,
					generateRankHunspell,
					anomalyReplacer,
					changeApplier,
					correctionEvaluator);
		}
	}
	

	public static CollectionReader getReader(String path, String language) throws ResourceInitializationException {

		return CollectionReaderFactory.createReader(SpellingReader.class, SpellingReader.PARAM_SOURCE_FILE, path,
				SpellingReader.PARAM_LANGUAGE_CODE, language);
	}
}