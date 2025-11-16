
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class DesktopBuddy extends JFrame {

    private boolean darkMode = true;
    private JPanel grid;
    private GradientHeader header;

    public DesktopBuddy() {
        super("All-in-One Launcher");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setUndecorated(false);

        header = new GradientHeader();
        header.setPreferredSize(new Dimension(900, 100));
        header.setLayout(new BorderLayout());

        JLabel title = new JLabel("Desktop Buddy", SwingConstants.LEFT);
        title.setFont(new Font("Segoe UI Semibold", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 0));

        JButton themeBtn = new JButton("Toggle Mode");
        themeBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        themeBtn.setForeground(Color.WHITE);
        themeBtn.setOpaque(false);
        themeBtn.setContentAreaFilled(false);
        themeBtn.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        themeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        themeBtn.addActionListener(e -> toggleMode());

        header.add(title, BorderLayout.WEST);
        header.add(themeBtn, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        grid = new JPanel(new GridLayout(2, 3, 30, 30));
        grid.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        grid.add(createTile("To-Do List", new Color(100, 149, 237), e -> new ToDoListApp()));
        grid.add(createTile("Timer App", new Color(46, 204, 113), e -> new TimerApp()));
        grid.add(createTile("Calendar", new Color(155, 89, 182), e -> new CalendarApp()));
        grid.add(createTile("Flappy Bird", new Color(231, 76, 60), e -> new FlappyBirdGame()));
        grid.add(createTile("Calculator", new Color(241, 196, 15), e -> new CalculatorApp()));
        grid.add(createTile("Browser", new Color(52, 152, 219), e -> new BrowserApp()));

        add(grid, BorderLayout.CENTER);

        applyTheme();
        setVisible(true);
    }

    private JButton createTile(String text, Color baseColor, ActionListener action) {
        JButton tile = new JButton(text);
        tile.setFont(new Font("Segoe UI", Font.BOLD, 24));
        tile.setFocusPainted(false);
        tile.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        tile.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        tile.putClientProperty("baseColor", baseColor);
        tile.setBackground(baseColor);
        tile.setForeground(Color.WHITE);
        tile.setOpaque(true);
        tile.setUI(new RoundedButtonUI());

        tile.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                tile.setBackground(baseColor.darker());
                tile.setFont(tile.getFont().deriveFont(26f));
                tile.setLocation(tile.getX(), tile.getY() - 2);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                tile.setBackground(baseColor);
                tile.setFont(tile.getFont().deriveFont(24f));
                tile.setLocation(tile.getX(), tile.getY() + 2);
            }
        });

        tile.addActionListener(action);
        return tile;
    }

    private void toggleMode() {
        darkMode = !darkMode;
        applyTheme();
    }

    private void applyTheme() {
        Color bg = darkMode ? new Color(28, 30, 38) : new Color(245, 247, 250);
        getContentPane().setBackground(bg);
        grid.setBackground(bg);
        header.setDarkMode(darkMode);

        for (Component c : grid.getComponents()) {
            if (c instanceof JButton tile) {
                Color base = (Color) tile.getClientProperty("baseColor");
                Color newColor = darkMode ? base.darker().darker() : base;
                tile.setBackground(newColor);
            }
        }

        repaint();
        revalidate();
    }

    class GradientHeader extends JPanel {

        private boolean isDark = true;

        GradientHeader() {
            setOpaque(false);
        }

        void setDarkMode(boolean dark) {
            isDark = dark;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            Color start = isDark ? new Color(45, 45, 60) : new Color(72, 118, 255);
            Color end = isDark ? new Color(20, 20, 30) : new Color(30, 60, 160);
            g2.setPaint(new GradientPaint(0, 0, start, getWidth(), getHeight(), end));
            g2.fillRect(0, 0, getWidth(), getHeight());
            super.paintComponent(g);
        }
    }

    class RoundedButtonUI extends javax.swing.plaf.basic.BasicButtonUI {

        @Override
        public void paint(Graphics g, JComponent c) {
            AbstractButton b = (AbstractButton) c;
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(0, 0, 0, 60));
            g2.fillRoundRect(4, 4, b.getWidth() - 8, b.getHeight() - 8, 20, 20);

            g2.setColor(b.getBackground());
            g2.fillRoundRect(0, 0, b.getWidth() - 8, b.getHeight() - 8, 20, 20);

            super.paint(g2, c);
            g2.dispose();
        }

        @Override
        public Dimension getPreferredSize(JComponent c) {
            Dimension d = super.getPreferredSize(c);
            d.width = Math.max(d.width, 150);
            d.height = Math.max(d.height, 100);
            return d;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DesktopBuddy::new);
    }
}
