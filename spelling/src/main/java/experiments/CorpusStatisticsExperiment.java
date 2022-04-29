package experiments;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.resource.ResourceInitializationException;
import org.uimafit.pipeline.SimplePipeline;

import evaluation.OverlapOfCorrectionsWithDictionary;
import preprocessing.PunctuationAnnotator;
import reader.SpellingReader;
import utils.ErrorRatePrinter;
import utils.AnalysisEngineProvider;

public class CorpusStatisticsExperiment {

	public static void main(String[] args) throws UIMAException, IOException {

		System.out.println("Processing CItA");
		evalCItA();
		System.out.println();

		System.out.println("Processing MERLIN-IT");
		evalMerlinIT();
		System.out.println();

		System.out.println("Processing MERLIN-CZ");
		evalMerlinCZ();
		System.out.println();

		System.out.println("Processing TOEFL-SPELL");
		evalTOEFL();
		System.out.println();

		System.out.println("Processing MERLIN-DE");
		evalMerlinDE();
		System.out.println();

		System.out.println("Processing SkaLa");
		evalSkala();
		System.out.println();

		System.out.println("Processing Litkey");
		evalLitkey();

	}

	public static void evalSkala() throws UIMAException, IOException {
		String lang = "de";
		String corpus = "src/main/resources/corpora/skala_spelling.xml";
		String dict = "src/main/resources/dictionaries/hunspell_dict_de.txt";
		getErrorRate(lang, corpus, "SkaLa");
		getOverlapBetweenCorrectionsAndDict(lang, corpus, dict);
	}

	public static void evalTOEFL() throws UIMAException, IOException {
		String lang = "en";
		String corpus = "src/main/resources/corpora/toefl_spelling.xml";
		String dict = "src/main/resources/dictionaries/hunspell_dict_en_US.txt";
		getErrorRate(lang, corpus, "TOEFL-SPELL");
		getOverlapBetweenCorrectionsAndDict(lang, corpus, dict);
	}

	public static void evalCItA() throws UIMAException, IOException {
		String lang = "it";
		String corpus = "src/main/resources/corpora/cita_spelling.xml";
		String dict = "src/main/resources/dictionaries/hunspell_dict_it.txt";
		getErrorRate(lang, corpus, "CIta");
		getOverlapBetweenCorrectionsAndDict(lang, corpus, dict);
	}

	public static void evalLitkey() throws UIMAException, IOException {
		String lang = "de";
		String corpus = "src/main/resources/corpora/litkey_spelling.xml";
		String dict = "src/main/resources/dictionaries/hunspell_dict_de.txt";
//		String dict = "src/main/resources/dictionaries/childlex_litkey.txt";
		getErrorRate(lang, corpus, "Litkey");
		getOverlapBetweenCorrectionsAndDict(lang, corpus, dict);
	}

	public static void evalMerlinDE() throws UIMAException, IOException {
		String lang = "de";
		String corpus = "src/main/resources/corpora/Merlin_spelling_german.xml";
		String dict = "src/main/resources/dictionaries/hunspell_dict_de.txt";
//		String dict = "src/main/resources/dictionaries/childlex_litkey.txt";
		getErrorRate(lang, corpus, "MERLIN-DE");
		getOverlapBetweenCorrectionsAndDict(lang, corpus, dict);
	}

	public static void evalMerlinIT() throws UIMAException, IOException {
		String lang = "it";
		String corpus = "src/main/resources/corpora/merlin-IT_spelling.xml";
		String dict = "src/main/resources/dictionaries/hunspell_dict_it.txt";
		getErrorRate(lang, corpus, "MERLIN-IT");
		getOverlapBetweenCorrectionsAndDict(lang, corpus, dict);
	}

	public static void evalMerlinCZ() throws UIMAException, IOException {
		String lang = "cz";
		String corpus = "src/main/resources/corpora/merlin-CZ_spelling.xml";
		String dict = "src/main/resources/dictionaries/hunspell_dict_cz.txt";
		getErrorRate(lang, corpus, "MERLIN-CZ");
		getOverlapBetweenCorrectionsAndDict(lang, corpus, dict);
	}

	public static void getErrorRate(String lang, String corpus_path, String config_name)
			throws UIMAException, IOException {

		CollectionReader reader = getReader(corpus_path, lang);
		AnalysisEngineDescription segmenter = AnalysisEngineProvider.getSegmenter(lang, config_name);

//		Do not count punctuation
		AnalysisEngineDescription punctuationAnnotator = createEngineDescription(PunctuationAnnotator.class);
		AnalysisEngineDescription errorRatePrinter = createEngineDescription(ErrorRatePrinter.class);
		SimplePipeline.runPipeline(reader, segmenter, punctuationAnnotator, errorRatePrinter);
	}

	public static void getOverlapBetweenCorrectionsAndDict(String lang, String corpus_path, String dict_path)
			throws UIMAException, IOException {
		CollectionReader reader = getReader(corpus_path, lang);
		AnalysisEngineDescription overlap = createEngineDescription(OverlapOfCorrectionsWithDictionary.class,
				OverlapOfCorrectionsWithDictionary.PARAM_DICTIONARY_FILE, dict_path);
		SimplePipeline.runPipeline(reader, overlap);
	}

	public static CollectionReader getReader(String path, String language) throws ResourceInitializationException {

		return CollectionReaderFactory.createReader(SpellingReader.class, SpellingReader.PARAM_SOURCE_FILE, path,
				SpellingReader.PARAM_LANGUAGE_CODE, language, SpellingReader.PARAM_FOR_ERROR_DETECTION, true);
	}
}