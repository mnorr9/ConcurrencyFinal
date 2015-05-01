package finalproject;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

/**
 *
 *
 */
public class AlterImageTask implements Runnable {

    private final String fileName;
    private final Future future;

    public AlterImageTask(String fileName, Future future) {

        this.future = future;
        this.fileName = fileName;
    }

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

            Graphics2D graphics = blackAndWhiteImg.createGraphics();
            graphics.drawImage(img, 0, 0, null);
            String bwFileName = "bw_" + fileName;
            ImageIO.write(blackAndWhiteImg, "jpg", new File(bwFileName));
            System.out.println("Altering : " + bwFileName + "; size: " + getSize(fileName) + "kb");
        } catch (IOException ex) {
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(AlterImageTask.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//end of run();

    private long getSize(String filename) {
        File file = new File("bw_"+filename);
        long kilobytes = 0;

        if (file.exists()) {
            long bytes = file.length();
            kilobytes = (bytes / 1024);
        }
        return kilobytes;
    }
    
}//end of class()
