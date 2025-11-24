import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CalculatorApp extends JFrame {

    private boolean darkMode = true;

    private JTextField display;
    private String current = "";
    private String operator = "";
    private double firstValue = 0;

    public CalculatorApp() {
        super("Desktop Buddy – Calculator");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(420, 560);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        GradientHeader header = new GradientHeader();
        header.setLayout(new BorderLayout());
        header.setPreferredSize(new Dimension(420, 65));

        JLabel title = new JLabel("Calculator");
        title.setFont(new Font("Poppins", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 0));

        ModernButton themeToggle = new ModernButton("Toggle Theme");
        themeToggle.addActionListener(e -> toggleTheme());

        header.add(title, BorderLayout.WEST);
        header.add(themeToggle, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        display = new JTextField("0");
        display.setEditable(false);
        display.setFont(new Font("Consolas", Font.BOLD, 46));
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        display.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

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
            grid.add(makeCalcButton(txt));
        }

        add(grid, BorderLayout.SOUTH);

        applyTheme();
        setVisible(true);
    }

    private void input(String key) {
        switch (key) {
            case "C" -> reset();
            case "←" -> backspace();
            case "+", "-", "×", "/", "%" -> applyOperator(key);
            case "+/-" -> toggleSign();
            case "=" -> calculate();
            case "." -> addDecimal();
            default -> addDigit(key);
        }
    }

    private void reset() {
        current = "";
        operator = "";
        firstValue = 0;
        display.setText("0");
    }

    private void backspace() {
        if (!current.isEmpty()) {
            current = current.substring(0, current.length() - 1);
            display.setText(current.isEmpty() ? "0" : current);
        }
    }

    private void applyOperator(String op) {
        if (!current.isEmpty()) firstValue = Double.parseDouble(current);
        else firstValue = Double.parseDouble(display.getText());
        operator = op;
        current = "";
    }

    private void toggleSign() {
        if (current.isEmpty()) return;
        current = current.startsWith("-") ? current.substring(1) : "-" + current;
        display.setText(current);
    }

    private void addDecimal() {
        if (!current.contains(".")) {
            current = current.isEmpty() ? "0." : current + ".";
            display.setText(current);
        }
    }

    private void addDigit(String digit) {
        if (current.equals("0")) current = "";
        current += digit;
        display.setText(current);
    }

    private void calculate() {
        if (operator.isEmpty() || current.isEmpty()) return;
        double second = Double.parseDouble(current);
        double result = switch (operator) {
            case "+" -> firstValue + second;
            case "-" -> firstValue - second;
            case "×" -> firstValue * second;
            case "/" -> (second == 0 ? 0 : firstValue / second);
            case "%" -> firstValue * (second / 100);
            default -> 0;
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
        Color bg = darkMode ? new Color(40, 44, 52) : new Color(245, 247, 250);
        Color txt = darkMode ? Color.WHITE : Color.BLACK;

        getContentPane().setBackground(bg);
        display.setBackground(darkMode ? new Color(60, 63, 70) : Color.WHITE);
        display.setForeground(txt);
        display.setCaretColor(txt);

        repaint();
    }

    private JButton makeCalcButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Poppins", Font.BOLD, 22));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBackground(new Color(230, 235, 250));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(200, 220, 255)); }
            public void mouseExited(MouseEvent e) { btn.setBackground(new Color(230, 235, 250)); }
        });

        btn.addActionListener(e -> input(text));
        return btn;
    }

    private ModernButton makeTopButton(String text) { return new ModernButton(text); }

    class RoundedPanel extends JPanel {
        RoundedPanel() { setOpaque(false); }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(darkMode ? new Color(50, 54, 61) : Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
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
            setFont(new Font("Poppins", Font.BOLD, 16));
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { setBackground(new Color(255, 255, 255, 50)); }
                @Override public void mouseExited(MouseEvent e) { setBackground(new Color(30, 30, 30, 80)); }
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

    public static void main(String[] args) { SwingUtilities.invokeLater(CalculatorApp::new); }
}

