
package finalproject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 *
 * 
 */
public class SaveImageTask implements Runnable {

    private final String URL_PATH = "http://elvis.rowan.edu/~mckeep82/ccpsp15/Astronomy/";

    private final String fileName;

    
    public SaveImageTask(String fileName) {
        this.fileName = fileName;

    }
    
    public String getFilename(){
        return fileName;
    }

    @Override
    public void run() {
    	//****************************
    	// Download all images
    	//****************************
    	try {

    		URL	url = new URL(URL_PATH+fileName);

    		InputStream is = url.openStream();
    		OutputStream os = new FileOutputStream(fileName);

    		byte[] b = new byte[2048];
    		int length;
                
    		while ((length = is.read(b)) != -1) {
    			os.write(b, 0, length);
    		}

    		is.close();
    		os.close();

    		System.out.println("Saving..."+fileName + "; size: " + getSize(fileName) + "kb");
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}

    }
    
    private long getSize(String filename){
        File file =new File(filename);
        long kilobytes = 0;
        
        if(file.exists()){
            long bytes = file.length();
            kilobytes = (bytes / 1024);
        }
        return kilobytes;
    }
}//end of class
