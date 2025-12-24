package snakegame;

import javax.swing.*;
import java.awt.*;

public class SnakeGame extends JFrame {

    SnakeGame() {
        setLayout(null);
        setBounds(0, 0, 1286, 676);
        setResizable(false);

        ImageIcon L1 = new ImageIcon(ClassLoader.getSystemResource("icons/h.png"));
        Image L2 = L1.getImage().getScaledInstance(1286, 676, Image.SCALE_SMOOTH);
        ImageIcon L3 = new ImageIcon(L2);
        JLabel image = new JLabel(L3);
        image.setBounds(0, 0, 1286, 676);
        image.setLayout(null);
        add(image);
        
        Board board = new Board();
        board.setBounds(300, 130, 653, 388);   
        image.add(board);
        
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new SnakeGame();
    }
}
