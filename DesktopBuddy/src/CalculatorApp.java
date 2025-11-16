
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CalculatorApp extends JFrame {

    private boolean darkMode = true;

    private Color accent = new Color(100, 149, 237);
    private Color darkBg = new Color(40, 44, 52);
    private Color lightBg = new Color(245, 247, 250);

    private JTextField display;

    public CalculatorApp() {
        super("Calculator");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(420, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        GradientPanel header = new GradientPanel(accent, new Color(72, 118, 255));
        header.setLayout(new BorderLayout());
        header.setPreferredSize(new Dimension(420, 65));

        JLabel title = new JLabel("Calculator");
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

        display = new JTextField("0");
        display.setEditable(false);
        display.setFont(new Font("Consolas", Font.BOLD, 38));
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        display.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        RoundedPanel displayPanel = new RoundedPanel();
        displayPanel.setLayout(new BorderLayout());
        displayPanel.add(display, BorderLayout.CENTER);
        add(displayPanel, BorderLayout.CENTER);

        JPanel grid = new JPanel(new GridLayout(5, 4, 12, 12));
        grid.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        grid.setOpaque(false);

        String[] buttons = {
            "C", "←", "/", "×",
            "7", "8", "9", "-",
            "4", "5", "6", "+",
            "1", "2", "3", "=",
            "0", ".", "%", "+/-"
        };

        for (String txt : buttons) {
            JButton btn = makeCalcButton(txt);
            grid.add(btn);
        }

        add(grid, BorderLayout.SOUTH);

        applyTheme();
        setVisible(true);
    }

    private String current = "";
    private String operator = "";
    private double firstValue = 0;

    private void input(String key) {
        switch (key) {
            case "C" -> {
                current = "";
                operator = "";
                firstValue = 0;
                display.setText("0");
            }
            case "←" -> {
                if (!current.isEmpty()) {
                    current = current.substring(0, current.length() - 1);
                    display.setText(current.isEmpty() ? "0" : current);
                }
            }
            case "+", "-", "×", "/", "%" -> {
                operator = key;
                firstValue = Double.parseDouble(display.getText());
                current = "";
            }
            case "+/-" -> {
                if (!current.isEmpty()) {
                    current = current.startsWith("-") ? current.substring(1) : "-" + current;
                    display.setText(current);
                }
            }
            case "=" ->
                calculate();
            case "." -> {
                if (!current.contains(".")) {
                    current += ".";
                    display.setText(current);
                }
            }
            default -> {
                current += key;
                display.setText(current);
            }
        }
    }

    private void calculate() {
        if (operator.isEmpty() || current.isEmpty()) {
            return;
        }

        double second = Double.parseDouble(current);
        double result = switch (operator) {
            case "+" ->
                firstValue + second;
            case "-" ->
                firstValue - second;
            case "×" ->
                firstValue * second;
            case "/" ->
                (second == 0 ? 0 : firstValue / second);
            case "%" ->
                firstValue % second;
            default ->
                0;
        };

        display.setText((result == (int) result) ? String.valueOf((int) result) : String.valueOf(result));
        current = String.valueOf(result);
        operator = "";
    }

    private void toggleTheme() {
        darkMode = !darkMode;
        applyTheme();
    }

    private void applyTheme() {
        Color bg = darkMode ? darkBg : lightBg;
        Color fg = darkMode ? Color.WHITE : Color.BLACK;

        getContentPane().setBackground(bg);
        display.setBackground(darkMode ? new Color(60, 63, 70) : Color.WHITE);
        display.setForeground(fg);

        repaint();
        revalidate();
    }

    private JButton makeCalcButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 22));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(230, 235, 250));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(200, 220, 255));
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(230, 235, 250));
            }
        });

        btn.addActionListener(e -> input(text));
        return btn;
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
        SwingUtilities.invokeLater(CalculatorApp::new);
    }
}
