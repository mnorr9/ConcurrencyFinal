
package finalproject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * This class is used to implement the downloading of files from the given website
 * 
 * @author Nacer Abreu
 * @author Michael Norris
 * @author Emmanuel Bonilla
 * 
 */
public class DownloadImageTask implements Runnable {

    private final String URL_PATH = "http://elvis.rowan.edu/~mckeep82/ccpsp15/Astronomy/";

    private final String fileName;

    /**
     * Constructor for filename to be downloaded
     * @param fileName - name of file to be downloaded
     */
    public DownloadImageTask(String fileName) {
        this.fileName = fileName;

    }
    
    /**
     * Returns the name of file
     * @return - returns the name of the file
     */
    public String getFilename(){
        return fileName;
    }

    @Override
    public void run() {
    	//****************************
    	// Download all images
    	//****************************
    	try {

    		//sets up the path to which we will be downloading
    		URL	url = new URL(URL_PATH+fileName);

    		//sets up the input and output streams for retrieving and saving the file to be downloaded
    		InputStream is = url.openStream();
    		OutputStream os = new FileOutputStream(fileName);

    		byte[] b = new byte[2048];
    		int length;
                
    		while ((length = is.read(b)) != -1) {
    			os.write(b, 0, length);
    		}

    		is.close();
    		os.close();

    		System.out.println("Downloaded : "+fileName + "; size: " + getSize(fileName) + "kb");
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}

    }
    
    /**
     * This method will take a filename and return the size of the file in kilobytes
     * @param filename - name of file to be processed
     * @return kilobytes - returns the size of the file in kilobytes
     */
    private long getSize(String filename){
        File file =new File(filename);
        long kilobytes = 0;
        
        if(file.exists()){
            long bytes = file.length();
            kilobytes = (bytes / 1024);
        }
        return kilobytes;
    } //end of getSize()
}//end of class
