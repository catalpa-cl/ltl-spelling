import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;


public class MergeCzech{
   
	// 1st argument: dic-File
	// 2nd argument: txt-dict in which to merge the words
	// 3rd argument: path to new merged dict
	public static void main(String[] args) throws IOException{
	
		HashSet<String> dict = new HashSet<String>();
		
		BufferedReader br = new BufferedReader(new FileReader(new File(args[0])));
		while(br.ready()){
			
			String line = br.readLine();
			if(line.contains("/")){
				line = line.substring(0,line.indexOf("/"));
			}
			dict.add(line);
		}	
		br.close();
		br = new BufferedReader(new FileReader(new File(args[1])));
		while(br.ready()){
			
			String line = br.readLine();
			if(line.contains("/")){
				line = line.substring(0,line.indexOf("/"));
			}
			dict.add(line);
		}	
		br.close();
		
		List<String> dictList = new ArrayList<String>();
		dictList.addAll(dict);
		dictList.sort(null);
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(args[2])));
		for(String word : dictList){
			bw.write(word);
			bw.newLine();
		}
		bw.close();
	}
}

