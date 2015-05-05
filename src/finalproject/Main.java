package finalproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * This program will connect to a website that has a folder which contains a
 * list of images. The images will then be downloaded to our local computer and
 * then altered, by changing it to a black and white .jpg file. The file will
 * then be written to the hard drive.
 * <p>
 * The purpose of this program is to create a true concurrent solution that will
 * begin altering the images while the other files are still downloading and
 * will perhaps even begin writing the images to the hard drive while the
 * downloads are still processing. This is an example of a true producer
 * consumer problem.
 *
 * @author Nacer Abreu
 * @author Michael Norris
 * @author Emmanuel Bonilla
 *
 */
public class Main {

    private static final String URL_PATH = "http://elvis.rowan.edu/~mckeep82/ccpsp15/Astronomy/";

    /**
     * Entry point of this program.
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {

        //****************************
        // Get the list of the file names
        //****************************
        ArrayList<String> fileNameList = buildUrlList();

        // Delete all JPG files
        DeleteImageTask.delJpg();

        // Determines the number of threads to use by finding out how many 
        // processors are available and then adding 1
        int NTHREADS = Runtime.getRuntime().availableProcessors() + 1;
        ExecutorService exec = Executors.newFixedThreadPool(NTHREADS);
        System.out.println("Thread Pool size: " + NTHREADS);
        
        // BlockingQueue of in progress downloads. 
        BlockingQueue<String> downloads = new ArrayBlockingQueue<>(fileNameList.size());

        // This hashmap is used by the consumer thread to verify that the file
        // that is going to be altered has finished downloading. That the task
        // is done. 
        HashMap<String, Future> futures = new HashMap<>();

        Collection<Callable<String>> callables = new ArrayList<>();

        callables.add(getConsumerCallable(fileNameList, downloads, futures, exec));

        callables.add(getProducerCallable(fileNameList, downloads, futures, exec));

        // invokeAll is a blocking method. It means – JVM won’t proceed to next 
        // line until the consumer and producer threads are done.
        exec.invokeAll(callables);

        // Waits until all tasks are completed before graciously shuting down
        // the executor
        exec.shutdown();

    }//end of main()

    /**
     * This method connects to the given URL path and reads the code to find and
     * build a list of .jpg files. The code is read and searches for lines that
     * contain .jpg extension. The line is then sent to the parseHtml method to
     * isolate and return the filename so it can be added to our urlList.
     *
     * @return urlList - returns a list of .jpg files
     */
    private static ArrayList<String> buildUrlList() {
        ArrayList<String> urlList = new ArrayList<>();

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
        }

        return urlList;
    } //end buildUrlList() method

    /**
     * This method returns the name of .jpg that will be processed later by
     * parsing through the line of code that is given to this method. This
     * method must be given a String that contains the HTML tags
     * <a href="somefilename.jpg">...</a>
     *
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

    /**
     *
     */
    private static Callable<String> getConsumerCallable(ArrayList<String> fileNameList,
            BlockingQueue<String> downloads, HashMap<String, Future> futures, ExecutorService exec) throws Exception {
        return ((Callable) () -> {
            System.out.println("Consumer waiting...");
            for (String loop : fileNameList) {

                String fileName = downloads.take();
                Future future = futures.get(fileName);
                exec.execute(new AlterImageTask(fileName, future));

            }//end of for..loop
            return null;
        });
    }//end of getConsumerCallable()

    /**
     *
     */
    private static Callable<String> getProducerCallable(ArrayList<String> fileNameList,
            BlockingQueue<String> downloads, HashMap<String, Future> futures, ExecutorService exec) {
        return (new Callable() {

            @Override
            public Object call() throws Exception {
                fileNameList.stream().map((fileName) -> {                    
                    Future<?> future = exec.submit(new DownloadImageTask(fileName));
                    futures.put(fileName, future);
                    return fileName;
                }).forEach((fileName) -> {
                    // In-progress download
                    downloads.add(fileName);
                });
                return null;
            }
        });
    }//end of getProducerCallable();

}//end of class