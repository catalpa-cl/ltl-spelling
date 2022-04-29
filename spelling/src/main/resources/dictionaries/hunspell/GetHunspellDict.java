import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//This does only work when executed via terminal, given that hunspell has been installed
public class GetHunspellDict {

	public static void main(String[] args) {
		try {
			getDict("German_de_DE.dic",
					"German_de_DE.aff",
					"hunspell_dict_de.txt");

			getDict("English_US.dic",
					"English_US.aff",
					"hunspell_dict_en_US.txt");
			
			getDict("Czech.aff",
					"Czech.dic",
					"hunspell_dict_cz.txt");

			getDict("Italian.aff",
					"Italian.dic",
					"hunspell_dict_italian.txt");

			mergeInMissingNonAffixWords(
					"hunspell_dict_DE.txt",
					"German_de_DE.dic",
					"hunspell_dict_DE_full.txt");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void getDict(String aff, String dic, String dicName) throws IOException {

		Set<String> words = new HashSet<String>();
		String wordforms = "/usr/local/bin/wordforms";

		String word = null;
		BufferedReader br = new BufferedReader(new FileReader(new File(dic)));
		while (br.ready()) {
			word = br.readLine();
			if(word.startsWith("/")) {
				continue;
			}
			if (word.contains("/")) {
				word = word.substring(0, word.indexOf("/"));

				//System.out.println("aff:\t"+aff);
				//System.out.println("dic:\t"+dic);
				//System.out.println("word:\t"+word);

				Process p = Runtime.getRuntime().exec(new String[] {"/bin/bash", wordforms, aff, dic, word });

				BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

				BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

				String s;
				// read the output from the command
				//System.out.println("Here is the standard output of the command:\n");
				while ((s = stdInput.readLine()) != null) {
					System.out.println("out:\t"+s);
					words.add(s);
				}

				// read any errors from the attempted command
				//System.out.println("Here is the standard error of the command (if any):\n");
				while ((s = stdError.readLine()) != null) {
					System.out.println("error:\t"+s);
				}
			}
			else {
				//System.out.println(word);
				words.add(word);
			}

		}
		br.close();

		List<String> dict = new ArrayList<String>();
		dict.addAll(words);
		dict.sort(null);

		FileWriter writer = new FileWriter(dicName);
		for (String str : dict) {
			writer.write(str + System.lineSeparator());
		}
		writer.close();
	}

	public static void mergeInMissingNonAffixWords(String old_hunspell, String dic, String dicName) throws IOException {

		Set<String> words = new HashSet<String>();

		// Add only those that are missing any affix instructions
		BufferedReader br = new BufferedReader(new FileReader(new File(dic)));
		while (br.ready()) {
			String word = br.readLine();
			if (!word.contains("/")) {
				words.add(word);
				System.out.println(word);
			}
		}
		br.close();

		// Add everything
		br = new BufferedReader(new FileReader(new File(old_hunspell)));
		while (br.ready()) {
			String word = br.readLine();
			words.add(word);
		}
		br.close();

		List<String> dict = new ArrayList<String>();
		dict.addAll(words);
		dict.sort(null);

		FileWriter writer = new FileWriter(dicName);
		for (String str : dict) {
			writer.write(str + System.lineSeparator());
		}
		writer.close();
	}
}