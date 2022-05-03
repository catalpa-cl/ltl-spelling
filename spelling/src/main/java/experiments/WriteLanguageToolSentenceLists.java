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
import evaluation.WriteLangToolSentences;
import preprocessing.LineBreakAnnotator;
import preprocessing.MarkSentenceBeginnings;
import preprocessing.MarkTokensToConsider;
import preprocessing.NumericAnnotator;
import preprocessing.PunctuationAnnotator;
import reader.SpellingReader;
import utils.AnalysisEngineProvider;

public class WriteLanguageToolSentenceLists {

	public static void main(String[] args) throws UIMAException, IOException {

		runCItA();
		runLitkey();
		runMerlinDE();
		runMerlinIT();
		runMerlinCZ();
		runTOEFL();
		runSkaLa();

	}

	private static void runTOEFL() throws UIMAException, IOException {

		String lang = "en";
		String path = "src/main/resources/corpora/TOEFL_spelling.xml";
		runErrorDetection("TOEFL_sentences", lang, path, false);

	}

	private static void runLitkey() throws UIMAException, IOException {

		String lang = "de";
		String path = "src/main/resources/corpora/Litkey_spelling.xml";
		runErrorDetection("Litkey_sentences", lang, path, true);

	}

	private static void runCItA() throws UIMAException, IOException {

		String lang = "it";
		String path = "src/main/resources/corpora/CItA_spelling.xml";
		runErrorDetection("CItA_sentences", lang, path, false);
	}

	private static void runSkaLa() throws UIMAException, IOException {

		String lang = "de";
		String path = "src/main/resources/corpora/Skala_spelling.xml";
		runErrorDetection("SkaLa_sentences", lang, path, false);

	}

	private static void runMerlinDE() throws UIMAException, IOException {

		String lang = "de";
		String path = "src/main/resources/corpora/MERLIN-DE_spelling.xml";
		runErrorDetection("MERLIN-DE_sentences", lang, path, false);

	}

	private static void runMerlinIT() throws UIMAException, IOException {

		String lang = "it";
		String path = "src/main/resources/corpora/MERLIN-IT_spelling.xml";
		runErrorDetection("MERLIN-IT_sentences", lang, path, false);

	}

	private static void runMerlinCZ() throws UIMAException, IOException {

		String lang = "cz";
		String path = "src/main/resources/corpora/MERLIN-CZ_spelling.xml";
		runErrorDetection("MERLIN-CZ_sentences", lang, path, false);

	}

	private static void runErrorDetection(String config_name, String lang, String corpus_path,
			boolean periodsAreSentenceBoundaries) throws UIMAException, IOException {

		CollectionReader reader = getReader(corpus_path, lang);
		AnalysisEngineDescription segmenter = AnalysisEngineProvider.getSegmenter(lang, config_name);
		AnalysisEngineDescription markSentenceBeginnings = createEngineDescription(MarkSentenceBeginnings.class,
				MarkSentenceBeginnings.PARAM_INCLUDE_PERIOD, periodsAreSentenceBoundaries);
		AnalysisEngineDescription numericAnnotator = createEngineDescription(NumericAnnotator.class);
		AnalysisEngineDescription punctuationAnnotator = createEngineDescription(PunctuationAnnotator.class);
		AnalysisEngineDescription lineBreakAnnotator = createEngineDescription(LineBreakAnnotator.class);
		AnalysisEngineDescription namedEntityRecognizer = AnalysisEngineProvider.getNamedEntityRecognizer(lang);
		AnalysisEngineDescription markTokensToConsider = createEngineDescription(MarkTokensToConsider.class);
		AnalysisEngineDescription evaluate = createEngineDescription(WriteLangToolSentences.class,
				EvaluateErrorDetection.PARAM_CONFIG_NAME, config_name);

		SimplePipeline.runPipeline(reader, segmenter, markSentenceBeginnings, numericAnnotator, punctuationAnnotator,
				lineBreakAnnotator, namedEntityRecognizer, markTokensToConsider, evaluate);
	}

	public static CollectionReader getReader(String path, String language) throws ResourceInitializationException {

		return CollectionReaderFactory.createReader(SpellingReader.class, SpellingReader.PARAM_SOURCE_FILE, path,
				SpellingReader.PARAM_LANGUAGE_CODE, language, SpellingReader.PARAM_FOR_ERROR_DETECTION, false);
	}
}