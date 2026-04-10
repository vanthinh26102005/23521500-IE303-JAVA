import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    // ===== BAI 1: Kich thuoc cua so, tieu de, background =====
    private static final int BOARD_WIDTH = 360;
    private static final int BOARD_HEIGHT = 640;

    private static final int BIRD_WIDTH = 34;
    private static final int BIRD_HEIGHT = 24;
    private static final int BIRD_START_X = BOARD_WIDTH / 8;
    private static final int BIRD_START_Y = BOARD_HEIGHT / 2;

    private static final int PIPE_WIDTH = 64;
    private static final int PIPE_HEIGHT = 512;
    private static final int PIPE_GAP = 150;
    private static final int PIPE_SPEED = 4;

    private static final int GRAVITY = 1;
    private static final int JUMP_VELOCITY = -9;

    private final Image backgroundImage;
    private final Image birdImage;
    private final Image topPipeImage;
    private final Image bottomPipeImage;

    private final Bird bird;
    private final List<Pipe> pipes;
    private final Random random;
    private final Timer gameLoop;
    private final Timer pipeSpawner;

    private int velocityY;
    private int score;
    private int highScore;
    private boolean gameOver;

    public FlappyBird() {
        // BAI 1: Thiet lap khung ve co dinh 360x640
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        setFocusable(true);
        addKeyListener(this);

        // BAI 1: Tai anh nen va tai nguyen game
        backgroundImage = loadImage("flappybirdbg.png");
        birdImage = loadImage("flappybird.png");
        topPipeImage = loadImage("toppipe.png");
        bottomPipeImage = loadImage("bottompipe.png");

        // BAI 2: Khoi tao doi tuong chim
        bird = new Bird(BIRD_START_X, BIRD_START_Y, BIRD_WIDTH, BIRD_HEIGHT, birdImage);
        pipes = new ArrayList<>();
        random = new Random();

        velocityY = 0;
        score = 0;
        highScore = 0;
        gameOver = false;

        // BAI 3: Game loop (60 FPS)
        gameLoop = new Timer(1000 / 60, this);
        gameLoop.start();

        // BAI 3: Bo sinh ong theo chu ky
        pipeSpawner = new Timer(1500, e -> {
            if (!gameOver) {
                placePipes();
            }
        });
        pipeSpawner.start();
    }

    private Image loadImage(String fileName) {
        String[] candidates = {
            fileName,
            "BTTH2/" + fileName,
            "./BTTH2/" + fileName
        };

        for (String path : candidates) {
            File file = new File(path);
            if (file.exists()) {
                return new ImageIcon(path).getImage();
            }
        }

        java.net.URL resource = FlappyBird.class.getResource("/" + fileName);
        if (resource != null) {
            return new ImageIcon(resource).getImage();
        }

        return null;
    }

    private void placePipes() {
        // BAI 3: Tao cap ong tren/duoi voi khoang trong ngau nhien
        int openingY = 120 + random.nextInt(BOARD_HEIGHT - 260);

        Pipe topPipe = new Pipe(
                BOARD_WIDTH,
                openingY - PIPE_HEIGHT,
                PIPE_WIDTH,
                PIPE_HEIGHT,
                topPipeImage,
                true
        );

        Pipe bottomPipe = new Pipe(
                BOARD_WIDTH,
                openingY + PIPE_GAP,
                PIPE_WIDTH,
                PIPE_HEIGHT,
                bottomPipeImage,
                false
        );

        pipes.add(topPipe);
        pipes.add(bottomPipe);
    }

    private void move() {
        // BAI 2: Hieu ung roi xuong theo trong luc
        velocityY += GRAVITY;
        bird.y += velocityY;

        if (bird.y < 0) {
            bird.y = 0;
        }

        Iterator<Pipe> iterator = pipes.iterator();
        while (iterator.hasNext()) {
            Pipe pipe = iterator.next();
            pipe.x -= PIPE_SPEED;

            // BAI 4: Co che tinh diem khi chim vuot qua ong tren
            if (pipe.isTop && !pipe.passed && bird.x > pipe.x + pipe.width) {
                pipe.passed = true;
                score++;
                if (score > highScore) {
                    highScore = score;
                }
            }

            if (collision(bird, pipe)) {
                // BAI 4: Va cham => game over
                gameOver = true;
            }

            if (pipe.x + pipe.width < 0) {
                iterator.remove();
            }
        }

        if (bird.y + bird.height >= BOARD_HEIGHT) {
            bird.y = BOARD_HEIGHT - bird.height;
            // BAI 4: Cham dat => game over
            gameOver = true;
        }
    }

    private boolean collision(Bird b, Pipe p) {
        Rectangle birdRect = new Rectangle(b.x, b.y, b.width, b.height);
        Rectangle pipeRect = new Rectangle(p.x, p.y, p.width, p.height);
        return birdRect.intersects(pipeRect);
    }

    private void restartGame() {
        // BAI 4: Restart game ve trang thai ban dau
        bird.x = BIRD_START_X;
        bird.y = BIRD_START_Y;
        velocityY = 0;
        score = 0;
        gameOver = false;
        pipes.clear();

        if (!pipeSpawner.isRunning()) {
            pipeSpawner.start();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // BAI 1: Ve hinh nen cua cua so
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, BOARD_WIDTH, BOARD_HEIGHT, null);
        } else {
            g.setColor(new Color(140, 200, 255));
            g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
        }

        for (Pipe pipe : pipes) {
            // BAI 3: Ve cac ong
            if (pipe.image != null) {
                g.drawImage(pipe.image, pipe.x, pipe.y, pipe.width, pipe.height, null);
            } else {
                g.setColor(new Color(36, 173, 57));
                g.fillRect(pipe.x, pipe.y, pipe.width, pipe.height);
            }
        }

        // BAI 2: Ve chim
        if (bird.image != null) {
            g.drawImage(bird.image, bird.x, bird.y, bird.width, bird.height, null);
        } else {
            g.setColor(Color.YELLOW);
            g.fillOval(bird.x, bird.y, bird.width, bird.height);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 26));
        // BAI 4: Hien thi diem
        g.drawString("Score: " + score, 16, 38);

        g.setFont(new Font("SansSerif", Font.PLAIN, 16));
        g.drawString("Best: " + highScore, 16, 62);

        if (gameOver) {
            // BAI 4: Man hinh game over
            g.setColor(new Color(0, 0, 0, 140));
            g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);

            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.BOLD, 34));
            g.drawString("GAME OVER", 74, 280);

            g.setFont(new Font("SansSerif", Font.PLAIN, 18));
            g.drawString("Nhan ENTER/SPACE hoac R de choi lai", 30, 320);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // BAI 3: Cap nhat game theo vong lap
        if (!gameOver) {
            move();
        } else {
            pipeSpawner.stop();
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        boolean jumpKey = keyCode == KeyEvent.VK_SPACE || keyCode == KeyEvent.VK_ENTER;

        if (jumpKey) {
            if (gameOver) {
                // BAI 4: ENTER/SPACE de choi lai
                restartGame();
            } else {
                // BAI 2: Hieu ung bay len khi nhan SPACE/ENTER
                velocityY = JUMP_VELOCITY;
            }
        }

        if (gameOver && keyCode == KeyEvent.VK_R) {
            // BAI 4: Phim R de restart
            restartGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private static class Bird {
        int x;
        int y;
        int width;
        int height;
        Image image;

        Bird(int x, int y, int width, int height, Image image) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.image = image;
        }
    }

    private static class Pipe {
        int x;
        int y;
        int width;
        int height;
        Image image;
        boolean isTop;
        boolean passed;

        Pipe(int x, int y, int width, int height, Image image, boolean isTop) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.image = image;
            this.isTop = isTop;
            this.passed = false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // BAI 1: Tao cua so Flappy Bird, khong cho resize
            JFrame frame = new JFrame("Flappy Bird");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);

            FlappyBird gamePanel = new FlappyBird();
            frame.add(gamePanel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            gamePanel.requestFocusInWindow();
        });
    }
}
