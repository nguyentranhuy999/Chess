package button;

import main.Panel;

public class EatMove extends Button{
    public EatMove(Panel panel, int x, int y, int width, int height) {
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
