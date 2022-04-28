package experiments;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.resource.ResourceInitializationException;
import org.dkpro.core.tokit.RegexSegmenter;
import org.uimafit.pipeline.SimplePipeline;

import de.tudarmstadt.ukp.dkpro.core.corenlp.CoreNlpSegmenter;
import de.tudarmstadt.ukp.dkpro.core.languagetool.LanguageToolSegmenter;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpSegmenter;
import evaluation.OverlapOfCorrectionsWithDictionary;
import preprocessing.PunctuationAnnotator;
import reader.SpellingReader;
import utils.ErrorRatePrinter;

public class CorpusStatisticsExperiment {

	public static void main(String[] args) throws UIMAException, IOException {
		
//		System.out.println("CItA");
//		evalCItA();
//		System.out.println();
//		System.out.println("Litkey");
//		evalLitkey();
//		System.out.println();
//		System.out.println("Merlin DE");
//		evalMerlinDE();
//		System.out.println();
//		System.out.println("Merlin IT");
//		evalMerlinIT();
//		System.out.println();
//		System.out.println("Merlin CZ");
//		evalMerlinCZ();
//		evalETS();
		evalSkala();
	}
	
	public static void evalSkala() throws UIMAException, IOException {
		String cita_lang = "de";
		String cita_corpus = "src/main/resources/corpora/skalaErrors_merged.xml";
		String cita_dict = "src/main/resources/dictionaries/hunspell_DE.txt";
		getErrorRate(cita_lang, cita_corpus, "Skala");
		getOverlapBetweenCorrectionsAndDict(cita_lang, cita_corpus, cita_dict);
	}
	
	public static void evalETS() throws UIMAException, IOException {
		String cita_lang = "en";
		String cita_corpus = "src/main/resources/corpora/ETS_spelling.xml";
		String cita_dict = "src/main/resources/dictionaries/hunspell_en_US.txt";
		getErrorRate(cita_lang, cita_corpus, "TOEFL");
		getOverlapBetweenCorrectionsAndDict(cita_lang, cita_corpus, cita_dict);
	}
	
	public static void evalETShigh() throws UIMAException, IOException {
		String cita_lang = "en";
		String cita_corpus = "src/main/resources/corpora/ETS_spelling_highScore.xml";
		String cita_dict = "src/main/resources/dictionaries/hunspell_en_US.txt";
		getErrorRate(cita_lang, cita_corpus, "TOEFLhigh");
		getOverlapBetweenCorrectionsAndDict(cita_lang, cita_corpus, cita_dict);
	}

	public static void evalCItA() throws UIMAException, IOException {
		String cita_lang = "it";
		String cita_corpus = "src/main/resources/corpora/CItA_spelling.xml";
		String cita_dict = "src/main/resources/dictionaries/hunspell_Italian_dict.txt";
		getErrorRate(cita_lang, cita_corpus, "Cita");
		getOverlapBetweenCorrectionsAndDict(cita_lang, cita_corpus, cita_dict);
	}

	public static void evalLitkey() throws UIMAException, IOException {
		String litkey_lang = "de";
		String litkey_corpus = "src/main/resources/corpora/litkey_spelling.xml";
		String litkey_dict = "src/main/resources/dictionaries/hunspell_DE.txt";
//		String litkey_dict = "src/main/resources/dictionaries/childlex_all.txt";
		getErrorRate(litkey_lang, litkey_corpus, "Litkey");
		getOverlapBetweenCorrectionsAndDict(litkey_lang, litkey_corpus, litkey_dict);
	}
	
	public static void evalMerlinDE() throws UIMAException, IOException {
		String merlin_lang = "de";
		String merlin_corpus = "src/main/resources/corpora/Merlin_spelling_german.xml";
//		String merlin_dict = "src/main/resources/dictionaries/hunspell_DE.txt";
		String merlin_dict = "src/main/resources/dictionaries/childlex_litkey.txt";
		getErrorRate(merlin_lang, merlin_corpus, "MerlinDE");
		getOverlapBetweenCorrectionsAndDict(merlin_lang, merlin_corpus, merlin_dict);
	}
	
	//TODO: include aux dict?
	public static void evalMerlinIT() throws UIMAException, IOException {
		String merlin_lang = "it";
		String merlin_corpus = "src/main/resources/corpora/Merlin_spelling_italian.xml";
		String merlin_dict = "src/main/resources/dictionaries/hunspell_Italian_dict.txt";
		getErrorRate(merlin_lang, merlin_corpus, "MerlinIT");
		getOverlapBetweenCorrectionsAndDict(merlin_lang, merlin_corpus, merlin_dict);
	}
	
	public static void evalMerlinCZ() throws UIMAException, IOException {
		String merlin_lang = "cz";
		String merlin_corpus = "src/main/resources/corpora/Merlin_spelling_czech.xml";
		String merlin_dict = "src/main/resources/dictionaries/hunspell_Czech_dict.txt";
		getErrorRate(merlin_lang, merlin_corpus, "MerlinCZ");
		getOverlapBetweenCorrectionsAndDict(merlin_lang, merlin_corpus, merlin_dict);
	}
	
	

	public static void getErrorRate(String lang, String corpus_path, String config_name) throws UIMAException, IOException {

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
//					RegexSegmenter.PARAM_WRITE_SENTENCE, false,
					RegexSegmenter.PARAM_SENTENCE_BOUNDARY_REGEX, " ([\\.!?\\n]+)|.$ "
					);
		}
		else if (lang.equals("en")) {
			segmenter = createEngineDescription(LanguageToolSegmenter.class);
		}
		else {
			segmenter = createEngineDescription(CoreNlpSegmenter.class);			
		}
//		Do not count punctuation
		AnalysisEngineDescription punctuationAnnotator = createEngineDescription(PunctuationAnnotator.class);
		AnalysisEngineDescription textPrinter = createEngineDescription(ErrorRatePrinter.class);
		SimplePipeline.runPipeline(reader, segmenter, punctuationAnnotator, textPrinter);
	}

	public static void getOverlapBetweenCorrectionsAndDict(String lang, String corpus_path, String dict_path)
			throws UIMAException, IOException {
		CollectionReader reader = getReader(corpus_path, lang);
		AnalysisEngineDescription overlap = createEngineDescription(OverlapOfCorrectionsWithDictionary.class,
				OverlapOfCorrectionsWithDictionary.PARAM_DICTIONARY_FILE, dict_path);
		SimplePipeline.runPipeline(reader, overlap);
	}

	public static CollectionReader getReader(String path, String language) throws ResourceInitializationException {

		return CollectionReaderFactory.createReader(SpellingReader.class,
				SpellingReader.PARAM_SOURCE_FILE, path,
				SpellingReader.PARAM_LANGUAGE_CODE, language,
				SpellingReader.PARAM_FOR_ERROR_DETECTION, true);
	}
}