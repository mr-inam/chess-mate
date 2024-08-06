package pieces;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Knight extends Piece{
    public Knight(int row, int col, int isBlack){
        super(row,col,isBlack);
        try {
            image = isBlack == 0 ? ImageIO.read(new File("pieces\\\\img\\\\wKnight.png")) : ImageIO.read(new File("pieces\\\\img\\\\bKnight.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        name = "knight";
    }
}
