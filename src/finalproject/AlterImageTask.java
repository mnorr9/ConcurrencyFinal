package finalproject;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;
import javax.imageio.ImageIO;

/**
 *
 * 
 */
public class AlterImageTask implements Callable {

    private final String fileName;

    public AlterImageTask(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public Object call() throws Exception {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(fileName));

            BufferedImage blackAndWhiteImg = new BufferedImage(
                    img.getWidth(), img.getHeight(),
                    BufferedImage.TYPE_BYTE_BINARY);

            Graphics2D graphics = blackAndWhiteImg.createGraphics();
            graphics.drawImage(img, 0, 0, null);
            String bwFileName = "bw_" + fileName;
            ImageIO.write(blackAndWhiteImg, "jpg", new File(bwFileName));
            System.out.println("Writing image..." + bwFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

}//end of class()
