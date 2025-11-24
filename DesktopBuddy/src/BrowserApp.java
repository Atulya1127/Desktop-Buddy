import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;

public class BrowserApp extends JFrame {

    private boolean darkMode = true;
    private final Color accentStart = new Color(100, 149, 237);
    private final Color accentEnd = new Color(72, 118, 255);
    private final Color darkBg = new Color(40, 44, 52);
    private final Color lightBg = new Color(245, 247, 250);

    private JTextField urlField;

    public BrowserApp() {
        super("Desktop Buddy - Browser");

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(540, 220);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(12, 12));

        GradientHeader header = new GradientHeader(accentStart, accentEnd);
        header.setLayout(new BorderLayout());
        header.setPreferredSize(new Dimension(540, 65));

        JLabel title = new JLabel("Browser Launcher");
        title.setFont(new Font("Poppins", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 0));

        ModernButton themeToggle = new ModernButton("Toggle Theme");
        themeToggle.addActionListener(e -> toggleTheme());

        header.add(title, BorderLayout.WEST);
        header.add(themeToggle, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);
        RoundedPanel mainPanel = new RoundedPanel();
        mainPanel.setLayout(new BorderLayout(15, 15));

        urlField = new JTextField("https://");
        urlField.setFont(new Font("Poppins", Font.PLAIN, 20));
        urlField.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));

        ModernButton openBtn = new ModernButton("Open");
        openBtn.setFont(new Font("Poppins", Font.BOLD, 18));
        openBtn.addActionListener(e -> openBrowser());

        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        inputPanel.setOpaque(false);
        inputPanel.add(urlField, BorderLayout.CENTER);
        inputPanel.add(openBtn, BorderLayout.EAST);

        mainPanel.add(inputPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        applyTheme();
        setVisible(true);
    }

    private void openBrowser() {
        try {
            String url = urlField.getText().trim();
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "https://" + url;
            }
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid URL!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void toggleTheme() {
        darkMode = !darkMode;
        applyTheme();
    }

    private void applyTheme() {
        Color bg = darkMode ? darkBg : lightBg;
        Color fg = darkMode ? Color.WHITE : Color.BLACK;
        getContentPane().setBackground(bg);
        urlField.setBackground(darkMode ? new Color(55, 58, 64) : Color.WHITE);
        urlField.setForeground(fg);
        urlField.setCaretColor(fg);
        repaint();
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

    class RoundedPanel extends JPanel {
        RoundedPanel() { setOpaque(false); }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(darkMode ? new Color(45, 48, 56) : Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    class GradientHeader extends JPanel {
        private final Color start, end;
        GradientHeader(Color s, Color e) { start = s; end = e; setOpaque(false); }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setPaint(new GradientPaint(0, 0, start, getWidth(), getHeight(), end));
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BrowserApp::new);
    }
}
