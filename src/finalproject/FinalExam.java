package finalproject;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.imageio.ImageIO;

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
	       
		//****************************
		// Get the list of the file names
		//****************************
		ArrayList<String> fileNameList = buildUrlList();

		//****************************
		// Download all images
		//****************************
		for(String fileName: fileNameList) {
			saveImage(fileName);
		}
		
		//****************************
		// Perform an alteration to the image
		// Write Images to Hard drive
		//****************************
		//alterImages();
		for(String localPath: localFileList ) {
			alterImages(localPath);
		}
	}


	private static void alterImages(String localPath) {
		// you make the image black and white or something similar
		// BufferedImage in Java makes this very easy
		
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(localPath));

			BufferedImage blackAndWhiteImg = new BufferedImage(
					img.getWidth(), img.getHeight(),
					BufferedImage.TYPE_BYTE_BINARY);

			Graphics2D graphics = blackAndWhiteImg.createGraphics();
			graphics.drawImage(img, 0, 0, null);
			String bwFileName = "bw_"+localPath;
			ImageIO.write(blackAndWhiteImg, "jpg", new File(bwFileName));
			System.out.println("Writing image..."+bwFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private static ArrayList<String> buildUrlList() {
		ArrayList<String> urlList = new ArrayList<String>();
		
		 try {
	            URL url = new URL(URL_PATH);
	            URLConnection conn = url.openConnection();
	            InputStream inputStream = conn.getInputStream();
	            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
	 
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	            	if(line.contains(".jpg")) {
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


	private static void saveImage(String fileName) throws IOException {
		
		localFileList.add(fileName);
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
	}

}
