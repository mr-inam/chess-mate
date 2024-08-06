package pieces;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Bishop extends Piece{
    public Bishop(int row, int col, int isBlack){
        super(row,col,isBlack);
        try {
            image = isBlack == 0 ? ImageIO.read(new File("pieces\\\\img\\\\wBishop.png")) : ImageIO.read(new File("pieces\\\\img\\\\bBishop.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        name = "bishop";
    }
}
