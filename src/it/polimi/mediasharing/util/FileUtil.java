package it.polimi.mediasharing.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FileUtil {
	
	public static void storeFile(String bytesContent, String destination){
		try {
			OutputStream out;
        	File file = new File(destination);
            if (!file.exists()) {
				file.createNewFile();
			}
            out = new FileOutputStream(file);	            	            
            String[] byteValues = bytesContent.substring(1, bytesContent.length() - 1).split(",");
            byte[] bytes = new byte[byteValues.length];
            for (int i=0, len=bytes.length; i<len; i++) {
            	bytes[i] = Byte.parseByte(byteValues[i].trim());     
            }
            out.write(bytes);
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
