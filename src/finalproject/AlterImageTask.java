package finalproject;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

/**
 * This class is used to alter an image to black and white
 * 
 * @author Nacer Abreu
 * @author Michael Norris
 * @author Emmanuel Bonilla
 */
public class AlterImageTask implements Runnable {

    private final String fileName;
    private final Future future;

    /**
     * Constructor for AlterImageTask class
     * @param fileName - name of file to be processed
     * @param future - name of future to be processed 
     */
    public AlterImageTask(String fileName, Future future) {

        this.future = future;
        this.fileName = fileName;
    } //end AlterImageTask()

    /**
     * Creating runnable to alter image to black and white 
     */
    @Override
    public void run() {

        try {

            // Wait/Block until the downloading task is done!
            future.get();

            BufferedImage img = null;

            img = ImageIO.read(new File(fileName));

            BufferedImage blackAndWhiteImg = new BufferedImage(
                    img.getWidth(), img.getHeight(),
                    BufferedImage.TYPE_BYTE_BINARY);

            //creating a Graphics2D object to be processed
            Graphics2D graphics = blackAndWhiteImg.createGraphics();
            graphics.drawImage(img, 0, 0, null);
            String bwFileName = "bw_" + fileName;
            //writes an image in the given format
            ImageIO.write(blackAndWhiteImg, "jpg", new File(bwFileName));
            System.out.println("Altering : " + bwFileName + "; size: " + getSize(fileName) + "kb");
        } catch (IOException ex) {
        } catch (InterruptedException | ExecutionException ex) {
            System.err.println("Filename: " + fileName);
            Logger.getLogger(AlterImageTask.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//end of run();

    /**
     * This method will take a filename and return the size of the file in kilobytes
     * @param filename - name of file
     * @return kilobytes - size of file in kilobytes
     */
    private long getSize(String filename) {
        File file = new File("bw_"+filename);
        long kilobytes = 0;

        if (file.exists()) {
            long bytes = file.length();
            kilobytes = (bytes / 1024);
        }
        return kilobytes;
    } //end getSize()
    
}//end of class()
