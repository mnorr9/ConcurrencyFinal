package finalproject;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.imageio.ImageIO;

/**
 *
 *
 */
public class AlterImageTask implements Runnable {

    private final String fileName;
    private final HashMap<String, Future> futures;

    public AlterImageTask(String fileName, HashMap<String, Future> futures) {

        this.futures = futures;
        this.fileName = fileName;
    }

    @Override
    public void run() {

        try {

            // Wait/Block until the saving task is done!
            futures.get(fileName).get();

            BufferedImage img = null;

            img = ImageIO.read(new File(fileName));

            BufferedImage blackAndWhiteImg = new BufferedImage(
                    img.getWidth(), img.getHeight(),
                    BufferedImage.TYPE_BYTE_BINARY);

            Graphics2D graphics = blackAndWhiteImg.createGraphics();
            graphics.drawImage(img, 0, 0, null);
            String bwFileName = "bw_" + fileName;
            ImageIO.write(blackAndWhiteImg, "jpg", new File(bwFileName));
            System.out.println("Writing image..." + bwFileName + "; size: " + getSize(fileName) + "kb");
        } catch (InterruptedException | ExecutionException | IOException ex) {
        }

    }//end of run();

    private long getSize(String filename) {
        File file = new File(filename);
        long kilobytes = 0;

        if (file.exists()) {
            long bytes = file.length();
            kilobytes = (bytes / 1024);
        }
        return kilobytes;
    }
    
}//end of class()
