package button;

import main.GamePanel;
import main.Panel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class NextEat extends Button{
    public boolean special;
    public boolean special1;

    public NextEat(Panel panel, int x, int y, int width, int height) {
        super(panel, x, y, width, height);
        this.special = false;
        this.special1 = false;
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