package Snake;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

    private final int B_WIDTH = 300;
    private final int B_HEIGHT = 300;
    private final int DOT_SIZE = 10;
    private final int ALL_DOTS = 900;
    private final int RAND_POS = 29;
    private final int DELAY = 140;

    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    private int dots;
    private int apple_x;
    private int apple_y;

    private boolean ignoreNextMove;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    private Timer timer;
    private Image ball;
    private Image apple;
    private Image head1;
    private Image head2;
    private Image head3;
    private Image head4;

    public Board() {

        initBoard();
    }

    private void initBoard() {

        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        initGame();
    }

    private void loadImages() {

        ImageIcon iie = new ImageIcon("src/images/snake/dot.png");
        ball = iie.getImage();

        ImageIcon iih = new ImageIcon("src/images/snake/apple.png");
        apple = iih.getImage();

        ImageIcon iia = new ImageIcon("src/images/snake/head1.png");
        head1 = iia.getImage();

        ImageIcon iib = new ImageIcon("src/images/snake/head2.png");
        head2 = iib.getImage();

        ImageIcon iic = new ImageIcon("src/images/snake/head3.png");
        head3 = iic.getImage();

        ImageIcon iid = new ImageIcon("src/images/snake/head4.png");
        head4 = iid.getImage();
    }

    private void initGame() {

        dots = 3;

        for (int z = 0; z < dots; z++) {
            x[z] = 120 - z * 10;
            y[z] = 150;
        }

        apple_x = (200);
        apple_y = (150);

        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g) {

        if (inGame) {

            g.drawImage(apple, apple_x, apple_y, this);


            for (int z = 0; z < dots; z++) {
                if (z != 0) {
                    g.drawImage(ball, x[z], y[z], this);
                }
            }
            if(upDirection){
                g.drawImage(head1, x[0]-3, y[0]-3, this);
            }else if(downDirection){
                g.drawImage(head2, x[0]-3, y[0]-185, this);
            }else if(rightDirection){
                g.drawImage(head3, x[0]-185, y[0]-3, this);
            }else if(leftDirection){
                g.drawImage(head4, x[0], y[0]-235, this);
            }
            Toolkit.getDefaultToolkit().sync();

        } else {

            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {

        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
    }

    private void checkApple() {

        if ((x[0] == apple_x) && (y[0] == apple_y)) {

            dots++;
            locateApple();
        }
    }

    private void move(boolean ignoreTheNextMove) {
        if (!ignoreNextMove){
            for (int z = dots; z > 0; z--) {
                x[z] = x[(z - 1)];
                y[z] = y[(z - 1)];
            }

            if (leftDirection) {
                x[0] -= DOT_SIZE;
            }

            if (rightDirection) {
                x[0] += DOT_SIZE;
            }

            if (upDirection) {
                y[0] -= DOT_SIZE;
            }

            if (downDirection) {
                y[0] += DOT_SIZE;
            }
            if (ignoreTheNextMove){
                ignoreNextMove = true;
            }
        } else{
            ignoreNextMove = false;
        }
    }

    private void checkCollision() {
        for (int z = dots; z > 0; z--) {

            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }

        if (y[0] >= B_HEIGHT) {
            inGame = false;
        }

        if (y[0] < 0) {
            inGame = false;
        }

        if (x[0] >= B_WIDTH) {
            inGame = false;
        }

        if (x[0] < 0) {
            inGame = false;
        }

        if (!inGame) {
            timer.stop();
        }
    }

    private void locateApple() {
        int r = (int) (Math.random() * RAND_POS);
        int d = (int) (Math.random() * RAND_POS);
        boolean inTheSnake = false;

        for (int z = 0; z < dots; z++) {
            if (r == x[z]) {
                inTheSnake = true;
                break;
            }else if (d == y[z]){
                inTheSnake = true;
                break;
            }
        }
        if(inTheSnake){
            locateApple();
        }
        apple_x = ((r * DOT_SIZE));
        apple_y = ((d * DOT_SIZE));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {
            move(false);
            checkApple();
            checkCollision();
        }

        repaint();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!rightDirection) && (!leftDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
                move(true);
            }

            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection) && (!rightDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
                move(true);
            }

            if ((key == KeyEvent.VK_UP) && (!downDirection) && (!upDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
                move(true);
            }

            if ((key == KeyEvent.VK_DOWN) && (!upDirection) && (!downDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
                move(true);
            }
            if ((key == KeyEvent.VK_R) && !inGame){
                inGame= true;
                rightDirection = true;
                upDirection = false;
                downDirection = false;
                leftDirection = false;
                initGame();
            }
        }
    }
}