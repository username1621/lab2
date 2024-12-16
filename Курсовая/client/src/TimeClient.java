import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.net.*;
import java.io.*;
import java.util.Timer;
import java.util.Calendar;
import java.util.TimeZone;
import static javax.swing.JOptionPane.showMessageDialog;

public class TimeClient {
    private JFrame frame;
    private static JLabel timeLabel;
    private static JLabel periodLabel, timeZoneLabel;
    private static JComboBox<String> timeZoneCombo;
    private JComboBox<Integer> syncPeriodCombo;
    public static JButton syncButton;
    private Timer timer;
    private static String serverTime;
    private static String host = "localhost";
    private static int port = 12345;
    public TimeClient() {
        createUI();
        Timer timer1;
        timer1 = new Timer(true);
        timer1.schedule(new TimerTask() {
            public void run() {
                updateTime();
            }
        }, 0, 1000);
        updateTime();
    }
    public static void updateTime() {
        String selectedTimeZone = (String) timeZoneCombo.getSelectedItem();
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(selectedTimeZone));
            String time = String.format("%02d:%02d:%02d",
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    calendar.get(Calendar.SECOND));
            timeLabel.setText(time);
    }
    private void createUI() {
        frame = new JFrame("Time Client");
        frame.setSize(350, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JButton loginButton = new JButton("Войти");
        loginButton.setBounds(10, 112, 150, 30);
        loginButton.addActionListener(e -> Input.Inp(host, port));
        frame.add(loginButton);
        JButton registerButton = new JButton("Регистрация");
        registerButton.setBounds(180, 112, 150, 30);
        registerButton.addActionListener(e -> Registration.Registr(host, port));
        frame.add(registerButton);

        timeLabel = new JLabel();
        timeLabel.setBounds(100, 30, 350, 30);
        timeLabel.setFont(new Font("Times New Roman", Font.PLAIN, 30));
        frame.add(timeLabel);

        timeZoneLabel = new JLabel("Временная зона");
        timeZoneLabel.setBounds(10, 145, 350, 30);
        frame.add(timeZoneLabel);
        timeZoneCombo = new JComboBox<>(TimeZone.getAvailableIDs());
        timeZoneCombo.setBounds(10, 170, 200, 30);
        frame.add(timeZoneCombo);

        periodLabel = new JLabel("Период (мин)");
        periodLabel.setBounds(220, 145, 200, 30);
        frame.add(periodLabel);
        Integer[] periods = {1, 5, 10, 30, 60};
        syncPeriodCombo = new JComboBox<>(periods);
        syncPeriodCombo.setBounds(220, 170, 80, 30);
        frame.add(syncPeriodCombo);

        syncButton = new JButton("Начать синхронизацию");
        syncButton.setBounds(10, 210, 250, 30);
        syncButton.addActionListener(e -> startSync());
        syncButton.setEnabled(false);
        frame.add(syncButton);
        frame.setVisible(true);
    }
    private void startSync() {
        if (timer != null) {
            timer.cancel();
        }

        int period = (Integer) syncPeriodCombo.getSelectedItem();
        timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                syncTime();
            }
        }, 0, period * 60 * 1000);
    }
    private void syncTime() {
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            out.println("TIME");
            out.flush();
            serverTime = in.readLine();
            String selectedTimeZone = (String) timeZoneCombo.getSelectedItem();
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(selectedTimeZone));
            serverTime = String.format("%02d:%02d:%02d",
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    calendar.get(Calendar.SECOND));
            timeLabel.setText(serverTime);
            out.close();
            in.close();

        } catch (IOException e) {
            showMessageDialog(null, e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TimeClient::new);
    }
}
