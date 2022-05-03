package reader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpellingItem_LanguageToolSuggestions {

	private String corpusName;
	private String id;
	private String text;
	Map<String, String> corrections = new HashMap<String, String>();
	List<String> langtoolSuggestions = new ArrayList<String>();

	public String getText() {
		return this.text;
	}

	public String getId() {
		return this.id;
	}

	public String getCorpusName() {
		return this.corpusName;
	}

	public Map<String, String> getCorrections() {
		return this.corrections;
	}

	public List<String> getLangtoolSuggestions() {
		return this.langtoolSuggestions;
	}

	public SpellingItem_LanguageToolSuggestions(String corpusName, String id, String text,
			Map<String, String> correctionMap, List<String> langtoolSuggestions) {
		this.text = text;
		this.id = id;
		this.corpusName = corpusName;
		this.corrections = correctionMap;
		this.langtoolSuggestions = langtoolSuggestions;
	}
}
