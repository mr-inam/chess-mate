package pieces;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class King extends Piece{
    boolean kingNotMoved = true;
    public King(int row, int col, int isBlack){
        super(row,col,isBlack);
        try {
            image = isBlack == 0 ? ImageIO.read(new File("pieces\\\\img\\\\wKing.png")) : ImageIO.read(new File("pieces\\\\img\\\\bKing.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        name = "king";
    }
}
