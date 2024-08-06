package pieces;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Rook extends Piece {
    public boolean rookNotMoved = true;
    public Rook(int row, int col, int isBlack){
        super(row,col,isBlack);
        try {
            image = isBlack == 0 ? ImageIO.read(new File("pieces\\img\\wRook.png")) : ImageIO.read(new File("pieces\\img\\bRook.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        name = "rook";
    }
}
