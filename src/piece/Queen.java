package piece;

import main.GamePanel;
import pair.Pair;

public class Queen extends ChessMan{
    public Queen(GamePanel panel, int x, int y, boolean white) {
        super(panel, x, y, white);
    }

    @Override
    public void setValue() {
        this.value = 9;
    }

    @Override
    public void setImageName() {
        this.name = "Queen";
    }

    @Override
    public void functionUpdate() {
        for (int a = 1; ; a++){
            if (this.i - a < 0){
                break;
            }
            if (panel.Board[i - a][j] == 0) {
                moves.add(new Pair<>(this.i - a, this.j));
            }
            else if (panel.Board[i - a][j] * value < 0) {
                eats.add(new Pair<>(this.i - a, this.j));
                break;
            }
            else{
                break;
            }
        }
        for (int a = 1; ; a++){
            if (this.i + a > 7){
                break;
            }
            if (panel.Board[i + a][j] == 0) {
                moves.add(new Pair<>(this.i + a, this.j));
            }
            else if (panel.Board[i + a][j] * value < 0) {
                eats.add(new Pair<>(this.i + a, this.j));
                break;
            }
            else{
                break;
            }
        }
        for (int a = 1; ; a++){
            if (this.j + a > 7){
                break;
            }
            if (panel.Board[i][j + a] == 0) {
                moves.add(new Pair<>(this.i, this.j + a));
            }
            else if (panel.Board[i][j + a] * value < 0) {
                eats.add(new Pair<>(this.i, this.j + a));
                break;
            }
            else{
                break;
            }
        }
        for (int a = 1; ; a++){
            if (this.j - a < 0){
                break;
            }
            if (panel.Board[i][j - a] == 0) {
                moves.add(new Pair<>(this.i, this.j - a));
            }
            else if (panel.Board[i][j - a] * value < 0) {
                eats.add(new Pair<>(this.i, this.j - a));
                break;
            }
            else{
                break;
            }
        }
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
