import java.awt.*;
import java.awt.event.KeyEvent;

public class Piece {

    private static final int BOARD_WIDTH = 10, BOARD_HEIGHT = 20, BLOCK_SIZE = 30;
    private final String type;
    private boolean collision = false;
    private static boolean paused = false;
    private int x = 4, y = 0, state = 1, delay = 400;
    private int[][] map, newMap;
    private Color color;
    private static long begin;

    public boolean update() {
        if (collision) {
            reset();
            return false;
        } else if (System.currentTimeMillis() - begin >= delay) {
            if (checkCollision()) collision = true;
            else if (!paused) y++;
            begin = System.currentTimeMillis();
        }
        return true;
    }

    public boolean checkCollision() {
        if (y + map.length == BOARD_HEIGHT) return true;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] != 0 && Board.board[y + i + 1][x + j] != Color.BLACK) {
                    return true;
                }
            }
        }
        return false;
    }

    public void reassign(int tempX, int tempY, int[][] tempMap, int tempState) {
        x = tempX; y = tempY; map = tempMap; state = tempState;
    }

    public void wallKickI(int testX, int tempY, int[][] tempMap, int tempState, int prev) {
        int tempX = testX;
        if (rotateCollision(tempX, tempY, tempMap)) {
            if ((tempState == 2 && prev == 1) || (tempState == 3 && prev == 4)) tempX -= 2;
            else if ((tempState == 1 && prev == 2) || (tempState == 4 && prev == 3)) tempX += 2;
            else if ((tempState == 3 && prev == 2) || (tempState == 4 && prev == 1)) tempX--;
            else tempX++;
        } else {
            reassign(tempX, tempY, tempMap, tempState);
            return;
        }

        if (rotateCollision(tempX, tempY, tempMap)) {
            tempX = testX;
            if ((tempState == 2 && prev == 1) || (tempState == 3 && prev == 4)) tempX++;
            else if ((tempState == 1 && prev == 2) || (tempState == 4 && prev == 3)) tempX--;
            else if ((tempState == 3 && prev == 2) || (tempState == 4 && prev == 1)) tempX += 2;
            else tempX -= 2;
        } else {
            reassign(tempX, tempY, tempMap, tempState);
            return;
        }
        if (!rotateCollision(tempX, tempY, tempMap)) {
            reassign(tempX, tempY, tempMap, tempState);
        }
    }

    public void wallKickRest(int testX, int testY, int[][] tempMap, int tempState, int prev) {
        int tempX = testX, tempY = testY;
        if (rotateCollision(tempX, tempY, tempMap)) {
            if (tempState == 2 || prev == 4) tempX--;
            else tempX++;
        } else {
            reassign(tempX, tempY, tempMap, tempState);
            return;
        }
        if (rotateCollision(tempX, tempY, tempMap)) {
            tempX = testX;
            if (tempState == 2) {
                tempX--; tempY--;
            } else if (prev == 2) {
                tempX++; tempY++;
            } else if (tempState == 4) {
                tempX++; tempY--;
            } else {
                tempX--; tempY++;
            }
        } else {
            reassign(tempX, tempY, tempMap, tempState);
            return;
        }
        if (!rotateCollision(tempX, tempY, tempMap)) {
            reassign(tempX, tempY, tempMap, tempState);
        }
    }

    public boolean rotateCollision(int tempX, int tempY, int[][] tempMap) {
        if (tempX < 0 || tempX + tempMap[0].length > BOARD_WIDTH || tempY + tempMap.length > BOARD_HEIGHT) return true;
        for (int i = 0; i < tempMap.length; i++) {
            for (int j = 0; j < tempMap[0].length; j++) {
                if (tempMap[i][j] != 0 && Board.board[tempY + i][tempX + j] != Color.BLACK) {
                    return true;
                }
            }
        }
        return false;
    }

    public void rotateLeft() {
        if (type.equals("O") || (type.equals("I") && y == 0)) return;
        int[][] tempMap = transpose("left");
        int tempX = x, tempY = y;
        int tempState = state, prev = tempState;
        tempState--;
        if (tempState == 0) tempState = 4;
        if (type.equals("I")) {
            if (tempState == 4) {
                tempX++; tempY--;
            } else if (tempState == 3) {
                tempX--; tempY += 2;
            } else if (tempState == 2) {
                tempX += 2; tempY-= 2;
            } else {
                tempX -= 2; tempY++;
            }
            wallKickI(tempX, tempY, tempMap, tempState, prev);
        } else {
            if (tempState == 2) {
                tempX++; tempY--;
            } else if (tempState == 1) tempX--;
            else if (tempState == 3) tempY++;
            wallKickRest(tempX, tempY, tempMap, tempState, prev);
        }
    }
    public void rotateRight() {
        if (type.equals("O") || (type.equals("I") && y == 0)) return;
        int[][] tempMap = transpose("right");
        int tempX = x, tempY = y;
        int tempState = state, prev = tempState;
        tempState++;
        if (tempState == 5) tempState = 1;
        if (type.equals("I")) {
            if (tempState == 4) {
                tempX++; tempY -= 2;
            } else if (tempState == 3) {
                tempX -= 2; tempY += 2;
            } else if (tempState == 2) {
                tempX += 2; tempY--;
            } else {
                tempX--; tempY++;
            }
            wallKickI(tempX, tempY, tempMap, tempState, prev);
        } else {
            if (tempState == 2) tempX++;
            else if (tempState == 3) {
                tempX--; tempY++;
            } else if (tempState == 4) tempY--;
            wallKickRest(tempX, tempY, tempMap, tempState, prev);
        }
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
        if (paused) return;
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
        } else if (e.getKeyCode() == KeyEvent.VK_X || e.getKeyCode() == KeyEvent.VK_UP) {
            rotateRight();
        }

    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_SPACE || (e.getKeyCode() == KeyEvent.VK_DOWN)) {
            delay = 400;
        }
    }
    public void drawNextPiece(int startX, int startY, Graphics g) {
        for (int i = 0; i < newMap.length; i++) {
            for (int j = 0; j < newMap[0].length; j++) {
                if (newMap[i][j] == 1) {
                    g.setColor(color);
                    g.fillRect(startX + j * BLOCK_SIZE, startY + i * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }
    }

    public void reset() {
        x = 4; y = 0; state = 1; delay = 400;
        collision = false;
        switch (type) {
            case ("I"):
                map = new int[][]{{1, 1, 1, 1}};
                newMap = new int[][]{{1, 1, 1, 1}};
                color = Color.RED;
                break;
            case ("T"):
                map = new int[][]{{0, 1, 0}, {1, 1, 1}};
                newMap = new int[][]{{0, 1, 0}, {1, 1, 1}};
                color = Color.CYAN;
                break;
            case ("L"):
                map = new int[][]{{0, 0, 1}, {1, 1, 1}};
                newMap = new int[][]{{0, 0, 1}, {1, 1, 1}};
                color = Color.ORANGE;
                break;
            case ("J"):
                map = new int[][]{{1, 0, 0}, {1, 1, 1}};
                newMap = new int[][]{{1, 0, 0}, {1, 1, 1}};
                color = Color.BLUE;
                break;
            case ("S"):
                map = new int[][]{{0, 1, 1}, {1, 1, 0}};
                newMap = new int[][]{{0, 1, 1}, {1, 1, 0}};
                color = Color.PINK;
                break;
            case ("Z"):
                map = new int[][]{{1, 1, 0}, {0, 1, 1}};
                newMap = new int[][]{{1, 1, 0}, {0, 1, 1}};
                color = Color.GREEN;
                break;
            case ("O"):
                map = new int[][]{{1, 1}, {1, 1}};
                newMap = new int[][]{{1, 1}, {1, 1}};
                color = Color.YELLOW;
        }
    }

    public Piece(String type) {
        this.type = type;
        reset();
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

    public String getType() {
        return type;
    }

    public int[][] getNewMap() {
        return newMap;
    }

    public static boolean isPaused() {
        return paused;
    }

    public static void setPaused(boolean paused) {
        Piece.paused = paused;
    }
}


