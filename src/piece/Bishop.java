package piece;

import main.GamePanel;
import pair.Pair;

public class Bishop extends ChessMan{
    public Bishop(GamePanel panel, int x, int y , boolean white) {
        super(panel, x, y, white);
    }

    @Override
    public void setValue() {
        this.value = 3;
    }

    @Override
    public void setImageName() {
        this.name = "Bishop";
    }

    @Override
    public void functionUpdate() {
        for (int a = 1; ; a++){
            if (this.i - a < 0 || this.j - a < 0){
                break;
            }
            if (panel.Board[i - a][j - a] == 0) {
                moves.add(new Pair<>(this.i - a, this.j - a));
            }
            else if (panel.Board[i - a][j - a] * value < 0) {
                eats.add(new Pair<>(this.i - a, this.j - a));
                break;
            }
            else{
                break;
            }
        }
        for (int a = 1; ; a++){
            if (this.i + a > 7 || this.j + a > 7){
                break;
            }
            if (panel.Board[i + a][j + a] == 0) {
                moves.add(new Pair<>(this.i + a, this.j + a));
            }
            else if (panel.Board[i + a][j + a] * value < 0) {
                eats.add(new Pair<>(this.i + a, this.j + a));
                break;
            }
            else{
                break;
            }
        }
        for (int a = 1; ; a++){
            if (this.j + a > 7 || this.i - a < 0){
                break;
            }
            if (panel.Board[i -a][j + a] == 0) {
                moves.add(new Pair<>(this.i - a, this.j + a));
            }
            else if (panel.Board[i - a][j + a] * value < 0) {
                eats.add(new Pair<>(this.i - a, this.j + a));
                break;
            }
            else{
                break;
            }
        }
        for (int a = 1; ; a++){
            if (this.j - a < 0 || this.i + a > 7){
                break;
            }
            if (panel.Board[i + a][j - a] == 0) {
                moves.add(new Pair<>(this.i + a, this.j - a));
            }
            else if (panel.Board[i + a][j - a] * value < 0) {
                eats.add(new Pair<>(this.i + a, this.j - a));
                break;
            }
            else{
                break;
            }
        }
    }
}
