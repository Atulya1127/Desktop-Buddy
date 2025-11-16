
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class TimerApp extends JFrame {

    private JLabel timerLabel = new JLabel("25:00", SwingConstants.CENTER);
    private JLabel quoteLabel = new JLabel("", SwingConstants.CENTER);
    private JLabel statsLabel = new JLabel("Cycles Completed : 0", SwingConstants.CENTER);

    private javax.swing.Timer timer;
    private int timeLeft = 25 * 60;
    private boolean isRunning = false;
    private boolean darkMode = true;
    private int sessionsCompleted = 0;

    private CircularTimer timerCircle = new CircularTimer();

    private Color accent = new Color(100, 149, 237);
    private Color darkBg = new Color(40, 44, 52);
    private Color lightBg = new Color(245, 247, 250);

    private final String[] quotes = {
        "Focus on progress, not perfection.",
        "Small steps lead to big results.",
        "Stay consistent. The results will come.",
        "Discipline beats motivation.",
        "Make today count!"
    };

    public TimerApp() {
        super("Timer App");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(520, 680);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        GradientPanel header = new GradientPanel(accent, new Color(72, 118, 255));
        header.setLayout(new BorderLayout());
        header.setPreferredSize(new Dimension(520, 60));

        JLabel title = new JLabel("TimerApp Take Frequent Brakes!");
        title.setFont(new Font("Segoe UI Semibold", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 0));

        JButton themeToggle = makeButton("Change Mode");
        themeToggle.setForeground(Color.WHITE);
        themeToggle.setOpaque(false);
        themeToggle.addActionListener(e -> toggleTheme());

        header.add(title, BorderLayout.WEST);
        header.add(themeToggle, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        RoundedPanel timerPanel = new RoundedPanel();
        timerPanel.setLayout(new BorderLayout(10, 10));

        timerLabel.setFont(new Font("Consolas", Font.BOLD, 42));
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel topInfo = new JPanel(new GridLayout(2, 1));
        topInfo.setOpaque(false);
        quoteLabel.setFont(new Font("Segoe UI Italic", Font.ITALIC, 22));
        statsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        topInfo.add(quoteLabel);
        topInfo.add(statsLabel);

        timerPanel.add(topInfo, BorderLayout.NORTH);
        timerPanel.add(timerCircle, BorderLayout.CENTER);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        controls.setOpaque(false);

        JButton start = makeButton("Start");
        JButton pause = makeButton("Pause");
        JButton reset = makeButton("Reset");

        start.addActionListener(e -> startTimer());
        pause.addActionListener(e -> pauseTimer());
        reset.addActionListener(e -> resetTimer());

        controls.add(start);
        controls.add(pause);
        controls.add(reset);

        timerPanel.add(controls, BorderLayout.SOUTH);

        add(timerPanel, BorderLayout.CENTER);

        randomQuote();
        applyTheme();
        setVisible(true);
    }

    private void startTimer() {
        if (isRunning) {
            return;
        }

        isRunning = true;
        timer = new javax.swing.Timer(1000, e -> {
            if (timeLeft > 0) {
                timeLeft--;
                updateTimerLabel();
            } else {
                timer.stop();
                isRunning = false;
                sessionsCompleted++;
                statsLabel.setText("Cycles Completed : " + sessionsCompleted);
                JOptionPane.showMessageDialog(this, "Time is up! Great job!");
                resetTimer();
                randomQuote();
            }
        });

        timer.start();
    }

    private void pauseTimer() {
        if (timer != null) {
            timer.stop();
        }
        isRunning = false;
    }

    private void resetTimer() {
        if (timer != null) {
            timer.stop();
        }
        isRunning = false;
        timeLeft = 25 * 60;
        updateTimerLabel();
        timerCircle.setProgress(0);
        timerCircle.repaint();
    }

    private void updateTimerLabel() {
        int minutes = timeLeft / 60;
        int seconds = timeLeft % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));

        timerCircle.setProgress(1.0 - (timeLeft / (25.0 * 60)));
        timerCircle.repaint();
    }

    private void randomQuote() {
        quoteLabel.setText("“" + quotes[new Random().nextInt(quotes.length)] + "”");
    }

    private void toggleTheme() {
        darkMode = !darkMode;
        applyTheme();
    }

    private void applyTheme() {
        Color bg = darkMode ? darkBg : lightBg;
        Color fg = darkMode ? Color.WHITE : Color.BLACK;

        getContentPane().setBackground(bg);

        timerLabel.setForeground(fg);
        quoteLabel.setForeground(fg);
        statsLabel.setForeground(fg);

        repaint();
    }

    class RoundedPanel extends JPanel {

        RoundedPanel() {
            setOpaque(false);
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight(), arc = 25;

            g2.setColor(new Color(0, 0, 0, 25));
            g2.fillRoundRect(4, 4, w - 8, h - 8, arc, arc);

            g2.setColor(darkMode ? new Color(50, 54, 61) : Color.WHITE);
            g2.fillRoundRect(0, 0, w - 8, h - 8, arc, arc);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    class GradientPanel extends JPanel {

        private Color start, end;

        GradientPanel(Color s, Color e) {
            start = s;
            end = e;
            setOpaque(false);
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setPaint(new GradientPaint(0, 0, start, getWidth(), getHeight(), end));
            g2.fillRect(0, 0, getWidth(), getHeight());
            super.paintComponent(g);
        }
    }

    class CircularTimer extends JPanel {

        private double progress = 0.0;

        CircularTimer() {
            setPreferredSize(new Dimension(250, 250));
            setOpaque(false);
        }

        void setProgress(double p) {
            progress = Math.max(0, Math.min(1, p));
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int size = Math.min(getWidth(), getHeight());
            int stroke = 12;

            g2.setStroke(new BasicStroke(stroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            g2.setColor(new Color(180, 180, 180, 70));
            g2.drawArc(stroke, stroke, size - 2 * stroke, size - 2 * stroke, 0, 360);

            g2.setColor(accent);
            g2.drawArc(stroke, stroke, size - 2 * stroke, size - 2 * stroke,
                    90, (int) -(progress * 360));

            g2.setFont(new Font("Consolas", Font.BOLD, 36));
            g2.setColor(darkMode ? Color.WHITE : Color.BLACK);

            String text = timerLabel.getText();
            FontMetrics fm = g2.getFontMetrics();

            int x = (size - fm.stringWidth(text)) / 2;
            int y = (size + fm.getAscent()) / 2 - 5;

            g2.drawString(text, x, y);
        }
    }

    private JButton makeButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        btn.setBackground(new Color(230, 235, 250));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(200, 220, 255));
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(230, 235, 250));
            }
        });

        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TimerApp::new);
    }
}
