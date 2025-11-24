import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DesktopBuddy extends JFrame {

    private boolean darkMode = true;
    private JPanel portal;
    private GradientHeader header;
    private BackgroundPanel background;
    private JLayeredPane layeredPane;
    private JPanel mainPanel;

    public DesktopBuddy() {
        super("Desktop Buddy - Atulya Jha 8th B");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 680);
        setLocationRelativeTo(null);
        setResizable(true);

        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(1000, 680));
        setContentPane(layeredPane);

        background = new BackgroundPanel();
        background.setBounds(0, 0, 1000, 680);
        layeredPane.add(background, JLayeredPane.DEFAULT_LAYER);

        mainPanel = new JPanel(new BorderLayout(0, 15));
        mainPanel.setOpaque(false);
        mainPanel.setBounds(0, 0, 1000, 680);
        layeredPane.add(mainPanel, JLayeredPane.PALETTE_LAYER);

        header = new GradientHeader();
        header.setPreferredSize(new Dimension(1000, 110));
        header.setLayout(null);
        mainPanel.add(header, BorderLayout.NORTH);

        JLabel title = new JLabel("Desktop Buddy");
        title.setFont(new Font("Poppins", Font.BOLD, 38));
        title.setForeground(Color.WHITE);
        title.setBounds(40, 25, 600, 60);
        header.add(title);

        JButton themeBtn = new ModernButton("Toggle Theme");
        themeBtn.setBounds(780, 30, 160, 45);
        themeBtn.addActionListener(e -> toggleMode());
        header.add(themeBtn);

        portal = new JPanel(new GridLayout(2, 3, 35, 35));
        portal.setOpaque(false);
        portal.setBorder(BorderFactory.createEmptyBorder(20, 50, 30, 50));
        mainPanel.add(portal, BorderLayout.CENTER);

        portal.add(createTile("To-Do List",    new Color(100, 149, 237), e -> new ToDoListApp()));
        portal.add(createTile("Timer App",     new Color(46, 204, 113), e -> new TimerApp()));
        portal.add(createTile("Calendar",      new Color(155, 89, 182), e -> new CalendarApp()));
        portal.add(createTile("Reward Game",   new Color(231, 76, 60),  e -> new FlappyBirdApp()));
        portal.add(createTile("Calculator",    new Color(241, 196, 15), e -> new CalculatorApp()));
        portal.add(createTile("Browser",       new Color(52, 152, 219), e -> new BrowserApp()));

        Footer footer = new Footer();
        footer.setPreferredSize(new Dimension(1000, 45));
        mainPanel.add(footer, BorderLayout.SOUTH);

        applyTheme();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int w = getWidth();
                int h = getHeight();

                layeredPane.setBounds(0, 0, w, h);
                background.setBounds(0, 0, w, h);
                mainPanel.setBounds(0, 0, w, h);

                layeredPane.revalidate();
                layeredPane.repaint();
            }
        });

        setVisible(true);
    }

    private JButton createTile(String text, Color baseColor, ActionListener action) {
        JButton tile = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(0, 0, 0, 45));
                g2.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 5, 25, 25);

                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 10, getHeight() - 10, 25, 25);

                super.paintComponent(g);
            }
        };

        tile.setFont(new Font("Poppins", Font.BOLD, 22));
        tile.setFocusPainted(false);
        tile.setContentAreaFilled(false);
        tile.setBorderPainted(false);
        tile.setForeground(Color.WHITE);
        tile.setBackground(baseColor);
        tile.putClientProperty("baseColor", baseColor);
        tile.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        tile.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                tile.setBackground(baseColor.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                tile.setBackground(baseColor);
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
        header.setDarkMode(darkMode);
        background.setDarkMode(darkMode);

        for (Component c : portal.getComponents()) {
            if (c instanceof JButton b) {
                Color base = (Color) b.getClientProperty("baseColor");
                b.setBackground(darkMode ? base.darker() : base);
                b.setForeground(darkMode ? Color.WHITE : new Color(60, 60, 60));
            }
        }

        repaint();
    }

    class GradientHeader extends JPanel {
        private boolean dark = true;

        public void setDarkMode(boolean dark) {
            this.dark = dark;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;

            Color start = dark ? new Color(90, 0, 140) : new Color(85, 140, 255);
            Color end   = dark ? new Color(50, 0, 90) : new Color(30, 80, 200);

            g2.setPaint(new GradientPaint(0, 0, start, 0, getHeight(), end));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
        }
    }

    class Footer extends JPanel {
        public Footer() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;

            Color c1 = darkMode ? new Color(30, 30, 40) : new Color(230, 235, 245);
            Color c2 = darkMode ? new Color(45, 45, 65) : new Color(210, 215, 225);

            g2.setPaint(new GradientPaint(0, 0, c1, getWidth(), getHeight(), c2));
            g2.fillRect(0, 0, getWidth(), getHeight());

            g2.setColor(darkMode ? Color.WHITE : Color.DARK_GRAY);
            g2.setFont(new Font("Poppins", Font.BOLD, 14));
            g2.drawString("Created by Atulya Jha • Desktop Buddy", getWidth() / 2 - 140, 25);
        }
    }

    class BackgroundPanel extends JPanel {
        private boolean dark = true;
        private List<Shape> shapes = new ArrayList<>();
        private Random rnd = new Random();

        public void setDarkMode(boolean dark) {
            this.dark = dark;
            generateShapes();
            repaint();
        }

        private void generateShapes() {
            shapes.clear();
            int w = Math.max(getWidth(), 1000);
            int h = Math.max(getHeight(), 680);

            for (int i = 0; i < 6; i++) {
                int x = rnd.nextInt(w);
                int y = rnd.nextInt(h);
                int width = 260 + rnd.nextInt(200);
                int height = 160 + rnd.nextInt(200);
                shapes.add(new RoundRectangle2D.Double(x, y, width, height, 150, 150));
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            Color start = dark ? new Color(15, 15, 35) : new Color(250, 250, 255);
            Color end   = dark ? new Color(55, 0, 75) : new Color(220, 230, 255);

            g2.setPaint(new GradientPaint(0, 0, start, getWidth(), getHeight(), end));
            g2.fillRect(0, 0, getWidth(), getHeight());

            Color shapeColor = dark
                    ? new Color(255, 255, 255, 18)
                    : new Color(70, 130, 255, 55);

            g2.setColor(shapeColor);

            for (Shape s : shapes) g2.fill(s);
        }
    }

    class ModernButton extends JButton {
        public ModernButton(String text) {
            super(text);
            setFont(new Font("Poppins", Font.BOLD, 16));
            setForeground(Color.WHITE);
            setBackground(new Color(30, 30, 30, 80));
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
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DesktopBuddy::new);
    }
}
