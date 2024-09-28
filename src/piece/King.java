package piece;

import main.GamePanel;
import main.Panel;
import pair.Pair;

public class King extends ChessMan{
    public King(GamePanel panel, int x, int y, boolean white) {
        super(panel, x, y, white);
    }

    @Override
    public void setImageName() {
        this.name = "King";
    }

    @Override
    public void setValue() {
        this.value = 1000;
    }

    @Override
    public void functionUpdate() {
        if (this.j + 1 < 8) {
            if (panel.Board[i][j + 1] == 0) {
                moves.add(new Pair<>(this.i, this.j + 1));
            }
            else if (panel.Board[i][j + 1] * value < 0) {
                eats.add(new Pair<>(this.i, this.j + 1));
            }
        }
        if(this.i + 1 < 8) {
            if (panel.Board[i + 1][j] == 0) {
                moves.add(new Pair<>(this.i + 1, this.j));
            }
            else if (panel.Board[i + 1][j] * value < 0) {
                eats.add(new Pair<>(this.i + 1, this.j));
            }
        }
        if(this.j + 1 < 8 && this.i + 1 < 8) {
            if (panel.Board[i + 1][j + 1] == 0) {
                moves.add(new Pair<>(this.i + 1, this.j + 1));
            }
            else if (panel.Board[i + 1][j + 1] * value < 0) {
                eats.add(new Pair<>(this.i + 1, this.j + 1));
            }

        }
        if(this.j - 1 > -1){
            if (panel.Board[i][j - 1] == 0) {
                moves.add(new Pair<>(this.i, this.j - 1));
            }
            else if (panel.Board[i][j - 1] * value < 0) {
                eats.add(new Pair<>(this.i, this.j - 1));
            }
        }
        if(this.i - 1 > -1){
            if (panel.Board[i - 1][j] == 0) {
                moves.add(new Pair<>(this.i - 1, this.j));
            }
            else if (panel.Board[i - 1][j] * value < 0) {
                eats.add(new Pair<>(this.i - 1, this.j));
            }
        }
        if(this.j - 1 > -1 && this.i - 1 > -1){
            if (panel.Board[i - 1][j - 1] == 0) {
                moves.add(new Pair<>(this.i - 1, this.j - 1));
            }
            else if (panel.Board[i - 1][j - 1] * value < 0) {
                eats.add(new Pair<>(this.i - 1, this.j - 1));
            }
        }
        if(this.j + 1 < 8 && this.i - 1 > -1){
            if (panel.Board[i - 1][j + 1] == 0) {
                moves.add(new Pair<>(this.i - 1, this.j + 1));
            }
            else if (panel.Board[i - 1][j + 1] * value < 0) {
                eats.add(new Pair<>(this.i - 1, this.j + 1));
            }
        }
        if(this.j - 1 > -1 && this.i + 1 < 8){
            if (panel.Board[i + 1][j - 1] == 0) {
                moves.add(new Pair<>(this.i + 1, this.j - 1));
            }
            else if (panel.Board[i + 1][j - 1] * value < 0) {
                eats.add(new Pair<>(this.i + 1, this.j - 1));
            }
        }
    }
}
