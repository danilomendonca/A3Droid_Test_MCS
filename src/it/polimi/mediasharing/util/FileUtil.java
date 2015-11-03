package it.polimi.mediasharing.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FileUtil {
	
	public static void storeFile(byte [] contentBytes, String destination, String name){
		try {
			OutputStream out;
			File folder = new File(destination);
			if(!folder.exists())
				folder.mkdir();
        	File file = new File(destination + "/" + name);			
            if(!file.exists())
				file.createNewFile();
            out = new FileOutputStream(file);            	            
            out.write(contentBytes);
            out.close();
          
		} catch (NumberFormatException nfe){
			nfe.printStackTrace();
        } catch (FileNotFoundException ex) {
            System.out.println("File not found. ");
        } catch (IOException e) {
			e.printStackTrace();
		}
	}

}
