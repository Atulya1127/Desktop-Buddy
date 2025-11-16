
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class CalendarApp extends JFrame {

    private LocalDate currentMonth;
    private JPanel calendarPanel;
    private JLabel monthLabel;
    private boolean darkMode = true;
    private Map<LocalDate, String> events = new HashMap<>();

    public CalendarApp() {
        super("Calendar App");

        currentMonth = LocalDate.now().withDayOfMonth(1);

        setSize(700, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(new EmptyBorder(10, 10, 10, 10));
        header.setBackground(new Color(30, 30, 30));

        JButton prevBtn = createHeaderButton("<");
        JButton nextBtn = createHeaderButton(">");

        monthLabel = new JLabel("", SwingConstants.CENTER);
        monthLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 26));
        monthLabel.setForeground(Color.WHITE);

        prevBtn.addActionListener(e -> changeMonth(-1));
        nextBtn.addActionListener(e -> changeMonth(1));

        header.add(prevBtn, BorderLayout.WEST);
        header.add(monthLabel, BorderLayout.CENTER);
        header.add(nextBtn, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        calendarPanel = new JPanel(new GridLayout(0, 7, 5, 5));
        calendarPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        calendarPanel.setBackground(new Color(40, 44, 52));
        add(calendarPanel, BorderLayout.CENTER);

        JButton toggleBtn = new JButton("Toggle Mode");
        toggleBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        toggleBtn.setForeground(Color.WHITE);
        toggleBtn.setBackground(new Color(70, 70, 70));
        toggleBtn.setFocusPainted(false);
        toggleBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        toggleBtn.addActionListener(e -> {
            darkMode = !darkMode;
            updateCalendar();
        });
        add(toggleBtn, BorderLayout.SOUTH);

        updateCalendar();
        setVisible(true);
    }

    private JButton createHeaderButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(60, 60, 60));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void changeMonth(int delta) {
        currentMonth = currentMonth.plusMonths(delta);
        updateCalendar();
    }

    private void updateCalendar() {
        calendarPanel.removeAll();

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMMM yyyy");
        monthLabel.setText(currentMonth.format(fmt));

        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : days) {
            JLabel lbl = new JLabel(day, SwingConstants.CENTER);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lbl.setOpaque(true);
            lbl.setBackground(darkMode ? new Color(60, 60, 60) : Color.LIGHT_GRAY);
            lbl.setForeground(darkMode ? Color.WHITE : Color.BLACK);
            calendarPanel.add(lbl);
        }

        LocalDate first = currentMonth.withDayOfMonth(1);
        int startDay = first.getDayOfWeek().getValue() % 7;

        for (int i = 0; i < startDay; i++) {
            calendarPanel.add(new JLabel(""));
        }

        int daysInMonth = currentMonth.lengthOfMonth();
        LocalDate today = LocalDate.now();

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = currentMonth.withDayOfMonth(day);
            JButton dayBtn = new JButton(String.valueOf(day));

            dayBtn.setOpaque(true);
            dayBtn.setBorderPainted(false);
            dayBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
            dayBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            if (date.equals(today)) {
                dayBtn.setBackground(new Color(231, 76, 60));
                dayBtn.setForeground(Color.WHITE);
            } else if (events.containsKey(date)) {
                dayBtn.setBackground(new Color(41, 128, 185));
                dayBtn.setForeground(Color.WHITE);
            } else {
                dayBtn.setBackground(darkMode ? new Color(50, 50, 50) : Color.WHITE);
                dayBtn.setForeground(darkMode ? Color.WHITE : Color.BLACK);
            }

            dayBtn.addActionListener(e -> {
                String existing = events.getOrDefault(date, "");
                String event = JOptionPane.showInputDialog(CalendarApp.this,
                        "Add/Edit event for " + date + ":", existing);
                if (event != null) {
                    if (event.isEmpty()) {
                        events.remove(date);
                    } else {
                        events.put(date, event);
                    }
                    updateCalendar();
                }
            });

            dayBtn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (events.containsKey(date)) {
                        dayBtn.setToolTipText(events.get(date));
                    }
                }
            });

            calendarPanel.add(dayBtn);
        }

        calendarPanel.revalidate();
        calendarPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CalendarApp::new);
    }
}
