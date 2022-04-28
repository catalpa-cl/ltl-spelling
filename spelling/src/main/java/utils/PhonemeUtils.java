package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Takes graphemes and requests their phonetic representation from BAS web
 * service. Processes batches as not to out in requests for single strings
 */
public class PhonemeUtils {

	/**
	 * To process a list of inputs.
	 */
	public static List<String> getPhonemes(List<String> graphemes, String language) throws IOException {

		language = getG2PLanguageCode(language);
		if (language.equals("")) {
			System.out.println("Language " + language
					+ " not supported by g2p application. It accepts ISO 639-1 codes or combined ISO 639-3-ISO3166-1 codes.");
		}

		// Create a temporary file containing the graphemes to process
		String tempLocation = "src/main/resources/tempGraphemes.txt";
		writeListToFile(graphemes, tempLocation);
		File file = new File(tempLocation);

		String phoneticTranscription = processFile(file, language);

		List<String> phonemes = new ArrayList<String>();
		String[] tokens = phoneticTranscription.split("\n");
		for (String token : tokens) {
			token = token.substring(token.indexOf(";") + 1);
			System.out.println(token);
			phonemes.add(token);
		}

		// Delete temp file containing graphemes
		file.delete();

		return phonemes;
	}

	/**
	 * To process a single word.
	 */
	public static String getPhoneticTranscriptionFromService(String grapheme, String language) {

		if (grapheme.equals("")) {
			return "";
		}

		// Must create a temporary file containing the graphemes to process
		String tempLocation = "src/main/resources/tempGraphemes.txt";

		try {
			writeStringToFile(grapheme, tempLocation);
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
		File file = new File(tempLocation);

		String phoneticTranscription = processFile(file, language);

		phoneticTranscription = phoneticTranscription.substring(phoneticTranscription.indexOf(";") + 1);
		phoneticTranscription = phoneticTranscription.replaceAll("\n", "");

		// Delete temp file containing graphemes
		file.delete();

		return phoneticTranscription;
	}

	/**
	 * To look up phonetic representation in stored representations
	 */
	public static String getPhoneticTranscription(String grapheme, String language) {
		language = getG2PLanguageCode(language);

		if (grapheme.equals("")) {
			return "";
		}

		String result = G2P_PhonemeMap.getInstance().lookupPhonemesInMaps(grapheme, language);

		if (result == null) {
			System.out.println("Did not find phonetic description for: " + grapheme);
			System.exit(0);
			// return getPhoneticTranscriptionFromService(grapheme, language);
		}
		return result;
	}

	public static void writeListToFile(List<String> list, String outputFileName) throws IOException {
		FileWriter writer = new FileWriter(outputFileName);
		for (String str : list) {
			writer.write(str + System.lineSeparator());
		}
		writer.close();
	}

	public static void writeStringToFile(String grapheme, String outputFileName) throws IOException {
		FileWriter writer = new FileWriter(outputFileName);
		writer.write(grapheme);
		writer.close();
	}

	private static String getG2PLanguageCode(String lang) {
		if (lang.length() == 2) {
			// Just check whether it is supported and formatted correctly (because bas fails
			// intransparently when called with unsupported language)
			return G2P_LanguageCodeMapper.getInstance().getBasFrom639_1(lang);

		} else {
			boolean langIsSupported = G2P_LanguageCodeMapper.getInstance().checkIfLanguageIsSupported(lang);
			if (langIsSupported) {
				// In this case the language code is supported
				return lang;
			}
			// In this case it is ill-formatted or not supported
			return "";
		}
	}

	private static String processFile(File file, String language) {

		language = getG2PLanguageCode(language);
		if (language.equals("")) {
			System.out.println("Language " + language
					+ " not supported by g2p application. It accepts ISO 639-1 codes or combined ISO 639-3-ISO3166-1 codes.");
		}

		// Setup for request to process the file
		HttpClient httpclient = HttpClientBuilder.create().build();
		HttpPost httppost = new HttpPost("http://clarin.phonetik.uni-muenchen.de/BASWebServices/services/runG2P");

		MultipartEntityBuilder entity = MultipartEntityBuilder.create();

		entity.addPart("i", new FileBody(file));

		StringBody sb_no = new StringBody("no", ContentType.TEXT_PLAIN);
//		StringBody sb_yes = new StringBody("yes", ContentType.TEXT_PLAIN);
		entity.addPart("com", sb_no);
		entity.addPart("align", sb_no);
		entity.addPart("stress", sb_no);
		entity.addPart("syl", sb_no);
		entity.addPart("embed", sb_no);
		entity.addPart("nrm", sb_no);
		entity.addPart("map", sb_no);

		StringBody sb_lng = new StringBody(language, ContentType.TEXT_PLAIN);
		entity.addPart("lng", sb_lng);

		StringBody sb_iform = new StringBody("list", ContentType.TEXT_PLAIN);
		entity.addPart("iform", sb_iform);

		StringBody sb_oform = new StringBody("tab", ContentType.TEXT_PLAIN);
		entity.addPart("oform", sb_oform);

		StringBody sb_featset = new StringBody("standard", ContentType.TEXT_PLAIN);
		entity.addPart("featset", sb_featset);

		StringBody sb_tgrate = new StringBody("16000", ContentType.TEXT_PLAIN);
		entity.addPart("tgrate", sb_tgrate);

		StringBody sb_tgitem = new StringBody("ort", ContentType.TEXT_PLAIN);
		entity.addPart("tgitem", sb_tgitem);

		StringBody sb_outsym = new StringBody("sampa", ContentType.TEXT_PLAIN);
		entity.addPart("outsym", sb_outsym);

		httppost.setEntity(entity.build());

		HttpResponse response = null;
		try {
			response = httpclient.execute(httppost);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Read result: downloadLink-Tag contains url to get result from
		Document doc = null;
		try {
			doc = Jsoup.parse(EntityUtils.toString(response.getEntity(), Charset.forName("UTF-8")));
		} catch (ParseException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Elements ele = doc.select("downloadLink");
		String link = ele.get(0).text();

		// Get request to read phonemes from link, collect them in a set and return
		HttpGet httpget = new HttpGet(link);
		HttpResponse result = null;
		try {
			result = httpclient.execute(httpget);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String phoneticTranscription = null;
		try {
			phoneticTranscription = EntityUtils.toString(result.getEntity(), Charset.forName("UTF-8"));
		} catch (ParseException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return phoneticTranscription;
	}
}