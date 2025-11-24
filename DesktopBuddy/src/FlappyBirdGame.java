import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class FlappyBirdApp extends JFrame {

    private boolean darkMode = true;

    public FlappyBirdApp() {
        super("Happy Ball");

        setSize(420, 720);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        GradientHeader header = new GradientHeader();
        header.setPreferredSize(new Dimension(420, 60));
        header.setLayout(new BorderLayout());

        JLabel title = new JLabel("Happy Ball");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Poppins", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 0));

        ModernButton themeToggle = new ModernButton("Toggle Theme");
        themeToggle.addActionListener(e -> toggleTheme());

        header.add(title, BorderLayout.WEST);
        header.add(themeToggle, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        GamePanel gamePanel = new GamePanel();
        add(gamePanel, BorderLayout.CENTER);

        applyTheme();
        setVisible(true);
        gamePanel.requestFocusInWindow();
    }

    private void toggleTheme() {
        darkMode = !darkMode;
        applyTheme();
        repaint();
    }

    private void applyTheme() {
        getContentPane().setBackground(darkMode ? new Color(40, 44, 52) : new Color(245, 247, 250));
    }

    class GradientHeader extends JPanel {
        GradientHeader() { setOpaque(false); }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color start = darkMode ? new Color(100, 149, 237) : new Color(85, 140, 255);
            Color end = darkMode ? new Color(72, 118, 255) : new Color(30, 80, 200);
            g2.setPaint(new GradientPaint(0, 0, start, getWidth(), getHeight(), end));
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    class ModernButton extends JButton {
        ModernButton(String text) {
            super(text);
            setFont(new Font("Poppins", Font.BOLD, 16));
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { setForeground(new Color(200, 220, 255)); }
                public void mouseExited(MouseEvent e) { setForeground(Color.WHITE); }
            });
        }
    }

    class GamePanel extends JPanel implements ActionListener, KeyListener {

        private final int WIDTH = 400, HEIGHT = 600, GROUND = 100, BALL_SIZE = 22, PIPE_WIDTH = 70, GAP_HEIGHT = 160;
        private final double GRAVITY = 0.5, JUMP = -8;

        private int ballX = 100, ballY = HEIGHT / 2;
        private double velocity = 0;

        private class Pipe { int x, gapY; boolean passed = false; Pipe(int x, int gapY){this.x=x;this.gapY=gapY;} }

        private final ArrayList<Pipe> pipes = new ArrayList<>();
        private final Random rand = new Random();
        private final ArrayList<Rectangle> clouds = new ArrayList<>();

        private boolean started = false, gameOver = false;
        private int score = 0;

        private final Timer timer = new Timer(1000/60,this);

        GamePanel() {
            setFocusable(true);
            addKeyListener(this);
            for(int i=0;i<5;i++) clouds.add(new Rectangle(rand.nextInt(WIDTH),rand.nextInt(200),60+rand.nextInt(40),30+rand.nextInt(15)));
            resetGame();
            timer.start();
        }

        private void addPipe(int x) { pipes.add(new Pipe(x, 100 + rand.nextInt(HEIGHT - GROUND - 200))); }
        private void resetGame() {
            ballY = HEIGHT/2; velocity=0; score=0; pipes.clear();
            for(int i=0;i<4;i++) addPipe(WIDTH+100+i*200);
            started=true; gameOver=false; timer.start();
        }
        private void jump(){ if(gameOver) resetGame(); velocity=JUMP; }

        public void actionPerformed(ActionEvent e){
            clouds.forEach(c->{c.x--; if(c.x+c.width<0)c.x=WIDTH+rand.nextInt(100);});
            if(started){
                pipes.forEach(p->p.x-=3);
                if(pipes.isEmpty() || pipes.get(pipes.size()-1).x+PIPE_WIDTH<WIDTH-160) addPipe(pipes.isEmpty()?WIDTH:pipes.get(pipes.size()-1).x+200);
                pipes.removeIf(p->p.x+PIPE_WIDTH<0);
                velocity+=GRAVITY; ballY+=(int)velocity;
                Rectangle ballRect=new Rectangle(ballX,ballY,BALL_SIZE,BALL_SIZE);
                for(Pipe p:pipes){
                    Rectangle top=new Rectangle(p.x,0,PIPE_WIDTH,p.gapY);
                    Rectangle bottom=new Rectangle(p.x,p.gapY+GAP_HEIGHT,PIPE_WIDTH,HEIGHT-p.gapY-GAP_HEIGHT-GROUND);
                    if(ballRect.intersects(top)||ballRect.intersects(bottom)) gameOver=true;
                    if(!p.passed && p.x+PIPE_WIDTH<ballX){p.passed=true;score++;}
                }
                if(ballY+BALL_SIZE>HEIGHT-GROUND || ballY<0) gameOver=true;
                if(gameOver) timer.stop();
            }
            repaint();
        }

        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            Graphics2D g2=(Graphics2D) g;

            g2.setPaint(new GradientPaint(0,0,new Color(135,206,250),0,HEIGHT,new Color(176,224,230)));
            g2.fillRect(0,0,WIDTH,HEIGHT);

            g2.setColor(new Color(255,255,255,200));
            clouds.forEach(c->g2.fillRoundRect(c.x,c.y,c.width,c.height,20,20));

            g2.setColor(new Color(222,184,135));
            g2.fillRect(0,HEIGHT-GROUND,WIDTH,GROUND);

            for(Pipe p:pipes){
                g2.setPaint(new GradientPaint(p.x,0,new Color(0,200,0),p.x+PIPE_WIDTH,0,new Color(0,120,0)));
                g2.fillRect(p.x,0,PIPE_WIDTH,p.gapY);
                g2.fillRect(p.x,p.gapY+GAP_HEIGHT,PIPE_WIDTH,HEIGHT-p.gapY-GAP_HEIGHT-GROUND);
            }

            g2.setColor(new Color(0,0,0,50)); g2.fillOval(ballX+3,ballY+3,BALL_SIZE,BALL_SIZE);
            g2.setColor(new Color(255,70,70)); g2.fillOval(ballX,ballY,BALL_SIZE,BALL_SIZE);
            g2.setColor(Color.WHITE); g2.fillOval(ballX+5,ballY+5,6,6);
            g2.setColor(Color.BLACK); g2.drawOval(ballX,ballY,BALL_SIZE,BALL_SIZE);

            g2.setFont(new Font("Poppins",Font.BOLD,26));
            g2.setColor(Color.BLACK);
            g2.drawString("Score: "+score,140,40);

            if(!started){ g2.setFont(new Font("Poppins",Font.BOLD,22)); g2.drawString("Press SPACE to start",85,HEIGHT/2);}
            if(gameOver){ g2.setFont(new Font("Poppins",Font.BOLD,34)); g2.setColor(Color.BLACK); g2.drawString("Game Over!",110,HEIGHT/2-20);
                          g2.setFont(new Font("Poppins",Font.PLAIN,20)); g2.drawString("Press SPACE to restart",90,HEIGHT/2+20);}
        }

        public void keyPressed(KeyEvent e){ if(e.getKeyCode()==KeyEvent.VK_SPACE) jump();}
        public void keyReleased(KeyEvent e){}
        public void keyTyped(KeyEvent e){}
    }

    public static void main(String[] args){ SwingUtilities.invokeLater(FlappyBirdApp::new);}
}

