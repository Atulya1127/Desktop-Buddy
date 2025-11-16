
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class ToDoListApp extends JFrame {

    private DefaultListModel<String> model = new DefaultListModel<>();
    private JList<String> taskList = new JList<>(model);
    private JTextField taskInput = new JTextField("Write your today's goals");

    private boolean darkMode = true;

    private Color accent = new Color(100, 149, 237);
    private Color darkBg = new Color(40, 44, 52);
    private Color lightBg = new Color(245, 247, 250);

    public ToDoListApp() {
        super("To-Do List");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(520, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        GradientPanel header = new GradientPanel(accent, new Color(72, 118, 255));
        header.setLayout(new BorderLayout());
        header.setPreferredSize(new Dimension(520, 60));

        JLabel title = new JLabel("To-Do List");
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

        taskInput.setForeground(Color.GRAY);
        taskInput.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        taskInput.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (taskInput.getText().equals("Write your today's goals")) {
                    taskInput.setText("");
                    taskInput.setForeground(darkMode ? Color.WHITE : Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (taskInput.getText().trim().isEmpty()) {
                    taskInput.setText("Write your today's goals");
                    taskInput.setForeground(Color.GRAY);
                }
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        buttonPanel.setOpaque(false);

        JButton addBtn = makeButton("Add Task");
        JButton removeBtn = makeButton("Remove Task");

        addBtn.addActionListener(e -> addTask());
        removeBtn.addActionListener(e -> removeTask());

        buttonPanel.add(addBtn);
        buttonPanel.add(removeBtn);

        RoundedPanel inputPanel = new RoundedPanel();
        inputPanel.setLayout(new BorderLayout(10, 10));
        inputPanel.add(taskInput, BorderLayout.CENTER);
        inputPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(inputPanel, BorderLayout.NORTH);

        taskList.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(taskList);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        mainPanel.add(scroll, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        loadTasks();

        applyTheme();
        setVisible(true);
    }

    private void addTask() {
        String task = taskInput.getText().trim();
        if (!task.isEmpty() && !task.equals("Write your today's goals")) {
            model.addElement("• " + task);
            taskInput.setText("");
            taskInput.setForeground(Color.GRAY);
            saveTasks();
        }
    }

    private void removeTask() {
        int index = taskList.getSelectedIndex();
        if (index != -1) {
            model.remove(index);
            saveTasks();
        }
    }

    private void saveTasks() {
        try (PrintWriter pw = new PrintWriter("tasks.txt")) {
            for (int i = 0; i < model.size(); i++) {
                pw.println(model.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTasks() {
        File file = new File("tasks.txt");
        if (file.exists()) {
            try (Scanner sc = new Scanner(file)) {
                while (sc.hasNextLine()) {
                    model.addElement(sc.nextLine());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        taskList.setBackground(bg);
        taskList.setForeground(fg);

        taskInput.setBackground(darkMode ? new Color(60, 63, 70) : Color.WHITE);

        if (taskInput.getText().equals("Write your today's goals")) {
            taskInput.setForeground(Color.GRAY);
        } else {
            taskInput.setForeground(fg);
        }

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ToDoListApp::new);
    }
}
