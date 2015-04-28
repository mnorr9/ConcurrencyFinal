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

/**
 * This program will connect to a website that has a folder which contains a list of images.
 * The images will then be downloaded to our local computer and then altered, by changing
 * it to a black and white .jpg file.  The file will then be written to the hard drive.
 * <p>
 * The purpose of this program is to create a true concurrent solution that will begin
 * altering the images while the other files are still downloading and will perhaps
 * even begin writing the images to the hard drive while the downloads are still processing.
 * This is an example of a true producer consumer problem.
 * @author Nacer Abreu
 * @author Michael Norris
 * @author Emmanuel Bonilla
 *
 */
public class FinalExam {

    private static final String URL_PATH = "http://elvis.rowan.edu/~mckeep82/ccpsp15/Astronomy/";

    public static void main(String[] args) throws Exception {
    	
    	// Delete all JPG files
    	DeleteJpgFiles.delJpg();
	 
        ExecutorService exec = null;

        //Determines the number of threads to use by finding out how many processors are available
        //and then adding 1
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
    
    /**
     * This method connects to the given URL path and reads the code
     * to find and build a list of .jpg files.  The code is read and searches
     * for lines that contain .jpg extension.  The line is then sent to
     * the parseHtml method to isolate and return the filename so it can 
     * be added to our urlList.
     * @return urlList - returns a list of .jpg files
     */
    private static ArrayList<String> buildUrlList() {
        ArrayList<String> urlList = new ArrayList<String>();

        try {
            //Sets up the URL PATH
        	URL url = new URL(URL_PATH);
            
            //Returns a URLConnection instance that represents a connection to the remote object referred to by the URL
            URLConnection conn = url.openConnection();
            
            InputStream inputStream = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.contains(".jpg")) {
                    //System.out.println(line);
                	//Calls parseHtml method to isolate the name of the .jpg file
                    String fileName = parseHtml(line);
                    //Adds the name of the .jpg file to our urlList
                    urlList.add(fileName);
                }
            }
            inputStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return urlList;
    } //end buildUrlList() method

    /**
     * This method returns the name of .jpg that will be processed later
     * by parsing through the line of code that is given to this method.
     * This method must be given a String that contains the HTML tags
     * <a href="somefilename.jpg">...</a>
     * @param line - the line of code that contains the name of the .jpg file
     * @return fileName - returns the name of the .jpg file
     */
    private static String parseHtml(String line) {

        //Parses the line into a new Document so we can use the appropriate methods in Document to access certain data 
        Document doc = Jsoup.parse(line);
        
        // finds the first link with "a" tags (contains href attributes)
        Element link = doc.select("a").first();

        // attr returns the given attribute value of the given key("href")
        String fileName = link.attr("href");

        return fileName;
    } //end parseHtml() method


}//end of class
