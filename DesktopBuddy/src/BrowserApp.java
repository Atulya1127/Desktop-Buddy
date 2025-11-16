
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;

public class BrowserApp extends JFrame {

    private boolean darkMode = true;

    private Color accent = new Color(100, 149, 237);
    private Color darkBg = new Color(40, 44, 52);
    private Color lightBg = new Color(245, 247, 250);

    private JTextField urlField;

    public BrowserApp() {
        super("Web Browser Launcher");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(520, 200);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        GradientPanel header = new GradientPanel(accent, new Color(72, 118, 255));
        header.setLayout(new BorderLayout());
        header.setPreferredSize(new Dimension(520, 60));

        JLabel title = new JLabel("Browser Launcher");
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

        RoundedPanel mainPanel = new RoundedPanel();
        mainPanel.setLayout(new BorderLayout(15, 15));
        mainPanel.setOpaque(false);

        urlField = new JTextField("https://");
        urlField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        urlField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton openBtn = makeButton("Open");
        openBtn.setFont(new Font("Segoe UI", Font.BOLD, 18));
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
            JOptionPane.showMessageDialog(this,
                    "Invalid URL!", "Error", JOptionPane.ERROR_MESSAGE);
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
        urlField.setBackground(darkMode ? new Color(60, 63, 70) : Color.WHITE);
        urlField.setForeground(fg);

        repaint();
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

    class RoundedPanel extends JPanel {

        RoundedPanel() {
            setOpaque(false);
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int arc = 25;
            g2.setColor(darkMode ? new Color(50, 54, 61) : Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BrowserApp::new);
    }
}
