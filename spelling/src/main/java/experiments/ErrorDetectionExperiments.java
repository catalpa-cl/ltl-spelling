package experiments;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.resource.ResourceInitializationException;

import evaluation.EvaluateErrorDetection;
import preprocessing.DictionaryChecker;
import preprocessing.HunspellDictionaryChecker;
import preprocessing.LineBreakAnnotator;
import preprocessing.MarkSentenceBeginnings;
import preprocessing.MarkTokensToConsider;
import preprocessing.MarkTokensToCorrect;
import preprocessing.NumericAnnotator;
import preprocessing.PunctuationAnnotator;
import reader.SpellingReader;
import utils.AnalysisEngineProvider;

public class ErrorDetectionExperiments {

	public static void main(String[] args) throws UIMAException, IOException {

		System.out.println("Processing CItA");
		runCItA();
		System.out.println();

		System.out.println("Processing MERLIN-IT");
		runMerlinIT();
		System.out.println();

		System.out.println("Processing MERLIN-CZ");
		runMerlinCZ();
		System.out.println();

		System.out.println("Processing TOEFL");
		runTOEFL();
		System.out.println();

		System.out.println("Processing SkaLa");
		runSkaLa();
		System.out.println();

		System.out.println("Processing MERLIN-DE");
		runMerlinDE();
		System.out.println();

		System.out.println("Processing Litkey");
		runLitkey();
		System.out.println();

	}

	private static void runTOEFL() throws UIMAException, IOException {

		String lang = "en";
		String path = "src/main/resources/corpora/toefl_spelling.xml";
		String dict = "/dictionaries/hunspell_dict_en_US.txt";
		String dic = "src/main/resources/dictionaries/hunspell/English_US.dic";
		String aff = "src/main/resources/dictionaries/hunspell/English_US.aff";
		runErrorDetection_hunspellTool("TOEFL_hunspellTool_web1t", lang, path, dic, aff, false);
		runErrorDetection_hunspellDict("TOEFL_hunspellDict_web1t", lang, path, dict, null, false, false);

	}

	private static void runLitkey() throws UIMAException, IOException {

		String lang = "de";
		String path = "src/main/resources/corpora/litkey_spelling.xml";
		String dict = "/dictionaries/hunspell_dict_de.txt";
		String dict_child = "/dictionaries/childLex_litkey.txt";
		String dic = "src/main/resources/dictionaries/hunspell/German_de_DE_aux.dic";
		String aff = "src/main/resources/dictionaries/hunspell/German_de_DE.aff";
		runErrorDetection_hunspellTool("Litkey_hunspellTool_web1t", lang, path, dic, aff, true);
		runErrorDetection_hunspellDict("Litkey_hunspellDict_web1t", lang, path, dict, null, false, true);
		runErrorDetection_hunspellDict("Litkey_childlexDict_web1t", lang, path, dict_child, null, false, true);

	}

	private static void runCItA() throws UIMAException, IOException {

		String lang = "it";
		String path = "src/main/resources/corpora/cita_spelling.xml";
		String dict = "/dictionaries/hunspell_dict_it.txt";
		String dic = "src/main/resources/dictionaries/hunspell/Italian_aux.dic";
		String aff = "src/main/resources/dictionaries/hunspell/Italian.aff";
		String italian_aux_dict = "/dictionaries/italian_include.txt";
		runErrorDetection_hunspellTool("CItA_hunspellTool_web1t", lang, path, dic, aff, false);
		runErrorDetection_hunspellDict("CItA_hunspellDict_web1t", lang, path, dict, italian_aux_dict, false, false);
	}

	private static void runSkaLa() throws UIMAException, IOException {

		String lang = "de";
		String path = "src/main/resources/corpora/skala_spelling.xml";
		String dict = "/dictionaries/hunspell_dict_de.txt";
		String dict_child = "/dictionaries/childLex_litkey.txt";
		String dic = "src/main/resources/dictionaries/hunspell/German_de_DE_aux.dic";
		String aff = "src/main/resources/dictionaries/hunspell/German_de_DE.aff";
		runErrorDetection_hunspellTool("SkaLa_hunspellTool_web1t", lang, path, dic, aff, false);
		runErrorDetection_hunspellDict("SkaLa_hunspellDict_web1t", lang, path, dict, null, false, false);
		runErrorDetection_hunspellDict("SkaLa_childLex_web1t", lang, path, dict_child, null, false, false);
	}

	private static void runMerlinDE() throws UIMAException, IOException {

		String lang = "de";
		String path = "src/main/resources/corpora/merlin-DE_spelling.xml";
		String dict = "/dictionaries/hunspell_dict_de.txt";
		String dict_child = "/dictionaries/childlex_litkey.txt";
		String dic = "src/main/resources/dictionaries/hunspell/German_de_DE_aux.dic";
		String aff = "src/main/resources/dictionaries/hunspell/German_de_DE.aff";
		runErrorDetection_hunspellTool("MERLIN-DE_hunspellTool_web1t", lang, path, dic, aff, false);
		runErrorDetection_hunspellDict("MerlinDE_hunspellDict_web1t", lang, path, dict, null, false, false);
		runErrorDetection_hunspellDict("MerlinDE_childLex_web1t", lang, path, dict_child, null, false, false);
	}

	private static void runMerlinIT() throws UIMAException, IOException {

		String lang = "it";
		String path = "src/main/resources/corpora/merlin-IT_spelling.xml";
		String dict = "/dictionaries/hunspell_dict_it.txt";
		String dic = "src/main/resources/dictionaries/hunspell/Italian_aux.dic";
		String aff = "src/main/resources/dictionaries/hunspell/Italian.aff";
		String italian_aux_dict = "/dictionaries/italian_include.txt";
		runErrorDetection_hunspellTool("MERLIN-IT_hunspellTool_web1t", lang, path, dic, aff, false);
		runErrorDetection_hunspellDict("MERLIN-IT_hunspellDict_web1t", lang, path, dict, italian_aux_dict, false,
				false);
	}

	private static void runMerlinCZ() throws UIMAException, IOException {

		String lang = "cz";
		String path = "src/main/resources/corpora/merlin-CZ_spelling.xml";
		String dict = "/dictionaries/hunspell_dict_cz.txt";
		String dic = "src/main/resources/dictionaries/hunspell/Czech.dic";
		String aff = "src/main/resources/dictionaries/hunspell/Czech.aff";
		runErrorDetection_hunspellTool("MERLIN-CZ_hunspellTool_web1t", lang, path, dic, aff, false);
		runErrorDetection_hunspellDict("MERLI-CZ_hunspellDict_web1t", lang, path, dict, null, false, false);
	}

	private static void runErrorDetection_hunspellDict(String config_name, String lang, String corpus_path,
			String dict_path, String aux_dict_path, boolean lowercase, boolean periodsAreSentenceBoundaries)
			throws UIMAException, IOException {

		CollectionReader reader = getReader(corpus_path, lang);
		AnalysisEngineDescription segmenter = AnalysisEngineProvider.getSegmenter(lang, config_name);
		AnalysisEngineDescription markSentenceBeginnings = createEngineDescription(MarkSentenceBeginnings.class,
				MarkSentenceBeginnings.PARAM_INCLUDE_PERIOD, periodsAreSentenceBoundaries);
		AnalysisEngineDescription numericAnnotator = createEngineDescription(NumericAnnotator.class);
		AnalysisEngineDescription punctuationAnnotator = createEngineDescription(PunctuationAnnotator.class);
		AnalysisEngineDescription lineBreakAnnotator = createEngineDescription(LineBreakAnnotator.class);
		AnalysisEngineDescription namedEntityRecognizer = AnalysisEngineProvider.getNamedEntityRecognizer(lang);
		AnalysisEngineDescription markTokensToConsider = createEngineDescription(MarkTokensToConsider.class);

		AnalysisEngineDescription dictionaryChecker = createEngineDescription(DictionaryChecker.class,
				DictionaryChecker.PARAM_LANGUAGE, lang, DictionaryChecker.PARAM_DICTIONARY_FILE, dict_path,
				DictionaryChecker.PARAM_AUXILIARY_DICTIONARY_FILE, aux_dict_path);

		AnalysisEngineDescription markTokensToCorrect = createEngineDescription(MarkTokensToCorrect.class);
		AnalysisEngineDescription evaluate = createEngineDescription(EvaluateErrorDetection.class,
				EvaluateErrorDetection.PARAM_CONFIG_NAME, config_name);

		SimplePipeline.runPipeline(reader, segmenter, markSentenceBeginnings, numericAnnotator, punctuationAnnotator,
				lineBreakAnnotator, namedEntityRecognizer, markTokensToConsider, dictionaryChecker, markTokensToCorrect,
				evaluate);
	}

	private static void runErrorDetection_hunspellTool(String config_name, String lang, String corpus_path,
			String dic_path, String aff_path, boolean periodsAreSentenceBoundaries) throws UIMAException, IOException {

		CollectionReader reader = getReader(corpus_path, lang);
		AnalysisEngineDescription segmenter = AnalysisEngineProvider.getSegmenter(lang, config_name);
		AnalysisEngineDescription markSentenceBeginnings = createEngineDescription(MarkSentenceBeginnings.class,
				MarkSentenceBeginnings.PARAM_INCLUDE_PERIOD, periodsAreSentenceBoundaries);
		AnalysisEngineDescription numericAnnotator = createEngineDescription(NumericAnnotator.class);
		AnalysisEngineDescription punctuationAnnotator = createEngineDescription(PunctuationAnnotator.class);
		AnalysisEngineDescription lineBreakAnnotator = createEngineDescription(LineBreakAnnotator.class);
		AnalysisEngineDescription namedEntityRecognizer = AnalysisEngineProvider.getNamedEntityRecognizer(lang);
		AnalysisEngineDescription markTokensToConsider = createEngineDescription(MarkTokensToConsider.class);

		AnalysisEngineDescription dictionaryChecker = createEngineDescription(HunspellDictionaryChecker.class,
				HunspellDictionaryChecker.PARAM_LANGUAGE, lang, HunspellDictionaryChecker.PARAM_DIC_FILE, dic_path,
				HunspellDictionaryChecker.PARAM_AFF_FILE, aff_path);

		AnalysisEngineDescription markTokensToCorrect = createEngineDescription(MarkTokensToCorrect.class);
		AnalysisEngineDescription evaluate = createEngineDescription(EvaluateErrorDetection.class,
				EvaluateErrorDetection.PARAM_CONFIG_NAME, config_name);

		SimplePipeline.runPipeline(reader, segmenter, markSentenceBeginnings, numericAnnotator, punctuationAnnotator,
				lineBreakAnnotator, namedEntityRecognizer, markTokensToConsider, dictionaryChecker, markTokensToCorrect,
				evaluate);
	}

	public static CollectionReader getReader(String path, String language) throws ResourceInitializationException {

		return CollectionReaderFactory.createReader(SpellingReader.class, SpellingReader.PARAM_SOURCE_FILE, path,
				SpellingReader.PARAM_LANGUAGE_CODE, language, SpellingReader.PARAM_FOR_ERROR_DETECTION, true);
	}
}