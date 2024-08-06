import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import pieces.*;

class Board extends JPanel implements MouseListener{

    private final int SQUARE_SIZE = 85;
    private ArrayList<Piece> pieces = new ArrayList<Piece>();
    private Piece activePiece = null;

    boolean wEnPassant = false;
    boolean bEnPassant = false;
    private boolean castledAtThisMove = false;

    Board() {
        setPreferredSize(new Dimension(8*SQUARE_SIZE, 8*SQUARE_SIZE));
        initPiece();
        addMouseListener(this);
    }
    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        { // this block code is just for to display the board
            boolean isFillSquareTurn;
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    isFillSquareTurn = (((i+j)%2) == 0) ? true : false;
                    if(isFillSquareTurn) 
                        g2d.setColor(new Color(228, 230, 202));
                    else
                        g2d.setColor(new Color(99, 145, 56));
                    g2d.fillRect(i*SQUARE_SIZE, j*SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
                }
                isFillSquareTurn = false;
            }
        }
        if (activePiece != null) {
            g2d.setColor(Color.YELLOW);
            g2d.fillRect(activePiece.getCol()*SQUARE_SIZE, activePiece.getRow()*SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
        }
        if (activePiece != null) {
            g2d.setColor(Color.BLACK);
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if(canMoved(j, i)){
                        if (getPiece(j, i) != null) {
                            if (getPiece(j, i).colorIsBlack != activePiece.colorIsBlack) {
                                g2d.drawOval(i*SQUARE_SIZE+7, j*SQUARE_SIZE+7, 71, 71);
                            }
                        }else{
                            g2d.fillOval(i*SQUARE_SIZE+27, j*SQUARE_SIZE+27, 30, 30);
                        }
                    }
                }
            }
        }
        for (Piece piece : pieces) {
            piece.draw(g2d);
        }
    }

    void initPiece(){
        // Black Pieces
        pieces.add(new Rook(0,0,1));
        pieces.add(new Knight(0,1,1));
        pieces.add(new Bishop(0,2,1));
        pieces.add(new Queen(0,3,1));
        pieces.add(new King(0,4,1));
        pieces.add(new Bishop(0,5,1));
        pieces.add(new Knight(0,6,1));
        pieces.add(new Rook(0,7,1));
        for (int i = 0; i < 8; i++) {
            pieces.add(new Pawn(1,i,1));
        }

        // White Pieces
        pieces.add(new Rook(7,0,0));
        pieces.add(new Knight(7,1,0));
        pieces.add(new Bishop(7,2,0));
        pieces.add(new Queen(7,3,0));
        pieces.add(new King(7,4,0));
        pieces.add(new Bishop(7,5,0));
        pieces.add(new Knight(7,6,0));
        pieces.add(new Rook(7,7,0));
        for (int i = 0; i < 8; i++) {
            pieces.add(new Pawn(6,i,0));
        }
    }

    private Piece getPiece(int row, int col){
        for (Piece p : pieces) {
            if (p.getRow() == row && p.getCol() == col) {
                return p;
            }
        }
        return null;
    }

    private boolean canMoved(int toR, int toC){
        int fromR = activePiece.getRow();
        int fromC = activePiece.getCol();
        String name = activePiece.name;
        switch (name) {
            case "king":
                if(activePiece.getRow() == 0 && activePiece.getCol() == 4){
                    Piece rookLeft = getPiece(0, 0);
                    Piece rookRight = getPiece(0, 7);
                    if (rookLeft!= null && rookLeft.name.equals("rook") && toR == 0 && toC == 2 && rookPathClear(fromR,fromC,toR,toC)){
                        castledAtThisMove = true;
                        return true;
                    }
                    if (rookRight!= null && rookRight.name.equals("rook") && toR == 0 && toC == 6 && rookPathClear(fromR,fromC,toR,toC)){
                        castledAtThisMove = true;
                        return true;
                    }
                }
                if(activePiece.getRow() == 7 && activePiece.getCol() == 4){
                    Piece rookLeft = getPiece(7, 0);
                    Piece rookRight = getPiece(7, 7);
                    if (rookLeft!= null && rookLeft.name.equals("rook") && toR == 7 && toC == 2 && rookPathClear(fromR,fromC,toR,toC)){
                        castledAtThisMove = true;
                        return true;
                    }
                    if (rookRight!= null && rookRight.name.equals("rook") && toR == 7 && toC == 6 && rookPathClear(fromR,fromC,toR,toC)){
                        castledAtThisMove = true;
                        return true;
                    }
                }
                return ((Math.abs(fromR-toR) + Math.abs(fromC-toC)) == 1 || (Math.abs(fromR-toR) * Math.abs(fromC-toC)) == 1);
            case "knight":
                return (Math.abs(fromC-toC) * Math.abs(fromR-toR) == 2);
            case "bishop":
                return ((Math.abs(fromC-toC) == Math.abs(fromR-toR)) && (bishopPathClear(fromR, fromC, toR, toC)));
            case "rook":
                return ((fromC == toC || fromR == toR) && (rookPathClear(fromR,fromC,toR,toC)));
            case "queen":
                return (((Math.abs(fromC-toC) == Math.abs(fromR-toR)) && (bishopPathClear(fromR, fromC, toR, toC))) || ((fromC == toC || fromR == toR) && (rookPathClear(fromR, fromC, toR, toC))));
            case "w-pawn":
                Piece p = getPiece(toR,toC);
                if(fromR == 3){
                    if(fromC+1 < 8 && getPiece(fromR,fromC+1) != null && getPiece(fromR,fromC+1).name.equals("b-pawn") && toR == fromR-1 && toC == fromC+1){
                        wEnPassant = true;
                        return true;
                    }
                    if(fromC-1 >=0 && getPiece(fromR,fromC-1) != null && getPiece(fromR,fromC-1).name.equals("b-pawn") && toR == fromR-1 && toC == fromC-1){
                        wEnPassant = true;
                        return true;
                    }
                }
                if((fromR == 6) && (toC == fromC) && (p == null) && (toR-fromR == -2) && (getPiece(toR+1,toC) == null)){
                    return true;
                }
                if((toC == fromC) && (p == null) && (toR-fromR == -1)){
                    return true;
                }
                if ((p != null) && (p.colorIsBlack == 1) && (toR-fromR == -1) && (Math.abs(toC-fromC) == 1)){
                    return true;
                }
                break;
            case "b-pawn":
                Piece piece = getPiece(toR,toC);
                if(fromR == 4){
                    if(fromC+1 < 8 && getPiece(fromR,fromC+1) != null && getPiece(fromR,fromC+1).name.equals("w-pawn") && toR == fromR+1 && toC == fromC+1){
                        bEnPassant = true;
                        return true;
                    }
                    if(fromC-1 >=0 && getPiece(fromR,fromC-1) != null && getPiece(fromR,fromC-1).name.equals("w-pawn") && toR == fromR+1 && toC == fromC-1){
                        bEnPassant = true;
                        return true;
                    }
                }
                if((fromR == 1) && (toC == fromC) && (piece == null) && (toR-fromR == 2) && (getPiece(toR-1,toC) == null)){
                    return true;
                }
                if((toC == fromC) && (piece == null) && (toR-fromR == 1)){
                    return true;
                }
                if ((piece != null) && (piece.colorIsBlack == 0) && (toR-fromR == 1) && (Math.abs(toC-fromC) == 1)){
                    return true;
                }
                break;
            default:
                break;
        }
        return false;
    }

    private boolean bishopPathClear(int fromR, int fromC, int toR, int toC){
        int r= toR > fromR ? 1 : -1;
        int c= toC > fromC ? 1 : -1;
        for (int i = 1; i < Math.abs(fromR-toR); i++) {
            if(getPiece(fromR + r*i,fromC + c*i) != null){
                return false;
            }
        }
        return true;
    }
    private boolean rookPathClear(int fromR, int fromC, int toR, int toC){
        int r= toR > fromR ? 1 : -1;
        int c= toC > fromC ? 1 : -1;
        if (fromR == toR) {
            for (int i = 1; i < Math.abs(fromC-toC); i++) {
                if(getPiece(fromR,fromC + c*i) != null){
                    return false;
                }
            }
        }
        if (fromC == toC) {
            for (int i = 1; i < Math.abs(fromR-toR); i++) {
                if(getPiece(fromR + r*i,fromC) != null){
                    return false;
                }
            }
        }
        return true;
    }

    private int isBlackTurn = 0;

    public void mouseClicked(MouseEvent e) {
        if (activePiece == null) {
            int row = e.getY()/SQUARE_SIZE;
            int col = e.getX()/SQUARE_SIZE;
            Piece p = getPiece(row, col);
            if(p != null && p.colorIsBlack == isBlackTurn){
                activePiece = p;
                repaint();
            }
        }else{
            int row = e.getY()/SQUARE_SIZE;
            int col = e.getX()/SQUARE_SIZE;
            if (getPiece(row, col) != null && activePiece.colorIsBlack == getPiece(row, col).colorIsBlack && isBlackTurn != -1) {
                activePiece = null;
                repaint();
            } else if (isBlackTurn != -1) {
                if (canMoved(row,col)) {
                    if(getPiece(row, col) != null){
                        if(getPiece(row, col).name == "king"){
                            if(isBlackTurn == 1){
                                JOptionPane.showMessageDialog(this, "Black won!");
                            }else{
                                JOptionPane.showMessageDialog(this, "White won!");
                            }
                            isBlackTurn = -1;
                            pieces.remove(getPiece(row, col));
                            activePiece.setRow(row);
                            activePiece.setCol(col);
                            activePiece = null;
                            repaint();
                            return;
                        }
                        pieces.remove(getPiece(row, col));
                    }
                    activePiece.setRow(row);
                    activePiece.setCol(col);
                    isBlackTurn = (isBlackTurn == 1) ? 0 : 1;
                }
                { // castling
                    if (activePiece.name == "king" && row == 0 && col == 6 && castledAtThisMove) {
                        Piece piece = getPiece(row, 7);
                        piece.setRow(row);
                        piece.setCol(col-1);
                        castledAtThisMove = false;
                    }
                    if (activePiece.name == "king" && row == 0 && col == 2 && castledAtThisMove) {
                        Piece piece = getPiece(row, 0);
                        piece.setRow(row);
                        piece.setCol(col+1);
                        castledAtThisMove = false;
                    }
                    if (activePiece.name == "king" && row == 7 && col == 6 && castledAtThisMove) {
                        Piece piece = getPiece(row, 7);
                        piece.setRow(row);
                        piece.setCol(col-1);
                        castledAtThisMove = false;
                    }
                    if (activePiece.name == "king" && row == 7 && col == 2 && castledAtThisMove) {
                        Piece piece = getPiece(row, 0);
                        piece.setRow(row);
                        piece.setCol(col+1);
                        castledAtThisMove = false;
                    }
                }
                { // enpassant
                    if(wEnPassant){
                        pieces.remove(getPiece(activePiece.getRow()+1,activePiece.getCol()));
                        wEnPassant = false;
                    }
                    if (bEnPassant){
                        pieces.remove(getPiece(activePiece.getRow()-1,activePiece.getCol()));
                        bEnPassant = false;
                    }
                }
                // promotion
                if((activePiece.name.equals("w-pawn") && row == 0) || (activePiece.name.equals("b-pawn") && row == 7) && canMoved(row, col)){
                    promotion(row, col);
                }else{
                    activePiece = null;
                    repaint();
                }
            }
        }
    }
    
    private void promotion(int row,int col){
        if(isBlackTurn == 0){
            JFrame window = new JFrame();
            window.setLayout(new GridLayout(1,4));
            window.setMinimumSize(new Dimension(344,86));
            window.setResizable(false);
            window.setLocationRelativeTo(null);
            window.setUndecorated(true);

            JButton queen = new JButton(new ImageIcon("pieces\\\\img\\\\bQueen.png"));
            queen.setFocusable(false);
            queen.setBackground(new Color(99,145,56));
            queen.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    pieces.remove(activePiece);
                    pieces.add(new Queen(row,col,1));
                    window.dispose();
                    activePiece = null;
                    repaint();
                }
            });
            window.add(queen);
            JButton rook = new JButton(new ImageIcon("pieces\\\\img\\\\bRook.png"));
            rook.setFocusable(false);
            rook.setBackground(new Color(99,145,56));
            rook.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    pieces.remove(activePiece);
                    pieces.add(new Rook(row,col,1));
                    window.dispose();
                    activePiece = null;
                    repaint();
                }
            });
            window.add(rook);
            JButton knight = new JButton(new ImageIcon("pieces\\\\img\\\\bKnight.png"));
            knight.setFocusable(false);
            knight.setBackground(new Color(99,145,56));
            knight.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    pieces.remove(activePiece);
                    pieces.add(new Knight(row,col,1));
                    window.dispose();
                    activePiece = null;
                    repaint();
                }
            });
            window.add(knight);
            JButton bishop = new JButton(new ImageIcon("pieces\\\\img\\\\bBishop.png"));
            bishop.setFocusable(false);
            bishop.setBackground(new Color(99,145,56));
            bishop.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    pieces.remove(activePiece);
                    pieces.add(new Bishop(row,col,1));
                    window.dispose();
                    activePiece = null;
                    repaint();
                }
            });
            window.add(bishop);
            window.pack();
            window.setVisible(true);
        } else if(isBlackTurn == 1){
            JFrame window = new JFrame();
            window.setLayout(new GridLayout());
            window.setMinimumSize(new Dimension(344,86));
            window.setResizable(false);
            window.setLocationRelativeTo(null);
            window.setUndecorated(true);

            JButton queen = new JButton(new ImageIcon("pieces\\\\img\\\\wQueen.png"));
            queen.setFocusable(false);
            queen.setBackground(new Color(99, 145, 56));
            queen.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    pieces.remove(activePiece);
                    pieces.add(new Queen(row,col,0));
                    window.dispose();
                    activePiece = null;
                    repaint();
                }
            });
            window.add(queen);
            JButton rook = new JButton(new ImageIcon("pieces\\\\img\\\\wRook.png"));
            rook.setFocusable(false);
            rook.setBackground(new Color(99, 145, 56));
            rook.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    pieces.remove(activePiece);
                    pieces.add(new Rook(row,col,0));
                    window.dispose();
                    activePiece = null;
                    repaint();
                }
            });
            window.add(rook);
            JButton knight = new JButton(new ImageIcon("pieces\\\\img\\\\wKnight.png"));
            knight.setFocusable(false);
            knight.setBackground(new Color(99, 145, 56));
            knight.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    pieces.remove(activePiece);
                    pieces.add(new Knight(row,col,0));
                    window.dispose();
                    activePiece = null;
                    repaint();
                }
            });
            window.add(knight);
            JButton bishop = new JButton(new ImageIcon("pieces\\\\img\\\\wBishop.png"));
            bishop.setFocusable(false);
            bishop.setBackground(new Color(99, 145, 56));
            bishop.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    pieces.remove(activePiece);
                    pieces.add(new Bishop(row,col,0));
                    window.dispose();
                    activePiece = null;
                    repaint();
                }
            });
            window.add(bishop);
            window.pack();
            window.setVisible(true);
        } else {
            activePiece = null;
            repaint();
        }
    }
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}
