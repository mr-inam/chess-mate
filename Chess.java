import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Chess extends JFrame{
    public static void main(String[] args) {
        new Chess();
    }
    Chess(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("chess-mate");
        setResizable(false);
        setLayout(new FlowLayout());
        setSize(720,720);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon("pieces\\img\\logo.png").getImage());

        add(new Board());
        
        getContentPane().setBackground(Color.WHITE);
        setVisible(true);
    }
}