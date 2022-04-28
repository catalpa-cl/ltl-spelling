package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//Takes a dictionary with one word per line (grapheme)
//and transforms it to one grapheme \t phoneme per line
//To be used for candidate generation based on phonetic distance
public class GraphemeDictionaryToPhonemeMap {

	public static void main(String[] args) throws IOException {

		processDictionary("src/main/resources/dictionaries/hunspell_dict_de.txt",
				"src/main/resources/dictionaries/hunspell_dict_de_phoneme_map.txt", "deu-DE");

		processDictionary("src/main/resources/dictionaries/hunspell_dict_en_US.txt",
				"src/main/resources/dictionaries/hunspell_dict_en_US_phoneme_map.txt", "eng-US");

		processDictionary("src/main/resources/dictionaries/hunspell_dict_cz.txt",
				"src/main/resources/dictionaries/hunspell_dict_cz_phoneme_map.txt", "cze-CZ");

		processDictionary("src/main/resources/dictionaries/hunspell_dict_it.txt",
				"src/main/resources/dictionaries/hunspell_dict_it_phoneme_map.txt", "it");

		processDictionary("src/main/resources/dictionaries/childlex_litkey.txt",
				"src/main/resources/dictionaries/childlex_litkey_phoneme_map.txt", "deu-DE");
	}

	// Use input file to derive phonemes
	private static void processDictionary(String path, String outputFileName, String language) throws IOException {

		// Read graphemes into list
		BufferedReader br = new BufferedReader(new FileReader(path));
		List<String> graphemes = new ArrayList<String>();
		while (br.ready()) {
			graphemes.add(br.readLine());
		}
		br.close();

		processDictionary(graphemes, outputFileName, language);
	}

	// Use list to derive phonemes
	public static void processDictionary(List<String> graphemes, String outputFileName, String language)
			throws IOException {

		FileWriter writer = new FileWriter(outputFileName);
		List<String> result;
		// Cannot process all at once, make batches of 10000
		List<String> subList;
		int stepSize = 10000;
		int numSteps = (int) Math.ceil(graphemes.size() * 1.0 / stepSize);
		System.out.println(graphemes.size());

		for (int i = 0; i < numSteps; i++) {
			System.out.println("Step: " + (i + 1) + "/" + numSteps);
			if ((i + 1) == numSteps) {
				subList = graphemes.subList(i * stepSize, graphemes.size());
			} else {
				subList = graphemes.subList(i * stepSize, (i + 1) * stepSize);
			}
			result = PhonemeUtils.getPhonemes(subList, language);
//			if(result.size() != subList.size()) {
//				System.exit(0);
//			}
			for (int j = 0; j < subList.size(); j++) {
				writer.write(subList.get(j) + "\t" + result.get(j) + System.lineSeparator());
			}
		}
		writer.flush();
		writer.close();
	}
}