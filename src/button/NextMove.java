package button;

import main.GamePanel;
import main.Panel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class NextMove extends Button{
    public NextMove(Panel panel, int x, int y, int width, int height) {
        super(panel, x, y, width, height);
    }

    @Override
    public void setImageName() {
        this.name1 = "Preview1";
        this.name2 = "Preview2";
    }

    @Override
    public void functionUpdate() {

    }
}
