package finalproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class FinalExam {

    private static final String URL_PATH = "http://elvis.rowan.edu/~mckeep82/ccpsp15/Astronomy/";
    private static Collection<String> localFileList = Collections.synchronizedCollection(new ArrayList<String>());

    public static void main(String[] args) throws Exception {

        // A true concurrent solution will begin altering images 
        // while the others are still downloading and will perhaps 
        // even begin writing the images to the hard drive while 
        // downloads are still processing.  This is a true producer 
        // consumer problem that can be sectioned into different 
        // easy to accomplish parts.	 
        List<Future> futuresList = new ArrayList<>();

        ExecutorService exec = null;

        int NTHREADS = Runtime.getRuntime().availableProcessors() + 1;

        exec = Executors.newFixedThreadPool(NTHREADS);

        //****************************
        // Get the list of the file names
        //****************************
        ArrayList<String> fileNameList = buildUrlList();

	//****************************
        // Download all images
        //****************************
        for (String fileName : fileNameList) {
            futuresList.add(exec.submit(new SaveImageTask(fileName)));
        }

        while (!futuresList.isEmpty()) {

            for (int i = 0; i < (futuresList.size()); i++) {
                Future future = futuresList.get(i);

                if ((future != null) && (future.isDone())) {
                    futuresList.remove(i);
                    SaveImageTask task;
                    task = (SaveImageTask) future.get();
                    exec.submit(new AlterImageTask(task.getFilename()));
                    break;
                }
                i++;
            }
        }//end of while loop

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
