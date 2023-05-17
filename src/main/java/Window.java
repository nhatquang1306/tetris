import javax.swing.*;
import java.awt.*;


public class Window {
    private static final int WIDTH = 445, HEIGHT = 640;
    private Board board;
    private JFrame window;

    public Window() {
        window = new JFrame("Tetris");
        window.setSize(WIDTH, HEIGHT);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setLocationRelativeTo(null);

        board = new Board();
        window.add(board);
        window.addKeyListener(board);
        window.setVisible(true);
    }

    public static void main(String[] args) {
        new Window();

    }
}
