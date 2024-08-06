package pieces;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Queen extends Piece{
    public Queen(int row, int col, int isBlack){
        super(row,col,isBlack);
        try {
            image = isBlack == 0 ? ImageIO.read(new File("pieces\\img\\wQueen.png")) : ImageIO.read(new File("pieces\\img\\bQueen.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        name = "queen";
    }
}
