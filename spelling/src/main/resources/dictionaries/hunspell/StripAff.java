import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;


public class StripAff{

	public static void main(String[] args) throws IOException{
	
		BufferedReader br = new BufferedReader(new FileReader(new File(args[0])));
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(args[1])));
		
		while(br.ready()){
			
			String line = br.readLine();
			if(line.contains("/")){
				line = line.substring(0,line.indexOf("/"));
			}
			bw.write(line);
			bw.newLine();
		}
		
		br.close();
		bw.close();
	}
}

