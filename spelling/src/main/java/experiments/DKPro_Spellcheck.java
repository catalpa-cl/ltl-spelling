package experiments;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.ExternalResourceFactory.createExternalResourceDescription;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReaderDescription;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.resource.ExternalResourceDescription;
import org.apache.uima.resource.ResourceInitializationException;
import org.dkpro.core.frequency.resources.Web1TFrequencyCountResource;
import org.dkpro.core.io.text.TextReader;
import org.dkpro.core.io.webanno.tsv.WebannoTsv3XWriter;

import candidateReranking.LanguageModelReranker;
import generateAndRank.GenerateAndRank_LevenshteinGrapheme;
import preprocessing.DictionaryChecker;
import preprocessing.LineBreakAnnotator;
import preprocessing.MarkSentenceBeginnings;
import preprocessing.MarkTokensToConsider;
import preprocessing.MarkTokensToCorrect;
import preprocessing.NumericAnnotator;
import preprocessing.PunctuationAnnotator;
import normalization.TransferSpellingAnomalies_ForTSV;
import utils.AnalysisEngineProvider;

public class DKPro_Spellcheck {

	public static void main(String[] args) {

		String lang = args[0];
		String file = args[1];

//		String lang = "de";
//		String file = "src/main/resources/corpora/test_de.txt";

		try {
			if (lang.equals("en")) {
				runErrorCorrection(lang, file, "/dictionaries/hunspell_dict_en_US.txt",
						System.getenv("WEB1T") + "/" + lang);
			} else if (lang.equals("de")) {
				runErrorCorrection(lang, file, "/dictionaries/hunspell_dict_de.txt",
						System.getenv("WEB1T") + "/" + lang);
			} else if (lang.equals("cz")) {
				runErrorCorrection(lang, file, "/dictionaries/hunspell_dict_cz.txt",
						System.getenv("WEB1T") + "/" + lang);
			} else if (lang.equals("it")) {
				runErrorCorrection(lang, file, "/dictionaries/hunspell_dict_it.txt",
						System.getenv("WEB1T") + "/" + lang);
			} else {
				System.out.println("Cannot process language " + lang
						+ "! Supported langauges are German (\"de\"), English (\"en\"), Italian (\"it\"), and Czech (\"cz\").");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UIMAException e) {
			e.printStackTrace();
		}
	}

	private static void runErrorCorrection(String lang, String corpus_path, String dict_path, String web1t_path)
			throws UIMAException, IOException {

		// Create web1t language model to set via parameter
		ExternalResourceDescription web1t = createExternalResourceDescription(Web1TFrequencyCountResource.class,
				Web1TFrequencyCountResource.PARAM_LANGUAGE, lang, Web1TFrequencyCountResource.PARAM_MIN_NGRAM_LEVEL,
				"1", Web1TFrequencyCountResource.PARAM_MAX_NGRAM_LEVEL, "4",
				Web1TFrequencyCountResource.PARAM_INDEX_PATH, web1t_path);

		CollectionReaderDescription reader = createReaderDescription(TextReader.class, TextReader.PARAM_SOURCE_LOCATION,
				corpus_path, TextReader.PARAM_LANGUAGE, lang);

		AnalysisEngineDescription segmenter = AnalysisEngineProvider.getSegmenter(lang, "");
		AnalysisEngineDescription markSentenceBeginnings = createEngineDescription(MarkSentenceBeginnings.class,
				MarkSentenceBeginnings.PARAM_INCLUDE_PERIOD, false);
		AnalysisEngineDescription lineBreakAnnotator = createEngineDescription(LineBreakAnnotator.class);

		AnalysisEngineDescription numericAnnotator = createEngineDescription(NumericAnnotator.class);
		AnalysisEngineDescription punctuationAnnotator = createEngineDescription(PunctuationAnnotator.class);

		AnalysisEngineDescription namedEntityRecognizer = AnalysisEngineProvider.getNamedEntityRecognizer(lang);
		AnalysisEngineDescription markTokensToConsider = createEngineDescription(MarkTokensToConsider.class);

		AnalysisEngineDescription dictionaryChecker = createEngineDescription(DictionaryChecker.class,
				DictionaryChecker.PARAM_LANGUAGE, lang, DictionaryChecker.PARAM_DICTIONARY_FILE, dict_path,
				DictionaryChecker.PARAM_AUXILIARY_DICTIONARY_FILE, "");

		AnalysisEngineDescription markTokensToCorrect = createEngineDescription(MarkTokensToCorrect.class);

		AnalysisEngineDescription generateRankGrapheme = createEngineDescription(
				GenerateAndRank_LevenshteinGrapheme.class, GenerateAndRank_LevenshteinGrapheme.PARAM_DICTIONARIES,
				dict_path, GenerateAndRank_LevenshteinGrapheme.PARAM_LOWERCASE, false,
				GenerateAndRank_LevenshteinGrapheme.PARAM_NUM_OF_CANDIDATES_TO_GENERATE, 3,
				GenerateAndRank_LevenshteinGrapheme.PARAM_INCLUDE_TRANSPOSITION, true);
		AnalysisEngineDescription lmReranker = createEngineDescription(LanguageModelReranker.class,
				LanguageModelReranker.RES_LANGUAGE_MODEL, web1t, LanguageModelReranker.PARAM_NGRAM_SIZE, 3);

		AnalysisEngineDescription anomaly_collector = createEngineDescription(TransferSpellingAnomalies_ForTSV.class);

		AnalysisEngineDescription writer = createEngineDescription(WebannoTsv3XWriter.class,
				WebannoTsv3XWriter.PARAM_STRIP_EXTENSION, true, 
				WebannoTsv3XWriter.PARAM_OVERWRITE, true,
				WebannoTsv3XWriter.PARAM_TARGET_LOCATION, corpus_path.substring(0, corpus_path.lastIndexOf("/")),
				WebannoTsv3XWriter.PARAM_FILENAME_EXTENSION, "_spellchecked.tsv");

		SimplePipeline.runPipeline(reader, segmenter, markSentenceBeginnings, lineBreakAnnotator, numericAnnotator,
				punctuationAnnotator, namedEntityRecognizer, markTokensToConsider, dictionaryChecker,
				markTokensToCorrect, generateRankGrapheme, lmReranker, anomaly_collector, writer);

	}

	public static CollectionReader getReader(String path, String language) throws ResourceInitializationException {

		return CollectionReaderFactory.createReader(TextReader.class, TextReader.PARAM_SOURCE_LOCATION, path);
	}
}
