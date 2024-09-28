package piece;

import main.GamePanel;
import pair.Pair;

public class Knight extends ChessMan{
    public Knight(GamePanel panel, int x, int y, boolean white) {
        super(panel, x, y, white);
    }

    @Override
    public void setValue() {
        this.value = 3;
    }

    @Override
    public void setImageName() {
        this.name = "Knight";
    }

    @Override
    public void functionUpdate() {
        if(this.i - 2 > -1 && this.j - 1 > -1){
            if (panel.Board[i - 2][j - 1] == 0) {
                moves.add(new Pair<>(this.i - 2, this.j - 1));
            }
            else if (panel.Board[i - 2][j - 1] * value < 0) {
                eats.add(new Pair<>(this.i - 2, this.j - 1));
            }
        }
        if(this.i + 2 < 8 && this.j - 1 > -1){
            if (panel.Board[i + 2][j - 1] == 0) {
                moves.add(new Pair<>(this.i + 2, this.j - 1));
            }
            else if (panel.Board[i + 2][j - 1] * value < 0) {
                eats.add(new Pair<>(this.i + 2, this.j - 1));
            }
        }
        if(this.i - 2 > -1 && this.j + 1 < 8){
            if (panel.Board[i - 2][j + 1] == 0) {
                moves.add(new Pair<>(this.i - 2, this.j + 1));
            }
            else if (panel.Board[i - 2][j + 1] * value < 0) {
                eats.add(new Pair<>(this.i - 2, this.j + 1));
            }
        }
        if(this.i + 2 < 8 && this.j + 1 < 8){
            if (panel.Board[i + 2][j + 1] == 0) {
                moves.add(new Pair<>(this.i + 2, this.j + 1));
            }
            else if (panel.Board[i + 2][j + 1] * value < 0) {
                eats.add(new Pair<>(this.i + 2, this.j + 1));
            }
        }
        if(this.i + 1 < 8 && this.j + 2 < 8){
            if (panel.Board[i + 1][j + 2] == 0) {
                moves.add(new Pair<>(this.i + 1, this.j + 2));
            }
            else if (panel.Board[i + 1][j + 2] * value < 0) {
                eats.add(new Pair<>(this.i + 1, this.j + 2));
            }
        }
        if(this.i + 1 < 8 && this.j - 2 > -1){
            if (panel.Board[i + 1][j - 2] == 0) {
                moves.add(new Pair<>(this.i + 1, this.j - 2));
            }
            else if (panel.Board[i + 1][j - 2] * value < 0) {
                eats.add(new Pair<>(this.i + 1, this.j - 2));
            }
        }
        if(this.i - 1 > -1 && this.j + 2 < 8){
            if (panel.Board[i - 1][j + 2] == 0) {
                moves.add(new Pair<>(this.i - 1, this.j + 2));
            }
            else if (panel.Board[i - 1][j + 2] * value < 0) {
                eats.add(new Pair<>(this.i - 1, this.j + 2));
            }
        }
        if(this.i - 1 > -1 && this.j - 2 > -1){
            if (panel.Board[i - 1][j - 2] == 0) {
                moves.add(new Pair<>(this.i - 1, this.j - 2));
            }
            else if (panel.Board[i - 1][j - 2] * value < 0) {
                eats.add(new Pair<>(this.i - 1, this.j - 2));
            }
        }
    }
}
