import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class Window {
    private static final int WIDTH = 480, HEIGHT = 638;
    private final Title title;
    private final JFrame window;

    public Window() {
        window = new JFrame("Tetris");
        window.setSize(WIDTH, HEIGHT);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setLocationRelativeTo(null);

        title = new Title();
        window.add(title);
        window.addMouseListener(title);
        window.setVisible(true);
    }

    public void startGame() {
        window.remove(title);
        window.removeMouseListener(title);

        Board board = new Board();
        window.add(board);
        window.addKeyListener(board);
        window.addMouseListener(board);
        window.revalidate();
    }

    public static void main(String[] args) {
        new Window();
    }

    private class Title extends JPanel implements MouseListener {

        public Title() {
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
            Font font = new Font("Arial", Font.BOLD, 20);
            g.setFont(font);
            g.setColor(Color.WHITE);
            g.drawString("CONTROLS:", 100, 100);
            g.drawString("CLICK ANYWHERE TO START", 100, 350);
            font = new Font("Arial", Font.BOLD, 12);
            g.setFont(font);
            g.drawString("LEFT: MOVE LEFT", 100, 130);
            g.drawString("RIGHT: MOVE RIGHT", 100, 160);
            g.drawString("DOWN: SOFT DROP", 100, 190);
            g.drawString("SPACE: HARD DROP", 100, 220);
            g.drawString("Z: ROTATE COUNTERCLOCKWISE", 100, 250);
            g.drawString("X/UP: ROTATE CLOCKWISE", 100, 280);


        }


        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            startGame();
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }


}
