package helpers;

import java.awt.*;
import java.awt.image.BufferedImage;


public class ArtDrawer {


    public final int textHeight = 24;
    public final int textWidth = 144;
    private Graphics2D _graphics;
    private BufferedImage _image;

    public ArtDrawer(){
        setup();
    }
    private void setup(){
        BufferedImage bufferedImage = new BufferedImage(textWidth, textHeight,BufferedImage.TYPE_INT_RGB);
        Graphics graphics = bufferedImage.getGraphics();
        graphics.setFont(new Font("Dialog", Font.PLAIN, 14));

        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        _graphics = graphics2D;
        _image = bufferedImage;
    }

    public void drawLine(){
        System.out.println("*".repeat(Math.max(0, textWidth)));
    }

    public void draw(String word){

        _graphics.drawString(word, 6, 12);


        for (int y = 0; y < textHeight; y++) {
            StringBuilder stringBuilder = new StringBuilder();

            for (int x = 0; x < textWidth; x++) {
                stringBuilder.append(_image.getRGB(x, y) == -16777216 ? " " : "*");
            }

            if (stringBuilder.toString().trim().isEmpty()) {
                continue;
            }

            System.out.println(stringBuilder);
        }

    }
}
