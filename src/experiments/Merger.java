package experiments;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Merger {
    
    
    public void merge21File(File dir, int blocknum) throws IOException {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(dir,"merged.txt"))));
                    for(int j = 0 ; j < blocknum ; j++) {
                            File thisfile = new File(dir, "model"+j+".txt");
                            
                            
                            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(thisfile)),16*1024*1024);
                            String line = br.readLine();
                            //kernel matrix must be the dense matrix so we do not have to reset the matrix every time
                            while(line != null) {
                            
                                    bw.write(line);
                                    bw.write("\r\n");
                                    line = br.readLine();
                            }
                            br.close();
                    }
            bw.flush();
            bw.close();
    }
    
    public static void main(String[] args) throws 
    IOException {
    	Merger merger = new Merger();
    	merger.merge21File(new File("/Dizertatie/Model/"), 2);
    }
}