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

    public ToDoListApp() {
        super("To-Do List");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(520, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        GradientHeader header = new GradientHeader();
        header.setLayout(new BorderLayout());
        header.setPreferredSize(new Dimension(520, 60));

        JLabel title = new JLabel("To-Do List");
        title.setFont(new Font("Poppins", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 0));

        ModernButton themeToggle = new ModernButton("Toggle Theme");
        themeToggle.setForeground(Color.WHITE);
        themeToggle.addActionListener(e -> toggleTheme());

        header.add(title, BorderLayout.WEST);
        header.add(themeToggle, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        RoundedPanel mainPanel = new RoundedPanel();
        mainPanel.setLayout(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        taskInput.setFont(new Font("Poppins", Font.PLAIN, 18));
        taskInput.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        taskInput.setForeground(Color.GRAY);

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

        ModernButton addBtn = new ModernButton("Add Task");
        ModernButton removeBtn = new ModernButton("Remove Task");

        addBtn.addActionListener(e -> addTask());
        removeBtn.addActionListener(e -> removeTask());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        buttonPanel.setOpaque(false);
        buttonPanel.add(addBtn);
        buttonPanel.add(removeBtn);

        RoundedPanel inputPanel = new RoundedPanel();
        inputPanel.setLayout(new BorderLayout(10, 10));
        inputPanel.add(taskInput, BorderLayout.CENTER);
        inputPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(inputPanel, BorderLayout.NORTH);

        taskList.setFont(new Font("Poppins", Font.PLAIN, 18));
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskList.setFixedCellHeight(40);

        JScrollPane scroll = new JScrollPane(taskList);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);

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
            JOptionPane.showMessageDialog(this, "Error saving tasks.");
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
                JOptionPane.showMessageDialog(this, "Error loading tasks.");
            }
        }
    }

    private void toggleTheme() {
        darkMode = !darkMode;
        applyTheme();
    }

    private void applyTheme() {
        Color bg = darkMode ? new Color(35, 35, 50) : new Color(245, 247, 250);
        Color fg = darkMode ? Color.WHITE : Color.BLACK;
        Color inputBg = darkMode ? new Color(60, 63, 70) : Color.WHITE;

        getContentPane().setBackground(bg);
        taskList.setBackground(bg);
        taskList.setForeground(fg);

        taskInput.setBackground(inputBg);
        if (taskInput.getText().equals("Write your today's goals")) {
            taskInput.setForeground(Color.GRAY);
        } else {
            taskInput.setForeground(fg);
        }

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
            g2.setColor(new Color(0, 0, 0, 35));
            g2.fillRoundRect(6, 6, w - 12, h - 12, arc, arc);

            g2.setColor(darkMode ? new Color(50, 54, 61) : Color.WHITE);
            g2.fillRoundRect(0, 0, w - 12, h - 12, arc, arc);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    class GradientHeader extends JPanel {
        GradientHeader() {
            setOpaque(false);
        }

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ToDoListApp::new);
    }
}
