import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Board extends JPanel implements KeyListener, MouseListener {

    public static final int BOARD_WIDTH = 10, BOARD_HEIGHT = 20, BLOCK_SIZE = 30;
    public static Color[][] board = new Color[BOARD_HEIGHT][BOARD_WIDTH];
    private Timer timer;
    private final Piece[] pieces;
    private Piece piece, nextPiece;
    private int lineCleared = 0;
    private final Random rand = new Random();
    private boolean gameOver = false;
    private long score = 0;
    private BufferedImage pause, refresh;

    public Board() {
        File fileP= new File("src/main/resources/pause.png");
        File fileR = new File("src/main/resources/refresh.png");
        try {
            pause = ImageIO.read(fileP);
            refresh = ImageIO.read(fileR);
        } catch (IOException e) {
            e.getLocalizedMessage();
        }

        pieces = new Piece[]{new Piece("T"), new Piece("L"), new Piece("J"), new Piece("S"), new Piece("Z"), new Piece("I"), new Piece("O")};
        for (Color[] row : board) {
            Arrays.fill(row, Color.BLACK);
        }
        piece = pieces[rand.nextInt(7)];
        nextPiece = pieces[rand.nextInt(7)];
        timer = new Timer(1, e -> {
            if (nextPiece.rotateCollision(4, 0, nextPiece.getNewMap())) {
                gameOver = true;
                timer.stop();
            } else if (!piece.update()) {
                piece = nextPiece;
                nextPiece = pieces[rand.nextInt(7)];
                return;
            }
            repaint();
        });
        timer.start();
    }

    public void restartGame() {
        for (Color[] row : board) {
            Arrays.fill(row, Color.BLACK);
        }
        Piece.setPaused(false);
        gameOver = false;
        piece.reset();
        piece = pieces[rand.nextInt(7)];
        nextPiece = pieces[rand.nextInt(7)];
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
        switch (toBeCleared) {
            case 1: score += 40; break;
            case 2: score += 100; break;
            case 3: score += 300; break;
            case 4: score += 1200;
        }
    }

    public void paintNext(Graphics g) {
        int startX, startY = 30, limitX, limitY = 2;
        if (nextPiece.getType().equals("I")) {
            limitX = 4; limitY = 1; startX = 320;
        } else if (nextPiece.getType().equals("O")) {
            limitX = 2; startX = 350;
        } else {
            limitX = 3; startX = 335;
        }
        nextPiece.drawNextPiece(startX, startY, g);
        for (int i = 0; i <= limitY; i++) {
            g.setColor(Color.WHITE);
            g.drawLine(startX, startY + BLOCK_SIZE * i, startX + BLOCK_SIZE * limitX, startY + BLOCK_SIZE * i);
        }
        for (int i = 0; i <= limitX; i++) {
            g.drawLine(startX + BLOCK_SIZE * i, startY, startX + BLOCK_SIZE * i, startY + BLOCK_SIZE * limitY);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.GRAY);
        g.fillRect(BLOCK_SIZE * BOARD_WIDTH, 0, getWidth(), getHeight());
        colorBoard(g);
        if (!gameOver) piece.drawPiece(g);
        for (int i = 0; i <= BOARD_HEIGHT; i++) {
            g.setColor(Color.WHITE);
            g.drawLine(0, BLOCK_SIZE * i, BLOCK_SIZE * BOARD_WIDTH, BLOCK_SIZE * i);
        }
        for (int i = 0; i <= BOARD_WIDTH; i++) {
            g.drawLine(BLOCK_SIZE * i, 0, BLOCK_SIZE * i, BLOCK_SIZE * BOARD_HEIGHT);
        }
        paintNext(g);
        Font font = new Font("Arial", Font.BOLD, 16);
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString("SCORE: " + score, 330, 300);
        g.drawString("LINES: " + lineCleared, 330, 340);
        g.drawImage(pause, 345, 380, null);
        g.drawImage(refresh, 345, 460, null);
        if (gameOver) {
            font = new Font("Arial", Font.BOLD, 32);
            g.setFont(font);
            g.drawString("GAME OVER", 70, 320);
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

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getX() >= 345 && e.getX() <= 345 + pause.getWidth() && e.getY() >= 380 + pause.getHeight() / 2  && e.getY() <= 380 + pause.getHeight() * 3 / 2) {
            Piece.setPaused(!Piece.isPaused());
        } else if (e.getX() >= 345 && e.getX() <= 345 + refresh.getWidth() && e.getY() >= 460 + refresh.getHeight() / 2  && e.getY() <= 460 + refresh.getHeight() * 3 / 2) {
            restartGame();
        }
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
