package FileClasses;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileCleaner {
	private String file;
	
	public FileCleaner(String s){
		file = s;
	}
	
        //removes blank lines from the file for uniform design
	public String fileClean(){
            FileReader wordFile = null;
            
            StringBuilder sb = new StringBuilder();
            try {
                String nextLine;
                wordFile = new FileReader(file);
                try (BufferedReader myReader = new BufferedReader(wordFile)) {
                    while((nextLine = myReader.readLine()) != null){
                            if(lineCheck(nextLine)){
                                    sb.append(nextLine).append("\n");
                            }
                    }
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FileCleaner.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(FileCleaner.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if(wordFile != null){wordFile.close();}
                } catch (IOException ex) {
                    Logger.getLogger(FileCleaner.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return sb.toString();
	}
	
	private boolean lineCheck(String s){
		if(s.equals("")){
			return false;
		}
		else
			return true;
		
		
	}

}

