package finalproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class FinalExam {

    private static final String URL_PATH = "http://elvis.rowan.edu/~mckeep82/ccpsp15/Astronomy/";

    public static void main(String[] args) throws Exception {
    	
    	// Delete all JPG files
    	DeleteJpgFiles.delJpg();

        // A true concurrent solution will begin altering images 
        // while the others are still downloading and will perhaps 
        // even begin writing the images to the hard drive while 
        // downloads are still processing.  This is a true producer 
        // consumer problem that can be sectioned into different 
        // easy to accomplish parts.	 

        ExecutorService exec = null;

        int NTHREADS = Runtime.getRuntime().availableProcessors() + 1;

        exec = Executors.newFixedThreadPool(NTHREADS);

        //****************************
        // Get the list of the file names
        //****************************
        ArrayList<String> fileNameList = buildUrlList();
        
        //Creating shared object
        BlockingQueue<String> sharedQueue = new ArrayBlockingQueue<String>(fileNameList.size());

        //****************************
        // Download all images
        //****************************
        
        // Start consumer
        exec.submit(new AlterImageTask(sharedQueue));
        
        // Start producer
        for (String fileName : fileNameList) {
            exec.submit(new SaveImageTask(fileName, sharedQueue)); 
            
        }
        
        exec.shutdown();

    }//end of main()

    private static ArrayList<String> buildUrlList() {
        ArrayList<String> urlList = new ArrayList<String>();

        try {
            URL url = new URL(URL_PATH);
            URLConnection conn = url.openConnection();
            InputStream inputStream = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.contains(".jpg")) {
                    //System.out.println(line);
                    String fileName = parseHtml(line);
                    urlList.add(fileName);
                }
            }
            inputStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return urlList;
    }

    private static String parseHtml(String line) {

        Document doc = Jsoup.parse(line);
        Element link = doc.select("a").first();
        String fileName = link.attr("href");

        return fileName;
    }


}//end of class
