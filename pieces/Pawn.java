package pieces;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Pawn extends Piece{
    public Pawn(int row, int col, int isBlack){
        super(row,col,isBlack);
        try {
            image = isBlack == 0 ? ImageIO.read(new File("pieces\\\\img\\\\wPawn.png")) : ImageIO.read(new File("pieces\\\\img\\\\bPawn.png"));
            name =  isBlack == 0 ? "w-pawn" : "b-pawn";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
