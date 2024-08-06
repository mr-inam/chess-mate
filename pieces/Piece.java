package pieces;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Piece {
    BufferedImage image = null;
    public int colorIsBlack;
    private int x, y;
    private final int SQUARE_SIZE = 85;
    public String name;

    Piece(int row, int col, int colorIsBlack){
        x = col*SQUARE_SIZE;
        y = row*SQUARE_SIZE;
        this.colorIsBlack = colorIsBlack;
    }  
    public int getCol() {
        return x/SQUARE_SIZE;
    }
    public int getRow() {
        return y/SQUARE_SIZE;
    }
    public void setRow(int row) {
        y = row*SQUARE_SIZE;
    }
    public void setCol(int col) {
        x = col*SQUARE_SIZE;
    }
    public void draw(Graphics2D g){
        g.drawImage(image,x,y,SQUARE_SIZE,SQUARE_SIZE,null);
    }
}
