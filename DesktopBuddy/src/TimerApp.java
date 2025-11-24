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
        setSize(520, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        GradientHeader header = new GradientHeader();
        header.setLayout(new BorderLayout());
        header.setPreferredSize(new Dimension(520, 60));

        JLabel title = new JLabel("Timer App • Stay Focused");
        title.setFont(new Font("Poppins", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 0));

        ModernButton themeToggle = new ModernButton("Toggle Theme");
        themeToggle.addActionListener(e -> toggleTheme());

        header.add(title, BorderLayout.WEST);
        header.add(themeToggle, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        RoundedPanel timerPanel = new RoundedPanel();
        timerPanel.setLayout(new BorderLayout(20, 20));
        timerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        timerLabel.setFont(new Font("Consolas", Font.BOLD, 42));

        JPanel topInfo = new JPanel(new GridLayout(2, 1));
        topInfo.setOpaque(false);
        quoteLabel.setFont(new Font("Poppins", Font.ITALIC, 20));
        statsLabel.setFont(new Font("Poppins", Font.PLAIN, 20));

        topInfo.add(quoteLabel);
        topInfo.add(statsLabel);

        timerPanel.add(topInfo, BorderLayout.NORTH);
        timerPanel.add(timerCircle, BorderLayout.CENTER);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        controls.setOpaque(false);

        ModernButton start = new ModernButton("Start");
        ModernButton pause = new ModernButton("Pause");
        ModernButton reset = new ModernButton("Reset");

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
        if (isRunning) return;

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
        if (timer != null) timer.stop();
        isRunning = false;
    }

    private void resetTimer() {
        if (timer != null) timer.stop();
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
        Color bg = darkMode ? new Color(35, 35, 50) : new Color(245, 247, 250);
        Color fg = darkMode ? Color.WHITE : Color.BLACK;

        getContentPane().setBackground(bg);

        timerLabel.setForeground(fg);
        quoteLabel.setForeground(fg);
        statsLabel.setForeground(fg);

        timerCircle.repaint();
        repaint();
    }

    class RoundedPanel extends JPanel {
        RoundedPanel() { setOpaque(false); }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight(), arc = 25;
            g2.setColor(new Color(0, 0, 0, 25));
            g2.fillRoundRect(6, 6, w - 12, h - 12, arc, arc);
            g2.setColor(darkMode ? new Color(50, 54, 61) : Color.WHITE);
            g2.fillRoundRect(0, 0, w - 12, h - 12, arc, arc);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    class GradientHeader extends JPanel {
        GradientHeader() { setOpaque(false); }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color start = darkMode ? new Color(100, 149, 237) : new Color(85, 140, 255);
            Color end = darkMode ? new Color(72, 118, 255) : new Color(30, 80, 200);
            g2.setPaint(new GradientPaint(0, 0, start, 0, getHeight(), end));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
        }
    }

    class ModernButton extends JButton {
        ModernButton(String text) {
            super(text);
            setFont(new Font("Poppins", Font.BOLD, 14));
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(new Color(255, 255, 255, 50));
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(new Color(30, 30, 30, 80));
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(70, 130, 255, 180));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            super.paintComponent(g);
        }
    }

    class CircularTimer extends JPanel {
        private double progress = 0.0;
        CircularTimer() { setPreferredSize(new Dimension(270, 270)); setOpaque(false); }
        void setProgress(double p) { progress = Math.max(0, Math.min(1, p)); }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int size = Math.min(getWidth(), getHeight());
            int stroke = 14;

            g2.setStroke(new BasicStroke(stroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setColor(new Color(180, 180, 180, 70));
            g2.drawArc(stroke, stroke, size - 2 * stroke, size - 2 * stroke, 0, 360);

            g2.setColor(new Color(100, 149, 237));
            g2.drawArc(stroke, stroke, size - 2 * stroke, size - 2 * stroke, 90, (int) -(progress * 360));

            g2.setFont(new Font("Consolas", Font.BOLD, 38));
            g2.setColor(darkMode ? Color.WHITE : Color.BLACK);

            String text = timerLabel.getText();
            FontMetrics fm = g2.getFontMetrics();
            int x = (size - fm.stringWidth(text)) / 2;
            int y = (size + fm.getAscent()) / 2 - 4;
            g2.drawString(text, x, y);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TimerApp::new);
    }
}

