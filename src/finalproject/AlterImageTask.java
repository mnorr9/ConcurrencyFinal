package finalproject;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.BlockingQueue;

import javax.imageio.ImageIO;

/**
 *
 * 
 */
public class AlterImageTask implements Runnable {

    private String fileName;
    private final BlockingQueue<String> sharedQueue;

    public AlterImageTask(BlockingQueue<String> sharedQueue) {
        this.sharedQueue = sharedQueue;
    }

    @Override
    public void run() {
    	
    	while(true){
    		try {
    			fileName = sharedQueue.take();
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
    		} catch (Exception ex) {
    			ex.printStackTrace();
    		}
    	}

    }

}//end of class()
