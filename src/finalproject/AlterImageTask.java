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

            // Wait/Block until computation is done!
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
            System.out.println("Writing image..." + bwFileName);
        } catch (InterruptedException | ExecutionException | IOException ex) {
        }

    }//end of run();

}//end of class()
