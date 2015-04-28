
package finalproject;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.concurrent.BlockingQueue;

/**
 *
 * 
 */
public class SaveImageTask implements Runnable {

    private final String URL_PATH = "http://elvis.rowan.edu/~mckeep82/ccpsp15/Astronomy/";

    private final String fileName;
    private final BlockingQueue<String> sharedQueue;
    
    public SaveImageTask(String fileName, BlockingQueue<String> sharedQueue) {
        this.fileName = fileName;
        this.sharedQueue = sharedQueue;

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

    		// Add fileName to shared Queue
    		sharedQueue.add(fileName);
    		System.out.println("Saving..."+fileName);
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}

    }
    
}//end of class
