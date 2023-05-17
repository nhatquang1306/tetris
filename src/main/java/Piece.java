import java.awt.*;
import java.awt.event.KeyEvent;

public class Piece {

    public static final int BOARD_WIDTH = 10, BOARD_HEIGHT = 20, BLOCK_SIZE = 30;
    private boolean collision = false;
    private int x = 4, y = 0, delay = 500;
    public int[][] map;
    public Color color;
    private long begin = 0;

    public boolean update() {
        if (collision) {
            reset();
            return false;
        }
        if (System.currentTimeMillis() - begin >= delay) {
            if (y + map.length == BOARD_HEIGHT || checkCollision()) {
                collision = true;
            } else {
                y++;
            }
            begin = System.currentTimeMillis();
        }
        return true;
    }

    public boolean checkCollision() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] != 0 && Board.board[y + i + 1][x + j] != Color.BLACK) {
                    return true;
                }
            }
        }
        return false;
    }

    public void rotateLeft() {
        map = transpose("left");
    }
    public void rotateRight() {
        map = transpose("right");
    }

    public int[][] transpose(String dir) {
        int[][] transposed = new int[map[0].length][map.length];
        if (dir.equals("right")) {
            for (int i = map[0].length - 1; i >= 0; i--) {
                for (int j = 0; j < map.length; j++) {
                    transposed[i][map.length - j - 1] = map[j][i];
                }
            }
        } else {
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[0].length; j++) {
                    transposed[map[0].length - j - 1][i] = map[i][j];
                }
            }
        }
        return transposed;
    }


    public void drawPiece(Graphics g) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] == 1) {
                    g.setColor(color);
                    g.fillRect((x + j) * BLOCK_SIZE, (y + i) * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }
    }


    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_SPACE) {
            delay = 10;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            delay = 50;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (x == 0) return;
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[i].length; j++) {
                    if (map[i][j] != 0 && Board.board[y + i][x + j - 1] != Color.BLACK) {
                        return;
                    }
                }
            }
            x--;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (x == BOARD_WIDTH - map[0].length) return;
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[i].length; j++) {
                    if (map[i][j] != 0 && Board.board[y + i][x + j + 1] != Color.BLACK) {
                        return;
                    }
                }
            }
            x++;
        } else if (e.getKeyCode() == KeyEvent.VK_Z) {
            rotateLeft();
        } else if (e.getKeyCode() == KeyEvent.VK_X) {
            rotateRight();
        }

    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_SPACE || (e.getKeyCode() == KeyEvent.VK_DOWN)) {
            delay = 500;
        }
    }

    public void reset() {
        x = 4;
        y = 0;
        collision = false;
        delay = 500;
    }

    public Piece(String type) {
        switch (type) {
            case ("I"):
                map = new int[][]{{1, 1, 1, 1}};
                color = Color.RED;
                break;
            case ("T"):
                map = new int[][]{{1, 1, 1}, {0, 1, 0}};
                color = Color.CYAN;
                break;
            case ("L"):
                map = new int[][]{{1, 1, 1}, {1, 0, 0}};
                color = Color.ORANGE;
                break;
            case ("J"):
                map = new int[][]{{1, 1, 1}, {0, 0, 1}};
                color = Color.BLUE;
                break;
            case ("S"):
                map = new int[][]{{0, 1, 1}, {1, 1, 0}};
                color = Color.PINK;
                break;
            case ("Z"):
                map = new int[][]{{1, 1, 0}, {0, 1, 1}};
                color = Color.GREEN;
                break;
            case ("O"):
                map = new int[][]{{1, 1}, {1, 1}};
                color = Color.YELLOW;
        }
    }

    public boolean isCollision() {
        return collision;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int[][] getMap() {
        return map;
    }

    public Color getColor() {
        return color;
    }
}


