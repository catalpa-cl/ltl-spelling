package reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.apache.uima.UimaContext;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;
import org.dkpro.core.api.io.JCasResourceCollectionReader_ImplBase;
import org.uimafit.util.JCasUtil;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import spelling.types.ExtendedSpellingAnomaly;
import spelling.types.SpellingError;
import spelling.types.SpellingText;
import spelling.types.SuggestedActionWithOrigin;

public class SpellingReader_LanguageToolSuggestions extends JCasResourceCollectionReader_ImplBase {

	public static final String PARAM_ENCODING = "Encoding";
	@ConfigurationParameter(name = PARAM_ENCODING, mandatory = false, defaultValue = "UTF-8")
	public String encoding;

	public static final String PARAM_LANGUAGE_CODE = "Language";
	@ConfigurationParameter(name = PARAM_LANGUAGE_CODE, mandatory = true)
	private String language;

	public static final String PARAM_SOURCE_FILE = "sourceFile";
	@ConfigurationParameter(name = PARAM_SOURCE_FILE, mandatory = true)
	protected String sourceFile;

	/**
	 * If the aim is to detect errors, they will not be annotated to the JCas as
	 * SpellingAnomalies but as SpellingErrors
	 */
	public static final String PARAM_FOR_ERROR_DETECTION = "forErrorDetection";
	@ConfigurationParameter(name = PARAM_FOR_ERROR_DETECTION, mandatory = true, defaultValue = "false")
	protected boolean errorDetection;

	protected int currentIndex;
	Queue<SpellingItem_LanguageToolSuggestions> items;
	int numberOfTexts = -1;

	@Override
	public void initialize(UimaContext aContext) throws ResourceInitializationException {

		items = new LinkedList<SpellingItem_LanguageToolSuggestions>();

		int nrOfErrors = 0;
		int nrOfGrammarErrors = 0;

		BufferedReader br = null;
		String corpusName = sourceFile.substring(sourceFile.lastIndexOf("/"));
//		System.out.println("Corpus name: " + corpusName);

		try {
			br = new BufferedReader(new FileReader(new File(sourceFile)));
			while (br.ready()) {

				String line = br.readLine();

				if (line.endsWith("languagetool_corrections")) {
					continue;
				}

				String[] entries = line.split("\t");

				Map<String, String> correctionMap = new HashMap<String, String>();

				List<String> langtoolCorrections = new ArrayList<String>();

				String id = entries[0];
//				System.out.println("Text ID: " + id);

				String text = entries[1];
//				System.out.println("Text: " + text);

				String langtool = entries[8];
				if (langtool.length() > 0) {
					langtool = langtool.substring(1, langtool.length() - 1);
				}

				if (langtool.length() > 0) {
					String[] langtoolArray = langtool.split("', '");

					for (String element : langtoolArray) {
						element = element.trim();
						element = element.replaceAll("^'", "");
						element = element.replaceAll("'$", "");
//						System.out.println(element);
						langtoolCorrections.add(element);
					}
				}

				nrOfErrors++;
				String misspelling = entries[2];
				String correction = entries[6];

				int startIndex = (int) Float.parseFloat(entries[3]);
				int endIndex = (int) Float.parseFloat(entries[4]);

				if (!(startIndex == -1 && endIndex == -1)) {

//					System.out.println(startIndex + "\tto\t" + endIndex + ":\t" + correction + "\t(was "
//							+ text.substring(startIndex, endIndex) + ")");

				} else {
					System.out.println("NOT CORRECTLY INDEXED");
					System.out.println(line);
					System.out.println(langtoolCorrections);
					System.out.println(misspelling);
					System.out.println(correction);
					System.out.println(startIndex);
					System.out.println(endIndex);
					System.out.println();

					startIndex = 0;
					endIndex = 0;
				}

				correctionMap.put(startIndex + "-" + endIndex, correction);

				items.add(new SpellingItem_LanguageToolSuggestions(corpusName, id, text, correctionMap,
						langtoolCorrections));

			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Number of spelling errors:\t" + nrOfErrors);
//		System.out.println("Number of grammar errors:\t" + nrOfGrammarErrors);

		numberOfTexts = items.size();
	}

	@Override
	public void getNext(JCas jcas) throws IOException, CollectionException {

		SpellingItem_LanguageToolSuggestions item = items.poll();
		getLogger().debug(item);

		jcas.setDocumentLanguage(language);
		jcas.setDocumentText(item.getText());

		DocumentMetaData dmd = DocumentMetaData.create(jcas);
		dmd.setDocumentId(String.valueOf(item.getId()));
		dmd.setDocumentTitle(String.valueOf(item.getId()));
		dmd.setCollectionId(item.getCorpusName());

		Map<String, String> corrections = item.getCorrections();

		for (String element : corrections.keySet()) {

//			If the pipeline aims to detect errors, do not annotate them as SpellingAnomalies, but as SpellingErrors
			if (errorDetection) {
				SpellingError spellingError = new SpellingError(jcas);
				String[] range = element.split("-");
				spellingError.setBegin(Integer.parseInt(range[0]));
				spellingError.setEnd(Integer.parseInt(range[1]));
				spellingError.setCorrection(corrections.get(element));
				spellingError.setErrorType("");
				spellingError.addToIndexes();
			} else {
				ExtendedSpellingAnomaly anomaly = new ExtendedSpellingAnomaly(jcas);
				String[] range = element.split("-");
				anomaly.setBegin(Integer.parseInt(range[0]));
				anomaly.setEnd(Integer.parseInt(range[1]));
				anomaly.setGoldStandardCorrection(corrections.get(element));
				anomaly.setMisspelledTokenText(anomaly.getCoveredText());
				List<String> langtoolSuggestions = item.getLangtoolSuggestions();
				if (langtoolSuggestions.size() > 0) {
					int i = 0;
					FSArray actions = new FSArray(jcas, langtoolSuggestions.size());
					for (String suggestion : langtoolSuggestions) {
						SuggestedActionWithOrigin action = new SuggestedActionWithOrigin(jcas);
						action.setReplacement(suggestion);
						action.setCertainty(1);
						action.setMethodThatGeneratedThisSuggestion("Langtool");
						actions.set(i, action);
						i++;
						System.out.println(this.getClass().getSimpleName() + "\t" + anomaly.getCoveredText() + "\t"
								+ action.getReplacement() + "\t" + action.getCertainty());
					}
					anomaly.setSuggestions(actions);
				}
				anomaly.addToIndexes();
//				System.out.println("SPELLING_ERROR: " + anomaly.getCoveredText() + "\t" + corrections.get(element));

			}
		}

		SpellingText text = new SpellingText(jcas, 0, jcas.getDocumentText().length());
		text.setId(item.getId());
		text.addToIndexes();
		currentIndex++;
		int numberOfAnomalies = JCasUtil.select(jcas, ExtendedSpellingAnomaly.class).size();

		if (numberOfAnomalies < 1) {
			System.out.println(item.getId());
			System.out.println(item.getText());
			System.out.println(item.getCorrections().keySet().iterator().next());
			System.out.println(item.getLangtoolSuggestions().size());
			System.exit(0);
		}
	}

	@Override
	public Progress[] getProgress() {
		return new Progress[] { new ProgressImpl(currentIndex, numberOfTexts, Progress.ENTITIES) };
	}

	@Override
	public boolean hasNext() throws IOException, CollectionException {
		return !items.isEmpty();
	}
}