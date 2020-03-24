package de.unidue.ltl.spelling.preprocessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpResponse;
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

import de.unidue.ltl.spelling.utils.PhonemeUtils;

//Takes a dictionary with one word per line (grapheme)
//and transforms it to one grapheme \t phoneme per line
//To be used for candidate generation based on phonetic distance
public class GraphemeDictionaryToPhonemeMap {
	
	public static void main(String[] args) throws IOException {
		
		//deu-DE OR eng-US
//		processDictionary(
//				"/Users/mariebexte/ltl-spelling/de.unidue.ltl.spelling/src/main/resources/dictionaries/hunspell_DE.txt",
//				"/Users/mariebexte/ltl-spelling/de.unidue.ltl.spelling/src/main/resources/dictionaries/hunspell_DE_phoneme_map.txt",
//				"deu-DE");
		
		processDictionary(
				"/Users/mariebexte/ltl-spelling/de.unidue.ltl.spelling/src/main/resources/dictionaries/hunspell_en_US.txt",
				"/Users/mariebexte/ltl-spelling/de.unidue.ltl.spelling/src/main/resources/dictionaries/hunspell_en_US_phoneme_map.txt",
				"deu-DE");
	}
	
	private static void processDictionary(String path,String outputFileName, String language) throws IOException {
		FileWriter writer = new FileWriter(outputFileName);
		//Read tokens into list
		FileReader fr = new FileReader(path);
		BufferedReader br = new BufferedReader(fr);
		List<String> graphemes = new ArrayList<String>();
		while(br.ready()) {
			graphemes.add(br.readLine());
		}
		br.close();
		
		List<String> result;
		//Cannot process all at once, make batches of 10000
		List<String> subList;
		int stepSize = 10000;
		int numSteps = (int) Math.floor(graphemes.size()/stepSize);
		for(int i = 0; i<=numSteps; i++) {
			if(i == numSteps) {
				subList = graphemes.subList(i*stepSize, graphemes.size()-1);
			}
			else{
				subList = graphemes.subList(i*stepSize, (i+1)*stepSize-1);
			}
			
			result =  PhonemeUtils.getPhonemes(subList,language);
			for(int j = 0; j<subList.size(); j++) {		
				writer.write(subList.get(j)+"\t"+result.get(j)+System.lineSeparator());
			}
		}	
		writer.close();
	}
}