package button;

import main.GamePanel;
import main.Panel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class NextEat extends Button{
    public NextEat(Panel panel, int x, int y, int width, int height) {
        super(panel, x, y, width, height);
    }

    @Override
    public void setImageName() {
        this.name1 = "Attack1";
        this.name2 = "Attack2";
    }

    @Override
    public void functionUpdate() {

    }
}