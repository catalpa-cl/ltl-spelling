package experiments;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.resource.ResourceInitializationException;
import org.uimafit.pipeline.SimplePipeline;

import evaluation.SpellingErrorListWriter;
import reader.SpellingReader;

// To pregenerate phonetic representations of misspellings in a corpus
// Necessary as not to bother the webservice with requests containing a single string
public class CollectMisspellingPhonemes {

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
		evalSkaLa();
		System.out.println();

		System.out.println("Processing Litkey");
		evalLitkey();

	}

	public static void evalSkaLa() throws UIMAException, IOException {
		String lang = "de";
		String corpus = "src/main/resources/corpora/skala_speling.xml";
		String outPath = "src/main/resources/dictionaries/skala_misspellingPhonemes.tsv";
		writeMisspellingPhonemes(lang, corpus, outPath);
	}

	public static void evalTOEFL() throws UIMAException, IOException {
		String lang = "en";
		String corpus = "src/main/resources/corpora/toefl_spelling.xml";
		String outPath = "src/main/resources/dictionaries/toefl_misspellingPhonemes.tsv";
		writeMisspellingPhonemes(lang, corpus, outPath);
	}

	public static void evalCItA() throws UIMAException, IOException {
		String lang = "it";
		String corpus = "src/main/resources/corpora/cita_spelling.xml";
		String outPath = "src/main/resources/dictionaries/cita_misspellingPhonemes.tsv";
		writeMisspellingPhonemes(lang, corpus, outPath);
	}

	public static void evalLitkey() throws UIMAException, IOException {
		String lang = "de";
		String corpus = "src/main/resources/corpora/litkey_spelling.xml";
		String outPath = "src/main/resources/dictionaries/litkey_misspellingPhonemes.tsv";
		writeMisspellingPhonemes(lang, corpus, outPath);
	}

	public static void evalMerlinDE() throws UIMAException, IOException {
		String lang = "de";
		String corpus = "src/main/resources/corpora/merlin-DE_spelling.xml";
		String outPath = "src/main/resources/dictionaries/merlin-DE_misspellingPhonemes.tsv";
		writeMisspellingPhonemes(lang, corpus, outPath);
	}

	public static void evalMerlinIT() throws UIMAException, IOException {
		String lang = "it";
		String corpus = "src/main/resources/corpora/merlin-IT_spelling.xml";
		String outPath = "src/main/resources/dictionaries/merlin-IT_misspellingPhonemes.tsv";
		writeMisspellingPhonemes(lang, corpus, outPath);
	}

	public static void evalMerlinCZ() throws UIMAException, IOException {
		String lang = "cz";
		String corpus = "src/main/resources/corpora/merlin-CZ_spelling.xml";
		String outPath = "src/main/resources/dictionaries/merlin-CZ_misspellingPhonemes.tsv";
		writeMisspellingPhonemes(lang, corpus, outPath);
	}

	public static void writeMisspellingPhonemes(String lang, String corpus_path, String outPath)
			throws UIMAException, IOException {

		CollectionReader reader = getReader(corpus_path, lang);
		AnalysisEngineDescription misspellingsToPhonemes = createEngineDescription(SpellingErrorListWriter.class,
				SpellingErrorListWriter.PARAM_LANGUAGE, lang, SpellingErrorListWriter.PARAM_OUTPUT_PATH, outPath);
		SimplePipeline.runPipeline(reader, misspellingsToPhonemes);
	}

	public static CollectionReader getReader(String path, String language) throws ResourceInitializationException {

		return CollectionReaderFactory.createReader(SpellingReader.class, SpellingReader.PARAM_SOURCE_FILE, path,
				SpellingReader.PARAM_LANGUAGE_CODE, language, SpellingReader.PARAM_FOR_ERROR_DETECTION, true);
	}
}