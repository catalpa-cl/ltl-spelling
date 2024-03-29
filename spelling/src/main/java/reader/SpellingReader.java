package reader;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.uima.UimaContext;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;
import org.dkpro.core.api.io.JCasResourceCollectionReader_ImplBase;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import spelling.types.ExtendedSpellingAnomaly;
import spelling.types.GrammarError;
import spelling.types.SpellingError;
import spelling.types.SpellingText;

public class SpellingReader extends JCasResourceCollectionReader_ImplBase {

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
	Queue<SpellingItem> items;
	int numberOfTexts = -1;

	@Override
	public void initialize(UimaContext aContext) throws ResourceInitializationException {

		items = new LinkedList<SpellingItem>();

		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;

		try {
			builder = builderFactory.newDocumentBuilder();

			int nrOfErrors = 0;
			int nrOfGrammarErrors = 0;

			Document document = builder.parse(new FileInputStream(sourceFile));
			Element rootElement = document.getDocumentElement();
			NodeList nodes = rootElement.getChildNodes();
			String corpusName = rootElement.getAttribute("name");
//			System.out.println("Corpus name: " + corpusName);

			for (int i = 0; i < nodes.getLength(); i++) {

				int endIndex = 0;
				Map<String, String> correctionMap = new HashMap<String, String>();
				Map<String, String> correctionErrorTypeMap = new HashMap<String, String>();
				Map<String, String> grammarCorrectionMap = new HashMap<String, String>();

				Node node = nodes.item(i);

				if (node instanceof Element) {

					Element spellingText = (Element) node;
					String id = spellingText.getAttribute("id");
//					System.out.println("Text ID: " + id);

					String lang = spellingText.getAttribute("lang");
//					System.out.println("Language : " + lang);

					// Workaround in case of missing lang attribute (merlin corpus)
					if (lang.equals("")) {
						if (id.startsWith("german")) {
							lang = "de";
						} else if (id.startsWith("italian")) {
							lang = "it";
						} else if (id.startsWith("czech")) {
							lang = "cz";
						} else {
							System.out.println("Reader: Unknown language or no language tag in xml");
							System.exit(0);
						}
					}

					// Only read in text the required language
					if (lang.equals(language)) {

						String text = spellingText.getTextContent();
//						System.out.println("Text: " + text);

						NodeList children = spellingText.getChildNodes();

						for (int e = 0; e < children.getLength(); e++) {

							Node childNode = children.item(e);
							if (childNode.getNodeName().equals("error")) {
								nrOfErrors++;
								String misspelling = childNode.getTextContent();
								String correction = ((Element) childNode).getAttribute("correct");
								String type = ((Element) childNode).getAttribute("type");

								int startIndex = endIndex;
								endIndex += misspelling.length();
								correctionMap.put(startIndex + "-" + endIndex, correction);
								correctionErrorTypeMap.put(startIndex + "-" + endIndex, type);
//								System.out.println(startIndex + "\tto\t" + endIndex + ":\t" + correction + "\t(was "
//									+ text.substring(startIndex, endIndex) + ")");
							} else if (childNode.getNodeName().equals("grammar_error")) {
								nrOfGrammarErrors++;
								String misspelling = childNode.getTextContent();
								String correction = ((Element) childNode).getAttribute("correct");

								int startIndex = endIndex;
								endIndex += misspelling.length();
								grammarCorrectionMap.put(startIndex + "-" + endIndex, correction);
//								System.out.println("Grammar: "+startIndex + "\tto\t" + endIndex + ":\t" + correction + "\t(was "
//									+ text.substring(startIndex, endIndex) + ")");
							}
							// Is text content
							else if (childNode.getNodeName().equals("#text")) {
								endIndex += childNode.getTextContent().length();
							} else if (childNode.getNodeName().contentEquals("#comment")) {
//								endIndex += childNode.getTextContent().length();
							} else {
								System.out.println(
										"Only expected texts in .xml to contain grammar_error and error tags, but found: "
												+ childNode.getNodeName());
								System.exit(0);
							}
						}

						items.add(new SpellingItem(corpusName, id, text, correctionMap, correctionErrorTypeMap,
								grammarCorrectionMap));
					}
				}
			}
			System.out.println("Reader: Number of spelling errors:\t" + nrOfErrors);
//			System.out.println("Reader: Number of grammar errors:\t" + nrOfGrammarErrors);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		numberOfTexts = items.size();
	}

	@Override
	public void getNext(JCas jcas) throws IOException, CollectionException {

		SpellingItem item = items.poll();
		getLogger().debug(item);

		jcas.setDocumentLanguage(language);
		jcas.setDocumentText(item.getText());

		DocumentMetaData dmd = DocumentMetaData.create(jcas);
		dmd.setDocumentId(String.valueOf(item.getId()));
		dmd.setDocumentTitle(String.valueOf(item.getId()));
		dmd.setCollectionId(item.getCorpusName());

		Map<String, String> corrections = item.getCorrections();
		Map<String, String> correctionErrorTypes = item.getCorrectionErrorTypes();

		for (String element : corrections.keySet()) {
//			If the pipeline aims to detect errors, do not annotate them as SpellingAnomalies, but as SpellingErrors
			if (errorDetection) {
				SpellingError spellingError = new SpellingError(jcas);
				String[] range = element.split("-");
				spellingError.setBegin(Integer.parseInt(range[0]));
				spellingError.setEnd(Integer.parseInt(range[1]));
				spellingError.setCorrection(corrections.get(element));
				spellingError.setErrorType(correctionErrorTypes.get(element));
				spellingError.addToIndexes();
			} else {
				ExtendedSpellingAnomaly anomaly = new ExtendedSpellingAnomaly(jcas);
				String[] range = element.split("-");
				anomaly.setBegin(Integer.parseInt(range[0]));
				anomaly.setEnd(Integer.parseInt(range[1]));
				anomaly.setGoldStandardCorrection(corrections.get(element));
				anomaly.setMisspelledTokenText(anomaly.getCoveredText());
				anomaly.addToIndexes();
			}
		}
//		In case of error detection also annotate GrammarErrors
		if (errorDetection) {

			Map<String, String> grammarCorrections = item.getGrammarCorrections();
			for (String element : grammarCorrections.keySet()) {
				GrammarError grammarError = new GrammarError(jcas);
				String[] range = element.split("-");
				grammarError.setBegin(Integer.parseInt(range[0]));
				grammarError.setEnd(Integer.parseInt(range[1]));
				grammarError.setCorrection(grammarCorrections.get(element));
				grammarError.addToIndexes();
			}
		}

		SpellingText text = new SpellingText(jcas, 0, jcas.getDocumentText().length());
		text.setId(item.getId());
		text.addToIndexes();
		currentIndex++;
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