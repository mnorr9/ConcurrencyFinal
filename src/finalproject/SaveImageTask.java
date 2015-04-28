
package finalproject;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.concurrent.Callable;

/**
 *
 * 
 */
public class SaveImageTask implements Callable{

    private final String URL_PATH = "http://elvis.rowan.edu/~mckeep82/ccpsp15/Astronomy/";

    private final String fileName;
    
    public SaveImageTask(String fileName) {
        this.fileName = fileName;

    }
    
    public String getFilename(){
        return fileName;
    }

    @Override
    public Object call() throws Exception {
	//****************************
        // Download all images
        //****************************
		//localFileList.add(fileName);
		URL url = new URL(URL_PATH+fileName);
		InputStream is = url.openStream();
		OutputStream os = new FileOutputStream(fileName);

		byte[] b = new byte[2048];
		int length;

		while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);
		}

		is.close();
		os.close();
		System.out.println("Saving..."+fileName);
        return this;
    }
    
}//end of class
