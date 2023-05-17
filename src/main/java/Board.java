import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Board extends JPanel implements KeyListener {

    public static final int BOARD_WIDTH = 10, BOARD_HEIGHT = 20, BLOCK_SIZE = 30;
    public static Color[][] board = new Color[BOARD_HEIGHT][BOARD_WIDTH];
    private Timer timer;
    private Piece[] pieces;
    private Piece piece;
    private int lineCleared = 0;
    private Random rand = new Random();



    public Board() {
        pieces = new Piece[]{new Piece("T"), new Piece("I"), new Piece("L"), new Piece("J"), new Piece("S"), new Piece("Z"), new Piece("I"), new Piece("O")};
        for (Color[] row : board) {
            Arrays.fill(row, Color.BLACK);
        }
        piece = pieces[rand.nextInt(8)];
        timer = new Timer(1, e -> {
            if (!piece.update()) {
                piece = pieces[rand.nextInt(8)];
                return;
            }
            repaint();
        });
        timer.start();
    }

    public void colorBoard(Graphics g) {

        if (piece.isCollision()) {
            for (int i = 0; i < piece.getMap().length; i++) {
                for (int j = 0; j < piece.getMap()[0].length; j++) {
                    if (piece.getMap()[i][j] == 1) {
                        board[piece.getY() + i][piece.getX() + j] = piece.getColor();
                    }
                }
            }
        }
        clearLines();
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                g.setColor(board[i][j]);
                g.fillRect(BLOCK_SIZE * j, BLOCK_SIZE * i, BLOCK_SIZE, BLOCK_SIZE);
            }
        }
    }

    public void clearLines() {
        int toBeCleared = 0;
        for (int i = BOARD_HEIGHT - 1; i >= 0; i--) {
            if (Arrays.stream(board[i]).noneMatch(e -> e == Color.BLACK)) {
                toBeCleared++;
                lineCleared++;
            } else if (toBeCleared > 0) {
                board[i + toBeCleared] = board[i].clone();
            }
        }
        for (int i = 0; i < toBeCleared; i++) {
            Arrays.fill(board[i], Color.BLACK);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.GRAY);
        g.fillRect(BLOCK_SIZE * BOARD_WIDTH, 0, getWidth(), getHeight());
        colorBoard(g);
        piece.drawPiece(g);
        for (int i = 0; i <= BOARD_HEIGHT; i++) {
            g.setColor(Color.WHITE);
            g.drawLine(0, BLOCK_SIZE * i, BLOCK_SIZE * BOARD_WIDTH, BLOCK_SIZE * i);
        }
        for (int i = 0; i <= BOARD_WIDTH; i++) {
            g.drawLine(BLOCK_SIZE * i, 0, BLOCK_SIZE * i, BLOCK_SIZE * BOARD_HEIGHT);
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        piece.keyPressed(e);

    }

    @Override
    public void keyReleased(KeyEvent e) {
        piece.keyReleased(e);
    }
}
